package com.example.cashpilot.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashpilot.data.model.DepositOfferDto
import com.example.cashpilot.data.model.VtbDepositAttrDto
import com.example.cashpilot.data.remote.DepositApi
import com.example.cashpilot.data.remote.RetrofitClient
import com.example.cashpilot.data.remote.TinkoffDepositApi
import com.example.cashpilot.data.remote.VtbDepositApi
import com.example.cashpilot.data.mapper.toDepositOfferDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DepositViewModel(
    private val sberApi: DepositApi = RetrofitClient.depositApi,
    private val vtbApi: VtbDepositApi = RetrofitClient.vtbDepositApi,
    private val tinkoffApi: TinkoffDepositApi = RetrofitClient.tinkoffDepositApi
) : ViewModel() {

    val amount = MutableStateFlow("")
    val termMonths = MutableStateFlow("")

    private val _deposits = MutableStateFlow<List<DepositOfferDto>>(emptyList())
    val deposits: StateFlow<List<DepositOfferDto>> = _deposits

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun onAmountChange(newAmount: String) {
        amount.value = newAmount
    }

    fun onTermChange(newTerm: String) {
        termMonths.value = newTerm
    }

    fun onShowClicked() {
        val userAmount = amount.value.toDoubleOrNull()
        val userTerm = termMonths.value.toIntOrNull()

        if (userAmount == null || userTerm == null || userAmount <= 0 || userTerm <= 0) {
            _errorMessage.value = "Введите корректные данные"
            _deposits.value = emptyList()
            return
        } else {
            _errorMessage.value = ""
        }

        viewModelScope.launch {
            _isLoading.value = true
            val offers = mutableListOf<DepositOfferDto>()
            try {
                // Сбербанк
                val sberOffers = sberApi.getDepositOffers().body()
                if (!sberOffers.isNullOrEmpty()) {
                    offers.addAll(sberOffers)
                }

                // ВТБ
                val vtbOffers = vtbApi.getVtbDepositOffers().body()
                if (!vtbOffers.isNullOrEmpty()) {
                    offers.addAll(vtbOffers.map { it.toDepositOfferDto() })
                }

                // Тинькофф
                val tinkoffOffers = tinkoffApi.getTinkoffDeposits().body()
                if (!tinkoffOffers.isNullOrEmpty()) {
                    tinkoffOffers.forEach { offer ->
                        val attrs = offer.attributes?.map {
                            VtbDepositAttrDto(label = it.subtitle ?: "", value = it.title ?: "")
                        } ?: emptyList()

                        offers.add(
                            DepositOfferDto(
                                bankName = "Тинькофф Банк",
                                depositName = offer.loanName ?: offer.depositName ?: "Вклад Тинькофф",
                                rate = attrs.find { it.label.contains("ставк", true) || it.label.contains("доходност", true) }?.value,
                                term = attrs.find { it.label.contains("срок", true) }?.value,
                                amount = attrs.find { it.label.contains("сумм", true) || it.label.contains("мин", true) }?.value,
                                attrs = attrs
                            )
                        )
                    }
                }

                _deposits.value = filterDeposits(offers, userAmount, userTerm)

            } catch (e: Exception) {
                Log.e("DepositVM", "Error loading deposits", e)
                _deposits.value = emptyList()
                _errorMessage.value = "Ошибка загрузки данных"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun filterDeposits(
        list: List<DepositOfferDto>,
        userAmount: Double,
        userTerm: Int
    ): List<DepositOfferDto> {
        return list.filter { deposit ->
            val depositAmount = deposit.amount?.let { extractNumber(it) }
                ?: deposit.attrs?.firstOrNull { it.label.contains("сумм", true) }
                    ?.value?.let { extractNumber(it) }

            val depositTerm = deposit.term?.let { extractNumber(it) }
                ?: deposit.attrs?.firstOrNull { it.label.contains("срок", true) }
                    ?.value?.let { extractNumber(it) }

            val isAmountOk = depositAmount == null || depositAmount <= userAmount
            val isTermOk = depositTerm == null || depositTerm <= userTerm

            isAmountOk && isTermOk
        }.shuffled() // перемешиваем, чтобы не шли подряд одинаковые банки
    }

    private fun extractNumber(text: String): Double? {
        val numberText = Regex("""\d+[\d\s]*""").find(text)?.value?.replace("\\s".toRegex(), "")
        return numberText?.toDoubleOrNull()
    }
}

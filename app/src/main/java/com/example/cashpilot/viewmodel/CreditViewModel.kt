package com.example.cashpilot.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashpilot.data.local.CreditOfferDao
import com.example.cashpilot.data.local.CreditOfferEntity
import com.example.cashpilot.data.model.CreditOfferDto
import com.example.cashpilot.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class CreditViewModel(private val dao: CreditOfferDao) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _termMonths = MutableStateFlow("")
    val termMonths: StateFlow<String> = _termMonths.asStateFlow()

    // Стейт для хранения кредитных офферов из БД
    private val dbOffersFlow = dao.getAll()
        .map { list ->
            list.map {
                CreditOfferDto(
                    bankName = it.bankName,
                    minAmount = it.minAmount,
                    term = it.term,
                    creditType = it.creditType
                )
            }
        }

    // Стейт для хранения Тинькофф офферов из сети
    private val _tinkoffOffers = MutableStateFlow<List<CreditOfferDto>>(emptyList())
    val tinkoffOffers = _tinkoffOffers.asStateFlow()

    // Объединяем офферы из БД и Тинькофф офферы в один общий список
    val offers: StateFlow<List<CreditOfferDto>> = combine(
        dbOffersFlow,
        tinkoffOffers
    ) { dbList, tinkoffList ->
        // Можно объединять списки, исключая дубликаты по уникальному признаку, например по bankName + creditType и т.д.
        (dbList + tinkoffList).distinctBy { it.bankName + it.creditType }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onAmountChange(newAmount: String) {
        _amount.value = newAmount
    }

    fun onTermChange(newTerm: String) {
        _termMonths.value = newTerm
    }

    fun onShowClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Загружаем основное API и сохраняем в БД
                val dtos = RetrofitClient.creditApi.getCreditOffers()
                println("🔍 Получено от API: ${dtos.size}")
                dtos.forEach {
                    println("📦 DTO: ${it.bankName}, ${it.minAmount}, ${it.term}")
                }
                val entities = dtos.map {
                    CreditOfferEntity(
                        bankName = it.bankName,
                        minAmount = it.minAmount,
                        term = it.term,
                        creditType = it.creditType
                    )
                }
                dao.clearAll()
                dao.insertAll(entities)
                println("✅ Сохранили в БД: ${entities.size}")




            } catch (e: Exception) {
                println("❌ Ошибка загрузки данных: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

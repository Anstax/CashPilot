package com.example.cashpilot.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.pow

class CalculatorViewModel : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _termMonths = MutableStateFlow("")
    val termMonths: StateFlow<String> = _termMonths

    private val _percent = MutableStateFlow("")
    val percent: StateFlow<String> = _percent

    private val _monthlyReplenishment = MutableStateFlow("")
    val monthlyReplenishment: StateFlow<String> = _monthlyReplenishment

    private val _monthlyWithdrawal = MutableStateFlow("")
    val monthlyWithdrawal: StateFlow<String> = _monthlyWithdrawal

    private val _isDepositMode = MutableStateFlow(true)
    val isDepositMode: StateFlow<Boolean> = _isDepositMode

    private val _capitalization = MutableStateFlow(false)
    val capitalization: StateFlow<Boolean> = _capitalization

    private val _replenishment = MutableStateFlow(false)
    val replenishment: StateFlow<Boolean> = _replenishment

    private val _partialWithdrawal = MutableStateFlow(false)
    val partialWithdrawal: StateFlow<Boolean> = _partialWithdrawal

    private val _earlyRepayment = MutableStateFlow(false)
    val earlyRepayment: StateFlow<Boolean> = _earlyRepayment

    private val _differentiated = MutableStateFlow(false)
    val differentiated: StateFlow<Boolean> = _differentiated

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result

    private val _tableData = MutableStateFlow<List<PaymentRow>>(emptyList())
    val tableData: StateFlow<List<PaymentRow>> = _tableData

    fun onAmountChange(value: String) { _amount.value = value }
    fun onTermChange(value: String) { _termMonths.value = value }
    fun onPercentChange(value: String) { _percent.value = value }
    fun onMonthlyReplenishmentChange(value: String) { _monthlyReplenishment.value = value }
    fun onMonthlyWithdrawalChange(value: String) { _monthlyWithdrawal.value = value }
    fun toggleDepositMode(isDeposit: Boolean) { _isDepositMode.value = isDeposit }

    fun updateFilter(label: String, value: Boolean) {
        when (label) {
            "С капитализацией" -> _capitalization.value = value
            "Возможность пополнения" -> _replenishment.value = value
            "Частичное снятие" -> _partialWithdrawal.value = value
            "С досрочным погашением" -> _earlyRepayment.value = value
            "Дифференцированный платёж" -> _differentiated.value = value
        }
    }

    fun calculate() {
        val principal = _amount.value.toDoubleOrNull() ?: 0.0
        val months = _termMonths.value.toIntOrNull() ?: 0
        val annualRate = _percent.value.toDoubleOrNull() ?: 0.0
        val replenishment = _monthlyReplenishment.value.toDoubleOrNull() ?: 0.0
        val withdrawal = _monthlyWithdrawal.value.toDoubleOrNull() ?: 0.0

        if (principal <= 0 || months <= 0 || annualRate <= 0) {
            _result.value = "Введите корректные данные"
            return
        }

        val chartPoints = mutableListOf<Float>()
        val tableRows = mutableListOf<PaymentRow>()

        if (_isDepositMode.value) {
            val monthlyRate = annualRate / 100 / 12
            var total = principal
            var totalProfit = 0.0

            for (i in 1..months) {
                if (_replenishment.value) total += replenishment
                if (_partialWithdrawal.value) total -= withdrawal

                val profitForMonth = total * monthlyRate
                total += profitForMonth
                totalProfit += profitForMonth

                chartPoints.add(total.toFloat())
                tableRows.add(PaymentRow(i, "%.2f".format(total), "%.2f".format(profitForMonth))) // теперь есть profitForMonth
            }

            val profit = total - principal
            _result.value = "Итоговая сумма: ${"%.2f".format(total)} ₽\nПрибыль: ${"%.2f".format(profit)} ₽"

    } else {
            val monthlyRate = annualRate / 100 / 12

            if (_differentiated.value) {
                val monthlyPrincipal = principal / months
                var totalPayment = 0.0

                for (i in 0 until months) {
                    val remainingPrincipal = principal - monthlyPrincipal * i
                    val interest = remainingPrincipal * monthlyRate
                    val payment = monthlyPrincipal + interest

                    totalPayment += payment

                    chartPoints.add((principal - monthlyPrincipal * i).toFloat())
                    tableRows.add(PaymentRow(i + 1, "%.2f".format(principal - monthlyPrincipal * i), "%.2f".format(payment)))
                }

                if (_earlyRepayment.value) {
                    totalPayment *= 0.95
                }

                val overpayment = totalPayment - principal
                _result.value = "Итоговая выплата: ${"%.2f".format(totalPayment)} ₽\nПереплата: ${"%.2f".format(overpayment)} ₽"

            } else {
                val annuityFactor = (monthlyRate * (1 + monthlyRate).pow(months)) / ((1 + monthlyRate).pow(months) - 1)
                val monthlyPayment = principal * annuityFactor
                var totalPayment = monthlyPayment * months

                if (_earlyRepayment.value) {
                    totalPayment *= 0.95
                }

                val overpayment = totalPayment - principal

                for (i in 0 until months) {
                    val remainingPrincipal = principal - (i + 1) * (principal / months)
                    chartPoints.add(remainingPrincipal.toFloat().coerceAtLeast(0f))
                    tableRows.add(PaymentRow(i + 1, "%.2f".format(remainingPrincipal.coerceAtLeast(0.0)), "%.2f".format(monthlyPayment)))
                }

                _result.value = "Ежемесячный платёж: ${"%.2f".format(monthlyPayment)} ₽\nПереплата: ${"%.2f".format(overpayment)} ₽\nИтоговая выплата: ${"%.2f".format(totalPayment)} ₽"
            }
        }

        _tableData.value = tableRows
    }
}

data class PaymentRow(val month: Int, val balance: String, val payment: String)

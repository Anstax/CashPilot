package com.example.cashpilot.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashpilot.R
import com.example.cashpilot.ui.components.StyledTextField
import com.example.cashpilot.viewmodel.CalculatorViewModel
import com.example.cashpilot.viewmodel.PaymentRow
import com.example.cashpilot.ui.theme.DarkGray
import com.example.cashpilot.ui.theme.LightGray
import com.example.cashpilot.ui.theme.GreenB
import com.example.cashpilot.ui.theme.LightBlack
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cashpilot.ui.components.Header


@Composable
fun CalculatorScreen(navController: NavController, viewModel: CalculatorViewModel = viewModel(), userName: String) {
    val amount by viewModel.amount.collectAsState()
    val term by viewModel.termMonths.collectAsState()
    val percent by viewModel.percent.collectAsState()
    val isDepositMode by viewModel.isDepositMode.collectAsState()
    val result by viewModel.result.collectAsState()
    val tableData = viewModel.tableData.collectAsState().value

    var showFilters by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGray)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            item {
                Header(userName = userName)

                    //Главная
                    Card(
                        colors = CardDefaults.cardColors(containerColor = LightGray),
                        shape = RoundedCornerShape(32.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Навигационные зеленые кнопки
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clickable { navController.navigate("deposit/$userName") }
                                        .background(GreenB.copy(alpha = 0.25f), shape = CircleShape)
                                        .padding(horizontal = 5.dp, vertical = 5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_arrow_left),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(38.dp)
                                        )
                                        Icon(
                                            painter = painterResource(R.drawable.ic_deposit),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(34.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "Калькулятор",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Box(
                                    modifier = Modifier
                                        .clickable { navController.navigate("credit/$userName") }
                                        .background(GreenB.copy(alpha = 0.25f), shape = CircleShape)
                                        .padding(horizontal = 5.dp, vertical = 5.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_calc),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(34.dp)
                                        )

                                        Icon(
                                            painter = painterResource(R.drawable.ic_arrow_right),
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(38.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Переключение режимов вклад/кредит
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp)
                                    .background(LightGray, RoundedCornerShape(50.dp))
                                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(50.dp))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp))
                                            .background(if (isDepositMode) GreenB else Color.Transparent)
                                            .clickable { viewModel.toggleDepositMode(true) }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Вклад",
                                            color = if (isDepositMode) Color.Black else Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }


                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                                            .background(if (!isDepositMode) GreenB else Color.Transparent)
                                            .clickable { viewModel.toggleDepositMode(false) }
                                            .padding(vertical = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Кредит",
                                            color = if (!isDepositMode) Color.Black else Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Поля ввода
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    StyledTextField(
                                        value = amount,
                                        onValueChange = { viewModel.onAmountChange(it) },
                                        label = if (isDepositMode) "Сумма вклада" else "Сумма кредита",
                                        modifier = Modifier.fillMaxWidth(0.93f)
                                    )

                                    StyledTextField(
                                        value = term,
                                        onValueChange = { viewModel.onTermChange(it) },
                                        label = if (isDepositMode) "Срок (мес.)" else "Срок кредита (мес.)",
                                        modifier = Modifier.fillMaxWidth(0.93f)
                                    )

                                    StyledTextField(
                                        value = percent,
                                        onValueChange = { viewModel.onPercentChange(it) },
                                        label = if (isDepositMode) "Процент по вкладу" else "Процентная ставка",
                                        modifier = Modifier.fillMaxWidth(0.93f)
                                    )

                                    // Добавление полей при примененных фильтрах
                                    if (isDepositMode && viewModel.replenishment.collectAsState().value) {
                                        Spacer(modifier = Modifier.height(12.dp))
                                        StyledTextField(
                                            value = viewModel.monthlyReplenishment.collectAsState().value,
                                            onValueChange = { viewModel.onMonthlyReplenishmentChange(it) },
                                            label = "Пополнение (в мес.)",
                                            modifier = Modifier.fillMaxWidth(0.93f)
                                        )
                                    }

                                    if (isDepositMode && viewModel.partialWithdrawal.collectAsState().value) {
                                        StyledTextField(
                                            value = viewModel.monthlyWithdrawal.collectAsState().value,
                                            onValueChange = { viewModel.onMonthlyWithdrawalChange(it) },
                                            label = "Снятия (в мес.)",
                                            modifier = Modifier.fillMaxWidth(0.8f)
                                        )
                                    }

                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                IconButton(onClick = { showFilters = true }, modifier = Modifier.size(64.dp)) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_filter),
                                        contentDescription = "Фильтры",
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    viewModel.calculate()
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .width(240.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = GreenB)
                            ) {
                                Text(
                                    "Рассчитать",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Результаты
                            if (result.isNotBlank() && result != "Введите корректные данные") {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = LightBlack),
                                    shape = RoundedCornerShape(24.dp),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(result, color = Color.White, fontSize = 16.sp)


                                        Spacer(modifier = Modifier.height(16.dp))
                                        PaymentTable(tableData, isDepositMode)

                                        Spacer(modifier = Modifier.height(16.dp))
                                        TipsBlock(isDeposit = isDepositMode)
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                            } else if (result == "Введите корректные данные") {
                                Text(
                                    result,
                                    color = Color.Red,
                                    fontSize = 20.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }

                    }
                }
            }
        }

    if (showFilters) {
        CalculatorFilterBottomSheet(
            isDepositMode = isDepositMode,
            capitalization = viewModel.capitalization.collectAsState().value,
            replenishment = viewModel.replenishment.collectAsState().value,
            partialWithdrawal = viewModel.partialWithdrawal.collectAsState().value,
            earlyRepayment = viewModel.earlyRepayment.collectAsState().value,
            differentiated = viewModel.differentiated.collectAsState().value,
            onDismiss = { showFilters = false },
            onUpdate = { label: String, value: Boolean -> viewModel.updateFilter(label, value) }
        )
    }
}

// Фильтры
@Composable
fun CalculatorFilterBottomSheet(
    isDepositMode: Boolean,
    capitalization: Boolean,
    replenishment: Boolean,
    partialWithdrawal: Boolean,
    earlyRepayment: Boolean,
    differentiated: Boolean,
    onDismiss: () -> Unit,
    onUpdate: (String, Boolean) -> Unit
) {
    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable { onDismiss() }
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = LightBlack,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Фильтры", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                if (isDepositMode) {
                    CalculatorFilterCheckbox("С капитализацией", capitalization) { value -> onUpdate("С капитализацией", value) }
                    CalculatorFilterCheckbox("Возможность пополнения", replenishment) { value -> onUpdate("Возможность пополнения", value) }
                    CalculatorFilterCheckbox("Частичное снятие", partialWithdrawal) { value -> onUpdate("Частичное снятие", value) }
                } else {
                    CalculatorFilterCheckbox("С досрочным погашением", earlyRepayment) { value -> onUpdate("С досрочным погашением", value) }
                    CalculatorFilterCheckbox("Дифференцированный платёж", differentiated) { value -> onUpdate("Дифференцированный платёж", value) }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenB)
                ) {
                    Text("Применить", color = Color.White)
                }
            }
        }
    }
}

// Окошко для фильтров
@Composable
fun CalculatorFilterCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(checkedColor = GreenB)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, color = Color.White)
    }
}



// Результаты -> Таблица с графиком
@Composable
fun PaymentTable(data: List<PaymentRow>, isDeposit: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (isDeposit) "Доход по вкладу" else "График платежей",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightBlack)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Мес.", color = Color.Gray, modifier = Modifier.weight(0.5f))
            Text(if (isDeposit) "Остаток" else "Остаток", color = Color.Gray, modifier = Modifier.weight(1.25f))
            Text(if (isDeposit) "Прибыль" else "Платёж", color = Color.Gray, modifier = Modifier.weight(1.25f))
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp)
        ) {
            items(data) { row ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${row.month}", color = Color.White, modifier = Modifier.weight(0.5f))
                        Text("${row.balance} ₽", color = Color.White, modifier = Modifier.weight(1.25f))
                        Text(
                            text = if (isDeposit) "${row.payment} ₽" else "${row.payment} ₽",
                            color = if (isDeposit) GreenB else Color.Red,
                            modifier = Modifier.weight(1.25f)
                        )
                    }
                }
            }
        }
    }
}


// Результаты -> Советы
@Composable
fun TipsBlock(isDeposit: Boolean) {
    val tips = if (isDeposit) listOf(
        "Капитализация увеличивает итоговую прибыль.",
        "Пополнение вклада увеличивает доходность.",
        "Частичное снятие уменьшает процентную выгоду."
    ) else listOf(
        "Аннуитет проще, но переплата выше.",
        "Дифференцированный платеж выгоден на длительных сроках.",
        "Досрочное погашение снижает переплату."
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Советы\uD83D\uDCA1 ", fontSize = 18.sp, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightGray, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            tips.forEach { tip ->
                Text("• $tip", color = Color.White, modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}


package com.example.cashpilot.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cashpilot.R
import com.example.cashpilot.ui.components.Header
import com.example.cashpilot.ui.components.StyledTextField
import com.example.cashpilot.viewmodel.DepositViewModel
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DepositScreen(
    navController: NavController,
    userName: String,
    viewModel: DepositViewModel = viewModel()
) {
    val amount by viewModel.amount.collectAsState()
    val term by viewModel.termMonths.collectAsState()
    val deposits by viewModel.deposits.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedCurrency by remember { mutableStateOf("₽") }
    val currencies = listOf("₽", "$", "€")

    var selectedTerm by remember { mutableStateOf("любой") }
    val termOptions = listOf(
        "любой", "месяц", "3 месяца", "6 месяцев",
        "12 месяцев", "больше года", "свой"
    )
    var customTerm by remember { mutableStateOf("") }

    var showFilters by remember { mutableStateOf(false) }

    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF202020))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            item {
                Header(userName = userName)

                // Форма фильтров
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Навигация назад
                            Box(
                                modifier = Modifier
                                    .clickable { navController.navigate("credit/$userName") }
                                    .background(
                                        Color(0xFF53B63A).copy(alpha = 0.25f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 9.dp, vertical = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_arrow_left),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(38.dp)
                                    )
                                    Spacer(Modifier.width(5.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_credit),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                            }

                            Text(
                                "Вклад",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            // Навигация к калькулятору
                            Box(
                                modifier = Modifier
                                    .clickable { navController.navigate("calculator/$userName") }
                                    .background(
                                        Color(0xFF53B63A).copy(alpha = 0.25f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 9.dp, vertical = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_calc),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(34.dp)
                                    )
                                    Spacer(Modifier.width(5.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_arrow_right),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StyledTextField(
                                value = amount,
                                onValueChange = { viewModel.onAmountChange(it) },
                                label = "Сумма",
                                modifier = Modifier.width(180.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            DropdownLikeTextField(
                                label = "",
                                selectedOption = selectedCurrency,
                                options = currencies,
                                onOptionSelected = { selectedCurrency = it },
                                modifier = Modifier.width(50.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            Image(
                                painter = painterResource(R.drawable.ic_filter),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp, 43.dp)
                                    .clickable { showFilters = true }
                            )
                        }

                        Spacer(Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                        StyledTextField(
                            value = term,
                            onValueChange = { viewModel.onTermChange(it) },
                            label = "Срок (мес.)",
                            modifier = Modifier.width(250.dp)
                        )
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.onShowClicked() },
                            modifier = Modifier
                                .width(150.dp)
                                .align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF53B63A))
                        ) {
                            Text(
                                "Показать",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF53B63A)
                        )
                    }
                }

                // Список вкладов
                deposits.forEach { deposit ->

                    val (logoResId, backgroundColor) = when (deposit.bankName) {
                        "Сбербанк" -> R.drawable.sber to Color(0xFF53B63A).copy(alpha = 0.24f)
                        "Тинькофф Банк" -> R.drawable.tbank to Color(0xFFFFDD2D).copy(alpha = 0.6f)
                        "ВТБ" -> R.drawable.vtb to Color(0xFF0D2B74).copy(alpha = 0.45f)
                        else -> R.drawable.sber to Color.Gray.copy(alpha = 0.2f)
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                BankLogo(deposit.bankName)

                                Column {
                                    Text(
                                        text = deposit.depositName,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = deposit.bankName,
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            // Ставка / Срок / Сумма (универсальный вывод)
                            val rate = deposit.rate ?: deposit.attrs?.find {
                                it.label?.contains(
                                    "ставк",
                                    true
                                ) == true || it.label?.contains("доходност", true) == true
                            }?.value
                            val term = deposit.term ?: deposit.attrs?.find {
                                it.label?.contains(
                                    "срок",
                                    true
                                ) == true
                            }?.value
                            val amount = deposit.amount ?: deposit.attrs?.find {
                                it.label?.contains(
                                    "сумм",
                                    true
                                ) == true || it.label?.contains("мин", true) == true
                            }?.value

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rate?.let { InfoChip("Ставка: $it") }
                                term?.let { InfoChip("Срок: $it") }
                                amount?.let { InfoChip("Сумма: $it") }
                            }

                            Spacer(Modifier.height(12.dp))

                            deposit.attrs?.forEach { attr ->
                                val label = attr.label ?: return@forEach
                                if (!(label.contains("ставк", true) || label.contains(
                                        "доходност",
                                        true
                                    ) || label.contains("срок", true) || label.contains(
                                        "сумм",
                                        true
                                    ) || label.contains("мин", true))
                                ) {
                                    Text(
                                        "${attr.label}: ${attr.value}",
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }





                }
                    }
                }



    if (showFilters) {
        FilterBottomSheet(onDismiss = { showFilters = false })
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownLikeTextField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { if (label.isNotBlank()) Text(label, color = Color.White) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.LightGray,
                errorTextColor = Color.Red,

                focusedContainerColor = Color(0xFF8D8D8D),
                unfocusedContainerColor = Color(0xFF8D8D8D),
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,

                cursorColor = Color(0xFF53B63A),

                focusedIndicatorColor = Color(0xFF53B63A),
                unfocusedIndicatorColor = Color.Gray,
                disabledIndicatorColor = Color.DarkGray,
                errorIndicatorColor = Color.Red
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = Color.White) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}




@Composable
fun FilterBottomSheet(onDismiss: () -> Unit) {
    var withoutWithdrawal by remember { mutableStateOf(false) }
    var withReplenishment by remember { mutableStateOf(false) }
    var withCapitalization by remember { mutableStateOf(false) }
    var onlyOnline by remember { mutableStateOf(false) }
    var withProlongation by remember { mutableStateOf(false) }

    BackHandler(onBack = onDismiss)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(onClick = onDismiss)
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color(0xFF1A1A1A),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Фильтры", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                FilterCheckbox("Без снятия", withoutWithdrawal) { withoutWithdrawal = it }
                FilterCheckbox("С пополнением", withReplenishment) { withReplenishment = it }
                FilterCheckbox("С капитализацией", withCapitalization) { withCapitalization = it }
                FilterCheckbox("Онлайн оформление", onlyOnline) { onlyOnline = it }
                FilterCheckbox("С пролонгацией", withProlongation) { withProlongation = it }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF53B63A))
                ) {
                    Text("Применить", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun FilterCheckbox(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
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
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF53B63A))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.White)
    }
}

@Composable
fun BadgeText(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF53B63A).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF53B63A),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun BankLogo(bankName: String, modifier: Modifier = Modifier) {
    val (logoRes, logoSize) = when (bankName.lowercase()) {
        "сбербанк" -> Pair(R.drawable.sber, 48.dp)
        "тинькофф банк" -> Pair(R.drawable.tbank, 48.dp)
        "втб" -> Pair(R.drawable.vtb, 48.dp)
        else -> Pair(R.drawable.sber, 48.dp)
    }

    Image(
        painter = painterResource(id = logoRes),
        contentDescription = bankName,
        modifier = modifier.size(logoSize)
    )
}



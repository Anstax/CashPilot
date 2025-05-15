package com.example.cashpilot.ui.screens

import android.content.ClipData.Item
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cashpilot.R
import com.example.cashpilot.ui.components.Header
import com.example.cashpilot.ui.components.StyledTextField
import com.example.cashpilot.viewmodel.CreditViewModel
import com.example.cashpilot.data.AppDatabase
import androidx.compose.ui.platform.LocalContext
import com.example.cashpilot.viewmodel.CreditViewModelFactory
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


@Composable
fun CreditScreen(navController: NavController, userName: String) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getInstance(context).creditOfferDao() }
    val factory = remember { CreditViewModelFactory(dao) }
    val viewModel: CreditViewModel = viewModel(factory = factory)

    val amount by viewModel.amount.collectAsState()
    val term by viewModel.termMonths.collectAsState()
    val offers by viewModel.offers.collectAsState()


    var selectedCurrency by remember { mutableStateOf("₽") }
    val currencies = listOf("₽", "$", "€")

    var selectedTerm by remember { mutableStateOf("любой") }
    val termOptions =
        listOf("любой", "до 1 года", "1-3 года", "более 3 лет", "более 10 лет", "свой")
    var customTerm by remember { mutableStateOf("") }

    var showFilters by remember { mutableStateOf(false) }

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


                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF292929)),
                    shape = RoundedCornerShape(32.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clickable { navController.navigate("deposit/$userName") }
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
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_deposit),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                            }


                            Text(
                                "Кредит",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

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
                                    Spacer(modifier = Modifier.width(5.dp))
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

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StyledTextField(
                                value = amount,
                                onValueChange = { viewModel.onAmountChange(it) },
                                label = "Сумма",
                                modifier = Modifier.width(180.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DropdownLikeTextField(
                                label = "",
                                selectedOption = selectedCurrency,
                                options = currencies,
                                onOptionSelected = { selectedCurrency = it },
                                modifier = Modifier.width(50.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            Image(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(width = 48.dp, height = 43.dp)
                                    .clickable { showFilters = true }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        DropdownLikeTextField(
                            label = "Срок",
                            selectedOption = selectedTerm,
                            options = termOptions,
                            onOptionSelected = { selectedTerm = it }
                        )
                        if (selectedTerm == "свой") {
                            Spacer(modifier = Modifier.height(8.dp))
                            StyledTextField(
                                value = customTerm,
                                onValueChange = { customTerm = it },
                                label = "Введите срок в месяцах"
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(24.dp))

                offers.forEach { offer ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = offer.bankName,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                                Text(text = "Условие: ${offer.creditType}", color = Color.White)
                                Text(text = "Сумма: ${offer.minAmount}", color = Color.White)
                                Text(text = "Срок: ${offer.term}", color = Color.White)

                        }
                    }
                }

            }
        }
    }

        if (showFilters) {
            CreditFilterBottomSheet(onDismiss = { showFilters = false })
        }
    }

@Composable
fun CreditFilterBottomSheet(onDismiss: () -> Unit) {
    var earlyRepayment by remember { mutableStateOf(false) }
    var noCollateral by remember { mutableStateOf(false) }
    var noInsurance by remember { mutableStateOf(false) }
    var onlyOnline by remember { mutableStateOf(false) }

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

                FilterCheckbox("С досрочным погашением", earlyRepayment) { earlyRepayment = it }
                FilterCheckbox("Без залога", noCollateral) { noCollateral = it }
                FilterCheckbox("Без страховки", noInsurance) { noInsurance = it }
                FilterCheckbox("Онлайн оформление", onlyOnline) { onlyOnline = it }

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

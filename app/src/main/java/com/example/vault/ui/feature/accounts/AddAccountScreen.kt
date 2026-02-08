package com.example.vault.ui.feature.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vault.data.local.entity.AccountType
import com.example.vault.ui.theme.EmeraldGrowth
import com.example.vault.ui.theme.RoyalAccent
import com.example.vault.domain.model.Currency

@Composable
fun AddAccountScreen(
    navController: NavController,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(AccountType.CASH) }
    var selectedColor by remember { mutableStateOf(RoyalAccent) }
    var selectedCurrency by remember { mutableStateOf(Currency.USD) }

    val colors = listOf(
        Color(0xFFEF4444), Color(0xFFF97316), Color(0xFFF59E0B),
        Color(0xFF10B981), Color(0xFF06B6D4), Color(0xFF3B82F6),
        Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFFEC4899)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Account") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (name.isNotBlank()) {
                            viewModel.createAccount(
                                name = name,
                                type = selectedType.name,
                                initialBalance = balance,
                                color = String.format("#%06X", (0xFFFFFF and selectedColor.toArgb())),
                                currencyCode = selectedCurrency.code
                            )
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = EmeraldGrowth)
                    }
                }
            )
        }
    ) { padding ->
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// ...

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Account Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balance,
                onValueChange = { balance = it },
                label = { Text("Current Balance") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Account Type Selector
            Text("Account Type", style = MaterialTheme.typography.titleMedium)
            
            // Simple Row of types (could be better UI but functional for MVP)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AccountType.values().take(3).forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.name) }
                    )
                }
            }

            // Currency Selector
            Text("Currency", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Currency.values()) { currency ->
                    FilterChip(
                        selected = selectedCurrency == currency,
                        onClick = { selectedCurrency = currency },
                        label = { Text("${currency.code} ${currency.symbol}") }
                    )
                }
            }
            
            // Color Selector
            Text("Color", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                colors.take(5).forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(color, CircleShape)
                            .clickable { selectedColor = color }
                            .then(
                                if (selectedColor == color) Modifier.padding(2.dp) else Modifier
                            )
                    )
                }
            }
        }
    }
}

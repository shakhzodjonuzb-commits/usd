package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CurrencyRate
import com.example.data.RateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class RateViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RateUiState())
    val uiState: StateFlow<RateUiState> = _uiState.asStateFlow()

    init {
        // Load default values initially
        loadRates(isInitial = true)
    }

    /**
     * Triggers rate reloading from the web.
     */
    fun loadRates(isInitial: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, syncError = null) }
            val result = RateRepository.getExchangeRates()
            
            _uiState.update { state ->
                val newRateList = result.rates
                // Locate currently selected rate inside the newly fetched list
                val newSelectedRate = newRateList.find { it.code == state.selectedCurrencyCode } 
                    ?: newRateList.firstOrNull()
                
                state.copy(
                    rates = newRateList,
                    lastUpdated = result.lastUpdated,
                    isLoading = false,
                    isLive = result.isLive,
                    usdRate = newRateList.find { it.code == "USD" },
                    selectedCurrencyCode = newSelectedRate?.code ?: "USD"
                )
            }
            
            // Re-run conversion check to align numbers after rate update
            recalculateConvertedValue(isUzsInput = false)
            
            if (!isInitial) {
                _uiState.update { it.copy(showSuccessToast = true) }
            }
        }
    }

    fun dismissToast() {
        _uiState.update { it.copy(showSuccessToast = false) }
    }

    /**
     * Changes selection style (Sotuv vs Xarid)
     */
    fun setConvertMode(mode: ConvertMode) {
        _uiState.update { it.copy(convertMode = mode) }
        recalculateConvertedValue(isUzsInput = false)
    }

    /**
     * Updates selected source foreign currency (USD, EUR, RUB)
     */
    fun setSelectedCurrency(code: String) {
        _uiState.update { it.copy(selectedCurrencyCode = code) }
        recalculateConvertedValue(isUzsInput = false)
    }

    /**
     * Handles typing in top input field (Foreign Currency)
     */
    fun onForeignAmountChanged(value: String) {
        // Allow raw digits and a single decimal point
        val sanitized = value.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(foreignAmountText = sanitized) }
        recalculateConvertedValue(isUzsInput = false)
    }

    /**
     * Handles typing in bottom input field (UZS Currency)
     */
    fun onUzsAmountChanged(value: String) {
        val sanitized = value.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(uzsAmountText = sanitized) }
        recalculateConvertedValue(isUzsInput = true)
    }

    /**
     * Core bidirectional conversion logic.
     */
    private fun recalculateConvertedValue(isUzsInput: Boolean) {
        val state = _uiState.value
        val rateObj = state.rates.find { it.code == state.selectedCurrencyCode } ?: return
        
        val activeRate = if (state.convertMode == ConvertMode.SOTUV) {
            rateObj.saleRate
        } else {
            rateObj.buyRate
        }

        if (activeRate <= 0.0) return

        if (isUzsInput) {
            // Typing in UZS -> convert to Foreign currency
            val uzsVal = state.uzsAmountText.toDoubleOrNull() ?: 0.0
            val foreignVal = if (uzsVal > 0.0) uzsVal / activeRate else 0.0
            _uiState.update {
                it.copy(
                    foreignAmountText = if (foreignVal > 0.0) formatWithDecimals(foreignVal) else ""
                )
            }
        } else {
            // Typing in Foreign (USD) -> convert to UZS
            val foreignVal = state.foreignAmountText.toDoubleOrNull() ?: 0.0
            val uzsVal = if (foreignVal > 0.0) foreignVal * activeRate else 0.0
            _uiState.update {
                it.copy(
                    uzsAmountText = if (uzsVal > 0.0) formatWithDecimals(uzsVal) else ""
                )
            }
        }
    }

    private fun formatWithDecimals(value: Double): String {
        return if (value % 1.0 == 0.0) {
            String.format(Locale.US, "%.0f", value)
        } else {
            String.format(Locale.US, "%.2f", value)
        }
    }

    /**
     * Launches external source URL in standard browser
     */
    fun openOriginalSource(context: Context) {
        try {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://agrobank.uz/uz/person/exchange_rates?exchange-rate=international")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Brauzer ochishda xatolik yuz berdi", Toast.LENGTH_SHORT).show()
        }
    }
}

enum class ConvertMode {
    SOTUV, // Sale rate (Default in photo)
    XARID  // Buy rate
}

data class RateUiState(
    val rates: List<CurrencyRate> = emptyList(),
    val usdRate: CurrencyRate? = null,
    val lastUpdated: String = "12.06.2026 11:30:00",
    val isLoading: Boolean = false,
    val isLive: Boolean = false,
    val syncError: String? = null,
    val showSuccessToast: Boolean = false,
    
    // Converter state variables
    val convertMode: ConvertMode = ConvertMode.SOTUV,
    val selectedCurrencyCode: String = "USD",
    val foreignAmountText: String = "1", // Starts with 1 USD
    val uzsAmountText: String = "12060"    // Starts with 12 060 UZS to perfectly match the photo
)

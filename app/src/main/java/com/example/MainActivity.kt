package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.CurrencyRate
import com.example.ui.ConvertMode
import com.example.ui.RateViewModel
import com.example.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel: RateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentTab by remember { mutableStateOf("rates") } // Start on rates tab
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        ImmersiveNavigationBar(
                            currentTab = currentTab,
                            onTabSelected = { currentTab = it }
                        )
                    },
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    when (currentTab) {
                        "rates" -> {
                            ExchangeScreen(
                                viewModel = viewModel,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                        "home" -> {
                            SimpleBlankSection(
                                title = "Agrobank Asosiy",
                                subtitle = "Agrobank xizmatlarining birinchi sahifasiga xush kelibsiz! Har qanday operatsiyalarni boshlash uchun quyidagi Valyuta bo'limiga o'ting.",
                                icon = Icons.Default.Home,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        "profile" -> {
                            SimpleBlankSection(
                                title = "Shaxsiy Kabinet",
                                subtitle = "Xavfsiz login operatsiyalari orqali profilingiz va shaxsiy kartalaringiz hisob-kitoblarini ko'rib boring.",
                                icon = Icons.Default.Person,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImmersiveNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        NavigationBarItem(
            selected = currentTab == "home",
            onClick = { onTabSelected("home") },
            label = { Text("Asosiy", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Asosiy"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF00159E),
                selectedTextColor = Color(0xFF00159E),
                indicatorColor = Color(0xFFDEE1FF)
            )
        )
        NavigationBarItem(
            selected = currentTab == "rates",
            onClick = { onTabSelected("rates") },
            label = { Text("Valyuta", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            icon = {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Valyuta Kurslari"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF00159E),
                selectedTextColor = Color(0xFF00159E),
                indicatorColor = Color(0xFFDEE1FF)
            )
        )
        NavigationBarItem(
            selected = currentTab == "profile",
            onClick = { onTabSelected("profile") },
            label = { Text("Kabinet", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Kabinet"
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF00159E),
                selectedTextColor = Color(0xFF00159E),
                indicatorColor = Color(0xFFDEE1FF)
            )
        )
    }
}

@Composable
fun SimpleBlankSection(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFDEE1FF), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00159E),
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF00159E),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = Color(0xFF44464F),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun ExchangeScreen(
    viewModel: RateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var isMenuExpanded by remember { mutableStateOf(false) }
    var showExchangeDialog by remember { mutableStateOf(false) }

    // Display a toast notifications on rate completion
    LaunchedEffect(uiState.showSuccessToast) {
        if (uiState.showSuccessToast) {
            Toast.makeText(context, "Valyuta kurslari yangilandi!", Toast.LENGTH_SHORT).show()
            viewModel.dismissToast()
        }
    }

    // Modal dialogue of successful exchange details
    if (showExchangeDialog) {
        val selectedRateObj = uiState.rates.find { it.code == uiState.selectedCurrencyCode }
        val conversionFactor = if (uiState.convertMode == ConvertMode.SOTUV) {
            selectedRateObj?.saleRate ?: 0.0
        } else {
            selectedRateObj?.buyRate ?: 0.0
        }

        ExchangeSuccessDialog(
            foreignAmount = uiState.foreignAmountText,
            foreignCode = uiState.selectedCurrencyCode,
            uzsAmount = uiState.uzsAmountText,
            activeRate = conversionFactor,
            mode = uiState.convertMode,
            onDismiss = { showExchangeDialog = false }
        )
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background) // Clean immersive background #F6F8FF
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Agrobank Branded Top Bar ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline, // Immersive grey-blue outline #E1E2EC
                    shape = RoundedCornerShape(12.dp)
                )
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .testTag("top_bar"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Branded live-rendered Agrobank Vector logo symbol (styled in blue)
                AgrobankLogoWidget(modifier = Modifier.size(26.dp))
                
                Spacer(modifier = Modifier.width(10.dp))
                
                Text(
                    text = "Agrobank",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary, // Distinct immersive brand blue #00159E
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = (-0.5).sp
                )
            }

            // High-premium anchor burger menu with nice dropdown with icons
            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(
                    onClick = { isMenuExpanded = true },
                    modifier = Modifier.testTag("menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu ochish",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                ) {
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Asl manba",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        onClick = {
                            isMenuExpanded = false
                            viewModel.openOriginalSource(context)
                        },
                        modifier = Modifier.testTag("menu_item_source")
                    )

                    HorizontalDivider(color = Color(0xFFF1F5F2))

                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Qayta yuklash",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        onClick = {
                            isMenuExpanded = false
                            viewModel.loadRates()
                        },
                        modifier = Modifier.testTag("menu_item_refresh")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Brand Header: "Valyuta Kurslari" ---
        Text(
            text = "Valyuta Kurslari",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary, // Beautiful deep immersive blue
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Loading state display overlay
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFFDEE1FF)) // Immersive light-blue tracker background
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.4f)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF00159E), Color(0xFF4559FF))
                            ),
                            shape = RoundedCornerShape(3.dp)
                        )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // --- Extracted Immersive Theme Elements: MB Gradient Card and Buy/Sell Grid ---
        val selectedRateObj = uiState.rates.find { it.code == uiState.selectedCurrencyCode } ?: uiState.rates.firstOrNull()
        val activeCode = selectedRateObj?.code ?: "USD"
        val activeMbRate = selectedRateObj?.mbRate ?: 12503.45
        val activeFlag = selectedRateObj?.flag ?: "🇺🇸"
        val activeBuyRate = selectedRateObj?.buyRate ?: 12450.0
        val activeSaleRate = selectedRateObj?.saleRate ?: 12560.0
        
        val trendText = if (activeCode == "USD") "+12.45 so'm (Bugun)" else if (activeCode == "EUR") "-18.20 so'm (Bugun)" else "+0.15 so'm (Bugun)"
        val isTrendUp = activeCode != "EUR"

        // Primary Immersive Card: MB Rate
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00159E), Color(0xFF4559FF))
                        )
                    )
                    .drawBehind {
                        // Ornamental blur circles matching Immersive HTML mockup
                        drawCircle(
                            color = Color.White.copy(alpha = 0.08f),
                            radius = 120.dp.toPx(),
                            center = Offset(size.width, 0f)
                        )
                    }
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Markaziy Bank kursi",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.85f),
                                letterSpacing = 1.sp
                            )
                            Row(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatRateValue(activeMbRate),
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "UZS",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        // Code flag circle / badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(activeFlag, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = activeCode,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isTrendUp) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (isTrendUp) Color(0xFFACF1CD) else Color(0xFFFFB0B0),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = trendText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Exchange Rates Grid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            // Sotib olish card (Xarid)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "SOTIB OLISH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF44464F),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = formatRateValue(activeBuyRate),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00159E),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Bank mijozdan oladi",
                        fontSize = 9.sp,
                        color = Color(0xFF767680),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Sotish card (Sotuv)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "SOTISH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF44464F),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = formatRateValue(activeSaleRate),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFB3261E),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Bank mijozga sotadi",
                        fontSize = 9.sp,
                        color = Color(0xFF767680),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // --- Card 1: Exchange Rates Table ---
        val borderModifier = Modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline, // Matching clean blue-grey stroke
            shape = RoundedCornerShape(16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(borderModifier)
                .testTag("rates_card"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Table Headers
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Valyuta",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF536357),
                        modifier = Modifier.weight(1.2f)
                    )
                    
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color(0xFF00159E), // Immersive brand blue
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Xarid",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44464F)
                        )
                    }

                    Row(
                        modifier = Modifier.weight(1F),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = Color(0xFFB3261E), // Immersive brand red
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sotuv",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44464F)
                        )
                    }

                    Row(
                        modifier = Modifier.weight(1.2f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = Color(0xFF4559FF), // Secondary blue
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "MB",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF44464F)
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFE1E2EC), thickness = 1.dp)

                // Render dynamic parsed live rates
                if (uiState.rates.isEmpty()) {
                    // Empty / Offline / loading indicator representation
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF00159E))
                    }
                } else {
                    uiState.rates.forEach { rate ->
                        val isSelected = uiState.selectedCurrencyCode == rate.code
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0xFFDEE1FF).copy(alpha = 0.6f) else Color.Transparent) // Selected blue row
                                .clickable { viewModel.setSelectedCurrency(rate.code) }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1.2f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = rate.flag,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                                Text(
                                    text = rate.code,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF1B1B1F)
                                )
                            }

                            // Buy Rate (Xarid) - Rich Blue
                            Text(
                                text = formatRateValue(rate.buyRate),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF00159E),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            // Sale Rate (Sotuv) - Rich Red
                            Text(
                                text = formatRateValue(rate.saleRate),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB3261E),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )

                            // Central Bank rate (MB)
                            Text(
                                text = formatRateValue(rate.mbRate),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF44464F),
                                modifier = Modifier.weight(1.2f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = Color(0xFFE1E2EC),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Date stamp row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Kalendar",
                        tint = Color(0xFF44464F),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Last Updated",
                            fontSize = 11.sp,
                            color = Color(0xFF767680),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = uiState.lastUpdated,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B1B1F)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Live vs Fallback label badge styled in modern light lavender
                    val badgeColor = if (uiState.isLive) Color(0xFFDEE1FF) else Color(0xFFFFD8E4)
                    val badgeTextColor = if (uiState.isLive) Color(0xFF00159E) else Color(0xFFB3261E)
                    val badgeText = if (uiState.isLive) "Agrobank online" else "Oflayn rejim"
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = badgeTextColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- Card 2: Bank valyuta konvertori ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline, // Immersive grey-blue border #E1E2EC
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("converter_card"),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .drawBehind {
                        // Subtle visual decorative geometric patterns in lavender-blue on bottom corners
                        val pathSize = 80.dp.toPx()
                        drawArc(
                            color = Color(0xFFDEE1FF).copy(alpha = 0.22f),
                            startAngle = 180f,
                            sweepAngle = 90f,
                            useCenter = false,
                            topLeft = Offset(size.width - pathSize, size.height - pathSize),
                            size = Size(pathSize * 2, pathSize * 2)
                        )
                    }
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bank valyuta konvertori",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary, // Immersive brand blue #00159E
                    modifier = Modifier.padding(bottom = 12.dp),
                    textAlign = TextAlign.Center
                )

                // Custom premium "Sotuv / Xarid" selector toggle
                Row(
                    modifier = Modifier
                        .width(200.dp)
                        .height(38.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(19.dp)
                        )
                        .background(Color.White, shape = RoundedCornerShape(19.dp))
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sotuv pill
                    val isSotuv = uiState.convertMode == ConvertMode.SOTUV
                    val sotuvBg = if (isSotuv) MaterialTheme.colorScheme.primary else Color.Transparent
                    val sotuvColor = if (isSotuv) Color.White else Color(0xFF44464F)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(17.dp))
                            .background(sotuvBg)
                            .clickable { viewModel.setConvertMode(ConvertMode.SOTUV) }
                            .testTag("toggle_sotuv"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sotuv",
                            fontSize = 14.sp,
                            fontWeight = if (isSotuv) FontWeight.Bold else FontWeight.Medium,
                            color = sotuvColor
                        )
                    }

                    // Xarid pill
                    val xaridBg = if (!isSotuv) MaterialTheme.colorScheme.primary else Color.Transparent
                    val xaridColor = if (!isSotuv) Color.White else Color(0xFF44464F)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(17.dp))
                            .background(xaridBg)
                            .clickable { viewModel.setConvertMode(ConvertMode.XARID) }
                            .testTag("toggle_xarid"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Xarid",
                            fontSize = 14.sp,
                            fontWeight = if (!isSotuv) FontWeight.Bold else FontWeight.Medium,
                            color = xaridColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Foregin source currency model details
                val selectedRate = uiState.rates.find { it.code == uiState.selectedCurrencyCode } ?: uiState.rates.firstOrNull()
                val activeFlag = selectedRate?.flag ?: "🇺🇸"

                // Box 1: Foreign input field
                var isForeignDropExpanded by remember { mutableStateOf(false) }
                
                CurrencyInputBox(
                    value = uiState.foreignAmountText,
                    onValueChange = { viewModel.onForeignAmountChanged(it) },
                    currencyLabel = uiState.selectedCurrencyCode,
                    flagEmoji = activeFlag,
                    hasDropdown = true,
                    onDropdownClick = { isForeignDropExpanded = true },
                    modifier = Modifier.testTag("foreign_input_box")
                )

                // Dropdown menu of currencies
                Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.BottomEnd)) {
                    DropdownMenu(
                        expanded = isForeignDropExpanded,
                        onDismissRequest = { isForeignDropExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        uiState.rates.forEach { rate ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(rate.flag, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${rate.code} - ${rate.name}", fontSize = 14.sp, color = Color(0xFF1B1B1F))
                                    }
                                },
                                onClick = {
                                    viewModel.setSelectedCurrency(rate.code)
                                    isForeignDropExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Box 2: UZS input field
                CurrencyInputBox(
                    value = uiState.uzsAmountText,
                    onValueChange = { viewModel.onUzsAmountChanged(it) },
                    currencyLabel = "UZS",
                    flagEmoji = "🇺🇿",
                    hasDropdown = false,
                    onDropdownClick = {},
                    modifier = Modifier.testTag("uzs_input_box")
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Exchange Now Gradient Blue/Indigo Button
                Button(
                    onClick = { showExchangeDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("exchange_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = WindowInsets.safeDrawing.asPaddingValues(), // Reset default matching colors
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF00159E), Color(0xFF4559FF))
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowOutward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Exchange Now",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Extracted Action Items Block from Immersive HTML Mockup ---
        // 1. Asl manba Tool Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(Color(0xFFFEF7FF), shape = RoundedCornerShape(20.dp))
                .clickable { viewModel.openOriginalSource(context) }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Pink icon background container reproducing HTML: bg-[#FFD8E4] text-[#31111D]
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFFFD8E4), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Asl manba",
                        tint = Color(0xFF31111D),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Asl manba",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1B1F)
                    )
                    Text(
                        text = "Agrobank.uz rasmiy sayti",
                        fontSize = 11.sp,
                        color = Color(0xFF767680)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowOutward,
                contentDescription = null,
                tint = Color(0xFF44464F),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Oxirgi yangilanish Info Card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(20.dp)
                )
                .background(Color(0xFFFEF7FF), shape = RoundedCornerShape(20.dp))
                .clickable { viewModel.loadRates() }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Blue icon background container reproducing HTML: bg-[#D3E4FF] text-[#001C3B]
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFD3E4FF), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Oxirgi yangilanish",
                        tint = Color(0xFF001C3B),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Oxirgi yangilanish",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1B1F)
                    )
                    Text(
                        text = uiState.lastUpdated,
                        fontSize = 11.sp,
                        color = Color(0xFF767680)
                    )
                }
            }
            Text(
                text = "Refresh",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00159E) // Link primary color style
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * Clean wrapper for standard currency values formatting in uzb card list (comma/dot formatting)
 */
fun formatRateValue(value: Double): String {
    return if (value % 1.0 == 0.0) {
        String.format(Locale.getDefault(), "%,.0f", value).replace(",", " ")
    } else {
        String.format(Locale.getDefault(), "%,.2f", value).replace(",", " ")
    }
}

/**
 * Beautiful custom component reproducing the currency input box styling from the picture.
 * It contains text input with a vertical divider and currency tag with dropdown handle.
 */
@Composable
fun CurrencyInputBox(
    value: String,
    onValueChange: (String) -> Unit,
    currencyLabel: String,
    flagEmoji: String,
    hasDropdown: Boolean,
    onDropdownClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary, // Immersive Brand Blue
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TextField on left side
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B1B1F)
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = "0",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB0B0B0)
                    )
                }
                innerTextField()
            }
        )

        // Vertical Divider line style from picture
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .padding(vertical = 12.dp)
                .background(MaterialTheme.colorScheme.outline) // Immersive border outline
        )

        Spacer(modifier = Modifier.width(14.dp))

        // Currency code label and dropdown arrow trigger
        Row(
            modifier = Modifier
                .then(if (hasDropdown) Modifier.clickable { onDropdownClick() } else Modifier)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = flagEmoji,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 6.dp)
            )
            Text(
                text = currencyLabel,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF1B1B1F)
            )
            if (hasDropdown) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Valyuta tanlash",
                    tint = Color(0xFF44464F),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Spacer(modifier = Modifier.width(12.dp)) // Equal spacing balance
            }
        }
    }
}

/**
 * Live-rendered vector canvas of Agrobank rotated geometric diamond logo symbol
 */
@Composable
fun AgrobankLogoWidget(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val sizePx = size.width
        val center = sizePx / 2f
        val radius = sizePx / 2f

        // The Agrobank shape is essentially a diamond (square rotated 45 degrees) center-cut
        // composed of 4 triangles or smaller meeting squares.
        rotate(degrees = 45f, pivot = Offset(center, center)) {
            val itemPadding = sizePx * 0.05f
            val half = sizePx / 2f
            val qSize = half - itemPadding

            // Top-left quadrant green square
            drawRect(
                color = Color(0xFF00159E), // Branded primary blue
                topLeft = Offset(itemPadding, itemPadding),
                size = Size(qSize, qSize)
            )

            // Bottom-right quadrant green square
            drawRect(
                color = Color(0xFF00159E),
                topLeft = Offset(half, half),
                size = Size(qSize, qSize)
            )

            // Top-right quadrant green square
            drawRect(
                color = Color(0xFF00159E),
                topLeft = Offset(half, itemPadding),
                size = Size(qSize, qSize)
            )

            // Bottom-left quadrant green square
            drawRect(
                color = Color(0xFF00159E),
                topLeft = Offset(itemPadding, half),
                size = Size(qSize, qSize)
            )

            // White central horizontal strip lines / crosses
            val stripWidth = sizePx * 0.08f
            drawRect(
                color = Color.White,
                topLeft = Offset(half - stripWidth / 2f, 0f),
                size = Size(stripWidth, sizePx)
            )

            drawRect(
                color = Color.White,
                topLeft = Offset(0f, half - stripWidth / 2f),
                size = Size(sizePx, stripWidth)
            )

            // Center tiny green diamond (creating the beautiful internal diamond logo detail!)
            val cdSize = sizePx * 0.18f
            drawRect(
                color = Color(0xFF00159E),
                topLeft = Offset(half - cdSize / 2f, half - cdSize / 2f),
                size = Size(cdSize, cdSize)
            )
        }
    }
}

/**
 * Dialog displaying conversion success details.
 */
@Composable
fun ExchangeSuccessDialog(
    foreignAmount: String,
    foreignCode: String,
    uzsAmount: String,
    activeRate: Double,
    mode: ConvertMode,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large decorative Success Icon styled in brand lavender/blue
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFDEE1FF), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                        contentDescription = null,
                        tint = Color(0xFF00159E),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                     text = "Hisob-Kitob Yakunlandi!",
                     fontSize = 18.sp,
                     fontWeight = FontWeight.Bold,
                     color = Color(0xFF1B1B1F)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val rateFormatted = String.format(Locale.getDefault(), "%,.2f", activeRate)
                Text(
                    text = "Agrobank rasmiy \"${if (mode == ConvertMode.SOTUV) "Sotuv" else "Xarid"}\" kursi bo'yicha ayirboshlandi:\n1 $foreignCode = $rateFormatted UZS",
                    fontSize = 13.sp,
                    color = Color(0xFF44464F),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Exchange calculations outline box with Immersive Background
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("O'tkazildi:", fontSize = 13.sp, color = Color(0xFF44464F))
                        Text("$foreignAmount $foreignCode", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B1B1F))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Olingan summa:", fontSize = 13.sp, color = Color(0xFF44464F))
                        Text("${if (uzsAmount.isEmpty()) "0" else uzsAmount} UZS", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color(0xFF00159E))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

package com.student.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.student.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Погода") },
                            actions = {
                                IconButton(onClick = { 
                                    // Открываем меню
                                    navController.navigate("menu")
                                }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "todayWeather",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("todayWeather") { TodayWeatherScreen(navController) }
                        composable("weeklyForecast") { WeeklyForecastScreen() }
                        composable("menu") { WeatherMenu(navController) }
                        composable("weatherAlerts") { WeatherAlertsScreen() }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherMenu(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("todayWeather") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Погода сегодня",
                    style = MaterialTheme.typography.labelLarge
                )
                Image(
                    painter = painterResource(id = R.drawable.today),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp),
                    contentDescription = "Погода сегодня",
                    contentScale = ContentScale.Fit
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("weeklyForecast") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Прогноз на неделю ",
                    style = MaterialTheme.typography.labelLarge
                )
                Image(
                    painter = painterResource(id = R.drawable.calendar),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp),
                    contentDescription = "Прогноз на неделю",
                    contentScale = ContentScale.Fit
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("weatherAlerts") },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Уведомления о погоде",
                    style = MaterialTheme.typography.labelLarge
                )
                Image(
                    painter = painterResource(id = R.drawable.alert),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp),
                    contentDescription = "Уведомления о погоде",
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun TodayWeatherScreen(navController: NavController) {
    val weatherApi = remember { WeatherServiceImplementation() }
    var weatherState by remember { mutableStateOf<WeatherResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Динамический фон в зависимости от погоды
            weatherState?.weather?.firstOrNull()?.let { weather ->
                Image(
                    painter = painterResource(id = getWeatherBackground(weather.description)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.default_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Градиентный оверлей
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            LaunchedEffect(Unit) {
                try {
                    withContext(Dispatchers.IO) {
                        weatherState = weatherApi.getCurrentWeather("Yoshkar-Ola", "137ae56b74c9930e6ffec69fe8e12ee3")
                    }
                    isLoading = false
                } catch (e: Exception) {
                    errorMessage = when {
                        e.message?.contains("Unable to resolve host") == true ->
                            "Нет подключения к интернету. Проверьте соединение."
                        else -> "Ошибка: ${e.message}"
                    }
                    isLoading = false
                }
            }

            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn() + slideInVertically(),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when {
                        errorMessage != null -> {
                            ErrorCard(errorMessage!!) {
                                errorMessage = null
                                isLoading = true
                                weatherState = null
                            }
                        }
                        weatherState != null -> {
                            WeatherContent(weatherState!!, navController)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LoadingIndicator()
            }
        }
    }
}

@Composable
private fun WeatherContent(weather: WeatherResponse, navController: NavController) {
    val isLandscape = isLandscape()

    if (isLandscape) {
        // Горизонтальная ориентация
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Левая колонка с основной информацией
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = translateCityName(weather.name),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier.padding(top = 32.dp)
                )

                CurrentTime()

                Card(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getWeatherEmoji(weather.weather[0].description),
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = " ${weather.main.temp.toInt()}°C ",
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Text(
                            text = translateWeatherDescription(weather.weather[0].description),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    }
                }
            }

            // Правая колонка с дополнительной информацией
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WeatherInfoCard(
                        icon = R.drawable.humidity,
                        value = "${weather.main.humidity}%",
                        label = "Влажность",
                        modifier = Modifier.weight(1f)
                    )
                    WeatherInfoCard(
                        icon = R.drawable.wind,
                        value = "${weather.main.wind} м/с",
                        label = "Ветер",
                        modifier = Modifier.weight(1f)
                    )
                    WeatherInfoCard(
                        icon = R.drawable.pressure,
                        value = "${(weather.main.pressure * 0.750062).toInt()} мм",
                        label = "Давление",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.navigate("weeklyForecast") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    )
                ) {
                    Text(
                        text = "Прогноз на неделю",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    } else {
        // Портретная ориентация
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Город и дата
            Text(
                text = translateCityName(weather.name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                modifier = Modifier.padding(top = 32.dp)
            )

            CurrentTime()

            // Температура и погода
            Card(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .fillMaxWidth()
                    .animateContentSize(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = getWeatherEmoji(weather.weather[0].description),
                        fontSize = 72.sp,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${weather.main.temp.toInt()}°C",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.White,
                    )
                    Text(
                        text = translateWeatherDescription(weather.weather[0].description),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }

            // Дополнительная информация
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeatherInfoCard(
                    icon = R.drawable.humidity,
                    value = "${weather.main.humidity}%",
                    label = "Влажность",
                    modifier = Modifier.weight(1f)
                )
                WeatherInfoCard(
                    icon = R.drawable.wind,
                    value = "${weather.main.wind} м/с",
                    label = "Ветер",
                    modifier = Modifier.weight(1f)
                )
                WeatherInfoCard(
                    icon = R.drawable.pressure,
                    value = "${(weather.main.pressure * 0.750062).toInt()} мм",
                    label = "Давление",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("weeklyForecast") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                )
            ) {
                Text(
                    text = "Прогноз на неделю",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun WeatherInfoCard(
    @DrawableRes icon: Int,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp),
                tint = Color.White
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Загрузка погоды...",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ErrorCard(error: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Повторить")
            }
        }
    }
}

private fun getCurrentDateTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("d MMMM, HH:mm", Locale("ru"))
    return current.format(formatter)
}

@Composable
fun WeeklyForecastScreen() {
    val weatherApi = remember { WeatherServiceImplementation() }
    var forecastState by remember { mutableStateOf<List<Forecast>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Динамический фон
            forecastState?.firstOrNull()?.weather?.firstOrNull()?.let { weather ->
                Image(
                    painter = painterResource(id = getWeatherBackground(weather.description)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                painter = painterResource(id = R.drawable.default_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Градиентный оверлей
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            LaunchedEffect(Unit) {
                try {
                    withContext(Dispatchers.IO) {
                        forecastState = weatherApi.getWeeklyForecast("Yoshkar-Ola", "137ae56b74c9930e6ffec69fe8e12ee3")?.list
                    }
                } catch (e: Exception) {
                    errorMessage = when {
                        e.message?.contains("Unable to resolve host") == true ->
                            "Нет подключения к интернету. Проверьте соединение."
                        else -> "Ошибка: ${e.message}"
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = if (isLandscape()) 8.dp else 16.dp
                    )
            ) {
                Text(
                    text = "Прогноз погоды на неделю",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (isLandscape()) 8.dp else 16.dp),
                    fontSize = if (isLandscape()) 20.sp else 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                    )
                ) {
                    when {
                        errorMessage != null -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = errorMessage ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    errorMessage = null
                                    forecastState = null
                                }) {
                                    Text("Повторить")
                                }
                            }
                        }
                        forecastState != null -> {
                            LazyColumn(
                                modifier = Modifier.padding(
                                    vertical = if (isLandscape()) 4.dp else 8.dp
                                )
                            ) {
                                items(forecastState!!) { dailyForecast ->
                                    Card(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                            .fillMaxWidth(),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                                            )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                Text(
                                                    text = getWeatherEmoji(dailyForecast.weather[0].description),
                                                    fontSize = if (isLandscape()) 24.sp else 32.sp,
                                                    modifier = Modifier.padding(4.dp),
                                                    textAlign = TextAlign.Center
                                                )
                                                Column {
                                                    Text(
                                                        text = formatDateTime(dailyForecast.dt_txt),
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                    Text(
                                                        text = translateWeatherDescription(dailyForecast.weather[0].description),
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                            }
                                            Text(
                                                text = "${dailyForecast.main.temp.toInt()}°C",
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        else -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Загрузка прогноза...")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDateTime(dateTime: String): String {
    // Простое форматирование даты и времени
    val parts = dateTime.split(" ")
    val dateParts = parts[0].split("-")
    return "${dateParts[2]}.${dateParts[1]} ${parts[1].substring(0, 5)}"
}

private fun translateWeatherDescription(description: String): String {
    return when (description.lowercase()) {
        "clear sky" -> "Ясно"
        "few clouds" -> "Малооблачно"
        "scattered clouds" -> "Рассеянные облака"
        "broken clouds" -> "Облачно с прояснениями"
        "shower rain" -> "Кратковременный дождь"
        "rain" -> "Дождь"
        "thunderstorm" -> "Гроза"
        "snow" -> "Снег"
        "light snow" -> "Небольшой снег"
        "moderate snow" -> "Умеренный снег"
        "heavy snow" -> "Сильный снег"
        "sleet" -> "Мокрый снег"
        "light rain" -> "Небольшой дождь"
        "moderate rain" -> "Умеренный дождь"
        "heavy intensity rain" -> "Сильный дождь"
        "very heavy rain" -> "Очень сильный дождь"
        "extreme rain" -> "Экстремальный дождь"
        "freezing rain" -> "Ледяной дождь"
        "light intensity shower rain" -> "Небольшой ливень"
        "heavy intensity shower rain" -> "Сильный ливень"
        "mist" -> "Туман"
        "smoke" -> "Дымка"
        "haze" -> "Мгла"
        "fog" -> "Туман"
        "sand/dust whirls" -> "Песчаная буря"
        "overcast clouds" -> "Пасмурно"
        "light thunderstorm" -> "Небольшая гроза"
        "thunderstorm with light rain" -> "Гроза с небольшим дождем"
        "thunderstorm with rain" -> "Гроза с дождем"
        "thunderstorm with heavy rain" -> "Гроза с сильным дождем"
        else -> description // Если перевод не найден, возвращаем оригинальное описание
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
    }
}

@Composable
private fun getWeatherBackground(weatherDescription: String): Int {
    return when (weatherDescription.lowercase()) {
        "clear sky" -> R.drawable.sunny_bg
        "few clouds", "scattered clouds" -> R.drawable.partly_cloudy_bg
        "broken clouds", "overcast clouds" -> R.drawable.cloudy_bg
        "shower rain", "rain", "light rain", "moderate rain" -> R.drawable.rainy_bg
        "thunderstorm" -> R.drawable.storm_bg
        "snow" -> R.drawable.snow_bg
        "mist" -> R.drawable.foggy_bg
        else -> R.drawable.default_bg
    }
}

private fun translateCityName(englishName: String): String {
    return when (englishName) {
        "Yoshkar-Ola" -> "Йошкар-Ола"
        "Moscow" -> "Москва"
        "Saint Petersburg" -> "Санкт-Петербург"
        "Kazan" -> "Казань"
        // Добавьте другие города по необходимости
        else -> englishName
    }
}

@Composable
private fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@SuppressLint("PrivateResource")
@Composable
fun WeatherAlertsScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)

    var rainNotification by remember { 
        mutableStateOf(sharedPreferences.getBoolean("rain_notification", true))
    }
    var stormNotification by remember { 
        mutableStateOf(sharedPreferences.getBoolean("storm_notification", true))
    }
    var snowNotification by remember { 
        mutableStateOf(sharedPreferences.getBoolean("snow_notification", false))
    }
    var temperatureNotification by remember { 
        mutableStateOf(sharedPreferences.getBoolean("temperature_notification", false))
    }
    var windNotification by remember { 
        mutableStateOf(sharedPreferences.getBoolean("wind_notification", false))
    }

    // Функция для сохранения состояния
    fun saveNotificationState(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.foggy_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Настройка уведомлений",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        WeatherAlertItem(
                            emoji = "🌧",
                            title = "Уведомления о дожде",
                            subtitle = "Предупреждать о приближающемся дожде",
                            checked = rainNotification,
                            onCheckedChange = { 
                                rainNotification = it
                                saveNotificationState("rain_notification", it)
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        )

                        WeatherAlertItem(
                            emoji = "⛈",
                            title = "Штормовое предупреждение",
                            subtitle = "Уведомлять о приближении грозы или шторма",
                            checked = stormNotification,
                            onCheckedChange = { 
                                stormNotification = it
                                saveNotificationState("storm_notification", it)
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        )

                        WeatherAlertItem(
                            emoji = "🌨",
                            title = "Уведомления о снеге",
                            subtitle = "Предупреждать о снегопаде",
                            checked = snowNotification,
                            onCheckedChange = { 
                                snowNotification = it
                                saveNotificationState("snow_notification", it)
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        )

                        WeatherAlertItem(
                            emoji = "🌡",
                            title = "Экстремальная температура",
                            subtitle = "Уведомлять о резких изменениях температуры",
                            checked = temperatureNotification,
                            onCheckedChange = { 
                                temperatureNotification = it
                                saveNotificationState("temperature_notification", it)
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        )

                        WeatherAlertItem(
                            emoji = "💨",
                            title = "Сильный ветер",
                            subtitle = "Предупреждать о сильном ветре",
                            checked = windNotification,
                            onCheckedChange = { 
                                windNotification = it
                                saveNotificationState("wind_notification", it)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Уведомления помогут вам быть в курсе важных изменений погоды",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun WeatherAlertItem(
    emoji: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 32.sp,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun CurrentTime() {
    var currentTime by remember { mutableStateOf(getCurrentDateTime()) }

    LaunchedEffect(Unit) {
        while(true) {
            currentTime = getCurrentDateTime()
            delay(60000) // Обновляем каждую минуту (60000 миллисекунд)
        }
    }

    Text(
        text = currentTime,
        style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
        modifier = Modifier.padding(top = 8.dp)
    )
}


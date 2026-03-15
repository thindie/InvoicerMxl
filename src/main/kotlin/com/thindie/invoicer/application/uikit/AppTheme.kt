package com.thindie.invoicer.application.uikit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Immutable
class InvoicerColors(
  val contentPrimary: Color,
  val contentSecondary: Color,
  val buttonContentPrimary: Color,
  val backgroundPrimary: Color,
  val backgroundSecondary: Color,
  val accentPrimary: Color,
  val onAccentPrimary: Color,
  val successPrimary: Color,
  val errorPrimary: Color,
)

private val LightColorScheme = InvoicerColors(
  contentPrimary = Color(0xFF1A1A1A),
  contentSecondary = Color(0xFF757575),
  backgroundPrimary = Color.White,
  backgroundSecondary = Color(0xFFF5F7FA),
  accentPrimary = Color(0xFF4766FF),
  onAccentPrimary = Color(0xFFF5F7FA),
  successPrimary = Color(0xFF2E7D32),
  errorPrimary = Color(0xFFD32F2F),
  buttonContentPrimary = Color(0xFFF5F7FA)
)

private val DarkColorScheme = InvoicerColors(
  contentPrimary = Color(0xFFF5F5F5),
  contentSecondary = Color(0xFF9E9E9E),
  backgroundPrimary = Color(0xFF121212),
  backgroundSecondary = Color(0xFF1E1E1E),
  accentPrimary = Color(0xFF9DADFF),
  onAccentPrimary = Color(0xFF121212),
  successPrimary = Color(0xFF4CAF50),
  errorPrimary = Color(0xFFF44336),
  buttonContentPrimary = Color(0xFF121212)
)

class ThemeSwitcher {
  private val _themeFlow =
	MutableSharedFlow<Choice>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST, extraBufferCapacity = 1)
  val themeFlow = _themeFlow.asSharedFlow()

  enum class Choice {
	Dark,
	Light,
	Auto
  }

  fun set(choice: Choice) {
	_themeFlow.tryEmit(choice)
  }
}

private val LocalInvoicerColors = compositionLocalOf { LightColorScheme }
private val LocalInvoicerTypo = staticCompositionLocalOf { InvoicerTypography }
val LocalThemeSwitcher = staticCompositionLocalOf { ThemeSwitcher() }


object InvoicerTheme {
  val colors: InvoicerColors
	@Composable
	@ReadOnlyComposable
	get() = LocalInvoicerColors.current

  val typography: InvoicerTypography
	@Composable
	@ReadOnlyComposable
	get() = LocalInvoicerTypo.current
}

@Composable
fun InvoicerTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val targetColors = if (darkTheme) DarkColorScheme else LightColorScheme

  val colorSpec: AnimationSpec<Color> = tween(durationMillis = 400)

  @Composable
  fun animateColor(target: Color) = animateColorAsState(target, colorSpec, label = "color").value

  val animatedColors = InvoicerColors(
	contentPrimary = animateColor(targetColors.contentPrimary),
	contentSecondary = animateColor(targetColors.contentSecondary),
	backgroundPrimary = animateColor(targetColors.backgroundPrimary),
	backgroundSecondary = animateColor(targetColors.backgroundSecondary),
	accentPrimary = animateColor(targetColors.accentPrimary),
	successPrimary = animateColor(targetColors.successPrimary),
	onAccentPrimary = animateColor(targetColors.onAccentPrimary),
	errorPrimary = animateColor(targetColors.errorPrimary),
	buttonContentPrimary = animateColor(targetColors.buttonContentPrimary),
  )

  CompositionLocalProvider(
	LocalInvoicerColors provides animatedColors,
	LocalInvoicerTypo provides InvoicerTypography
  ) {
	content()
  }
}


private val nunitoFamily = FontFamily(Font("font/nunito.ttf"))


@Immutable
object InvoicerTypography {
  val headlineLarge = TextStyle(
	fontSize = 40.sp,
	lineHeight = 36.sp,
	letterSpacing = 1.26.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )

  val headlineMedium = TextStyle(
	fontSize = 32.sp,
	lineHeight = 28.sp,
	letterSpacing = 1.24.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )

  val headlineSmall = TextStyle(
	fontSize = 24.sp,
	lineHeight = 28.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )

  val titleLarge = TextStyle(
	fontSize = 20.sp,
	lineHeight = 24.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )

  val titleMedium = TextStyle(
	fontSize = 18.sp,
	lineHeight = 22.sp,
	letterSpacing = 1.2.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W500,
  )
  val titleSmall = TextStyle(
	fontSize = 16.sp,
	lineHeight = 20.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W700,
  )

  val button = TextStyle(
	fontSize = 16.sp,
	lineHeight = 20.sp,
	letterSpacing = 1.2.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W700,
  )


  val bodyMedium = TextStyle(
	fontSize = 14.sp,
	lineHeight = 18.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W500,
  )

  val bodySmall = TextStyle(
	fontSize = 14.sp,
	lineHeight = 18.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )

  val labelLarge = TextStyle(
	fontSize = 12.sp,
	lineHeight = 16.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )

  val labelMedium = TextStyle(
	fontSize = 10.sp,
	lineHeight = 14.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )
}


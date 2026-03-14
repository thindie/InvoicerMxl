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
  val backgroundPrimary: Color,
  val backgroundSecondary: Color,
  val accentPrimary: Color,
  val successPrimary: Color,
  val errorPrimary: Color,
)

private val LightColorScheme = InvoicerColors(
  contentPrimary = Color.Black,
  contentSecondary = Color.LightGray,
  backgroundPrimary = Color.White,
  backgroundSecondary = Color(0xFFDCDCDC),
  accentPrimary = Color(0xFF7593FF),
  successPrimary = Color(0xFF47C158),
  errorPrimary = Color(0xFFC15947)
)

private val DarkColorScheme = InvoicerColors(
  contentPrimary = Color.White,
  contentSecondary = Color.LightGray,
  backgroundPrimary = Color.Black,
  backgroundSecondary = Color(0xFF030717),
  accentPrimary = Color(0xFF243772),
  successPrimary = Color(0xFF1A401A),
  errorPrimary = Color(0xFF55281F)
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
	errorPrimary = animateColor(targetColors.errorPrimary)
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
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )

  val headlineMedium = TextStyle(
	fontSize = 32.sp,
	lineHeight = 28.sp,
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
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W500,
  )
  val titleSmall = TextStyle(
	fontSize = 16.sp,
	lineHeight = 20.sp,
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

  val labelMedium = TextStyle(
	fontSize = 10.sp,
	lineHeight = 14.sp,
	fontFamily = nunitoFamily,
	fontWeight = FontWeight.W400,
  )
}


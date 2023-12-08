package ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp


private val nunitoFamily = FontFamily(Font("font/nunito.ttf"))


internal val InvoicerTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 40.sp,
        lineHeight = 36.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W400,
    ),

    headlineMedium = TextStyle(
        fontSize = 32.sp,
        lineHeight = 28.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W400,
    ),

    headlineSmall = TextStyle(
        fontSize = 24.sp,
        lineHeight = 28.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W400,
    ),

    titleLarge = TextStyle(
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W400,
    ),

    titleMedium = TextStyle(
        fontSize = 18.sp,
        lineHeight = 22.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W500,
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W700,
    ),


    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W500,
    ),

    bodySmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 18.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W400,
    ),

    labelMedium = TextStyle(
        fontSize = 10.sp,
        lineHeight = 14.sp,
        fontFamily = nunitoFamily,
        fontWeight = FontWeight.W400,
    )
)
package ui.feature_from_from_central_invoice.screen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ScreenHint(modifier: Modifier = Modifier, hint: String) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text =
            hint,
            style = MaterialTheme.typography.labelMedium.copy(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Justify,

                ), modifier = modifier.padding(horizontal = 20.dp)
        )
    }
}

val invoiceScreenHint: String =
    "Здесь мы можем, используя текстовой файл, сгенерированный 1С 7.7 внешней обработкой 'писанина' \n " +
            "сформировать MXL файл для заказа поставщику, а также, используя инвентаризацию по нашему складу -> \n" +
            "сделать заказ из наиболее продающихся товаров с другого склада, которые мы не имеем в наличии"
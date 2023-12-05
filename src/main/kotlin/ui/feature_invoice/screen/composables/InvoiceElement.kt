package ui.feature_invoice.screen.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun InvoiceElement(
    modifier: Modifier = Modifier,
    pathTitle: String,
    currentPath: String?,
    onClickDismiss: () -> Unit,
    onClickConfirm: () -> Unit
) {

    val currentTitle by derivedStateOf { currentPath ?: "" }

    AnimatedVisibility(currentTitle.isNotBlank()) {

        Row(
            modifier = modifier
                .height(36.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colors.background,
                            MaterialTheme.colors.primary
                        )
                    )
                )
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = pathTitle, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.W900))
            Text(
                text = currentTitle,
                style = MaterialTheme.typography.subtitle2.copy(
                    color = MaterialTheme.colors.background,
                    fontWeight = FontWeight.W900
                )
            )

            Row(modifier = modifier.height(24.dp)) {

                Button(
                    onClick = onClickConfirm,
                    contentPadding = PaddingValues(2.dp)
                ) {
                    Icon(painter = rememberVectorPainter(Icons.Default.Build), null)
                    Text("Создать")
                }
                IconButton(onClickDismiss) {
                    Icon(
                        painter = rememberVectorPainter(
                            Icons.Default.Delete,
                            ), null,
                        tint = MaterialTheme.colors.background
                    )
                }
            }
        }
    }
}

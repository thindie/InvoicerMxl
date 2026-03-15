package com.thindie.invoicer.application.uikit

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.*

@Composable
fun Button(
  modifier: Modifier = Modifier,
  text: String,
  onClick: () -> Unit,
  loading: Boolean = false,
  enabled: Boolean = true,
) {
  val contentColor by animateColorAsState(
	if (enabled) InvoicerTheme.colors.buttonContentPrimary
	else InvoicerTheme.colors.buttonContentPrimary.copy(alpha = ContentAlpha.disabled)
  )

  val backgroundColor by animateColorAsState(
	if (enabled) InvoicerTheme.colors.accentPrimary
	else InvoicerTheme.colors.accentPrimary.copy(alpha = ContentAlpha.disabled)
  )
  Box(
	modifier = modifier
	  .height(52.dp)
	  .widthIn(min = 328.dp)
	  .surface(
		shape = RoundedCornerShape(16.dp),
		backgroundColor = backgroundColor,
		shadowElevation = 0f,
		border = null
	  )
	  .clickable(
		enabled = enabled && !loading,
		indication = null,
		interactionSource = remember { MutableInteractionSource() },
		onClick = onClick
	  )
	  .fillMaxSize()
	  .padding(horizontal = 24.dp),
	contentAlignment = Alignment.Center
  ) {
	AnimatedVisibility(
	  visible = loading,
	  enter = fadeIn(),
	  exit = fadeOut()
	) {
	  CircularProgress(
		modifier = Modifier.size(24.dp),
	  )
	}

	AnimatedVisibility(
	  visible = !loading,
	  enter = fadeIn(),
	  exit = fadeOut()
	) {
	  Text(
		text = text.uppercase(),
		style = InvoicerTheme.typography.button,
		color = contentColor,
		textAlign = TextAlign.Center
	  )
	}
  }
}

@Stable
fun Modifier.surface(
  shape: Shape = RoundedCornerShape(16.dp),
  backgroundColor: Color = Color.Transparent,
  border: BorderStroke? = null,
  shadowElevation: Float = 0f,
  enabled: Boolean = true,
  onClick: (() -> Unit)? = null,
) = this
  .graphicsLayer(shadowElevation = shadowElevation, shape = shape, clip = false)
  .then(if (border != null) Modifier.border(border, shape) else Modifier)
  .background(color = backgroundColor, shape = shape)
  .clip(shape)
  .clickable(
	onClick = if (onClick != null && enabled) onClick else {
	  { }
	},
	enabled = onClick != null && enabled,
  )


@Composable
fun <S : State, C : Command> ScreenScope<S, C>.ErrorMessage() {
  val error = this@ErrorMessage.error.value ?: return
  Column(
	modifier = Modifier
	  .fillMaxSize()
	  .padding(16.dp)
	  .verticalScroll(rememberScrollState()),
	verticalArrangement = Arrangement.Center,
	horizontalAlignment = Alignment.CenterHorizontally,
  ) {
	Text(
	  text = error.message,
	  style = InvoicerTheme.typography.titleMedium,
	)
	Row(
	  horizontalArrangement = Arrangement.spacedBy(8.dp),
	  modifier = Modifier.padding(top = 16.dp),
	) {
	  error.actions[ScreenScopeError.Actions.Common.DismissMain]?.let { cmd ->
		Button(
		  text = "Dismiss",
		  onClick = {
			when {
			  cmd as? ServiceCommand.Prioritized != null -> cmd.execute()
			  else -> send(cmd as C)
			}
		  },
		  loading = processing.value == cmd
		)
	  }
	  error.actions[ScreenScopeError.Actions.Common.ButtonSecondaryRetry]?.let { cmd ->
		val action = error.actions.keys.filterIsInstance<ScreenScopeError.Actions.Common>()
		  .first { it is ScreenScopeError.Actions.Common.ButtonSecondaryRetry }
		Button(
		  text = action?.title.orEmpty(),
		  onClick = {
			when {
			  cmd as? ServiceCommand.Prioritized != null -> cmd.execute()
			  else -> send(cmd as C)
			}
		  },
		  loading = processing.value == cmd
		)
	  }
	  error.actions[ScreenScopeError.Actions.Common.ButtonMain]?.let { cmd ->
		val action = error.actions.keys.filterIsInstance<ScreenScopeError.Actions.Common>()
		  .first { it is ScreenScopeError.Actions.Common.ButtonMain }
		Button(
		  text = action?.title.orEmpty(),
		  onClick = {
			when {
			  cmd as? ServiceCommand.Prioritized != null -> cmd.execute()
			  else -> send(cmd as C)
			}
		  },
		  loading = processing.value == cmd
		)
	  }
	}
  }
}

@Composable
fun SentenceRow(
  modifier: Modifier = Modifier,
  painter: Painter?,
  title: String,
  subtitle: String?,
  enabled: Boolean = true,
  loading: Boolean?,
  onClick: (() -> Unit)? = null,
) {
  val colorsPrimary = if (enabled) InvoicerTheme.colors.backgroundPrimary else {
	InvoicerTheme.colors.backgroundPrimary.copy(alpha = ContentAlpha.disabled)
  }

  val tint = if (enabled) InvoicerTheme.colors.accentPrimary else {
	InvoicerTheme.colors.accentPrimary.copy(alpha = ContentAlpha.disabled)
  }

  val colorsSecondary = if (enabled) InvoicerTheme.colors.backgroundSecondary else {
	InvoicerTheme.colors.backgroundSecondary.copy(alpha = ContentAlpha.disabled)
  }
  Row(
	modifier = modifier
	  .background(color = colorsPrimary, shape = RoundedCornerShape(20.dp))
	  .clip(shape = RoundedCornerShape(20.dp))
	  .clickable(
		enabled = enabled,
		onClick = if (onClick != null && enabled) {
		  onClick
		} else {
		  {}
		},
	  )
	  .padding(horizontal = 16.dp, vertical = 8.dp),
	verticalAlignment = Alignment.CenterVertically,
  ) {
	if (loading != null) {
	  Crossfade(targetState = loading) { loading ->
		if (loading) {
		  CircularProgress(
			modifier = Modifier
			  .size(24.dp),
		  )
		} else {
		  if (painter == null) {
			HSpacer(40.dp)
		  } else {
			Icon(
			  painter = painter,
			  contentDescription = null,
			  modifier = Modifier
				.background(color = colorsSecondary, shape = RoundedCornerShape(20.dp))
				.padding(8.dp)
				.size(32.dp),
			  tint = tint
			)
		  }
		}
	  }
	} else {
	  if (painter == null) {
		HSpacer(40.dp)
	  } else {
		Icon(
		  painter = painter,
		  contentDescription = null,
		  modifier = Modifier
			.background(color = colorsSecondary, shape = RoundedCornerShape(20.dp))
			.padding(8.dp)
			.size(32.dp),
		  tint = tint
		)
	  }
	}
	HSpacer(12.dp)
	if (subtitle != null) {
	  Column {
		Text(
		  text = title,
		  style = InvoicerTheme.typography.titleMedium,
		  color = InvoicerTheme.colors.contentPrimary
		)
		VSpacer(2.dp)
		Text(
		  text = subtitle,
		  style = InvoicerTheme.typography.bodyMedium,
		  color = InvoicerTheme.colors.contentSecondary
		)
	  }
	} else {
	  Text(
		text = title,
		style = InvoicerTheme.typography.titleMedium,
		color = InvoicerTheme.colors.contentPrimary
	  )
	}
  }
}

@Composable
fun CircularProgress(modifier: Modifier = Modifier) {
  CircularProgressIndicator(
	modifier = modifier,
	color = InvoicerTheme.colors.accentPrimary,
	strokeWidth = 1.2.dp,
	strokeCap = StrokeCap.Round
  )
}

@Composable
fun TopAppBar(
  title: String? = null,
  description: String? = null,
  onBack: (() -> Unit)? = null,
  onClose: (() -> Unit)? = null,
) {
  Row(
	modifier = Modifier.fillMaxWidth(),
	horizontalArrangement = Arrangement.SpaceBetween,
	verticalAlignment = Alignment.CenterVertically,
  ) {
	if (onBack != null) {
	  IconButton(onClick = onBack) {
		Icon(
		  painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
		  contentDescription = null,
		  tint = InvoicerTheme.colors.accentPrimary,
		)
	  }
	} else HSpacer(12.dp)
	if (description != null) {
	  Column(
		horizontalAlignment = Alignment.CenterHorizontally,
	  ) {
		Text(
		  text = title.orEmpty(),
		  style = InvoicerTheme.typography.titleLarge,
		  color = InvoicerTheme.colors.contentSecondary,
		)
		VSpacer(2.dp)
		Text(
		  text = description,
		  style = InvoicerTheme.typography.labelLarge,
		  color = InvoicerTheme.colors.accentPrimary,
		)
	  }
	} else {
	  Text(
		text = title.orEmpty(),
		style = InvoicerTheme.typography.titleLarge,
		color = InvoicerTheme.colors.contentSecondary,
	  )
	}
	if (onClose != null) {
	  IconButton(onClick = onClose) {
		Icon(
		  painter = rememberVectorPainter(image = Icons.Default.Close),
		  contentDescription = null,
		  tint = InvoicerTheme.colors.accentPrimary,
		)
	  }
	} else HSpacer(12.dp)
  }
}

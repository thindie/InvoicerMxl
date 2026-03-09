package com.thindie.invoicer.application.uikit

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.thindie.invoicer.application.Command
import com.thindie.invoicer.application.ScreenScope
import com.thindie.invoicer.application.ScreenScopeError
import com.thindie.invoicer.application.State

@Composable
fun Button(
  modifier: Modifier = Modifier,
  text: String,
  onClick: () -> Unit,
  loading: Boolean? = null,
  enabled: Boolean = true,
) {
  val colors = if (enabled) MaterialTheme.colorScheme.secondaryContainer else {
	MaterialTheme.colorScheme.secondaryContainer.copy(alpha = ContentAlpha.disabled)
  }
  Row(
	modifier = modifier
	  .height(52.dp)
	  .background(color = colors, shape = RoundedCornerShape(20.dp))
	  .clip(shape = RoundedCornerShape(20.dp))
	  .clickable(
		enabled = enabled,
		onClick = onClick,
	  )
	  .padding(horizontal = 16.dp, vertical = 8.dp),
	verticalAlignment = Alignment.CenterVertically,
	horizontalArrangement = Arrangement.SpaceEvenly
  ) {
	if (loading != null) {
	  Crossfade(targetState = loading) { loading ->
		if (loading) {
		  CircularProgress(
			modifier = Modifier.size(24.dp)
		  )
		} else {
		  Unit
		}
	  }
	}
	Text(
	  text = text,
	)
  }
}

@Composable
fun <S : State, C : Command> ScreenScope<S, C>.ErrorMessage() {
  val error = this@ErrorMessage.error.value ?: return
  Column(
	modifier = Modifier
	  .fillMaxSize()
	  .verticalScroll(rememberScrollState()),
	verticalArrangement = Arrangement.Center,
	horizontalAlignment = Alignment.CenterHorizontally,
  ) {
	Text(
	  text = error.message,
	  style = MaterialTheme.typography.bodyLarge,
	)
	Row(
	  horizontalArrangement = Arrangement.spacedBy(8.dp),
	  modifier = Modifier.padding(top = 16.dp),
	) {
	  error.actions[ScreenScopeError.Actions.DismissMain]?.let { cmd ->
		Button(
		  text = "Dismiss",
		  onClick = { send(cmd as C) },
		  loading = processing.value == cmd
		)
	  }
	  error.actions[ScreenScopeError.Actions.ButtonSecondary]?.let { cmd ->
		Button(
		  text = "Secondary",
		  onClick = { send(cmd as C) },
		  loading = processing.value == cmd
		)
	  }
	  error.actions[ScreenScopeError.Actions.ButtonMain]?.let { cmd ->
		Button(
		  text = "Retry",
		  onClick = { send(cmd as C) },
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
  val colorsPrimary = if (enabled) MaterialTheme.colorScheme.surfaceContainer else {
	MaterialTheme.colorScheme.surfaceContainer.copy(alpha = ContentAlpha.disabled)
  }

  val tint = if (enabled) MaterialTheme.colorScheme.surfaceTint else {
	MaterialTheme.colorScheme.surfaceTint.copy(alpha = ContentAlpha.disabled)
  }

  val colorsSecondary = if (enabled) MaterialTheme.colorScheme.onPrimary else {
	MaterialTheme.colorScheme.onPrimary.copy(alpha = ContentAlpha.disabled)
  }
  Row(
	modifier = modifier
	  .background(color = colorsPrimary, shape = RoundedCornerShape(20.dp))
	  .clip(shape = RoundedCornerShape(20.dp))
	  .clickable(
		enabled = if (onClick != null) enabled else false,
		onClick = if (onClick != null) {
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
		  CircularProgressIndicator(
			modifier = Modifier
			  .size(24.dp)
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
		  style = MaterialTheme.typography.titleMedium,
		  color = MaterialTheme.colorScheme.onPrimaryContainer
		)
		VSpacer(2.dp)
		Text(
		  text = subtitle,
		  style = MaterialTheme.typography.labelSmall,
		  color = MaterialTheme.colorScheme.onTertiaryContainer
		)
	  }
	} else {
	  Text(
		text = title,
		style = MaterialTheme.typography.titleLarge,
		color = MaterialTheme.colorScheme.onPrimaryContainer
	  )
	}
  }
}

@Composable
fun CircularProgress(modifier: Modifier = Modifier) {
  CircularProgressIndicator(
	modifier = modifier,
	color = MaterialTheme.colorScheme.surfaceTint,
	strokeWidth = 1.2.dp,
	strokeCap = StrokeCap.Round
  )
}
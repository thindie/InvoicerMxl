package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import com.thindie.invoicer.application.State

@Stable
interface HowToInvoiceState : State {
  val question: String
  val answer: String?
  val extraExplanation: String?
  val imageRes: String?
  val image: ImageBitmap?
  val primaryAction: String
  val backAction: String
}

@Immutable
data class HowToIntroState(
  override val question: String = HowToStrings.questionIntro,
  override val answer: String? = HowToStrings.answerIntro,
  override val extraExplanation: String? = buildString {
	append(HowToStrings.extraItemAnnotation)
	append("\n")
	append(HowToStrings.extraIntroImportant)
	append("\n")
	append(HowToStrings.extraIntroImportantResolution)
  },
  override val imageRes: String? = "appData/common/step_0.jpg",
  override val image: ImageBitmap? = null,
  override val primaryAction: String = HowToStrings.primaryIntro,
  override val backAction: String = HowToStrings.backFromIntro,

  ) : HowToInvoiceState

@Immutable
data class HowToStep0State(
  override val question: String = HowToStrings.questionStep0,
  override val answer: String? = HowToStrings.answerStep0,
  override val extraExplanation: String? = null,
  override val imageRes: String? = "appData/common/step_1.jpg",
  override val image: ImageBitmap?,
  override val primaryAction: String = HowToStrings.primaryNext,
  override val backAction: String = HowToStrings.backOnStep,
) : HowToInvoiceState

@Immutable
data class HowToStep1State(
  override val question: String = HowToStrings.questionStep1,
  override val answer: String? = HowToStrings.answerStep1,
  override val extraExplanation: String? = null,
  override val imageRes: String? = "appData/common/step_2.jpg",
  override val image: ImageBitmap?,
  override val primaryAction: String = HowToStrings.primaryNext,
  override val backAction: String = HowToStrings.backOnStep,
) : HowToInvoiceState

@Immutable
data class HowToStep2State(
  override val question: String = HowToStrings.questionStep2,
  override val answer: String? = HowToStrings.answerStep2,
  override val extraExplanation: String? = null,
  override val imageRes: String? = null,
  override val image: ImageBitmap?,
  override val primaryAction: String = HowToStrings.primaryDone,
  override val backAction: String = HowToStrings.backOnStep,
) : HowToInvoiceState

private object HowToStrings {
  const val questionIntro = "Где мне взять файл рейтинга?"
  const val answerIntro =
	"Файл рейтинга создаётся в 1С с помощью специального помощника. Давай покажу это по шагам."
  const val extraItemAnnotation = "Обработка скорее всего уже находится в папке 1С. но ты можешь взять его у меня!"

  const val extraIntroImportant =
	"Важно! В открывшемся диалоге лучше сразу сохраняй файл с расширением"

  const val extraIntroImportantResolution =
	".ert"

  const val questionStep0 = "Шаг 1. Запусти помощник рейтинга в 1С"
  const val answerStep0 =
	"Открой 1С и через меню \"Файл\" или \"Открыть\" выбери файл помощника *название*.ert. Обрати внимание, что лучше ему находиться в адресе как на скриншоте." +
		"Можешь добавить _ - нижнее подчеркивание, тогда он будет вверху списка. Хакерство!"

  const val questionStep1 = "Шаг 2. Заполни данные для расчёта рейтинга"
  const val answerStep1 =
	"Здесь просто выбираем дату и ограничение по количеству."

  const val questionStep2 = "Шаг 3. Сохрани файл рейтинга"
  const val answerStep2 =
	"Здесь главная цель - полученный рейтинг сохранить как текстовой документ. лучше всего в .txt. Сохрани в удобную папку, обычно это рабочий стол. Ты сможешь выбрать его там, где мы начали учиться работе с выгрузкой."

  const val primaryIntro = "Показать шаги"
  const val primaryNext = "Дальше"
  const val primaryDone = "Готово"

  const val backFromIntro = "Назад к выбору файла"
  const val backOnStep = "Назад"
}

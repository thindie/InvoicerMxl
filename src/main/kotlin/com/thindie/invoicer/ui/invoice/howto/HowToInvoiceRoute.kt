package com.thindie.invoicer.ui.invoice.howto

import androidx.compose.ui.graphics.ImageBitmap
import com.thindie.invoicer.application.RouteFactory

fun HowToFlow.howToIntro(designation: HowToFlow.Designation) = RouteFactory.create(
  initialState = configureIntro(designation),
  execute = this::howToIntroExecute,
  routeContent = { HowToIntroScreen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)

fun HowToFlow.howToStep0(stepIllustration: ImageBitmap?, designation: HowToFlow.Designation) = RouteFactory.create(
  initialState = configureStep0(stepIllustration, designation),
  execute = this::howToStep0Execute,
  routeContent = { HowToStep0Screen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)

fun HowToFlow.howToStep1(stepIllustration: ImageBitmap?, designation: HowToFlow.Designation) = RouteFactory.create(
  initialState = configureStep1(stepIllustration, designation),
  execute = this::howToStep1Execute,
  routeContent = { HowToStep1Screen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)

fun HowToFlow.howToStep2(stepIllustration: ImageBitmap?, designation: HowToFlow.Designation) = RouteFactory.create(
  initialState = configureStep2(stepIllustration, designation),
  execute = this::howToStep2Execute,
  routeContent = { HowToStep2Screen() },
  errorMapper = { e -> invoiceFlowErrors(e) }
)

fun configureIntro(designation: HowToFlow.Designation): HowToIntroState {
  return when (designation) {
	HowToFlow.Designation.UpdateStock -> HowToIntroState()
	HowToFlow.Designation.MergeUpdateStock -> HowToIntroState(
	  question = "Где мне взять инвентаризацию?",
	  answer = "Файл рейтинга создаётся в 1С с помощью стандартной обработки",
	  extraExplanation = null,
	  imageRes = "step_0_1.jpg",
	  image = null,
	  primaryAction = "Показать шаги",
	  backAction = "Назад к выбору файла"
	)
  }
}

fun configureStep0(stepIllustration: ImageBitmap?, designation: HowToFlow.Designation): HowToStep0State {
  return when (designation) {
	HowToFlow.Designation.UpdateStock -> HowToStep0State(image = stepIllustration)
	HowToFlow.Designation.MergeUpdateStock -> HowToStep0State(
	  question = "Шаг 1. Запусти инвентаризацию в 1С",
	  answer = "Открой 1С и через вверху окна найди инвентаризацию",
	  extraExplanation = null,
	  imageRes = "step_1_1.jpg",
	  image = stepIllustration,
	  primaryAction = "Дальше",
	  backAction = "Назад"
	)
  }
}

fun configureStep1(stepIllustration: ImageBitmap?, designation: HowToFlow.Designation): HowToStep1State {
  return when (designation) {
	HowToFlow.Designation.UpdateStock -> HowToStep1State(image = stepIllustration)
	HowToFlow.Designation.MergeUpdateStock -> HowToStep1State(
	  question = "Шаг 2. Найди нужные элементы управления",
	  answer = "Здесь просто нажимаем заполнить",
	  extraExplanation = null,
	  imageRes = "step_2_1.jpg",
	  image = stepIllustration,
	  primaryAction = "Дальше",
	  backAction = "Назад"
	)
  }
}

fun configureStep2(stepIllustration: ImageBitmap?, designation: HowToFlow.Designation): HowToStep2State {
  return when (designation) {
	HowToFlow.Designation.UpdateStock -> HowToStep2State(image = stepIllustration)
	HowToFlow.Designation.MergeUpdateStock -> HowToStep2State(
	  question = "Шаг 3. Сохрани файл инвентаризации",
	  answer = "Здесь главная цель - полученный рейтинг сохранить как текстовой документ. лучше всего в .txt. Сохрани в удобную папку, обычно это рабочий стол. Ты сможешь выбрать его там, где мы начали учиться работе с выгрузкой.",
	  extraExplanation = null,
	  imageRes = null,
	  image = stepIllustration,
	  primaryAction = "Завершить",
	  backAction = "Назад"
	)
  }
}

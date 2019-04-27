package com.foundaml.server.domain.models.labels.transformers

import java.util.UUID

import com.foundaml.server.domain.models.{
  DynamicLabelsConfiguration,
  LabelsConfiguration,
  OneOfLabelsConfiguration
}
import com.foundaml.server.domain.models.errors.LabelsTransformerError
import com.foundaml.server.domain.models.labels._
import com.foundaml.server.domain.services.ExampleUrlService

case class TensorFlowLabelsTransformer(fields: Map[String, String]) {

  def transformWithOneOfConfiguration(
      predictionId: String,
      tfLabels: TensorFlowClassificationLabels
  ) = {
    val labelsString = tfLabels.result.flatten.map(_._1).toSet
    if (labelsString == fields.keys) {
      val labels: Set[ClassificationLabel] = tfLabels.result.flatten.flatMap {
        case (label, probability) =>
          fields
            .get(label)
            .map { label =>
              val correctUrl =
                ExampleUrlService.correctClassificationExampleUrl(
                  predictionId,
                  label
                )
              val incorrectUrl =
                ExampleUrlService.incorrectClassificationExampleUrl(
                  predictionId,
                  label
                )
              ClassificationLabel(
                label,
                probability,
                correctUrl,
                incorrectUrl
              )
            }

      }.toSet

      if (labels.size == fields.keys.size) {
        Right(labels)
      } else {
        Left(
          LabelsTransformerError(
            "A label could not be mapped from TensorFlow result"
          )
        )
      }
    } else {
      Left(
        LabelsTransformerError(
          "labels do not exactly match with classification results"
        )
      )
    }
  }

  def transformWithDynamicLabelsConfiguration(
      predictionId: String,
      tfLabels: TensorFlowClassificationLabels
  ) = {
    val labels = tfLabels.result.flatten.map {
      case (label, probability) =>
        val correctUrl =
          ExampleUrlService.correctClassificationExampleUrl(
            predictionId,
            label
          )
        val incorrectUrl =
          ExampleUrlService.incorrectClassificationExampleUrl(
            predictionId,
            label
          )
        ClassificationLabel(
          label,
          probability,
          correctUrl,
          incorrectUrl
        )
    }.toSet

    Right(labels)
  }

  def transform(
      labelsConfiguration: LabelsConfiguration,
      predictionId: String,
      tfLabels: TensorFlowClassificationLabels
  ): Either[LabelsTransformerError, Set[ClassificationLabel]] = {

    labelsConfiguration match {
      case OneOfLabelsConfiguration(_, _) =>
        transformWithOneOfConfiguration(predictionId, tfLabels)
      case DynamicLabelsConfiguration(_) =>
        transformWithDynamicLabelsConfiguration(predictionId, tfLabels)
    }
  }
}

object TensorFlowLabelsTransformer {
  def identity(projectLabels: Set[String]) =
    TensorFlowLabelsTransformer(projectLabels.zip(projectLabels).toMap)
}
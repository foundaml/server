package com.foundaml.server.utils

import com.foundaml.server.models._
import com.foundaml.server.models.features._
import com.foundaml.server.models.labels._
import com.foundaml.server.models.backends._

object ProjectGenerator {

    def compute(
        features: Features 
    ): Labels =
      TensorFlowClassificationLabels(
        List(
          TensorFlowClassicationLabel(
            List(1, 2, 3),
            List(0.0f, 0.1f, 0.2f),
            List("class1", "class2", "class3"),
            List(0.0f, 0.0f, 0.0f)
          )
        )
   )

    val defaultAlgorithm = Algorithm(
      "algorithm id",
      Local(compute)
    )


  def withLocalBackend() = Project(
    "id",
    "example project",
    Classification,
    "tf.cl",
    "tf.cl",
    Map.empty,
    DefaultAlgorithm(defaultAlgorithm) 
  )
}
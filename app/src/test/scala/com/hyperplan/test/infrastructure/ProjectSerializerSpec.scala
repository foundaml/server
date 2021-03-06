package com.hyperplan.test.infrastructure

import com.hyperplan.domain.models._
import com.hyperplan.infrastructure.serialization.ProjectSerializer
import com.hyperplan.test.SerializerTester
import com.hyperplan.test.SerializerTester
import io.circe.{Decoder, Encoder}
import org.scalatest.{FlatSpec, Matchers}

class ProjectSerializerSpec
    extends FlatSpec
    with SerializerTester
    with Matchers {

  val encoder: Encoder[Project] = ProjectSerializer.encoder
  val decoder: Decoder[Project] = ProjectSerializer.decoder

  it should "correctly encode a classification project" in {

    val projectId = "test-project-encode"
    val projectName = "test project"
    import com.hyperplan.domain.models.features.Scalar
    import com.hyperplan.domain.models.features.StringFeatureType
    val configuration = ClassificationConfiguration(
      FeatureVectorDescriptor(
        "id",
        List(
          FeatureDescriptor(
            "",
            StringFeatureType,
            Scalar,
            ""
          )
        )
      ),
      LabelVectorDescriptor(
        "id",
        OneOfLabelsDescriptor(
          Set(""),
          ""
        )
      ),
      None
    )
    val project = ClassificationProject(
      projectId,
      projectName,
      configuration,
      Nil,
      NoAlgorithm()
    )

    testEncoder(project: Project) { json =>
      val expectedJson =
        """{"id":"test-project-encode","name":"test project","problem":"classification","algorithms":[],"policy":{"class":"NoAlgorithm"},"configuration":{"features":{"id":"id","data":[{"name":"","type":"string","dimension":"scalar","description":""}]},"labels":{"id":"id","data":{"type":"oneOf","oneOf":[""],"description":""}},"dataStream":null}}"""
      json.noSpaces should be(expectedJson)
    }(encoder)
  }

  it should "correctly decode a classification project" in {
    val projectId = "test-project-encode"
    val projectName = "test project"

    val projectJson =
      s"""{"id":"$projectId","name":"$projectName","problem":"classification","algorithms":[],"policy":{"class":"NoAlgorithm"},"configuration":{"features":{"id":"id","data":[{"name":"","type":"","dimension":"scalar","description":""}]},"labels":{"id":"id","data":{"type":"oneOf","oneOf":[""],"description":""}},"dataStream":null}}"""

    testDecoder[Project](projectJson) {
      case project: ClassificationProject =>
        assert(project.id == projectId)
        assert(project.name == projectName)
        assert(project.policy == NoAlgorithm())
      case _: RegressionProject =>
        fail()
    }(decoder)
  }
}

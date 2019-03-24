package com.foundaml.server.infrastructure.serialization

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.auto._
import io.circe.parser.decode
import io.circe.syntax._
import com.foundaml.server.domain.models._
import io.circe._

object ProblemTypeSerializer {

  import io.circe.generic.extras.semiauto._

  implicit val discriminator: Configuration =
    Configuration.default.withDiscriminator("class")
  implicit val encoder: Encoder[ProblemType] = deriveEncoder
  implicit val decoder: Decoder[ProblemType] = deriveDecoder

  def encodeJsonString(problem: ProblemType): String = {
    problem.asJson.noSpaces
  }

  def encodeJson(problem: ProblemType): Json = {
    problem.asJson
  }

  def decodeJson(n: String): Either[io.circe.Error, ProblemType] = {
    decode[ProblemType](n)
  }
}

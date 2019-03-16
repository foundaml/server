package com.foundaml.server.infrastructure.serialization

import io.circe.{Decoder, Encoder}
import io.circe.generic.extras.Configuration
import io.circe.parser.decode
import io.circe.syntax._

import com.foundaml.server.domain.models._

object AlgorithmPolicySerializer {

  object Implicits {

  }

  import io.circe.generic.extras.semiauto._

  implicit val discriminator: Configuration =
    Configuration.default.withDiscriminator("class")

  implicit val encoder: Encoder[AlgorithmPolicy] = deriveEncoder
  implicit val decoder: Decoder[AlgorithmPolicy] = deriveDecoder

  def encodeJson(policy: AlgorithmPolicy): String = {
    policy.asJson.noSpaces
  }

  def decodeJson(n: String): Either[io.circe.Error, AlgorithmPolicy] = {
    decode[AlgorithmPolicy](n)
  }
}

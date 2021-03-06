/*
 *
 * This file is part of the Hyperplan project.
 * Copyright (C) 2019  Hyperplan
 * Authors: Antoine Sauray
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gn/u.org/licenses/>.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the Hyperplan software without
 * disclosing the source code of your own applications.
 *
 *
 */

package com.hyperplan.infrastructure.serialization

import com.hyperplan.application.controllers.requests.PatchProjectRequest
import com.hyperplan.domain.models._
import io.circe._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import cats.effect.IO
import cats.implicits._

object PatchProjectRequestSerializer {

  import com.hyperplan.domain.models.ProblemType

  implicit val problemTypeEncoder = ProblemTypeSerializer.encoder

  implicit val algorithmPolicyDecoder =
    AlgorithmPolicySerializer.Implicits.decoder
  implicit val algorithmPolicyEncoder =
    AlgorithmPolicySerializer.Implicits.encoder

  import com.hyperplan.domain.models.AlgorithmPolicy
  implicit val decoder: Decoder[PatchProjectRequest] =
    (c: HCursor) =>
      for {
        name <- c.downField("name").as[Option[String]]
        policy <- c.downField("policy").as[Option[AlgorithmPolicy]]
      } yield PatchProjectRequest(name, policy)

  implicit val encoder: Encoder[PatchProjectRequest] =
    Encoder.forProduct2("name", "policy")(r => (r.name, r.policy))

  implicit val entityDecoder: EntityDecoder[IO, PatchProjectRequest] =
    jsonOf[IO, PatchProjectRequest]

  implicit val entityEncoder: EntityEncoder[IO, PatchProjectRequest] =
    jsonEncoderOf[IO, PatchProjectRequest]

}

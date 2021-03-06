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

package com.hyperplan.domain.services

import cats.data.{EitherT, NonEmptyChain}
import cats.effect.IO

import com.hyperplan.application.controllers.requests.PostProjectRequest
import com.hyperplan.domain.errors.{AlgorithmError, ProjectError}
import com.hyperplan.domain.models.{
  Algorithm,
  AlgorithmPolicy,
  Project,
  SecurityConfiguration
}
import com.hyperplan.domain.models.backends.Backend

trait ProjectsService {
  def createProject(
      projectRequest: PostProjectRequest
  ): EitherT[IO, NonEmptyChain[ProjectError], Project]

  def updateProject(
      projectId: String,
      name: Option[String],
      policy: Option[AlgorithmPolicy]
  ): EitherT[IO, NonEmptyChain[ProjectError], Project]
  def deleteProject(projectId: String): IO[Int]

  def readProjects: IO[List[Project]]
  def readProject(id: String): IO[Option[Project]]
  def deleteAlgorithm(projectId: String, algorithmId: String): IO[Int]
  def createAlgorithm(
      id: String,
      backend: Backend,
      projectId: String,
      security: SecurityConfiguration
  ): EitherT[IO, NonEmptyChain[AlgorithmError], Algorithm]
}

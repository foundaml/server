package com.foundaml.server.domain.repositories

import doobie._
import doobie.implicits._
import scalaz.zio.Task
import scalaz.zio.interop.catz._
import com.foundaml.server.domain.models.Algorithm
import com.foundaml.server.domain.models.backends._
import com.foundaml.server.domain.models.errors.AlgorithmAlreadyExists
import com.foundaml.server.domain.repositories.AlgorithmsRepository.AlgorithmData
import com.foundaml.server.infrastructure.serialization._
import doobie.postgres.sqlstate

class AlgorithmsRepository(implicit xa: Transactor[Task]) {

  implicit val backendGet: Get[Either[io.circe.Error, Backend]] =
    Get[String].map(BackendSerializer.decodeJson)
  implicit val backendPut: Put[Backend] =
    Put[String].contramap(BackendSerializer.encodeJsonNoSpaces)

  def insertQuery(algorithm: Algorithm): doobie.Update0 =
    sql"""INSERT INTO algorithms(
      id, 
      backend, 
      project_id
    ) VALUES(
      ${algorithm.id},
      ${algorithm.backend},
      ${algorithm.projectId}
    )""".update

  def insert(algorithm: Algorithm) =
    insertQuery(algorithm).run
      .attemptSomeSqlState {
        case sqlstate.class23.UNIQUE_VIOLATION =>
          AlgorithmAlreadyExists(algorithm.id)
      }
      .transact(xa)

  def readQuery(algorithmId: String): doobie.Query0[AlgorithmData] =
    sql"""
      SELECT id, backend, project_id 
      FROM algorithms 
      WHERE id=$algorithmId
      """
      .query[AlgorithmData]

  def read(algorithmId: String): Task[AlgorithmData] =
    readQuery(algorithmId).unique.transact(xa)

  def readForProjectQuery(projectId: String): doobie.Query0[AlgorithmData] =
    sql"""
      SELECT id, backend, project_id 
      FROM algorithms 
      WHERE project_id=$projectId
      """
      .query[AlgorithmData]

  def readForProject(projectId: String): Task[List[AlgorithmData]] =
    readForProjectQuery(projectId).to[List].transact(xa)

  def readAllQuery(): doobie.Query0[AlgorithmData] =
    sql"""
      SELECT id, backend, project_id 
      FROM algorithms 
      """
      .query[AlgorithmData]

  def readAll(): Task[List[AlgorithmData]] =
    readAllQuery().to[List].transact(xa)

}

object AlgorithmsRepository {
  type AlgorithmData = (String, Either[io.circe.Error, Backend], String)
}
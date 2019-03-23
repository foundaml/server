package com.foundaml.server.domain.models.errors

sealed trait ProjectError extends Throwable

case class InvalidProjectIdentifier(message: String) extends ProjectError
case class FeaturesConfigurationError(message: String) extends ProjectError
case class ProjectNotFound(projectId: String) extends ProjectError
case class ProjectAlreadyExists(projectId: String) extends ProjectError

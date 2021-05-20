package com.github.ojacquemart.realtime.admin

import com.github.ojacquemart.realtime.admin.project.NewProjectRequest
import com.github.ojacquemart.realtime.admin.project.NewTableRequest
import com.github.ojacquemart.realtime.admin.project.ProjectService
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType

@Path("admin/api")
class AdminResource(
  val projectService: ProjectService
) {

  @Path("projects")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  fun createProject(@Valid payload: NewProjectRequest) =
    projectService.create(payload)

  @Path("projects/{projectName}/collections")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  fun createTable(projectName: String, @Valid payload: NewTableRequest) =
    projectService.createTable(projectName, payload)

}
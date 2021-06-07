package com.github.ojacquemart.realtime.admin

import com.github.ojacquemart.realtime.admin.project.NewCollectionRequest
import com.github.ojacquemart.realtime.admin.project.NewProjectRequest
import com.github.ojacquemart.realtime.admin.project.ProjectService
import javax.validation.Valid
import javax.ws.rs.Consumes
import javax.ws.rs.GET
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

  @Path("projects")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  fun getProjects() =
    projectService.getList()

  @Path("projects/{projectName}/collections")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  fun createCollection(projectName: String, @Valid payload: NewCollectionRequest) =
    projectService.createCollection(projectName, payload)

}

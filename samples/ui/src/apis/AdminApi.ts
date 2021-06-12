import { ApiResponse, errorResponse } from '@/apis/ApiResponse'
import { NewNameRequest } from '@/apis/NewNameRequest'
import { NewCollectionRequest } from '@/apis/NewCollectionRequest'
import { ProjectModel } from '@/apis/ProjectModel'

export class AdminApi {

  static async createProject(request: NewNameRequest) {
    return fetch('http://localhost:8080/admin/api/projects', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    })
  }

  static async fetchProjects(): Promise<ApiResponse<string[]>> {
    try {
      const response = await fetch('http://localhost:8080/admin/api/projects')
      if (response.ok) {
        return {data: await response.json(), error: false, loading: false}
      }

      return errorResponse()
    } catch (e) {
      console.log('admin-api @ error while gettings the projects', e)

      return errorResponse()
    }
  }

  static async fetchProject(name: string): Promise<ProjectModel> {
    const response = await fetch(`http://localhost:8080/admin/api/projects/${name}`)

    return response.json()
  }

  static async createCollection(request: NewCollectionRequest) {
    return fetch(`http://localhost:8080/admin/api/projects/${request.project}/collections`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({name: request.collection}),
    })
  }

}

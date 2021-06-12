import { ApiResponse, errorResponse } from '@/apis/ApiResponse'
import { NewNameRequest } from '@/apis/NewNameRequest'
import { NewCollectionRequest } from '@/apis/NewCollectionRequest'
import { ProjectModel } from '@/apis/ProjectModel'

import { Urls } from '@/shared/Urls'

export class AdminApi {

  static async createProject(request: NewNameRequest) {
    return this.post('admin/api/projects', request)
  }

  static async fetchProjects(): Promise<ApiResponse<string[]>> {
    try {
      const response = await fetch(`${Urls.getBackendUrl()}/admin/api/projects`)
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
    const response = await fetch(`${Urls.getBackendUrl()}/admin/api/projects/${name}`)

    return response.json()
  }

  static async createCollection(request: NewCollectionRequest) {
    return this.post(`admin/api/projects/${request.project}/collections`, {name: request.collection})
  }

  static async post(url: string, request: any) {
    return fetch(`${Urls.getBackendUrl()}/${url}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    })
  }

}

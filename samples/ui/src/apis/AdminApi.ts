import { ApiRequest } from '@/apis/ApiRequest'
import { ApiResponse, ApiStatus, error } from '@/apis/ApiResponse'
import { NewCollectionRequest } from '@/apis/NewCollectionRequest'
import { NewNameRequest } from '@/apis/NewNameRequest'
import { ProjectModel } from '@/apis/ProjectModel'

export class AdminApi {

  static async createProject(request: NewNameRequest) {
    return this.post('admin/api/projects', request)
  }

  static async fetchProjects(request: ApiRequest): Promise<ApiResponse<string[]>> {
    try {
      const {url, headers} = request
      const response = await fetch(`${url}/admin/api/projects`, {
        headers,
      })
      if (response.ok) {
        return {status: ApiStatus.SUCCESS, data: await response.json()}
      }

      return error()
    } catch (e) {
      console.log('admin-api @ error while gettings the projects', e)

      return error()
    }
  }

  static async fetchProject(request: NewNameRequest): Promise<ProjectModel> {
    const {url, headers, name} = request
    const response = await fetch(`${url}/admin/api/projects/${name}`, {
      headers,
    })

    return response.json()
  }

  static async createCollection(request: NewCollectionRequest) {
    return this.post(`admin/api/projects/${request.project}/collections`, {
      url: request.url,
      headers: request.headers,
      name: request.collection,
    })
  }

  static async post(urlSuffix: string, request: NewNameRequest) {
    const {url, headers} = request

    return fetch(`${url}/${urlSuffix}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        ...headers,
      },
      body: JSON.stringify(request),
    })
  }

}

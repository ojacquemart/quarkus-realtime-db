import { ApiResponse, errorResponse } from '@/apis/ApiResponse'
import { NamePayload } from '@/apis/NamePayload'

export class AdminApi {

  static async createProject(payload: NamePayload) {
    return fetch('http://localhost:8080/admin/api/projects', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
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

}

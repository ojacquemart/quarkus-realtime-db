import { ApiResponse, ApiStatus } from '@/apis/ApiResponse'

export class StoreProjects {
  status?: ApiStatus = ApiStatus.PENDING
  items?: string[]

  copy(response: ApiResponse<any>) {
    this.status = response.status
    this.items = response.data
  }

  isPending() {
    return this.hasStatus(ApiStatus.PENDING)
  }

  isError() {
    return this.hasStatus(ApiStatus.ERROR)
  }

  hasStatus(expected: ApiStatus) {
    return this.status === expected
  }

}

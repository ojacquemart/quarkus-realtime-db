import { ApiRequest } from '@/apis/ApiRequest'

export interface NewNameRequest extends ApiRequest {
  name?: string
}

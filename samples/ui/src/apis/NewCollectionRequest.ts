import { ApiRequest } from '@/apis/ApiRequest'

export interface NewCollectionRequest extends ApiRequest {
  project?: string
  collection?: string
}

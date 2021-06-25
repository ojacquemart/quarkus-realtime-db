export interface ApiResponse<T> {
  status?: ApiStatus
  data?: T
}

export enum ApiStatus {
  PENDING = 'PENDING',
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR'
}

export const pending = <T>(): ApiResponse<T> => {
  return {status: ApiStatus.PENDING}
}
export const error = <T>(): ApiResponse<T> => {
  return {status: ApiStatus.ERROR}
}

export interface ApiResponse<T> {
  error?: boolean
  loading?: boolean
  data?: T
}

export const errorResponse = <T>(): ApiResponse<T> => {
  return {error: true, loading: false}
}

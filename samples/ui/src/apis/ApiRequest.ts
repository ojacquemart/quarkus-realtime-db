export interface HttpHeaders {
  [key: string]: string
}

export interface ApiRequest {
  url: string
  headers: HttpHeaders
}

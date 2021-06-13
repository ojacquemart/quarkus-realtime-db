import { ProjectModel } from '@/apis/ProjectModel'

export class StoreCollections {
  project?: ProjectModel
  collection?: string

  clear() {
    this.project = undefined
    this.collection = undefined
  }

  hasCollection() {
    return !!this.collection
  }

  setCollection(name: string) {
    this.collection = name

  }

  getProjectCollectionUrlPart(): string | undefined {
    if (!this.collection) {
      return undefined
    }

    return `${this.project?.name}/${this.collection}/_all`
  }

  appendCollection(name: string) {
    if (this.project?.collections) {
      this.project.collections = this.project.collections.concat(name)
    }
  }

}

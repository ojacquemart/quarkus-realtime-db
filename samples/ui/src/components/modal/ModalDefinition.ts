import { Store } from 'vuex'

import { FormControl, FormControlValidators } from '@/components/modal/FormControl'

const DEFAULT_VALIDATOR = (controls: FormControl[]) => FormControlValidators.requiredNotEmpty(controls)
const NOOP = () => {
  // do nothing
}

export interface ModalOptions {
  payloadObject?: boolean
  validator?: (controls: FormControl[]) => boolean
  afterSave?: () => void
}

export class ModalDefinition {
  id: string
  title: string
  dispatch: string
  controls: FormControl[]
  validator: (controls: FormControl[]) => boolean
  afterSave: () => void
  payloadObject?: boolean

  constructor(
    id: string, title: string, dispatch: string, controls: FormControl[],
    options?: ModalOptions,
  ) {
    this.id = id
    this.title = title
    this.dispatch = dispatch
    this.controls = controls
    this.validator = options?.validator ?? DEFAULT_VALIDATOR
    this.afterSave = options?.afterSave ?? NOOP
    this.payloadObject = options?.payloadObject ?? false
  }

  validate(): boolean {
    return this.validator(this.controls || []);
  }

  async save(store: Store<any>) {
    const payload = this.getPayload()

    await store.dispatch(this.dispatch, payload)
    store.commit('modals/close', this.id)

    this.afterSave()
  }

  private getPayload() {
    if (this.payloadObject) {
      return this.getPayloadObject()
    }

    return this.controls?.[0]?.value?.value || ''
  }

  private getPayloadObject() {
    return this.controls
      .filter(it => (it.value || '') as string)
      .reduce((acc, it) => {
        acc[it.key] = it.value?.value || ''

        return acc
      }, {} as { [key: string]: string })
  }

}


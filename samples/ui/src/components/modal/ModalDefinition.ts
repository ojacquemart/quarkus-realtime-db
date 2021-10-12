import { Store } from 'vuex'

import { FormControl, FormControlValidators } from '@/components/modal/FormControl'

const DEFAULT_VALIDATOR = (controls: FormControl[]) => FormControlValidators.requiredNotEmpty(controls)

export interface ModalOptions {
  payloadObject?: boolean,
  validator?: (controls: FormControl[]) => boolean,
}

export class ModalDefinition {
  id: string
  title: string
  dispatch: string
  validator: (controls: FormControl[]) => boolean
  payloadObject?: boolean
  controls: FormControl[]

  constructor(
    id: string, title: string, dispatch: string, controls: FormControl[],
    options?: ModalOptions,
  ) {
    this.id = id
    this.title = title
    this.dispatch = dispatch
    this.validator = options?.validator ?? DEFAULT_VALIDATOR
    this.payloadObject = options?.payloadObject ?? false
    this.controls = controls
  }

  validate(): boolean {
    return this.validator(this.controls || []);
  }

  async save(store: Store<any>) {
    const payload = this.getPayload()

    await store.dispatch(this.dispatch, payload)
    store.commit('modals/close', this.id)
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


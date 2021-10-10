import { Store } from 'vuex'

import { FormControl, FormControlValidators } from '@/components/modal/FormControl'

export class ModalDefinition {
  id: string
  title: string
  dispatch: string
  validator: (controls: FormControl[]) => boolean
  controls: FormControl[]

  constructor(
    id: string, title: string, dispatch: string, controls: FormControl[],
    validator?: (controls: FormControl[]) => boolean,
  ) {
    this.id = id
    this.title = title
    this.dispatch = dispatch
    this.validator = validator ?? FormControlValidators.requiredNotEmpty
    this.controls = controls
  }

  validate(): boolean {
    return this.validator(this.controls || []);
  }

  async save(store: Store<any>) {
    await store.dispatch(this.dispatch || '', this.controls?.[0]?.value?.value || '')
    store.commit('modals/close', this.id)
  }
}


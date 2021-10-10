import { Ref } from 'vue'
import exp from 'constants'

export type FormControlTye = 'text' | 'textarea'

export interface FormControl {
  key: string
  label?: string
  // i18n key label
  i18nLabel?: string
  type: FormControlTye
  value?: Ref<string | null>
  required?: boolean
}

export class FormControlValidators {

  static requiredNotEmpty(controls: FormControl[]) {
    return this.required(controls)
      .filter((it: string) => it.length > 0)
      .length > 0
  }

  static equalsFirstRequired(controls: FormControl[], expected: string) {
    const first = this.required(controls)[0]

    return first === expected
  }

  private static required(controls: FormControl[]) {
    return controls
      .filter((it: FormControl) => it.required)
      .map((it: FormControl) => (it.value || '') as string)
  }

}

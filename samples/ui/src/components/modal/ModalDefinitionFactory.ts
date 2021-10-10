import { ref } from 'vue'

import { Store } from 'vuex'
import { TranslateResult } from 'vue-i18n'
import { Path } from '@intlify/core-base'

import { FormControl, FormControlValidators } from '@/components/modal/FormControl'
import { ModalDefinition } from '@/components/modal/ModalDefinition'

export interface Map {
  [key: string]: ModalDefinition
}

export interface ModalQuery {
  store: Store<any>,
  i18n: {
    t(key: Path, named: Record<string, unknown>): TranslateResult;
  }
  id: string
}

export class ModalDefinitionFactory {

  static get(query: ModalQuery) {
    const {store, i18n, id} = query

    const definitions: Map = {
      settings: new ModalDefinition(
        'settings',
        'modals.settings.title',
        'settings/setUrl',
        [
          {
            key: 'name',
            i18nLabel: 'modals.settings.url',
            value: ref(store.getters['settings/getUrl']),
            type: 'text',
            required: true,
          },
        ],
      ),
      'new-project': new ModalDefinition(
        'new-project',
        'projects.new_project',
        'projects/createProject',
        [
          {
            key: 'name',
            i18nLabel: 'common.name',
            type: 'text',
            value: ref(null),
            required: true,
          },
        ],
      ),
      'confirm-delete': new ModalDefinition(
        'confirm-delete',
        'modals.deletion.title',
        'collections/deleteEntryById',
        [
          {
            key: 'name',
            label: i18n.t('modals.deletion.confirm', {current: store.getters['collections/getActiveMessageId']}),
            type: 'text',
            value: ref(null),
            required: true,
          },
        ],
        (controls: FormControl[]) =>
          FormControlValidators.equalsFirstRequired(controls, store.getters['collections/getActiveMessageId']),
      ),
      'new-collection': new ModalDefinition(
        'new-collection',
        'collections.new_collection',
        'collections/createCollection',
        [
          {
            key: 'name',
            i18nLabel: 'common.name',
            type: 'text',
            value: ref(null),
            required: true,
          },
        ],
      ),
      'update-entry':
        new ModalDefinition(
          'update-entry',
          'modals.edit_entry.title',
          'collections/updateEntry',
          [
            {
              key: 'name',
              i18nLabel: 'common.content',
              type: 'textarea',
              value: ref(store.getters['collections/getActiveMessageContent']),
              required: true,
            },
          ],
        ),
      'socket-entry':
        new ModalDefinition(
          'socket-entry',
          'modals.new_entry.title',
          'collections/createEntry',
          [
            {
              key: 'name',
              i18nLabel: 'common.content',
              type: 'textarea',
              value: ref(null),
              required: true,
            },
          ],
        ),
    }

    return definitions[id]
  }

}

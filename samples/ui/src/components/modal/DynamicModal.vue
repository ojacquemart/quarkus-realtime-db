<template>
  <rtdb-modal :modal-id="modal.id">
    <template v-slot:header>
      {{ $t(modal.title) }}
    </template>
    <template v-slot:body>
      <div v-for="(control, index) of modal.controls"
           :class="{'mb-4': Number(index) < modal.controls.length - 1}">
        <p v-if="control.label" v-html="control.label"></p>
        <p v-else v-html="$t(control.i18nLabel)"></p>
        <input v-if="control.type === 'text'" v-model="control.value"
               class="relative outline-none rounded py-3 px-3 w-full bg-primary shadow text-sm text-white placeholder-gray-400 focus:ring-1 focus:border-gray-300"
               type="text"/>
        <textarea v-if="control.type === 'textarea'"
                  v-model="control.value"
                  class="relative outline-none rounded py-3 px-3 w-full h-60 bg-primary shadow text-sm text-white placeholder-gray-400 focus:ring-1 focus:border-gray-300"/>
      </div>
    </template>
    <template v-slot:buttons>
      <button :disabled="!modal.validate()"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary-light text-base font-medium text-white focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-500 disabled:opacity-50 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="save">
        {{ $t('common.ok') }}
      </button>
      <button ref="cancelButtonRef"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-gray-100 text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-300 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="$store.commit('modals/close', modal.id)">
        {{ $t('common.cancel') }}
      </button>
    </template>
  </rtdb-modal>
</template>

<script lang="ts">
import { defineComponent, reactive } from 'vue'

import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'

import Modal from '@/components/modal/Modal.vue'
import { ModalDefinitionFactory } from '@/components/modal/ModalDefinitionFactory'

export default defineComponent({
  components: {
    'rtdb-modal': Modal,
  },
  props: [
    'modal',
  ],
  setup: () => {
    const store = useStore()
    const i18n = useI18n()

    const id = store.getters['modals/id']
    const modal = ModalDefinitionFactory.get({store, i18n: {t: i18n.t}, id})
    if (!modal) {
      throw Error('Modal definition not found: ' + id)
    }

    return {
      modal: reactive(modal),
      save: () => {
        modal.save(store)
      },
    }
  },
})
</script>

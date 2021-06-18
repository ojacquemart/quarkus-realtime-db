<template>
  <rtdb-modal v-model:is-opened="open">
    <template v-slot:header>
      {{ t(i18nTitle) }}
    </template>
    <template v-slot:body>
      <p>{{ t(i18nInputLabel || 'common.name') }}</p>
      <textarea v-if="inputType === 'textarea'"
                v-model="textView"
                class="relative outline-none rounded py-3 px-3 w-full h-60 bg-primary shadow text-sm text-white placeholder-gray-400 focus:ring-1 focus:border-gray-300"
                type="text"/>
      <input v-else v-model="textView"
             class="relative outline-none rounded py-3 px-3 w-full bg-primary shadow text-sm text-white placeholder-gray-400 focus:ring-1 focus:border-gray-300"
             type="text"/>
    </template>
    <template v-slot:buttons>
      <button :disabled="(textView || '').length === 0"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary-light text-base font-medium text-white focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-500 disabled:opacity-50 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="save">
        {{ t('common.ok') }}
      </button>
      <button ref="cancelButtonRef"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-gray-100 text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-300 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="open = false">
        {{ t('common.cancel') }}
      </button>
    </template>
  </rtdb-modal>
</template>

<script lang="typescript">
import { computed, defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'

import Modal from '@/components/Modal.vue'

export default defineComponent({
  components: {
    'rtdb-modal': Modal,
  },
  props: [
    'i18nTitle',
    'i18nInputLabel',
    'inputType',
    'dispatchType',
    'textValue',
    'isOpened',
  ],
  setup: (props, {emit}) => {
    const store = useStore()
    const open = computed({
      get: () => props.isOpened,
      set: (value) => emit('update:is-opened', value),
    })

    const textView = ref((props.textValue || '').slice())

    return {
      ...useI18n(),
      open,
      textView,
      save: () => {
        console.log(`modalNewText @ save "${textView.value}"`)

        store.dispatch(props.dispatchType, textView.value)
        open.value = false
        textView.value = null
      },
    }
  },
})
</script>

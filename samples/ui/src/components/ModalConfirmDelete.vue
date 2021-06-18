<template>
  <rtdb-modal v-model:is-opened="open">
    <template v-slot:header>
      {{ t('modals.deletion.title') }}
    </template>
    <template v-slot:body>
      <p>{{ t('modals.deletion.confirm') }} <span
        class="font-bold text-red-300">{{ messageId }}</span></p>
      <input v-model="confirmValue"
             class="relative outline-none rounded py-3 px-3 w-full bg-primary shadow text-sm text-white placeholder-gray-400 focus:ring-1 focus:border-gray-300"
             type="text"/>
    </template>
    <template v-slot:buttons>
      <button :disabled="!areEquals"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-500 text-base font-medium text-white focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-500 disabled:opacity-50 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="confirm">
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

import Modal from './Modal.vue'

export default defineComponent({
  components: {
    'rtdb-modal': Modal,
  },
  props: [
    'isOpened',
    'messageId',
  ],
  setup: (props, {emit}) => {
    const store = useStore()
    const open = computed({
      get: () => props.isOpened,
      set: (value) => emit('update:is-opened', value),
    })
    const confirmValue = ref(null)
    const messageId = ref(props.messageId)

    const areEquals = computed(() => {
      return confirmValue.value === props.messageId
    })

    return {
      ...useI18n(),
      open,
      confirmValue,
      messageId,
      areEquals,
      confirm: async () => {
        console.log(`modal-confirm-delete @ delete id "${messageId.value}"`)
        await store.dispatch('collections/deleteEntryById', messageId.value)

        open.value = false
        confirmValue.value = null

      },
    }
  },
})
</script>

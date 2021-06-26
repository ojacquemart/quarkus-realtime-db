<template>
  <rtdb-modal :modal-id="modalId">
    <template v-slot:header>
      {{ $t('modals.deletion.title') }}
    </template>
    <template v-slot:body>
      <p>{{ $t('modals.deletion.confirm') }} <span
        class="font-bold text-red-300">{{ messageId }}</span></p>
      <input v-model="confirmValue"
             class="relative outline-none rounded py-3 px-3 w-full bg-primary shadow text-sm text-white placeholder-gray-400 focus:ring-1 focus:border-gray-300"
             type="text"/>
    </template>
    <template v-slot:buttons>
      <button :disabled="!areEquals"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-red-500 text-base font-medium text-white focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-500 disabled:opacity-50 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="confirm">
        {{ $t('common.ok') }}
      </button>
      <button ref="cancelButtonRef"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-gray-100 text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-300 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="$store.commit('modals/close', modalId)">
        {{ $t('common.cancel') }}
      </button>
    </template>
  </rtdb-modal>
</template>

<script lang=" typescript">
import { computed, defineComponent, ref } from 'vue'
import { useStore } from 'vuex'

import Modal from './Modal.vue'

export default defineComponent({
  components: {
    'rtdb-modal': Modal,
  },
  props: [
    'modalId',
  ],
  setup: (props) => {
    const store = useStore()

    const confirmValue = ref(null)
    const messageId = computed(() => {
      return store.getters['collections/getActiveMessageId']
    })

    const areEquals = computed(() => {
      return confirmValue.value === store.getters['collections/getActiveMessageId']
    })

    return {
      confirmValue,
      modalId: props.modalId,
      messageId,
      areEquals,
      confirm: async () => {
        console.log(`modal-confirm-delete @ delete id "${messageId.value}"`)

        await store.dispatch('collections/deleteEntryById', messageId.value)
        await store.commit('modals/close', props.modalId)

        confirmValue.value = null
      },
    }
  },
})
</script>

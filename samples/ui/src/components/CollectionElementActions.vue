<template>
  <div class="inline">
    <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-0 md:ml-2">📝</span>
    <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-2" @click="openModal">🗑️</span>
  </div>
  <rtdb-modal-confirm-delete
    v-model:is-opened="isOpened"
    :message-id="messageId"
  ></rtdb-modal-confirm-delete>
</template>

<script lang="typescript">
import { defineComponent, ref } from 'vue'

import ModalConfirmDelete from '@/components/ModalConfirmDelete.vue'

export default defineComponent({
  components: {
    'rtdb-modal-confirm-delete': ModalConfirmDelete,
  },
  props: [
    'messageId',
  ],
  setup: () => {
    const isOpened = ref(false)

    return {
      isOpened,
      openModal: ($event) => {
        isOpened.value = true

        $event.stopPropagation()
      },
    }
  },
})
</script>

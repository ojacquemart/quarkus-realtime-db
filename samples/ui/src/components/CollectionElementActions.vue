<template>
  <div class="inline">
    <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-0 md:ml-2" @click="updateEntry">📝</span>
    <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-2" @click="deleteEntry">🗑️</span>
  </div>
  <rtdb-modal-edit-message
    v-model:is-opened="isUpdateOpened"
    :text-value="currentContent"
    dispatch-type="collections/updateEntry"
    i18n-input-label="common.content"
    i18n-title="modals.edit_entry.title"
    input-type="textarea">

  </rtdb-modal-edit-message>
  <rtdb-modal-confirm-delete
    v-model:is-opened="isDeleteOpened"
    :message-id="message.content['_id']">
  </rtdb-modal-confirm-delete>
</template>

<script lang="typescript">
import { defineComponent, ref } from 'vue'

import ModalConfirmDelete from '@/components/ModalConfirmDelete.vue'
import ModalNewText from '@/components/ModalNewText.vue'

export default defineComponent({
  components: {
    'rtdb-modal-confirm-delete': ModalConfirmDelete,
    'rtdb-modal-edit-message': ModalNewText,
  },
  props: [
    'message',
  ],
  setup: (props) => {
    const isDeleteOpened = ref(false)
    const isUpdateOpened = ref(false)
    const currentContent = ref(JSON.stringify(props.message.content))

    const showModal = (refValue, $event) => {
      refValue.value = true

      $event.stopPropagation()
    }

    return {
      isUpdateOpened,
      isDeleteOpened,
      currentContent,
      updateEntry: ($event) => showModal(isUpdateOpened, $event),
      deleteEntry: ($event) => showModal(isDeleteOpened, $event),
    }
  },
})
</script>

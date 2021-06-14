<template>
  <rtdb-socket-toolbar-button text="➕" @click="isOpened = true"></rtdb-socket-toolbar-button>
  <rtdb-modal-new-text v-model:is-opened="isOpened"
                       i18n-title="collections.new_json_entry"
                       i18n-input-label="common.content"
                       input-type="textarea"
                       dispatch-type="collections/sendMessage">
  </rtdb-modal-new-text>
</template>

<script lang="typescript">
import { defineComponent, ref } from 'vue'

import ModalNewText from '@/components/ModalNewText.vue'

import SocketToolbarButton from './SocketToolbarButton.vue'

export default defineComponent({
  components: {
    'rtdb-socket-toolbar-button': SocketToolbarButton,
    'rtdb-modal-new-text': ModalNewText,
  },
  setup: () => {
    const isOpened = ref(false)

    return {
      isOpened
    }
  },
})
</script>

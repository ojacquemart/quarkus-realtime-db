<template>
  <rtdb-socket-toolbar-button text="➕"
                              @click="$store.commit('modals/open', 'socket-entry')"></rtdb-socket-toolbar-button>
  <rtdb-modal-new-text dispatch-type="collections/createEntry"
                       i18n-input-label="common.content"
                       i18n-title="modals.new_entry.title"
                       input-type="textarea"
                       modal-id="socket-entry">
  </rtdb-modal-new-text>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

import ModalNewText from '@/components/ModalNewText.vue'

import SocketToolbarButton from './SocketToolbarButton.vue'

export default defineComponent({
  components: {
    'rtdb-socket-toolbar-button': SocketToolbarButton,
    'rtdb-modal-new-text': ModalNewText,
  },
  setup: () => {
  },
})
</script>

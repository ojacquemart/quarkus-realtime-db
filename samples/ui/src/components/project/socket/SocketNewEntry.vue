<template>
  <rtdb-socket-toolbar-button text="➕"
                              @click="$store.commit('modals/open', 'socket-entry')">
  </rtdb-socket-toolbar-button>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import SocketToolbarButton from './SocketToolbarButton.vue'

export default defineComponent({
  components: {
    'rtdb-socket-toolbar-button': SocketToolbarButton,
  },
})
</script>

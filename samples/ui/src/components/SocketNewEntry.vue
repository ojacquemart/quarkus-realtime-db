<template>
  <rtdb-socket-toolbar-button text="➕"></rtdb-socket-toolbar-button>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

import SocketToolbarButton from './SocketToolbarButton.vue'

export default defineComponent({
  components: {
    'rtdb-socket-toolbar-button': SocketToolbarButton,
  },
  setup: () => {
    return {}
  },
})
</script>

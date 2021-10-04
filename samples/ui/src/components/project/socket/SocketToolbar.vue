<template>
  <div v-if="$store.getters['collections/hasCollection']" class="mt-4">
    <rtdb-socket-url></rtdb-socket-url>
    <div class="mt-2 md:inline md:ml-2">
      <rtdb-socket-toolbar-clipboard></rtdb-socket-toolbar-clipboard>
      <rtdb-socket-new-entry></rtdb-socket-new-entry>
    </div>
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

import SocketUrl from './SocketUrl.vue'
import SocketToolbarClipboard from './SocketToolbarClipboard.vue'
import SocketNewEntry from '@/components/project/socket/SocketNewEntry.vue'

export default defineComponent({
  components: {
    'rtdb-socket-url': SocketUrl,
    'rtdb-socket-toolbar-clipboard': SocketToolbarClipboard,
    'rtdb-socket-new-entry': SocketNewEntry,
  },
  setup: () => {
    return {}
  },
})
</script>

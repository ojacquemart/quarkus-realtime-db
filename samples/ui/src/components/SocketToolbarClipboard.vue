<template>
  <rtdb-socket-toolbar-button text="🌐" @click="copyWsUrl">🌐</rtdb-socket-toolbar-button>
  <rtdb-socket-toolbar-button text="🔑" @click="copyApikey">🌐</rtdb-socket-toolbar-button>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'
import { useStore } from 'vuex'

import SocketToolbarButton from '@/components/SocketToolbarButton.vue'

import { Clipboards } from '@/shared/Clipboard'
import { Urls } from '@/shared/Urls'

export default defineComponent({
  components: {
    'rtdb-socket-toolbar-button': SocketToolbarButton,
  },
  setup: () => {
    const store = useStore()

    return {
      copyWsUrl: async () => Clipboards.copy(
        [Urls.getWebsocketUrl(), store.getters['collections/getProjectCollectionUrlPart']].join('/'),
      ),
      copyApikey: async () => Clipboards.copy(
        store.getters['collections/getApikey'],
      ),
    }
  },
})
</script>

<template>
  <div class="inline-block sm:mb-2 md:mb-2 border border-white p-2 rounded-lg select-all">
    {{ wsBaseUrl }}/{{ $store.getters['collections/getProjectCollectionUrlPart'] }}
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

import { Urls } from '@/shared/Urls'

export default defineComponent({
  setup: () => {
    return {
      wsBaseUrl: Urls.getWebsocketUrl(),
    }
  },
})
</script>

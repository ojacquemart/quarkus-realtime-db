<template>
  <header class="mb-6 mx-0 md:mx-5 p-2 flex justify-between">
    <h1 class="text-xl text-left cursor-pointer" @click="$router.push('/')">
      🔥 {{ $t('title') }}
    </h1>
    <rtdb-settings></rtdb-settings>
  </header>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import Settings from '@/components/shared/Settings.vue'

export default defineComponent({
  components: {
    'rtdb-settings': Settings,
  },
})
</script>

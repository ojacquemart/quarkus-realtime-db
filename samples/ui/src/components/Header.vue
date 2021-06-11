<template>
  <header class="mb-6 ml-0 md:ml-5 p-2">
    <h1 class="text-xl text-left cursor-pointer" @click="$router.push('/')">
      🔥 {{ t('title') }}
    </h1>
  </header>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  setup: () => {
    return {
      ...useI18n(),
    }
  },
})
</script>

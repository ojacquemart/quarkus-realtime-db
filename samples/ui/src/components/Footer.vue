<template>
  <footer class="mt-2 py-2">
    <p class="text-sm">{{ t('footer.title') }} </p>
  </footer>
</template>

<script lang="typescript">
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


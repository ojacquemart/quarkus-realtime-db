<template>
  <header>
    <h1>
      {{ t('title') }}
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

<style>
header {
  padding: .5em;
}
</style>

<template>
  <button class="rounded bg-secondary py-2 px-4 mr-0.5 text-white">{{ text }}</button>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

export default defineComponent({
  props: [
    'text',
  ],
  setup: () => {
    return {}
  },
})
</script>

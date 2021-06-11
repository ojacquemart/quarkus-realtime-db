<template>
  <div class="rounded overflow-hidden shadow-lg bg-secondary h-full cursor-pointer" @click="goTo">
    <div class="flex items-center justify-center h-full">
      <div class="font-bold text-xl">{{ project }}</div>
    </div>
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

export default defineComponent({
  props: ['project'],
  methods: {
    goTo() {
      this.$router.push(`/projects/${this.project}/collections`)
    },
  },
})

</script>

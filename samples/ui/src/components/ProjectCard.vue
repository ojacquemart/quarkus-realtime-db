<template>
  <div class="rounded overflow-hidden shadow-lg bg-secondary h-full cursor-pointer" @click="goTo('foo')">
    <div class="flex items-center justify-center h-full">
      <div class="font-bold text-xl">Mountain</div>
    </div>
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

export default defineComponent({
  methods: {
    goTo(name) {
      console.log(`project @ go to project "${name}"`)

      this.$router.push(`/projects/${name}/collections`)
    },
  },
})

</script>

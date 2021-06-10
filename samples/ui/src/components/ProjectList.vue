<template>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
  <rtdb-project-card></rtdb-project-card>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'

import ProjectCard from './ProjectCard.vue'

export default defineComponent({
  components: {
    'rtdb-project-card': ProjectCard,
  },
  setup: () => {
    return {
      ...useI18n(),
    }
  },
})

</script>

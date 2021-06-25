<template>
  <rtdb-project-card-skeleton v-if="$store.getters['projects/isPending']"></rtdb-project-card-skeleton>
  <rtdb-project-card-error v-else-if="$store.getters['projects/isError']"></rtdb-project-card-error>
  <rtdb-project-card v-for="project in $store.state.projects.items" v-else :project="project"></rtdb-project-card>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'

import ProjectCard from '@/components/ProjectCard.vue'
import ProjectCardError from '@/components/ProjectCardError.vue'
import ProjectCardSkeleton from '@/components/ProjectCardSkeleton.vue'

export default defineComponent({
  components: {
    'rtdb-project-card': ProjectCard,
    'rtdb-project-card-error': ProjectCardError,
    'rtdb-project-card-skeleton': ProjectCardSkeleton,
  },
  setup() {
    const store = useStore()
    store.dispatch('projects/fetchProjects')

    return {
      ...useI18n(),
    }
  },
})

</script>

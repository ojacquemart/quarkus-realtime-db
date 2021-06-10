<template>
  <div class="ml-auto mr-auto sm:px-4 md:px-0 sm:w-screen md:w-2/3">
    <h2 class="text-xl text-left font-bold mb-3">{{ t('projects.title') }}</h2>

    <div class="grid sm:grid-cols-1 md:grid-cols-3 auto-rows-8 gap-4">
      <rtdb-new-project></rtdb-new-project>
      <rtdb-project-list></rtdb-project-list>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'

import NewProject from '../components/NewProject.vue'
import ProjectList from '../components/ProjectList.vue'

export default defineComponent({
  name: 'Home',
  components: {
    'rtdb-new-project': NewProject,
    'rtdb-project-list': ProjectList,
  },
  setup: () => {
    return {
      ...useI18n(),
    }
  },
})
</script>

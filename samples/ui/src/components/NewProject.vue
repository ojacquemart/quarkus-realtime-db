<template>
  <div class="rounded items-center overflow-hidden shadow-lg bg-secondary px-4 py-8 cursor-pointer"
       @click="$store.commit('modals/open', 'new-project')">
    <div class="flex items-center justify-center h-full items-stretch flex-col">
      <div class="flex items-center justify-center font-bold">➕</div>
      <div class="flex items-center justify-center font-bold">{{ t('projects.new_project') }}</div>
    </div>
  </div>

  <rtdb-modal-new-project
    dispatch-type="projects/createProject"
    i18n-title="projects.new_project"
    modal-id="new-project">
  </rtdb-modal-new-project>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'

import ModalNewText from '@/components/ModalNewText.vue'

export default defineComponent({
  components: {
    'rtdb-modal-new-project': ModalNewText,
  },
  setup: () => {
    return {
      ...useI18n(),
    }
  },
})
</script>

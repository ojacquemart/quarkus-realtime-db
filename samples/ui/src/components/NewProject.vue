<template>
  <div class="rounded items-center overflow-hidden shadow-lg bg-secondary px-4 py-8 cursor-pointer"
       @click="isOpened = true">
    <div class="flex items-center justify-center h-full items-stretch flex-col">
      <div class="flex items-center justify-center font-bold">➕</div>
      <div class="flex items-center justify-center font-bold">{{ t('projects.new_project') }}</div>
    </div>
  </div>

  <rtdb-modal-new-project
    v-model:is-opened="isOpened"
    dispatch-type="projects/createProject"
    i18n-title="projects.new_project">
  </rtdb-modal-new-project>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import ModalNewText from '@/components/ModalNewText.vue'

export default defineComponent({
  components: {
    'rtdb-modal-new-project': ModalNewText,
  },
  setup: () => {
    const isOpened = ref(false)

    return {
      ...useI18n(),
      isOpened,
    }
  },
})
</script>

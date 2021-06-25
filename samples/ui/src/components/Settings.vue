<template>
  <div>
    <button class="rounded text-lg" @click="$store.commit('modals/open', 'settings')">⚙️</button>
    <rtdb-settings-modal
      :text-value="$store.getters['settings/getUrl']"
      dispatch-type="settings/setUrl"
      i18n-input-label="modals.settings.url"
      i18n-title="modals.settings.title"
      input-type="text"
      modal-id="settings"></rtdb-settings-modal>
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

import ModalNewText from '@/components/ModalNewText.vue'

export default defineComponent({
  components: {
    'rtdb-settings-modal': ModalNewText,
  },
  setup: () => {
  },
})
</script>

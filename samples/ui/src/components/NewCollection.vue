<template>
  <button class="rounded bg-secondary hover:bg-blue-700 py-2 px-4 text-white group"
          @click="$store.commit('modals/open', 'new-collection')">
    ➕ {{ t('collections.collection') }}
  </button>

  <rtdb-modal-new-collection
    dispatch-type="collections/createCollection"
    i18n-title="collections.new_collection"
    modal-id="new-collection">
  </rtdb-modal-new-collection>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'
import { useI18n } from 'vue-i18n'

import ModalNewText from '@/components/ModalNewText.vue'

export default defineComponent({
  components: {
    'rtdb-modal-new-collection': ModalNewText,
  },
  setup: () => {
    return {
      ...useI18n(),
    }
  },
})
</script>

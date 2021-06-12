<template>
  <button class="rounded bg-secondary hover:bg-blue-700 py-2 px-4 text-white"
          @click="isOpened = true">
    ➕ {{ t('collections.collection') }}
  </button>

  <rtdb-modal-new-collection
    v-model:is-opened="isOpened"
    dispatch-type="collections/createCollection"
    i18n-title="collections.new_collection">
  </rtdb-modal-new-collection>
</template>

<script lang="typescript">
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import ModalNewText from '@/components/ModalNewText.vue'

export default defineComponent({
  components: {
    'rtdb-modal-new-collection': ModalNewText,
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

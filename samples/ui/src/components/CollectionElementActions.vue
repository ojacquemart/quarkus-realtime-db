<template>
  <div class="inline">
    <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-0 md:ml-2" @click="updateEntry">📝</span>
    <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-2" @click="deleteEntry">🗑️</span>
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'
import { useStore } from 'vuex'

export default defineComponent({
  props: [
    'message',
  ],
  setup: () => {
    const store = useStore()

    const showModal = (id) => {
      store.commit('modals/open', id)
    }

    return {
      updateEntry: () => showModal('update-entry'),
      deleteEntry: () => showModal('confirm-delete'),
    }
  },
})
</script>

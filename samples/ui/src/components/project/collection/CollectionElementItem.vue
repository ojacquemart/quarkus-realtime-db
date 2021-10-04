<template>
  <div class="inline-table" @click="$store.commit('collections/setActiveIndex', index)">
    <div class="inline">
      <span class="mr-2">➕</span>
      <span>{{ message.content._id }}</span>
    </div>
    <rtdb-collection-element-actions :index="index"></rtdb-collection-element-actions>
  </div>
  <div v-if="$store.getters['collections/getActiveIndex'] === index" class="mt-2">
    <pre class="p-2 bg-gray-200">{{ message.content }}</pre>
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'

import CollectionElementActions from '@/components/project/collection/CollectionElementActions.vue'

export default defineComponent({
  components: {
    'rtdb-collection-element-actions': CollectionElementActions,
  },
  props: [
    'message',
    'index',
  ],
  setup: () => {
    return {}
  },
})
</script>

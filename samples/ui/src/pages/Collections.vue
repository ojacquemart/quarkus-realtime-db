<template>
  <div class="text-left ml-auto mr-auto sm:px-4 md:px-0 sm:w-screen md:w-2/3">
    <div class="flex justify-start items-end">
      <span class="text-xl text-left font-bold mr-4">
        {{ t('collections.project') }} {{ $route.params.name }}
      </span>
      <rtdb-new-collection></rtdb-new-collection>
    </div>
    <rtdb-collections-select></rtdb-collections-select>
    <rtdb-collection-socket-toolbar></rtdb-collection-socket-toolbar>

    <div class="mt-2 py-2 rounded-lg bg-secondary-light text-black">
      <div v-for="index in 10" class="cursor-pointer py-1 px-2">
        <div class="inline-table" @click="activeId = index">
          <div class="inline">
            <span class="mr-2">➕</span>
            <span>123e4567-e89b-12d3-a456-42661417400{{ index }}</span>
          </div>
          <div class="inline">
            <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-0 md:ml-2">📝</span>
            <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-2">🗑️</span>
          </div>
        </div>
        <div v-if="activeId === index" class="mt-2">
          <pre class="p-2 bg-gray-200">{ "_id": "foo{{ activeId }}", "name": "foobarqix" }</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="typescript">
import { defineComponent, onUnmounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

import CollectionsSelect from '@/components/CollectionsSelect.vue'
import CollectionSocketToolbar from '@/components/SocketToolbar.vue'
import NewCollection from '@/components/NewCollection.vue'

export default defineComponent({
  components: {
    'rtdb-collections-select': CollectionsSelect,
    'rtdb-new-collection': NewCollection,
    'rtdb-collection-socket-toolbar': CollectionSocketToolbar,
  },
  setup: () => {
    const route = useRoute()
    const store = useStore()
    store.dispatch('collections/fetchProject', route.params.name)

    onUnmounted(() => {
      store.commit('collections/clear')
    })

    const activeId = ref(-1)

    return {
      ...useI18n(),
      activeId,
      changeCollection: ($event) => {
        store.commit('collections/setCollection', $event.target.value)
      },
    }
  },
})
</script>

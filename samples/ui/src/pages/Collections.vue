<template>
  <div class="text-left ml-auto mr-auto sm:px-4 md:px-0 sm:w-screen md:w-2/3">
    <span class="text-xl text-left font-bold mt-6 mb-3 mr-4">
      {{ t('collections.project') }} {{ $route.params.name }}
    </span>

    <rtdb-new-collection></rtdb-new-collection>
    <rtdb-collections-select></rtdb-collections-select>

    <div class="mt-4">
      <div class="inline-block sm:mb-2 md:mb-2 border border-white p-2 rounded-lg select-all">
        wss://localhost:8080/foobarqix/demo/_all
      </div>
      <div class="mt-2 md:inline md:ml-2">
        <button class="rounded bg-secondary py-2 px-4 mr-0.5 text-white">📋</button>
        <button class="rounded bg-secondary py-2 px-4 mr-0.5 text-white">➕</button>
        <button class="rounded bg-secondary py-2 px-4 text-white">🗑️</button>
      </div>
    </div>

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
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

import CollectionsSelect from '@/components/CollectionsSelect.vue'
import NewCollection from '@/components/NewCollection.vue'

export default defineComponent({
  components: {
    'rtdb-collections-select': CollectionsSelect,
    'rtdb-new-collection': NewCollection,
  },
  setup: () => {
    const activeId = ref(-1)
    const store = useStore()
    const route = useRoute()
    store.dispatch('collections/fetchProject', route.params.name)

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

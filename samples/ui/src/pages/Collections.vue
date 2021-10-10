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

    <rtdb-collection-elements-view></rtdb-collection-elements-view>
  </div>
</template>

<script lang="typescript">
import { defineComponent, onUnmounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

import CollectionsSelect from '@/components/project/collection/CollectionsSelect.vue'
import CollectionSocketToolbar from '@/components/project/socket/SocketToolbar.vue'
import CollectionElementsView from '@/components/project/collection/CollectionElementsView.vue'
import NewCollection from '@/components/project/collection/NewCollection.vue'

export default defineComponent({
  components: {
    'rtdb-collections-select': CollectionsSelect,
    'rtdb-new-collection': NewCollection,
    'rtdb-collection-socket-toolbar': CollectionSocketToolbar,
    'rtdb-collection-elements-view': CollectionElementsView,
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

<template>
  <div v-if="$store.getters['collections/hasCollection']" class="relative mt-6 border-none">
    <select
      class="bg-secondary-light appearance-none border-none inline-block text-black py-3 pl-3 pr-8 rounded leading-tight w-full"
      @change="changeCollection">
      <option v-for="collection in $store.getters['collections/getCollections']"
              :value="collection"
              class="pt-6">
        {{ collection }}
      </option>
    </select>
  </div>
</template>

<script lang="typescript">
import { defineComponent } from 'vue'
import { useStore } from 'vuex'

export default defineComponent({
  setup: () => {
    const store = useStore()

    return {
      changeCollection: ($event) => {
        store.commit('collections/setCollection', $event.target.value)
      },
    }
  },
})
</script>

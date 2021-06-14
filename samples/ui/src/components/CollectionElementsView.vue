<template>
  <div class="mt-2 py-2 rounded-lg bg-secondary-light text-black" v-if="$store.getters['collections/hasMessages']">
    <div v-for="(message, index) in $store.getters['collections/getMessages']" class="cursor-pointer py-1 px-2">
      <div class="inline-table" @click="activeId = index">
          <div class="inline">
            <span class="mr-2">➕</span>
            <span>{{ message.content._id }}</span>
          </div>
        <div class="inline">
          <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-0 md:ml-2">📝</span>
          <span class="opacity-100 sm:opacity-0 hover:opacity-90 ml-2">🗑️</span>
        </div>
      </div>
      <div v-if="activeId === index" class="mt-2">
        <pre class="p-2 bg-gray-200">{{ message.content }}</pre>
      </div>
    </div>
  </div>
  <div v-else>
    <p class="text-center mt-4">{{ t('collections.no_data1') }}</p>
    <p class="text-center">{{ t('collections.no_data2') }}</p>
  </div>
</template>

<script lang="typescript">
import { defineComponent, ref } from 'vue'

import { useI18n } from 'vue-i18n'

export default defineComponent({
  setup: () => {
    const activeId = ref(-1)

    return {
      ...useI18n(),
      activeId,
    }
  },
})
</script>

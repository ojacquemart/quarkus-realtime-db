<template>
  <div class="rounded items-center overflow-hidden shadow-lg bg-secondary px-4 py-8 cursor-pointer"
       @click="isModalOpened = true">
    <div class="flex items-center justify-center h-full items-stretch flex-col">
      <div class="flex items-center justify-center font-bold">➕</div>
      <div class="flex items-center justify-center font-bold">{{ t('projects.new_project') }}</div>
    </div>
  </div>

  <rtdb-modal v-model:is-opened="isModalOpened">
    <template v-slot:header>
      {{ t('projects.new_project') }}
    </template>
    <template v-slot:body>
      <p>{{ t('common.name') }}</p>
      <input v-model="name"
             class="relative outline-none rounded py-3 px-3 w-full bg-primary shadow text-sm text-white placeholder-gray-400 focus:ring-1 focus:border-gray-300"
             type="text"/>
    </template>
    <template v-slot:buttons>
      <button :disabled="(name || '').length === 0"
              class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-primary-light text-base font-medium text-white focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-500 disabled:opacity-50 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="save">
        {{ t('common.ok') }}
      </button>
      <button ref="cancelButtonRef"
              class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-gray-100 text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-0 focus:ring-gray-300 sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm"
              type="button" @click="isModalOpened = false">
        {{ t('common.cancel') }}
      </button>
    </template>
  </rtdb-modal>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import Modal from './Modal.vue'

export default defineComponent({
  components: {
    'rtdb-modal': Modal,
  },
  setup: () => {
    const isModalOpened = ref(false)
    const name = ref(null)

    return {
      isModalOpened,
      name,
      ...useI18n(),
    }
  },
  methods: {
    save() {
      console.log(`newProject @ save "${this.name}"`)

      this.isModalOpened = false
      this.name = null
    },
  },
})
</script>

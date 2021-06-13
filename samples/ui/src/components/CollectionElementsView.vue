<template>
  <div class="mt-2 py-2 rounded-lg bg-secondary-light text-black">
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
</template>

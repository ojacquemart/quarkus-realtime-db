<template>
  <button class="rounded bg-secondary hover:bg-blue-700 py-2 px-4 text-white group"
          @click="$store.commit('modals/open', 'new-collection')">
    ➕ {{ $t('collections.collection') }}
  </button>
</template>

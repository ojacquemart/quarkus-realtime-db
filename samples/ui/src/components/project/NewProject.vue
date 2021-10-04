<template>
  <div class="rounded items-center overflow-hidden shadow-lg bg-secondary px-4 py-8 cursor-pointer"
       @click="$store.commit('modals/open', 'new-project')">
    <div class="flex items-center justify-center h-full items-stretch flex-col">
      <div class="flex items-center justify-center font-bold">➕</div>
      <div class="flex items-center justify-center font-bold">{{ $t('projects.new_project') }}</div>
    </div>
  </div>
</template>


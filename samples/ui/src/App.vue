<template>
  <div class="grid grid-rows-layout-3 min-h-full">
    <rtdb-header></rtdb-header>
    <div class="px-2 sm:px-2 md:px-0 lg:px-0 xl:px-0 2xl:px0">
      <Suspense>
        <router-view></router-view>
      </Suspense>
      <rtdb-modal v-if="$store.getters['modals/hasSelection']"></rtdb-modal>
    </div>
    <rtdb-footer></rtdb-footer>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import Header from '@/components/shared/Header.vue'
import Footer from '@/components/shared/Footer.vue'
import DynamicModal from '@/components/modal/DynamicModal.vue'

export default defineComponent({
  name: 'App',
  components: {
    'rtdb-header': Header,
    'rtdb-modal': DynamicModal,
    'rtdb-footer': Footer,
  },
})
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  height: 100%;
}
</style>

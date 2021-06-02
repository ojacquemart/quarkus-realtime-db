<template>
  <rtdb-header></rtdb-header>
  <router-view></router-view>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import Header from './components/Header.vue'

export default defineComponent({
  name: 'App',
  components: {
    'rtdb-header': Header
  },
})
</script>

<style>
:root {
  --background-color: #1b262c;
  --text-color: #fff;
}

html, body {
  background: var(--background-color);
  color: var(--text-color);

  margin: 0;
  padding: 0;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
</style>

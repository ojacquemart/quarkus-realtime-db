<template>
  <div v-if="$store.getters['collections/hasMessages']" class="mt-2 py-2 rounded-lg bg-secondary-light text-black">
    <transition-group name="list" tag="div">
      <template v-for="(message, index) in $store.getters['collections/getMessages']"
                :key="index">
        <div class="cursor-pointer py-1 px-2" v-bind:class="[message.type]">
          <rtdb-collection-element-item :index="index" :message="message"></rtdb-collection-element-item>
        </div>
      </template>
    </transition-group>
  </div>
  <div v-else>
    <p class="text-center mt-4">{{ $t('collections.no_data1') }}</p>
    <p class="text-center">{{ $t('collections.no_data2') }}</p>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import CollectionElementItem from '@/components/project/collection/CollectionElementItem.vue'

export default defineComponent({
  components: {
    'rtdb-collection-element-item': CollectionElementItem,
  },
})
</script>

<style>
.list-enter-active,
.list-leave-active {
  transition: all 1s ease;
}

.UPDATE {
  animation: update 1s ease;
}

.DELETE {
  animation: delete 1s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(-30px);
}

.list-enter-from {
  background: green;
}

@keyframes update {
  0% {
    background: orange;
  }
  100% {
    background: transparent;
  }
}

@keyframes delete {
  0% {
    background: red;
  }
  100% {
    background: transparent;
  }
}
</style>

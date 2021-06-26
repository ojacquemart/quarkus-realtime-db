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

    <div v-if="$store.getters['collections/getActiveIndex'] !== -1">
      <rtdb-modal-edit-message
        :text-value="activeMessageContent"
        dispatch-type="collections/updateEntry"
        i18n-input-label="common.content"
        i18n-title="modals.edit_entry.title"
        input-type="textarea"
        modal-id="update-entry">
      </rtdb-modal-edit-message>
      <rtdb-modal-confirm-delete
        modal-id="confirm-delete">
      </rtdb-modal-confirm-delete>
    </div>
  </div>
  <div v-else>
    <p class="text-center mt-4">{{ $t('collections.no_data1') }}</p>
    <p class="text-center">{{ $t('collections.no_data2') }}</p>
  </div>
</template>

<script lang="typescript">
import { computed, defineComponent } from 'vue'
import { useStore } from 'vuex'

import ModalConfirmDelete from '@/components/ModalConfirmDelete.vue'
import ModalNewText from '@/components/ModalNewText.vue'
import CollectionElementItem from '@/components/CollectionElementItem.vue'

export default defineComponent({
  components: {
    'rtdb-modal-confirm-delete': ModalConfirmDelete,
    'rtdb-modal-edit-message': ModalNewText,
    'rtdb-collection-element-item': CollectionElementItem,
  },
  setup: () => {
    const store = useStore()

    const activeMessageContent = computed(() => {
      return store.getters['collections/getActiveMessageContent']
    })
    return {
      activeMessageContent,
    }
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

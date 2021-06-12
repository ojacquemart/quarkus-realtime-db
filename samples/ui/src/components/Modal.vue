<template>
  <TransitionRoot :show="open" as="template">
    <Dialog :open="open" as="div" class="fixed z-10 inset-0 overflow-y-auto" static @close="open = false">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100"
                         leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
          <DialogOverlay class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"/>
        </TransitionChild>

        <span aria-hidden="true" class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>
        <TransitionChild as="template" enter="ease-out duration-300"
                         enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                         enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200"
                         leave-from="opacity-100 translate-y-0 sm:scale-100"
                         leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
          <div
            class="inline-block align-bottom bg-primary rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
            <div class="bg-primary px-4 pt-5 pb-4 sm:p-6 sm:pb-4 bg-primary">
              <div class="sm:flex sm:items-start">
                <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                  <DialogTitle as="h3" class="text-lg leading-6 font-medium text-white">
                    <slot name="header"></slot>
                  </DialogTitle>
                  <div class="mt-2">
                    <div class="p-2">
                      <slot name="body"></slot>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="bg-secondary-light px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
              <slot name="buttons"></slot>
            </div>
          </div>
        </TransitionChild>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script lang="ts">
import { computed, defineComponent } from 'vue'

import { Dialog, DialogOverlay, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'
import { useI18n } from 'vue-i18n'

export default defineComponent({
  components: {
    Dialog,
    DialogOverlay,
    DialogTitle,
    TransitionChild,
    TransitionRoot,
  },
  props: {
    isOpened: {
      type: Boolean,
      default: false,
    },
  },
  setup(props, {emit}) {
    const toggleOpen = (value) => {
      emit('update:is-opened', value)
    }
    const open = computed({
      get: () => props.isOpened,
      set: (value) => toggleOpen(value),
    })

    return {
      ...useI18n(),
      open,
      cancel: () => toggleOpen(false),
    }
  },
})
</script>

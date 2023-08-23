<template>
  <v-snackbar v-model="show" :timeout="timeout" :color="colorType">
    {{ $store.getters['message/text'] }}
    <template #action="{ attrs }">
      <v-btn
        color="white"
        text
        v-bind="attrs"
        @click="$store.dispatch('message/hide')"
      >
        Close
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script>
import { Component } from 'nuxt-property-decorator'
import Vue from 'vue'

@Component
export default class MessageInfo extends Vue {
  get show() {
    return this.$store.getters['message/show']
  }

  get colorType() {
    const type = this.$store.getters['message/type']
    switch (type) {
      case 'success':
        return 'green'
      case 'info':
        return '#1976d2'
      case 'error':
        return 'red'
    }
  }

  set show(value) {
    if (value) {
      return this.$store.dispatch('message/show')
    } else {
      return this.$store.dispatch('message/hide')
    }
  }

  get timeout() {
    return this.$store.getters['message/timeout']
  }
}
</script>

<template>
  <v-app dark>
    <v-app-bar :clipped-left="true" fixed app>
      <v-toolbar-title style="cursor: pointer" @click="$router.push('/')">
        {{ title }}
      </v-toolbar-title>
      <v-spacer />
    </v-app-bar>
    <v-main>
      <v-container>
        <Nuxt />
        <message-info />
      </v-container>
    </v-main>
    <v-footer :absolute="false" app>
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
import { Component } from 'nuxt-property-decorator'
import Vue from 'vue'

@Component({
  name: 'login-layout',
})
export default class LoginLayout extends Vue {
  drawer = false
  items = [
    {
      icon: 'mdi-apps',
      title: 'Welcome',
      to: '/',
    },
    {
      icon: 'mdi-chart-bubble',
      title: 'Inspire',
      to: '/inspire',
    },
  ]

  rightDrawer = false
  title = 'Sample App'
}
</script>
<style>
.v-autocomplete.v-select--is-menu-active .v-input__icon--append .v-icon {
  transform: none;
}
</style>

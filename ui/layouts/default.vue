<template>
  <v-app dark>
    <v-navigation-drawer
      v-model="drawer"
      :mini-variant="false"
      :clipped="true"
      fixed
      app
    >
      <v-list>
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          :to="item.to"
          router
          exact
        >
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>
              {{ item.title }}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-app-bar :clipped-left="true" fixed app>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title style="cursor: pointer" @click="$router.push('/')">
        {{ title }}
      </v-toolbar-title>
      <v-autocomplete
        dense
        clearable
        rounded
        solo-inverted
        hide-details
        append-icon="mdi-magnify"
        class="mx-3"
      ></v-autocomplete>
      <v-btn fab dark rounded small>
        <v-icon> mdi-account-plus </v-icon>
      </v-btn>
      <v-btn class="mx-3" fab dark rounded small>
        <v-icon> mdi-chat </v-icon>
      </v-btn>
      <v-spacer />
      <v-btn icon @click.stop="rightDrawer = !rightDrawer">
        <v-img contain max-height="40" src="avatar.png"></v-img>
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-container>
        <Nuxt />
        <message-info />
      </v-container>
    </v-main>
    <v-navigation-drawer v-model="rightDrawer" :right="true" temporary fixed>
      <v-list-item class="px-2">
        <v-list-item-avatar>
          <v-img src="avatar.png"></v-img>
        </v-list-item-avatar>
        <v-list-item-title class="mx-1">John Leider</v-list-item-title>
      </v-list-item>
      <v-divider />
      <v-list-item to="/setting">
        <v-icon> mdi-cog </v-icon>
        <v-list-item-title class="mx-3"> Setting </v-list-item-title>
      </v-list-item>
      <v-list-item @click="$authService.logout()">
        <v-icon> mdi-logout </v-icon>
        <v-list-item-title class="mx-3"> Logout </v-list-item-title>
      </v-list-item>
    </v-navigation-drawer>
    <v-footer :absolute="false" app>
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
import { Component } from 'nuxt-property-decorator'
import Vue from 'vue'

@Component({
  middleware: 'auth',
})
export default class Main extends Vue {
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

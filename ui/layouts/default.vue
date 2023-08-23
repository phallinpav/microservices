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
    <v-app-bar :clipped-left="true" fixed app height="60px">
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title style="cursor: pointer" @click="$router.push('/')">
        {{ title }}
      </v-toolbar-title>
      <v-autocomplete
        :loading="isLoading"
        :search-input.sync="search"
        :items="accounts"
        item-text="username"
        item-value="id"
        dense
        clearable
        rounded
        solo-inverted
        hide-details
        hide-no-data
        append-icon="mdi-magnify"
        class="mx-3"
        @click="getAccountList()"
      >
        <template #item="data">
          <v-list-item-avatar
            color="indigo"
            class="text-h5 font-weight-light white--text"
          >
            {{ data.item.id }}
          </v-list-item-avatar>
          <v-list-item-content @click="requestToAcc(data.item)">
            <v-list-item-title>
              <span v-text="data.item.username"></span>
            </v-list-item-title>
            <v-list-item-subtitle v-if="data.item.isFriend === true"
              >Friend</v-list-item-subtitle
            >
          </v-list-item-content>
        </template>
      </v-autocomplete>
      <v-menu offset-y :close-on-click="closeOnClick">
        <template #activator="{ on, attrs }">
          <v-btn
            class="mr-3"
            v-bind="attrs"
            fab
            light
            rounded
            small
            @click="hasFriRequest = 0"
            v-on="on"
          >
            <v-badge
              v-if="hasFriRequest > 0"
              :content="hasFriRequest"
              color="error"
            >
              <v-icon>mdi-account-plus</v-icon>
            </v-badge>
            <v-icon v-else>mdi-account-plus</v-icon>
          </v-btn>
        </template>

        <v-list class="py-1" width="400px">
          <span class="px-4 overline"> Friend Requests </span>
          <div v-if="friendRequests.length === 0">
            <span class="px-6 caption"> No friend requests </span>
          </div>
          <div v-for="(item, index) in friendRequests" :key="index">
            <v-list-item>
              <v-list-item-icon class="py-1">
                <v-avatar :color="randomColor(item.id)" size="35">
                  <span class="text-h5">{{
                    item.username[0].toUpperCase()
                  }}</span>
                </v-avatar>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>{{ item.username }}</v-list-item-title>
                <v-list-item-subtitle class="py-1">
                  <v-btn
                    small
                    color="primary mx-1"
                    @click="acceptFriendRequest(item.id)"
                  >
                    Accept
                  </v-btn>
                  <v-btn
                    small
                    color="red darken-1 mx-1"
                    @click="denyFriendRequest(item.id)"
                  >
                    Deny
                  </v-btn>
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>

            <v-divider></v-divider>
          </div>
        </v-list>
      </v-menu>

      <v-menu offset-y :close-on-click="closeOnClick">
        <template #activator="{ on, attrs }">
          <v-btn
            class="mr-3"
            v-bind="attrs"
            fab
            light
            rounded
            small
            @click="hasNewChat = 0"
            v-on="on"
          >
            <v-badge v-if="hasNewChat > 0" :content="hasNewChat" color="error">
              <v-icon>mdi-chat</v-icon>
            </v-badge>
            <v-icon v-else>mdi-chat</v-icon>
          </v-btn>
        </template>

        <v-list class="py-0" width="400px">
          <div v-for="(item, index) in chats" :key="index">
            <v-list-item>
              <v-list-item-icon>
                <v-avatar :color="randomColor(item.id)" size="35">
                  <span class="text-h5">{{ item.name[0].toUpperCase() }}</span>
                </v-avatar>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>{{ item.name }}</v-list-item-title>
                <v-list-item-subtitle>{{ item.text }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>

            <v-divider></v-divider>
          </div>
        </v-list>
      </v-menu>

      <v-spacer />
      <v-btn icon @click.stop="rightDrawer = !rightDrawer">
        <v-avatar color="red" size="40">
          <v-img
            v-if="authUser.profileImgUrl"
            :src="authUser.profileImgUrl"
          ></v-img>
          <span v-else class="text-h5">{{
            authUser.username[0].toUpperCase()
          }}</span>
        </v-avatar>
        <!-- if have profile photo use v-img -->
        <!-- <v-img contain max-height="40" src="avatar.png"></v-img> -->
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-container fluid px-0 py-0 mx-0 my-0>
        <Nuxt />
        <message-info />
      </v-container>
    </v-main>
    <v-navigation-drawer v-model="rightDrawer" :right="true" temporary fixed>
      <v-list-item class="px-2">
        <v-list-item-avatar>
          <v-avatar color="red">
            <v-img
              v-if="authUser.profileImgUrl"
              :src="authUser.profileImgUrl"
            ></v-img>
            <span v-else class="text-h5">{{
              authUser.username[0].toUpperCase()
            }}</span>
          </v-avatar>
          <!-- if have profile photo use v-img -->
          <!-- <v-img src="avatar.png"></v-img> -->
        </v-list-item-avatar>
        <v-list-item-title class="mx-1">{{
          authUser.username
        }}</v-list-item-title>
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
    <v-footer :absolute="false" app height="30px">
      <span>&copy; {{ new Date().getFullYear() }}</span>
    </v-footer>
  </v-app>
</template>

<script>
import { Component, Getter, Watch } from 'nuxt-property-decorator'
import Vue from 'vue'

@Component({
  middleware: 'auth',
})
export default class Main extends Vue {
  drawer = false
  items = [
    {
      icon: 'mdi-home',
      title: 'Home',
      to: '/',
    },
    {
      icon: 'mdi-account-multiple',
      title: 'Friends',
      to: '/friends',
    },
    {
      icon: 'mdi-chat',
      title: 'Messages',
      to: '/messages',
    },
  ]

  rightDrawer = false
  hasFriRequest = 0
  hasNewChat = 0

  title = 'APP SOCIAL'
  authUser = this.getAuthUser
  accounts = []
  search = ''
  isLoading = false
  account = ''

  id = this.$store.getters['acc-info/id']
  socket

  chats = []
  friendRequests = []
  closeOnClick = true

  colorCache = {}

  @Getter
  get getAuthUser() {
    return this.$store.getters['acc-info/all']
  }

  @Watch('search')
  onSearchChanged(newVal) {
    this.accounts = []
    if (this.isLoading) {
      return
    }

    this.isLoading = true

    this.$accountService.searchAccountByUsername(newVal).then((res) => {
      this.accounts = res
      this.isLoading = false
    })
  }

  async getAccountList() {
    await this.$accountService.searchAccounts().then((res) => {
      this.accounts = res
    })
  }

  async requestToAcc(account) {
    if (account.isFriend === true) {
      await this.$friendsService
        .removeFriend(account.id)
        .then(() => {
          console.log('success')
        })
        .catch((error) => {
          console.log(error.response.data.message)
        })
    } else {
      await this.$friendsService.addFriend(account.id).catch((error) => {
        console.log(error.response.data.message)
      })
    }
  }

  async mounted() {
    document.documentElement.style.overflowY = 'auto'

    // TODO: this socket code could move to certain file specifically for better structure
    // FIXME: localhost:5236 declare this properly in setting somewhere
    this.socket = new window.WebSocket(
      `ws://localhost:5555/chat-notify/${this.id}`
    )
    // this.socket = new window.WebSocket(`ws://localhost:5236/notification/${this.id}`)

    // Event handler for WebSocket connection open
    // this.socket.onopen = () => {
    //   console.log('WebSocket connection established.')
    // }

    // Event handler for receiving WebSocket messages
    this.socket.onmessage = async (event) => {
      // console.log('Received message:', event.data)
      this.hasNewChat += 1

      const fromAccId = parseInt(event.data.split(':')[1])

      const chat = await this.$chatService.getChatWith(fromAccId, 1)
      if (chat) {
        this.chats = this.chats.map((oldChat) => {
          if (oldChat.id === fromAccId) {
            return {
              ...oldChat,
              text: chat[0].message,
            }
          }
          return oldChat
        })
      }

      const payload = {
        // Add any data you want to pass as parameters
        accId: fromAccId,
      }

      // Trigger the event from the normal page with parameters
      this.$root.$emit('newChat', payload)
    }

    // // Event handler for WebSocket connection close
    // this.socket.onclose = () => {
    //   console.log('WebSocket connection closed.')
    // }

    // // Event handler for WebSocket connection errors
    // this.socket.onerror = (error) => {
    //   console.error('WebSocket error:', error)
    // }

    this.friendRequests = await this.$friendsService.getFriendRequest()
    this.hasFriRequest = this.friendRequests.length

    const friends = await this.$friendsService.getFriends()
    for (const friend of friends) {
      const chat = await this.$chatService.getChatWith(friend.id, 1)
      if (chat && chat[0]) {
        this.chats.push({
          id: friend.id,
          name: friend.username,
          text: chat[0].message,
        })
      }
    }
  }

  beforeDestroy() {
    if (
      process.client &&
      this.socket &&
      this.socket.readyState === WebSocket.OPEN
    ) {
      this.socket.close()
    }
  }

  randomColor(id) {
    const r = () => Math.floor(256 * Math.random())

    return (
      this.colorCache[id] ||
      (this.colorCache[id] = `rgb(${r()}, ${r()}, ${r()})`)
    )
  }

  acceptFriendRequest(id) {
    this.$friendsService.acceptFriend(id).then(() => {
      this.friendRequests = this.friendRequests.filter((item) => item.id !== id)
    })
  }

  denyFriendRequest(id) {
    this.$friendsService.denyFriend(id).then(() => {
      this.friendRequests = this.friendRequests.filter((item) => item.id !== id)
    })
  }
}
</script>
<style>
.v-autocomplete.v-select--is-menu-active .v-input__icon--append .v-icon {
  transform: none;
}

.v-menu__content {
  box-shadow: 2px 2px 6px cadetblue;
}
</style>

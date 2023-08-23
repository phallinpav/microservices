<template>
  <div class="row my-0 py-0">
    <div class="col-3 my-0 py-0 pr-0 scrollable-container">
      <v-list class="scrollable-list">
        <v-subheader>FRIENDS</v-subheader>
        <v-list-item-group v-model="selectedItem" mandatory color="primary">
          <v-list-item v-for="(user, i) in friends" :key="i">
            <v-list-item-icon>
              <v-avatar :color="randomColor(user.username)" size="35">
                <span class="text-h5">{{
                  user.username[0].toUpperCase()
                }}</span>
              </v-avatar>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>{{ user.username }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </div>
    <div class="col-9 my-0 py-0 pl-1">
      <chat-box
        class="scrollable-container"
        :messages="messages"
        :with-acc-id="withAccId"
      ></chat-box>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Watch } from 'nuxt-property-decorator'
import Vue from 'vue'
import ChatBox from '~/components/ChatBox.vue'

@Component({
  components: { ChatBox },
  middleware: 'auth',
})
export default class Main extends Vue {
  authUser = this.$store.getters['acc-info/all']
  selectedItem = 0
  friends: any = []
  colorCache: any = {}

  messages: any = []
  withAccId: number = 0

  randomColor(id: any) {
    const r = () => Math.floor(256 * Math.random())

    return (
      this.colorCache[id] ||
      (this.colorCache[id] = `rgb(${r()}, ${r()}, ${r()})`)
    )
  }

  mounted() {
    this.$friendsService.getFriends().then((friends: any) => {
      this.friends = friends

      if (friends) {
        this.withAccId = this.friends[0].id
        this.updateChatMessagesWith(this.withAccId, this.friends[0].username)
      }
    })

    this.$root.$on('newChat', this.handleEventNewChat)
  }

  handleEventNewChat(payload: any) {
    const friend = this.friends.find((f: any) => f.id === payload.accId)
    if (friend) {
      this.updateChatMessagesWith(payload.accId, friend.username)
    }
  }

  @Watch('selectedItem')
  onSelectedItemChanged(newValue: any) {
    if (this.friends && this.friends[newValue]) {
      this.withAccId = this.friends[newValue].id
      this.updateChatMessagesWith(
        this.withAccId,
        this.friends[newValue].username
      )
    }
  }

  updateChatMessagesWith(accId: number, accName: string) {
    this.$chatService.getChatWith(accId).then((messages: any) => {
      this.messages = messages.map((value: any) => {
        return {
          name:
            this.authUser.id === value.accFrom
              ? this.authUser.username
              : accName,
          text: value.message,
          timestamp: value.sendAt,
          isFromUser: this.authUser.id === value.accFrom,
        }
      })
    })
  }
}
</script>
<style scoped>
.scrollable-container {
  height: calc(100vh - 90px); /* 90px = 60px header + 30px footer  */
  overflow-y: auto;
}

.scrollable-list {
  height: 100%; /* Fill the container height */
}
</style>

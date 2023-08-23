<template>
  <v-card class="chat-box">
    <v-list
      v-if="messages"
      :ref="scrollContainerRef"
      class="message-list d-flex flex-column-reverse"
    >
      <v-list-item
        v-for="(message, index) in currentMessage"
        :key="index"
        :class="{
          'message-left': !message.isFromUser,
          'message-right': message.isFromUser,
        }"
      >
        <v-list-item-content
          :class="{ 'd-flex flex-row-reverse': message.isFromUser }"
        >
          <v-list-item-subtitle
            v-if="
              currentMessage[index + 1] &&
              message.name != currentMessage[index + 1].name
            "
          >
            <v-avatar v-if="!message.isFromUser" color="red" size="20">
              <span class="text">{{ message.name[0].toUpperCase() }}</span>
            </v-avatar>
            {{ message.name }}
            <v-avatar v-if="message.isFromUser" color="red" size="20">
              <span class="text">{{ message.name[0].toUpperCase() }}</span>
            </v-avatar>
          </v-list-item-subtitle>
          <v-list-item-title
            class="message-bubble py-1"
            style="white-space: break-spaces"
            >{{ message.text }}</v-list-item-title
          >
          <v-list-item-subtitle
            v-if="
              !(index > 0 && message.name == currentMessage[index - 1].name)
            "
            class="caption"
            >{{ updateUTCDateFormat(message.timestamp) }}</v-list-item-subtitle
          >
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <v-divider></v-divider>
    <v-card-actions class="chat-actions">
      <v-textarea
        v-model="newMessage"
        label="Type your message"
        rows="1"
        outlined
        hide-details
      ></v-textarea>
      <v-btn color="primary ml-2" @click="sendMessage">Send</v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  props: {
    messages: {
      type: Array,
      required: true,
      default: () => Array,
    },
    withAccId: {
      type: Number,
      required: true,
      default: 0,
    },
  },
  data() {
    return {
      newMessage: '',
      scrollContainerRef: 'scrollContainer',
      currentMessage: '',
    }
  },
  watch: {
    messages(newMessages) {
      this.currentMessage = [...newMessages]
    },
  },
  methods: {
    sendMessage() {
      if (this.newMessage.trim() !== '') {
        this.currentMessage.unshift({
          name: this.$store.getters['acc-info/username'],
          text: this.newMessage,
          timestamp: new Date().toUTCString(),
          isFromUser: true,
        })
        this.$chatService.sendChatTo(this.withAccId, this.newMessage)
        this.newMessage = ''

        // Scroll to the bottom after sending a new message
        this.$nextTick(() => {
          const scrollContainer = this.$refs.scrollContainer
          scrollContainer.scrollTop = scrollContainer.scrollHeight
        })
      }
    },
    updateUTCDateFormat(date) {
      const utcDate = new Date(date)

      // Format as "YYYY-MM-DD HH:mm:ss"
      const formattedTime = utcDate.toLocaleString('en-US', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        timeZone: 'Asia/Bangkok', // Set the desired time zone
      })

      return formattedTime // Output: 2023-06-02 19:09:26
    },
  },
}
</script>

<style scoped>
.chat-box {
  display: flex;
  flex-direction: column;
}

.message-list {
  flex: 1;
  overflow-y: auto;
}

.chat-actions {
  margin-top: auto;
}

.message-left {
  text-align: left;
}

.message-right {
  text-align: right;
}

.message-bubble {
  background-color: #7b2dce;
  border-radius: 10px;
  padding: 8px 12px;
  display: inline-block;
  max-width: fit-content;
}
</style>

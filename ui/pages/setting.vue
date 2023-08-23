<template>
  <div>
    <v-card class="mx-auto">
      <v-img
        src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
        height="300px"
      ></v-img>

      <v-card-title class="">
        <v-avatar color="red" class="mx-2" size="200">
          <v-img
            v-if="authUser.profileImgUrl"
            :src="authUser.profileImgUrl"
          ></v-img>
          <span v-else class="text-h1">{{
            authUser.username[0].toUpperCase()
          }}</span>
        </v-avatar>
        <div>
          <span class="title"> {{ authUser.username }} </span>
          <br />
          <span class="subtitle-1"> {{ authUser.email }} </span>
          <br />

          <v-dialog v-model="dialog" width="500">
            <template #activator="{ on, attrs }">
              <v-btn color="primary" v-bind="attrs" v-on="on">
                <v-icon>mdi-account-edit</v-icon>
              </v-btn>
            </template>

            <v-card>
              <v-card-title class="text-h5 primary">
                Profile Edit
              </v-card-title>

              <v-card-text class="mt-2"> Profile Image </v-card-text>
              <v-card-actions>
                <v-avatar color="red" class="mx-2" size="200">
                  <v-img v-if="!!fileImg" :src="fileImg"></v-img>
                  <v-img
                    v-else-if="authUser.profileImgUrl"
                    :src="authUser.profileImgUrl"
                  ></v-img>
                  <span v-else class="text-h1">{{
                    authUser.username[0].toUpperCase()
                  }}</span>
                </v-avatar>
              </v-card-actions>
              <v-card-actions>
                <v-file-input
                  show-size
                  truncate-length="20"
                  accept="image/*"
                  label="Profile image input"
                  @change="onFileChange"
                ></v-file-input>
              </v-card-actions>

              <v-divider></v-divider>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary" @click="updateProfileImg">
                  Update
                </v-btn>
                <v-btn color="error" @click="dialog = false"> Close </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </div>
      </v-card-title>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Getter } from 'nuxt-property-decorator'
import Vue from 'vue'

@Component({
  middleware: 'auth',
})
export default class Main extends Vue {
  authUser = this.getAuthUser
  dialog = false
  fileImg = ''
  file = ''

  onFileChange(file: any) {
    this.file = file
    this.fileImg = URL.createObjectURL(file)
  }

  updateProfileImg() {
    this.$accountService.updateProfileImg(this.file).then((data) => {
      this.dialog = false
      this.$store.dispatch('acc-info/create', {
        ...this.authUser,
        profileImgUrl: `${data.profileImgUrl}&timestamp=${Date.now()}`,
      })
    })
  }

  @Getter
  public get getAuthUser(): any | null {
    return this.$store.getters['acc-info/all']
  }
}
</script>

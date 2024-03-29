<template>
  <v-form ref="form" v-model="valid" lazy-validation>
    <v-text-field
      v-model="username"
      :counter="30"
      :rules="usernameRules"
      label="Username"
      required
    ></v-text-field>

    <v-text-field
      v-model="email"
      :rules="emailRules"
      label="Email"
      required
    ></v-text-field>

    <v-text-field
      v-model="password"
      :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
      :rules="passwordRules"
      :type="showPassword ? 'text' : 'password'"
      label="Password"
      :hint="hintPass"
      counter
      @click:append="showPassword = !showPassword"
    ></v-text-field>

    <v-text-field
      v-model="confirmPassword"
      :append-icon="showConfirmPassword ? 'mdi-eye' : 'mdi-eye-off'"
      :rules="confirmPasswordRules"
      :type="showConfirmPassword ? 'text' : 'password'"
      label="Confirm Password"
      :hint="hintConfirmPass"
      counter
      @click:append="showConfirmPassword = !showConfirmPassword"
    ></v-text-field>

    <v-btn
      class="my-3"
      :disabled="!valid || loading"
      color="primary"
      @click="createAccount"
    >
      <v-icon class="mr-1" dense> mdi-account-plus </v-icon>
      Register
      <v-progress-circular
        v-if="loading"
        :size="20"
        class="ml-2"
        indeterminate
        color="primary"
      />
    </v-btn>

    <v-btn class="my-3 mx-3" color="primary" to="/login">
      <v-icon class="mr-1" dense> mdi-login </v-icon>
      Login
    </v-btn>
  </v-form>
</template>
<script lang="ts">
import Vue from 'vue'
import { Component } from 'nuxt-property-decorator'
import {
  usernameRules,
  emailRules,
  passwordRules,
  passwordHint,
  confirmPasswordRules,
} from '~/utils/validation/common-rules'

@Component({
  layout: 'login-layout',
})
export default class CreateAccount extends Vue {
  valid = true
  username = ''
  usernameRules = usernameRules

  email = ''
  emailRules = emailRules

  password = ''
  passwordRules = passwordRules
  hintPass = passwordHint
  showPassword = false

  confirmPassword = ''
  showConfirmPassword = false
  hintConfirmPass = 'Confirm Password must be same as Password'

  loading = false

  get confirmPasswordRules() {
    return confirmPasswordRules(this.password)
  }

  get form(): Vue & { validate: () => boolean } {
    return this.$refs.form as Vue & { validate: () => boolean }
  }

  async createAccount() {
    if (this.form.validate()) {
      const account = {
        username: this.username,
        email: this.email,
        password: this.password,
      }

      this.loading = true
      await this.$accountService
        .createAccount(account)
        .then(() => {
          this.$store.dispatch('message/info', {
            text: 'Please activate your account via email that we sent to you!',
            timeout: 5000,
          })
          this.$router.push('/login')
        })
        .catch((error) => {
          if (error.response.data.status === 409) {
            const message = error.response.data.message
            const reg = /^Duplicate entry '(.*)' for key 'account.(.*)'$/gi
            const matches = reg.exec(message)
            if (matches) {
              if (matches[2] === 'UK_acc_username') {
                // TODO: make the error message on field username as well
                this.$store.dispatch('message/error', {
                  text: 'username already exist!',
                })
              } else if (matches[2] === 'UK_acc_email') {
                // TODO: make the error message on field email as well
                this.$store.dispatch('message/error', {
                  text: 'email already exist!',
                })
              }
            } else {
              this.$store.dispatch('message/error', {
                text: 'Unexpected error!',
              })
            }
          } else {
            this.$store.dispatch('message/error', { text: 'Unexpected error!' })
          }
        })
        .finally(() => {
          this.loading = false
        })
    }
  }
}
</script>

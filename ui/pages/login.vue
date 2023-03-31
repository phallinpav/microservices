<template>
  <div>
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-text-field
        v-model="name"
        :counter="10"
        :rules="nameRules"
        label="Username"
        required
      ></v-text-field>

      <v-text-field
        v-model="password"
        :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
        :rules="passwordRules"
        :type="showPassword ? 'text' : 'password'"
        label="Password"
        counter
        @click:append="showPassword = !showPassword"
      ></v-text-field>

      <v-btn
        class="my-3"
        :disabled="!valid || loading"
        color="primary"
        @click="login"
      >
        <v-icon class="mr-1" dense> mdi-login </v-icon>
        Login
        <v-progress-circular
          v-if="loading"
          :size="20"
          class="ml-2"
          indeterminate
          color="primary"
        />
      </v-btn>

      <v-btn class="my-3 mx-3" color="primary" to="/register">
        <v-icon class="mr-1" dense> mdi-account-plus </v-icon>
        Register
      </v-btn>
    </v-form>
  </div>
</template>
<script lang="ts">
import Vue from 'vue'
import { Component } from 'nuxt-property-decorator'

@Component({
  layout: 'login-layout',
})
export default class Login extends Vue {
  valid = true
  name = ''
  nameRules = [
    (v: any) => !!v || 'Name is required',
    (v: string | any[]) =>
      (v && v.length <= 10) || 'Name must be less than 10 characters',
  ]

  password = ''
  passwordRules = [
    (v: any) => !!v || 'Password is required',
    (v: string) =>
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[\w\W]{6,}$/.test(v) ||
      'Password must be at least 6 characters with uppercase, lowercase, and number',
  ]

  showPassword = false

  loading = false

  get userToken(): string {
    return this.$store.getters['user-token/userToken']
  }

  get form(): Vue & { validate: () => boolean } {
    return this.$refs.form as Vue & { validate: () => boolean }
  }

  async login() {
    if (this.form.validate()) {
      this.loading = true
      const res = await this.$authService
        .login(this.name, this.password)
        .finally(() => (this.loading = false))
      if (res) {
        this.$store.dispatch('message/success', 'Login success!')
        this.$router.push('/')
      } else {
        this.$store.dispatch('message/error', 'Login fail!')
      }
    }
  }
}
</script>

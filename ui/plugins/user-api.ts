import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import type { NuxtAxiosInstance } from '@nuxtjs/axios'

export default function ({ $axios }: Context, inject: Inject) {
  // Create a custom axios instance
  const api = $axios.create()
  inject('userApi', api)
}

declare module '@nuxt/types' {
  interface Context {
    $userApi: NuxtAxiosInstance
  }
}

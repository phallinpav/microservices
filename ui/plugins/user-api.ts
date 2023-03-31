import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import type { NuxtAxiosInstance } from '@nuxtjs/axios'

export default function ({ $axios }: Context, inject: Inject) {
  // Create a custom axios instance
  const api = $axios.create()

  // api.setToken('cHVibGljOnB1YmxpY1Bhc3N3b3Jk', 'Basic', ['post'])

  inject('userApi', api)
}

declare module '@nuxt/types' {
  interface Context {
    $userApi: NuxtAxiosInstance
  }
}

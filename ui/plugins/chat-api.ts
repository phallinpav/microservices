import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import type { NuxtAxiosInstance } from '@nuxtjs/axios'

export default function ({ $axios }: Context, inject: Inject) {
  // Create a custom axios instance
  const chatApi = $axios.create({
    baseURL: 'http://localhost:5555',
  })

  inject('chatApi', chatApi)
}

declare module '@nuxt/types' {
  interface Context {
    $chatApi: NuxtAxiosInstance
  }
}

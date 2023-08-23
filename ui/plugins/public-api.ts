import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import type { NuxtAxiosInstance } from '@nuxtjs/axios'

export default function ({ $axios }: Context, inject: Inject) {
  const api = $axios.create()
  inject('publicApi', api)
}

declare module '@nuxt/types' {
  interface Context {
    $publicApi: NuxtAxiosInstance
  }
}

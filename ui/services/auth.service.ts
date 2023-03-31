import type { NuxtAxiosInstance } from '@nuxtjs/axios'
import { NuxtCookies } from 'cookie-universal-nuxt'
import { Store } from 'vuex/types/index'
import VueRouter from 'vue-router'
import { TokenModel } from '~/utils/type/token'

export interface AuthService {
  login(username: string, password: string): Promise<boolean>
  logout(): void
}

export class AuthServiceImpl implements AuthService {
  readonly api: NuxtAxiosInstance
  readonly cookies: NuxtCookies
  readonly store: Store<any>
  readonly router: VueRouter

  constructor(
    userApi: NuxtAxiosInstance,
    cookies: NuxtCookies,
    store: Store<any>,
    router: VueRouter
  ) {
    this.api = userApi
    this.cookies = cookies
    this.store = store
    this.router = router
  }

  async login(username: string, password: string) {
    return await this.api
      .$get('/token/generate', {
        auth: {
          username,
          password,
        },
      })
      .then((value: TokenModel) => {
        this.store.dispatch('user-token/create', value)
        this.cookies.set('user-token', value, { maxAge: 30 * 60 })
        return true
      })
      .catch(() => {
        return false
      })
  }

  logout() {
    this.store.dispatch('user-token/remove')
    this.cookies.remove('user-token')
    this.router.push('/login')
  }
}

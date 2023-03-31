import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import { AccountService, AccountServiceImpl } from '~/services/account.service'
import { AuthService, AuthServiceImpl } from '~/services/auth.service'

export default function (context: Context, inject: Inject) {
  const accountService = new AccountServiceImpl(context.$publicApi)
  const authService = new AuthServiceImpl(
    context.$userApi,
    context.app.$cookies,
    context.store,
    context.app.router!
  )

  inject('accountService', accountService)
  inject('authService', authService)
}

declare module 'vue/types/vue' {
  interface Vue {
    $accountService: AccountService
    $authService: AuthService
  }
}

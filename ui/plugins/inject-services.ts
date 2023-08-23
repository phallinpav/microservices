import { Context } from '@nuxt/types'
import { Inject } from '@nuxt/types/app'
import { AccountService, AccountServiceImpl } from '~/services/account.service'
import { AuthService, AuthServiceImpl } from '~/services/auth.service'
import { ChatService, ChatServiceImpl } from '~/services/chat.service'
import { FriendsService, FriendsServiceImpl } from '~/services/friends.service'

export default function (context: Context, inject: Inject) {
  const userToken = context.$cookies.get('user-token')

  if (userToken) {
    context.store.dispatch('user-token/create', userToken)
    context.$userApi.setHeader('x-api-key', userToken.token)
    context.$chatApi.setHeader('x-api-key', userToken.token)
  }

  const accountService = new AccountServiceImpl(
    context.$publicApi,
    context.$userApi
  )
  const authService = new AuthServiceImpl(
    context.$userApi,
    context.app.$cookies,
    context.store,
    context.app.router!
  )

  const chatService = new ChatServiceImpl(context.$chatApi)
  const friendsService = new FriendsServiceImpl(context.$userApi)

  inject('accountService', accountService)
  inject('authService', authService)
  inject('chatService', chatService)
  inject('friendsService', friendsService)
}

declare module 'vue/types/vue' {
  interface Vue {
    $chatService: ChatService
    $accountService: AccountService
    $authService: AuthService
    $friendsService: FriendsService
  }
}

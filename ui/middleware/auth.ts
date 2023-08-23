import { Context } from '@nuxt/types'
import { Account } from '~/utils/type/account'

export default async function ({
  app,
  store,
  $userApi,
  $chatApi,
  redirect,
}: Context) {
  const userToken = app.$cookies.get('user-token')

  if (userToken) {
    store.dispatch('user-token/create', userToken)
    $userApi.setHeader('x-api-key', userToken.token)
    $chatApi.setHeader('x-api-key', userToken.token)

    await app.$accountService
      .getAccount()
      .then((acc: Account) => {
        store.dispatch('acc-info/create', acc)
      })
      .catch(() => {
        redirect('/login')
      })
  } else {
    redirect('/login')
  }
}

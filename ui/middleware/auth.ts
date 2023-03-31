import { Context } from '@nuxt/types'

export default function ({ app, store, redirect }: Context) {
  const userToken = app.$cookies.get('user-token')

  if (userToken) {
    store.dispatch('user-token/create', userToken)
  } else {
    redirect('/login')
  }
}

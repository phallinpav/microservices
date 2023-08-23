import { Account } from '~/utils/type/account'

export default {
  create(state: Account, acc: Account) {
    state.id = acc.id
    state.username = acc.username
    state.email = acc.email
    state.profileImgUrl = acc.profileImgUrl
  },
  remove(state: Account) {
    state.id = ''
    state.username = ''
    state.email = ''
    state.profileImgUrl = ''
  },
}

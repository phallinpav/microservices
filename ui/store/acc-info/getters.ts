import { Account } from '~/utils/type/account'

export default {
  all(state: Account) {
    return state
  },
  id(state: Account) {
    return state.id
  },
  username(state: Account) {
    return state.username
  },
  email(state: Account) {
    return state.email
  },
  profileImgUrl(state: Account) {
    return state.profileImgUrl
  },
}

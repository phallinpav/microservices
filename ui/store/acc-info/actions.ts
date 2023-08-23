import { Store } from 'vuex'
import { Account } from '~/utils/type/account'

export default {
  create({ commit }: Store<any>, account: Account) {
    commit('create', account)
  },

  remove({ commit }: Store<any>) {
    commit('remove')
  },
}

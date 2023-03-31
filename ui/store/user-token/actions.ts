import { Store } from 'vuex'
import { TokenModel } from '~/utils/type/token'

export default {
  create({ commit }: Store<any>, token: TokenModel) {
    commit('create', token)
  },

  remove({ commit }: Store<any>) {
    commit('remove')
  },
}

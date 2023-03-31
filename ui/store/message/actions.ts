import { Store } from 'vuex'

export default {
  success({ commit }: Store<any>, text: string) {
    commit('set', { show: true, type: 'success', text })
  },

  info({ commit }: Store<any>, text: string) {
    commit('set', { show: true, type: 'info', text })
  },

  error({ commit }: Store<any>, text: string) {
    commit('set', { show: true, type: 'error', text })
  },

  hide({ commit }: Store<any>) {
    commit('set', { show: false, text: '' })
  },
}

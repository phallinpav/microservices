import { Store } from 'vuex'

export default {
  success({ commit }: Store<any>, form: MessageForm) {
    commit('set', {
      show: true,
      type: 'success',
      text: form.text,
      timeout: form.timeout,
    })
  },

  info({ commit }: Store<any>, form: MessageForm) {
    commit('set', {
      show: true,
      type: 'info',
      text: form.text,
      timeout: form.timeout,
    })
  },

  error({ commit }: Store<any>, form: MessageForm) {
    commit('set', {
      show: true,
      type: 'error',
      text: form.text,
      timeout: form.timeout,
    })
  },

  hide({ commit }: Store<any>) {
    commit('set', { show: false, text: '' })
  },
}

export class MessageForm {
  text: string
  timeout: number = 2000

  constructor(text: string, timeout?: number) {
    this.text = text
    if (timeout) this.timeout = timeout
  }
}

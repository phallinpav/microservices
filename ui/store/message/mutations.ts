export default {
  set(state: any, { show, type, text, timeout }: any) {
    state.show = show
    state.type = type
    state.text = text
    state.timeout = timeout
  },
}

import { TokenModel } from '~/utils/type/token'

export default {
  create(state: TokenModel, token: TokenModel) {
    state.token = token.token
    state.refreshToken = token.refreshToken
  },
  remove(state: TokenModel) {
    state.token = ''
    state.refreshToken = ''
  },
}

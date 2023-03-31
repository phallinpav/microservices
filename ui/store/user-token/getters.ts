import { TokenModel } from '~/utils/type/token'

export default {
  all(state: TokenModel) {
    return state
  },
  token(state: TokenModel) {
    return state.token
  },
  refreshToken(state: TokenModel) {
    return state.refreshToken
  },
}

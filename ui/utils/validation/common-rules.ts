import { basicPassword, fieldRules, Rules, RuleType } from './rule'

export const usernameRules = fieldRules('Name', [
  RuleType.REQUIRED,
  new Rules(RuleType.BETWEEN_LENGTH, [3, 30]),
])
export const emailRules = fieldRules('Email', [
  RuleType.REQUIRED,
  RuleType.EMAIL,
  new Rules(RuleType.BETWEEN_LENGTH, [3, 125]),
])
export const passwordRules = fieldRules('Password', [
  RuleType.REQUIRED,
  RuleType.PASSWORD_BASIC,
])
export const passwordHint = basicPassword('Password')('')

export const confirmPasswordRules = (password: string) =>
  fieldRules('Confirm Password', [
    RuleType.REQUIRED,
    RuleType.PASSWORD_BASIC,
    new Rules(RuleType.CONFIRM_PASSWORD, ['Password', password]),
  ])

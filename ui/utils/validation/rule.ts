export enum RuleType {
  REQUIRED,
  MIN_LENGTH,
  MAX_LENGTH,
  BETWEEN_LENGTH,
  EMAIL,
  PASSWORD_BASIC,
  PASSWORD,
  CONFIRM_PASSWORD,
}

export class Rules {
  type: RuleType
  args?: any[]

  constructor(type: RuleType, args: any[]) {
    this.type = type
    this.args = args
  }
}

export function fieldRules(fieldName: string, rules: (Rules | RuleType)[]) {
  const fieldRules: Function[] = []
  let min = 3
  let max = 30
  rules.forEach((rule) => {
    switch (rule instanceof Rules ? rule.type : rule) {
      case RuleType.REQUIRED:
        fieldRules.push(required(fieldName))
        break
      case RuleType.MIN_LENGTH:
        if (rule instanceof Rules && rule.args && rule.args[0]) {
          min = rule.args[0]
        }
        fieldRules.push(minLength(fieldName, min))
        break
      case RuleType.MAX_LENGTH:
        if (rule instanceof Rules && rule.args && rule.args[0]) {
          max = rule.args[0]
        }
        fieldRules.push(maxLength(fieldName, max))
        break
      case RuleType.BETWEEN_LENGTH:
        if (rule instanceof Rules && rule.args && rule.args[0]) {
          min = rule.args[0]
        }
        if (rule instanceof Rules && rule.args && rule.args[1]) {
          max = rule.args[1]
        }
        fieldRules.push(betweenLength(fieldName, min, max))
        break
      case RuleType.EMAIL:
        fieldRules.push(email(fieldName))
        break
      case RuleType.PASSWORD_BASIC:
        fieldRules.push(basicPassword(fieldName))
        break
      case RuleType.PASSWORD:
        if (rule instanceof Rules && rule.args && rule.args.length > 0) {
          fieldRules.push(
            password(
              fieldName,
              rule.args[0],
              rule.args[1],
              rule.args[2],
              rule.args[3],
              rule.args[4]
            )
          )
        } else {
          fieldRules.push(basicPassword(fieldName))
        }
        break
      case RuleType.CONFIRM_PASSWORD:
        if (rule instanceof Rules && rule.args && rule.args.length > 0) {
          fieldRules.push(confirmEqual(fieldName, rule.args[0], rule.args[1]))
        } else {
          // need to throw error
        }
        break
      default:
        break
    }
  })
  return fieldRules
}

export function required(fieldName: string) {
  return (value: any) => !!value || `${fieldName} is required`
}

export function minLength(fieldName: string, min: number) {
  return (value: any) =>
    value.length >= min ||
    `${fieldName} must be more than or equal ${min} characters`
}

export function maxLength(fieldName: string, max: number) {
  return (value: any) =>
    value.length <= max ||
    `${fieldName} must be less than or equal ${max} characters`
}

export function betweenLength(fieldName: string, min: number, max: number) {
  return (value: any) =>
    (value.length >= min && value.length <= max) ||
    `${fieldName} must be between ${min} and ${max} characters`
}

export function email(fieldName: string) {
  return (value: any) => /.+@.+\..+/.test(value) || `${fieldName} must be valid`
}

export function confirmEqual(
  fieldName: string,
  fieldName2: string,
  value2: string
) {
  return (value: any) =>
    value === value2 || `${fieldName} must be same as ${fieldName2}`
}

export function basicPassword(fieldName: string) {
  return password(fieldName, 6, 30, true, true, true)
}

export function password(
  fieldName: string,
  min?: number | null,
  max?: number | null,
  hasUpper?: boolean,
  hasLower?: boolean,
  hasNumber?: boolean
) {
  let regex = '[\\w\\W]'
  let message = `${fieldName} must have `
  if (hasUpper) {
    regex = '(?=.*[A-Z])' + regex
    message += 'uppercase, '
  }
  if (hasLower) {
    regex = '(?=.*[a-z])' + regex
    message += 'lowercase, '
  }
  if (hasNumber) {
    regex = '(?=.*\\d)' + regex
    message += 'number, '
  }
  if (min && max) {
    message += `characters between ${min} and ${max}, `
    regex += `{${min},${max}}`
  } else if (min) {
    message += `characters more than or equal ${min}, `
    regex += `{${min},}`
  } else if (max) {
    message += `characters less than or equal ${max}, `
    regex += `{,${max}}`
  }
  message = message.replace(/(,.)(?!.*\1)/, '.')
  message = message.replace(/(,.)(?!.*\1)/, ', and ')
  regex = '^' + regex + '$'

  return (value: any) => new RegExp(regex).test(value) || message
}

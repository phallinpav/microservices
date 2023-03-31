import type { NuxtAxiosInstance } from '@nuxtjs/axios'
import { Account } from '~/utils/type/account'

export interface AccountService {
  createAccount(account: Account): Promise<any>
}

export class AccountServiceImpl implements AccountService {
  readonly api: NuxtAxiosInstance

  constructor(publicApi: NuxtAxiosInstance) {
    this.api = publicApi
  }

  async createAccount(account: Account) {
    return await this.api.$post('/sys/accounts', account)
  }
}

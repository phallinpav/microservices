import type { NuxtAxiosInstance } from '@nuxtjs/axios'
import { Account } from '~/utils/type/account'

export interface AccountService {
  createAccount(account: Account): Promise<any>
  getAccount(): Promise<any>
  updateProfileImg(image: any): Promise<any>
  searchAccountByUsername(username: string): Promise<any>
  searchAccounts(): Promise<any>
}

export class AccountServiceImpl implements AccountService {
  readonly publicApi: NuxtAxiosInstance
  readonly userApi: NuxtAxiosInstance

  constructor(publicApi: NuxtAxiosInstance, userApi: NuxtAxiosInstance) {
    this.publicApi = publicApi
    this.userApi = userApi
  }

  async createAccount(account: Account) {
    return await this.publicApi.$post('/account', account)
  }

  async getAccount() {
    return await this.userApi.$get('/account')
  }

  async updateProfileImg(image: any) {
    const formData = new FormData()
    formData.append('image', image)
    return await this.userApi.$post('/account/profileImg', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  }

  async searchAccounts() {
    return await this.userApi.$get('/accounts/search')
  }

  async searchAccountByUsername(username: string) {
    const url = `/accounts/search?username=${encodeURIComponent(username)}`
    return await this.userApi.$get(url)
  }
}

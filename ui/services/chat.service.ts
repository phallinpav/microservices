import type { NuxtAxiosInstance } from '@nuxtjs/axios'

export interface ChatService {
  getChatWith(accountId: number, limit?: number): Promise<any>
  sendChatTo(accountId: number, text: string): Promise<any>
}

export class ChatServiceImpl implements ChatService {
  readonly chatApi: NuxtAxiosInstance

  constructor(chatApi: NuxtAxiosInstance) {
    this.chatApi = chatApi
  }

  async getChatWith(accountId: number, limit?: number) {
    let url = '/chat/' + accountId
    if (limit) {
      url += `?limit=${limit}`
    }
    return await this.chatApi.$get(url)
  }

  async sendChatTo(accountId: number, text: string) {
    return await this.chatApi.$post('/chat/' + accountId, { message: text })
  }
}

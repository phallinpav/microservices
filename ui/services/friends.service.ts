import type { NuxtAxiosInstance } from '@nuxtjs/axios'

export interface FriendsService {
  getFriends(): Promise<any>
  getFriendRequest(fromMe: boolean): Promise<any>
  acceptFriend(id: number): Promise<any>
  denyFriend(id: number): Promise<any>
  addFriend(id: number): Promise<any>
  removeFriend(id: number): Promise<any>
}

export class FriendsServiceImpl implements FriendsService {
  readonly userApi: NuxtAxiosInstance

  constructor(userApi: NuxtAxiosInstance) {
    this.userApi = userApi
  }

  async getFriends() {
    return await this.userApi.$get('/friends')
  }

  async getFriendRequest(fromMe?: boolean) {
    let url = '/friends/requests'
    if (fromMe) {
      url += `?fromMe=${fromMe}`
    }
    return await this.userApi.$get(url)
  }

  async acceptFriend(id: number) {
    return await this.userApi.$patch('/friends/request/' + id, {
      status: 'accepted',
    })
  }

  async denyFriend(id: number) {
    return await this.userApi.$patch('/friends/request/' + id, {
      status: 'denied',
    })
  }

  async removeFriend(id: number): Promise<any> {
    return await this.userApi.$patch(`/friends/request/` + id, {
      status: 'removed',
    })
  }

  async addFriend(id: number): Promise<any> {
    return await this.userApi.$post('/friends/add/' + id)
  }
}

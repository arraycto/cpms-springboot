import request from '@/utils/request'
import Qs from 'qs'
export function login(data) {
  return request({
    url: '/login/ulogin',
    method: 'post',
    data:Qs.stringify(data)  
  })
}

export function getInfo() {
  return request({
    url: '/user/info',
    method: 'get',
    params: ''
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}


export function getAllUser(params) {
    return request({
      url: '/user/allUser',
      method: 'get',
      params: params
    })
}

export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data:Qs.stringify(data)
  })
}

export function editUser(data) {
    return request({
      url: '/user/edit',
      method: 'post',
      data:Qs.stringify(data)
    })
}

export function deleteUser(params) {
    return request({
      url: '/user/delete',
      method: 'get',
      params
    })
}
import request from '@/utils/request'
import Qs from 'qs'
export function getAllRole(params) {
  return request({
    url: '/role/allRole',
    method: 'get',
    params: params
  })
}

export function addRole(data) {
    return request({
      url: '/role/add',
      method: 'post',
      data:Qs.stringify(data)
    })
}

export function deleteRole(params) {
    return request({
      url: '/role/delete',
      method: 'get',
      params
    })
}

export function editRole(data) {
    return request({
      url: '/role/edit',
      method: 'post',
      data:Qs.stringify(data)
    })
}
import request from '@/utils/request'
import Qs from 'qs'
export function upload(data) {
  return request({
    url: '/test/upload',
    method: 'post',
    data:data  
  })
}

export function chunkUpload(data) {
  return request({
    url: '/test/chunkUpload',
    method: 'post',
    data:data  
  })
}
export function mergeChunk(data) {
  return request({
    url: '/test/mergeChunk',
    method: 'post',
    data:data  
  })
}
export function exportExcel() {
  return request({
    url: '/test/exportExcel',
    method: 'get',
    responseType: 'blob',  // 获取二进制数据
    params: ''
  })
}

export function importExcel(data) {
  return request({
    url: '/test/importExcel',
    method: 'post',
    data:data  
  })
}
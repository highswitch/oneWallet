import request from '@/utils/request'

// 查询fifox监听列表
export function listMonitor(query) {
  return request({
    url: '/system/monitor/list',
    method: 'get',
    params: query
  })
}

// 查询fifox监听详细
export function getMonitor(id) {
  return request({
    url: '/system/monitor/' + id,
    method: 'get'
  })
}

// 新增fifox监听
export function addMonitor(data) {
  return request({
    url: '/system/monitor',
    method: 'post',
    data: data
  })
}

// 修改fifox监听
export function updateMonitor(data) {
  return request({
    url: '/system/monitor',
    method: 'put',
    data: data
  })
}

// 删除fifox监听
export function delMonitor(id) {
  return request({
    url: '/system/monitor/' + id,
    method: 'delete'
  })
}

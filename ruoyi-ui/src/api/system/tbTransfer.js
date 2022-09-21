import request from '@/utils/request'

// 查询转帐列表
export function listTbTransfer(query) {
  return request({
    url: '/system/tbTransfer/list',
    method: 'get',
    params: query
  })
}

// 查询转帐详细
export function getTbTransfer(id) {
  return request({
    url: '/system/tbTransfer/' + id,
    method: 'get'
  })
}

// 新增转帐
export function addTbTransfer(data) {
  return request({
    url: '/system/tbTransfer',
    method: 'post',
    data: data
  })
}

// 修改转帐
export function updateTbTransfer(data) {
  return request({
    url: '/system/tbTransfer',
    method: 'put',
    data: data
  })
}

// 删除转帐
export function delTbTransfer(id) {
  return request({
    url: '/system/tbTransfer/' + id,
    method: 'delete'
  })
}

// 导出转帐
export function exportTbTransfer(query) {
  return request({
    url: '/system/tbTransfer/export',
    method: 'get',
    params: query
  })
}
// 获取指定帐号的转帐信息
export function listByAccount(query) {
  return request({
    url: '/system/tbTransfer/listByAccount',
    method: 'get',
    params: query
  })
}

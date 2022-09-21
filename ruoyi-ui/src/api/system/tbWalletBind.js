import request from '@/utils/request'

// 查询用户钱包绑定列表
export function listTbWalletBind(query) {
  return request({
    url: '/system/tbWalletBind/list',
    method: 'get',
    params: query
  })
}

// 查询用户钱包绑定详细
export function getTbWalletBind(id) {
  return request({
    url: '/system/tbWalletBind/' + id,
    method: 'get'
  })
}

// 新增用户钱包绑定
export function addTbWalletBind(data) {
  return request({
    url: '/system/tbWalletBind',
    method: 'post',
    data: data
  })
}

// 修改用户钱包绑定
export function updateTbWalletBind(data) {
  return request({
    url: '/system/tbWalletBind',
    method: 'put',
    data: data
  })
}

// 删除用户钱包绑定
export function delTbWalletBind(id) {
  return request({
    url: '/system/tbWalletBind/' + id,
    method: 'delete'
  })
}

// 导出用户钱包绑定
export function exportTbWalletBind(query) {
  return request({
    url: '/system/tbWalletBind/export',
    method: 'get',
    params: query
  })
}
// 获取用户绑定的钱包信息
export function getWalletBindInfo() {
  return request({
    url: '/system/tbWalletBind/getWalletBindInfo',
    method: 'get'
  })
}
// 获取用户绑定的钱包信息
export function switchWalletAccount(walletId) {
  return request({
    url: '/system/tbWalletBind/switchWalletAccount',
    method: 'get',
    params: {
      walletId: walletId
    }
  })
}

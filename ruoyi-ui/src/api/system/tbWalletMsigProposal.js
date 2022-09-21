import request from '@/utils/request'

// 查询多签提案处理列表
export function listTbWalletMsigProposal(query) {
  return request({
    url: '/system/tbWalletMsigProposal/list',
    method: 'get',
    params: query
  })
}

// 查询多签提案处理详细
export function getTbWalletMsigProposal(id) {
  return request({
    url: '/system/tbWalletMsigProposal/' + id,
    method: 'get'
  })
}

// 新增多签提案处理
export function addTbWalletMsigProposal(data) {
  return request({
    url: '/system/tbWalletMsigProposal',
    method: 'post',
    data: data
  })
}

// 修改多签提案处理
export function updateTbWalletMsigProposal(data) {
  return request({
    url: '/system/tbWalletMsigProposal',
    method: 'put',
    data: data
  })
}

// 删除多签提案处理
export function delTbWalletMsigProposal(id) {
  return request({
    url: '/system/tbWalletMsigProposal/' + id,
    method: 'delete'
  })
}

// 导出多签提案处理
export function exportTbWalletMsigProposal(query) {
  return request({
    url: '/system/tbWalletMsigProposal/export',
    method: 'get',
    params: query
  })
}
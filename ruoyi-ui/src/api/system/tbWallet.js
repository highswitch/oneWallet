import request from '@/utils/request'

// 查询钱包列表
export function listTbWallet(query) {
  return request({
    url: '/system/tbWallet/list',
    method: 'get',
    params: query
  })
}

// 查询钱包详细
export function getTbWallet(id) {
  return request({
    url: '/system/tbWallet/' + id,
    method: 'get'
  })
}

// 新增钱包
export function addTbWallet(data) {
  return request({
    url: '/system/tbWallet',
    method: 'post',
    data: data
  })
}

// 修改钱包
export function updateTbWallet(data) {
  return request({
    url: '/system/tbWallet',
    method: 'put',
    data: data
  })
}

// 删除钱包
export function delTbWallet(id) {
  return request({
    url: '/system/tbWallet/' + id,
    method: 'delete'
  })
}

// 导出钱包
export function exportTbWallet(query) {
  return request({
    url: '/system/tbWallet/export',
    method: 'get',
    params: query
  })
}
// 生成助记词
export function genMnemonic() {
  return request({
    url: '/system/tbWallet/genMnemonic',
    method: 'get'
  })
}
//钱包帐号信息
export function info(id){
  return request({
    url: '/system/tbWallet/info',
    method: 'get',
    params: {
      walletId: id
    }
  })
}
//导入帐号
export function importAccount(data){
  return request({
    url: '/system/tbWallet/importAccount',
    method: 'post',
    data: data
  })
}
//获取用户的帐号列表
export function getWalletListByUser(){
  return request({
    url: '/system/tbWallet/getListByUser',
    method: 'get'
  })
}
//获取绑定的钱包帐号信息
export function getBindWalletInfo(){
  return request({
    url: '/system/tbWallet/getBindWalletInfo',
    method: 'get'
  })
}
export function showAccount(account){
  let le = account.length;
  return account.substring(0, 6) + "..." + account.substring(le -6, le);
}

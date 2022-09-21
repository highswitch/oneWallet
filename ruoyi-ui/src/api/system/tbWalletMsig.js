import request from '@/utils/request'
import {getExecuteRet} from "@/api/system/LinuxCmd";

// 查询多签钱包列表
export function listTbWalletMsig(query) {
  return request({
    url: '/system/tbWalletMsig/list',
    method: 'get',
    params: query
  })
}

// 查询多签钱包详细
export function getTbWalletMsigDetail(id, walletId) {
  return request({
    url: '/system/tbWalletMsig/getTbWalletMsigDetail',
    method: 'get',
    params: {
      id: id,
      walletId: walletId
    }
  })
}


// 查询多签钱包详细
export function getTbWalletMsig(id) {
  return request({
    url: '/system/tbWalletMsig/' + id,
    method: 'get'
  })
}

// 新增多签钱包
export function addTbWalletMsig(data) {
  return request({
    url: '/system/tbWalletMsig',
    method: 'post',
    data: data
  })
}

// 修改多签钱包
export function updateTbWalletMsig(data) {
  return request({
    url: '/system/tbWalletMsig',
    method: 'put',
    data: data
  })
}

// 删除多签钱包
export function delTbWalletMsig(id) {
  return request({
    url: '/system/tbWalletMsig/' + id,
    method: 'delete'
  })
}

// 导出多签钱包
export function exportTbWalletMsig(query) {
  return request({
    url: '/system/tbWalletMsig/export',
    method: 'get',
    params: query
  })
}

// 移除签署人
export function removeSigner(id, account, walletId) {
  return request({
    url: '/system/tbWalletMsig/removeSigner',
    method: 'get',
    params: {
      id: id,
      account: account,
      walletId: walletId
    }
  })
}
// 关闭提案
export function cancelTask(taskForm) {
  return request({
    url: '/system/tbWalletMsig/cancelTask',
    method: 'get',
    params: taskForm
  })
}
// 允许提案
export function approveTask(taskForm) {
  return request({
    url: '/system/tbWalletMsig/approveTask',
    method: 'get',
    params: taskForm
  })
}
// 移除签署人
export function editThreshold(form) {
  return request({
    url: '/system/tbWalletMsig/editThreshold',
    method: 'get',
    params: form
  })
}
// 移除签署人
export function addSigners(form) {
  return request({
    url: '/system/tbWalletMsig/addSigners',
    method: 'get',
    params: form
  })
}
// 移除签署人
export function editSigners(form) {
  return request({
    url: '/system/tbWalletMsig/editSigners',
    method: 'get',
    params: form
  })
}

export function cmdTimeInterVal(uuid, that, sucessFun, failureFun){
  const index = setInterval(function () {
    getExecuteRet(uuid).then(executeRet => {
      const msg = executeRet.msg;
      if (executeRet.code == 200){
        if (msg == "ok"){
          clearInterval(index);
          sucessFun();
          that.loadText = "加载中";
        } else {
          that.loadText = msg;
        }
      } else {
        failureFun(msg);
      }
    }).catch(executeError => {
      failureFun(executeError)
      clearInterval(index);
    });
  }, 2000);
}

import request from "@/utils/request";

/**
 * 获取命令执行结果
 * @param data
 * @returns {AxiosPromise}
 */
export function getExecuteRet(uuid) {
  return request({
    url: '/api/linuxCmd/getExecuteRet',
    method: 'get',
    headers:  {
      isToken: false
    },
    params: {
      uuid: uuid
    }
  })
}

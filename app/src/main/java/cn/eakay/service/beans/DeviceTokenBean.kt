package cn.eakay.service.beans

/**
 * @packageName: UserService
 * @fileName: DeviceTokenBean
 * @author: chitian
 * @date: 2019-07-16 09:09
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class DeviceTokenBean :BaseResponse() {
    private var datas: String? = null

    fun setDatas(datas: String) {
        this.datas = datas
    }

    fun getDatas(): String? {
        return this.datas
    }
}
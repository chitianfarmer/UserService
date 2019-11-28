package cn.eakay.service.beans.response

import cn.eakay.service.beans.base.BaseResponse

/**
 * @packageName: UserService
 * @fileName: WorkBean
 * @author: chitian
 * @date: 2019-07-18 16:45
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class WorkBean : BaseResponse() {
    /**
     * datas : {}
     */

    private var datas: DatasBean? = null

    fun getDatas(): DatasBean? {
        return datas
    }

    fun setDatas(datas: DatasBean) {
        this.datas = datas
    }

    class DatasBean {

    }
}
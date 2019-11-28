package cn.eakay.service.beans.messages

import com.baidu.location.BDLocation

/**
 * @packageName: UserService
 * @fileName: LocationMessage
 * @author: chitian
 * @date: 2019-11-28 09:25
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
data class LocationMessage(var code:Int,var bdLocation: BDLocation?)
package cn.eakay.service.beans.messages

import cn.eakay.service.beans.response.LocationAddressBean

/**
 * @packageName: UserService
 * @fileName: AddressMessage
 * @author: chitian
 * @date: 2019-11-26 17:24
 * @description: 地址的消息
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
data class AddressMessage(var code:Int,var bean: LocationAddressBean)
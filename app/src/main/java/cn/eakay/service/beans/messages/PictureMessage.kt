package cn.eakay.service.beans.messages

/**
 * @packageName: UserService
 * @fileName: PictureMessage
 * @author: chitian
 * @date: 2019-07-19 08:55
 * @description: 图片消息
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class PictureMessage {
    private var picType: Int = -1
    private var localPath: String? = null
    private var remotePath: String? = null
    private var picStatus: String? = null

    constructor(picType: Int, localPath: String?, remotePath: String?, picStatus: String?) {
        this.picType = picType
        this.localPath = localPath
        this.localPath = remotePath
        this.picStatus = picStatus
    }

    fun getPicType(): Int {
        return picType
    }

    fun setPicType(picType: Int) {
        this.picType = picType
    }

    fun getLocalPath(): String? {
        return localPath
    }

    fun setLocalPath(localPath: String?) {
        this.localPath = localPath
    }

    fun getRemotePath(): String? {
        return remotePath
    }

    fun setRemotePath(remotePath: String?) {
        this.remotePath = remotePath
    }

    fun getPicStatus(): String? {
        return picStatus
    }

    fun setPicStatus(picStatus: String?) {
        this.picStatus = picStatus
    }
}
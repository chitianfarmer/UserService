package cn.eakay.service.beans

import java.io.Serializable

/**
 * @packageName: UserService
 * @fileName: PictureOrderMessage
 * @author: chitian
 * @date: 2019-07-19 08:49
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class PictureOrderMessage : Serializable {
    private var picType: Int = -1
    private var localPath: String? = null
    private var remotePath: String? = null

    constructor(picType: Int, localPath: String?, remotePath: String?) {
        this.picType = picType
        this.localPath = localPath
        this.localPath = remotePath
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
}
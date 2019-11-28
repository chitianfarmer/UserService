package cn.eakay.service.photos.assistcamera.exif

import java.io.InputStream
import java.nio.ByteBuffer
import kotlin.experimental.and

/**
 * @packageName: UserService
 * @fileName: ByteBufferInputStream
 * @author: chitian
 * @date: 2019-11-28 10:10
 * @description: 字节流输入
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class ByteBufferInputStream : InputStream() {
    private var mBuf: ByteBuffer? = null

    fun ByteBufferInputStream(buf: ByteBuffer?) {
        mBuf = buf
    }

    override fun read(): Int {
        return if (!mBuf!!.hasRemaining()) {
            -1
        } else (mBuf!!.get() and 0xFF.toByte()).toInt()
    }

    override fun read(bytes: ByteArray?, off: Int, len: Int): Int {
        var len = len
        if (!mBuf!!.hasRemaining()) {
            return -1
        }
        len = Math.min(len, mBuf!!.remaining())
        mBuf!![bytes, off, len]
        return len
    }
}
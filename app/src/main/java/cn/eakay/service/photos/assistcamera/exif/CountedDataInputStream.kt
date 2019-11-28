package cn.eakay.service.photos.assistcamera.exif

import okhttp3.internal.and
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

/**
 * @packageName: UserService
 * @fileName: FilterInputStream
 * @author: chitian
 * @date: 2019-11-28 10:19
 * @description: 数据输入流
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class CountedDataInputStream : FilterInputStream{
    private var mCount = 0
    // allocate a byte buffer for a long value;
    private val mByteArray = ByteArray(8)
    private val mByteBuffer = ByteBuffer.wrap(mByteArray)
   constructor(stream: InputStream?):super(stream)
    fun getReadByteCount(): Int {
        return mCount
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray?): Int {
        val r: Int = `in`.read(b)
        mCount += if (r >= 0) r else 0
        return r
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray?, off: Int, len: Int): Int {
        val r: Int = `in`.read(b, off, len)
        mCount += if (r >= 0) r else 0
        return r
    }

    @Throws(IOException::class)
    override fun read(): Int {
        val r: Int = `in`.read()
        mCount += if (r >= 0) 1 else 0
        return r
    }

    @Throws(IOException::class)
    override fun skip(length: Long): Long {
        val skip: Long = `in`.skip(length)
        mCount += skip.toInt()
        return skip
    }

    @Throws(IOException::class)
    fun skipOrThrow(length: Long) {
        if (skip(length) != length) {
            throw EOFException()
        }
    }

    @Throws(IOException::class)
    fun skipTo(target: Long) {
        val cur = mCount.toLong()
        val diff = target - cur
        assert(diff >= 0)
        skipOrThrow(diff)
    }

    @Throws(IOException::class)
    fun readOrThrow(b: ByteArray?, off: Int, len: Int) {
        val r = read(b, off, len)
        if (r != len) {
            throw EOFException()
        }
    }

    @Throws(IOException::class)
    fun readOrThrow(b: ByteArray) {
        readOrThrow(b, 0, b.size)
    }

    fun setByteOrder(order: ByteOrder?) {
        mByteBuffer.order(order)
    }

    fun getByteOrder(): ByteOrder? {
        return mByteBuffer.order()
    }

    @Throws(IOException::class)
    fun readShort(): Short {
        readOrThrow(mByteArray, 0, 2)
        mByteBuffer.rewind()
        return mByteBuffer.short
    }

    @Throws(IOException::class)
    fun readUnsignedShort(): Int {
        return readShort() and 0xffff
    }

    @Throws(IOException::class)
    fun readInt(): Int {
        readOrThrow(mByteArray, 0, 4)
        mByteBuffer.rewind()
        return mByteBuffer.int
    }

    @Throws(IOException::class)
    fun readUnsignedInt(): Long {
        return (readInt() and 0xffffffffL.toInt()).toLong()
    }

    @Throws(IOException::class)
    fun readLong(): Long {
        readOrThrow(mByteArray, 0, 8)
        mByteBuffer.rewind()
        return mByteBuffer.long
    }

    @Throws(IOException::class)
    fun readString(n: Int): String? {
        val buf = ByteArray(n)
        readOrThrow(buf)
        return String(buf, Charset.forName("UTF8"))
    }

    @Throws(IOException::class)
    fun readString(n: Int, charset: Charset?): String? {
        val buf = ByteArray(n)
        readOrThrow(buf)
        return String(buf, charset!!)
    }

}
package cn.eakay.service.utils

import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * @packageName: UserService
 * @fileName: MD5Utils
 * @author: chitian
 * @date: 2019-11-26 11:08
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object MD5Utils {
    /**
     * md5值.
     *
     * @param paramStr 需要md5的字符串.
     * @return 32位大写md5值.
     */
    fun MD5(paramStr: String): String? { // 用于加密的字符
        val md5String = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
        )
        return try { // 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            val btInput =
                paramStr.toByteArray(Charset.forName("utf-8"))
            // 获得指定摘要算法的 MessageDigest对象，此处为MD5
            // MessageDigest类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
            // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            val mdInst = MessageDigest.getInstance("MD5")
            // MD5 Message Digest from SUN, <initialized>
            // MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput)
            // System.out.println(mdInst);
            // MD5 Message Digest from SUN, <in progress>
            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            val md = mdInst.digest()
            // System.out.println(md);
            // 把密文转换成十六进制的字符串形式
            val j = md.size
            // System.out.println(j);
            val str = CharArray(j * 2)
            var k = 0
            // i = 0
            for (i in 0 until j) { // 95
                val byte0 = md[i]
                // 5
                str[k++] = md5String[byte0.toInt().ushr(4) and 0xf]
                // F
                str[k++] = md5String[byte0.toInt() and 0xf]
            }
            // 返回经过加密后的字符串
            String(str)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun MD516(paramStr: String): String {
        var atString = MD5(paramStr)
        atString = atString!!.substring(8, 24)
        return atString.toUpperCase()
    }
}
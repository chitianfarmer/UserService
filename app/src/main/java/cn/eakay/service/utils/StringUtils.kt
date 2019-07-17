package cn.eakay.service.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.icu.text.UnicodeSet.CASE_INSENSITIVE
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import cn.eakay.service.R
import cn.eakay.service.base.Constants
import com.alibaba.fastjson.JSONObject
import com.changyoubao.vipthree.base.LSPUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern.compile

/**
 * @packageName: UserService
 * @fileName: StringUtils
 * @author: chitian
 * @date: 2019-07-11 10:58
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
object StringUtils {
    /**
     * is null or its length is 0 or it is made by space
     *
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
    </pre> *
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    fun isBlank(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.length == 0
    }

    /**
     * is null or its length is 0
     *
     *
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
    </pre> *
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    fun isEmpty(str: CharSequence?): Boolean {
        return str == null || str.length == 0
    }

    /**
     * get length of CharSequence
     *
     *
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
    </pre> *
     *
     * @param str
     * @return if str is null or empty, return 0, else return [CharSequence.length].
     */
    fun length(str: CharSequence?): Int {
        return str?.length ?: 0
    }

    /**
     * null Object to empty string
     *
     *
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
    </pre> *
     *
     * @param str
     * @return
     */
    fun nullStrToEmpty(str: Any?): String {
        return if (str == null)
            ""
        else
            str as? String ?: str.toString()
    }

    /**
     * capitalize first letter
     *
     *
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
    </pre> *
     *
     * @param str
     * @return
     */
    fun capitalizeFirstLetter(str: String): String? {
        if (isEmpty(str)) {
            return str
        }

        val c = str[0]
        return if (!Character.isLetter(c) || Character.isUpperCase(c))
            str
        else
            StringBuilder(str.length)
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString()
    }

    /**
     * encoded in utf-8
     *
     *
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
    </pre> *
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    fun utf8Encode(str: String): String? {
        if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                return URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(
                    "UnsupportedEncodingException occurred. ", e
                )
            }

        }
        return str
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    fun utf8Encode(str: String, defultReturn: String): String? {
        if (!isEmpty(str) && str.toByteArray().size != str.length) {
            try {
                return URLEncoder.encode(str, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                return defultReturn
            }

        }
        return str
    }

    /**
     * get innerHtml from href
     *
     *
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
    </pre> *
     *
     * @param href
     * @return
     *  * if href is null, return ""
     *  * if not match regx, return source
     *  * return the last string that match regx
     *
     */
    fun getHrefInnerHtml(href: String): String {
        if (isEmpty(href)) {
            return ""
        }

        val hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*"
        val hrefPattern = compile(
            hrefReg,
            CASE_INSENSITIVE
        )
        val hrefMatcher = hrefPattern.matcher(href)
        return if (hrefMatcher.matches()) {
            hrefMatcher.group(1)
        } else href
    }

    /**
     * process special char in html
     *
     *
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
    </pre> *
     *
     * @param source
     * @return
     */
    fun htmlEscapeCharsToString(source: String): String? {
        return if (StringUtils.isEmpty(source))
            source
        else
            source.replace("&lt;".toRegex(), "<").replace("&gt;".toRegex(), ">")
                .replace("&amp;".toRegex(), "&").replace("&quot;".toRegex(), "\"")
    }

    /**
     * transform half width char to full width char
     *
     *
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
    </pre> *
     *
     * @param s
     * @return
     */
    fun fullWidthToHalfWidth(s: String): String? {
        if (isEmpty(s)) {
            return s
        }

        val source = s.toCharArray()
        for (i in source.indices) {
            if (source[i].toInt() == 12288) {
                source[i] = ' '
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i].toInt() >= 65281 && source[i].toInt() <= 65374) {
                source[i] = (source[i].toInt() - 65248).toChar()
            } else {
                source[i] = source[i]
            }
        }
        return String(source)
    }

    /**
     * transform full width char to half width char
     *
     *
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
    </pre> *
     *
     * @param s
     * @return
     */
    fun halfWidthToFullWidth(s: String): String? {
        if (isEmpty(s)) {
            return s
        }

        val source = s.toCharArray()
        for (i in source.indices) {
            if (source[i] == ' ') {
                source[i] = 12288.toChar()
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i].toInt() >= 33 && source[i].toInt() <= 126) {
                source[i] = (source[i].toInt() + 65248).toChar()
            } else {
                source[i] = source[i]
            }
        }
        return String(source)
    }

    /**
     * 尽量用于文本展示，不要参与计算
     * 会保留两位小数
     *
     * @param input
     * @return 123-->￥123.00    123456.389-->￥123,456.39
     */
    fun formatRMBPrice(input: String): String {
        var result = ""
        try {
            val value = java.lang.Double.valueOf(input)
            val nf = NumberFormat.getCurrencyInstance(Locale.CHINESE)
            result = nf.format(value)
        } catch (nfe: Exception) {
        }

        return result
    }

    /**
     * 小数计算，小数点后四舍五入，保留2位
     * 注意此时price是整数的话，返回结果只有一位小数，比如 123456-->123456.0
     */
    fun decimalRoundHalfUp(price: Double): Double {
        val b = BigDecimal(price)
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 强制保留两位小数
     *
     * @param num
     * @return
     */
    fun forceTwoDecimals(num: Double): String {
        val df = DecimalFormat("#0.00")
        return df.format(num)
    }

    /**
     * 确保非空字符串
     *
     * @param str 原始字符串
     * @return 如果为空，就return ""
     */
    fun toNonEmptyString(str: String): String {
        return if (isEmpty(str)) {
            ""
        } else {
            str
        }
    }

    fun toNonEmptyString(str: String, defaultStr: String): String {
        return if (isEmpty(str)) {
            if (isEmpty(defaultStr)) "" else defaultStr
        } else {
            str
        }
    }

    /**
     * 验证密码格式, 密码必须由6至14位数字，字母或下划线组成
     *
     * @param pwd 密码，
     * @return 是否符合要求
     */
    fun matchRulePwd(pwd: String): Boolean {
        val p = compile("[0-9a-zA-Z_]{6,14}")
        val m = p.matcher(pwd)
        return m.matches()
    }

    /**
     * 验证身份证格式
     *
     * @param ID 身份证号
     * @return 匹配返回true
     */
    fun matchID(ID: String): Boolean {
        if (isEmpty(ID)) {
            return false
        }
        // 定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        val p = compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])")
        // 通过Pattern获得Matcher
        val m = p.matcher(ID)
        return m.matches()
    }

    /**
     * 验证电邮
     *
     * @param email 邮箱
     * @return 匹配返回true
     */
    fun matchEmail(email: String): Boolean {
        if (isEmpty(email)) {
            return false
        }
        val p =
            compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$")
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * 身份证 工具
     */
    object CheckIdCardUtils {
        /**
         * 中国公民身份证号码最小长度。
         */
        val CHINA_ID_MIN_LENGTH = 15

        /**
         * 中国公民身份证号码最大长度。
         */
        val CHINA_ID_MAX_LENGTH = 18

        /**
         * 每位加权因子
         */
        val power = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)

        /**
         * 数字验证
         *
         * @param val
         * @return 提取的数字。
         */
        fun isNum(`val`: String): Boolean {
            return !isEmpty(`val`) && `val`.matches("^[0-9]{1,}".toRegex())
        }

        /**
         * 将字符数组转换成数字数组
         *
         * @param ca 字符数组
         * @return 数字数组
         */
        fun converCharToInt(ca: CharArray): IntArray {
            val len = ca.size
            val iArr = IntArray(len)
            try {
                for (i in 0 until len) {
                    iArr[i] = Integer.parseInt(ca[i].toString())
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            return iArr
        }

        /**
         * 将power和值与11取模获得余数进行校验码判断
         *
         * @param iSum
         * @return 校验位
         */
        fun getCheckCode18(iSum: Int): String {
            var sCode = ""
            when (iSum % 11) {
                10 -> sCode = "2"
                9 -> sCode = "3"
                8 -> sCode = "4"
                7 -> sCode = "5"
                6 -> sCode = "6"
                5 -> sCode = "7"
                4 -> sCode = "8"
                3 -> sCode = "9"
                2 -> sCode = "x"
                1 -> sCode = "0"
                0 -> sCode = "1"
                else -> {
                }
            }
            return sCode
        }

        /**
         * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
         *
         * @param iArr
         * @return 身份证编码。
         */
        fun getPowerSum(iArr: IntArray): Int {
            var iSum = 0
            if (power.size == iArr.size) {
                for (i in iArr.indices) {
                    for (j in power.indices) {
                        if (i == j) {
                            iSum = iSum + iArr[i] * power[j]
                        }
                    }
                }
            }
            return iSum
        }

        /**
         * 将15位身份证号码转换为18位
         *
         * @param idCard 15位身份编码
         * @return 18位身份编码
         */
        @SuppressLint("SimpleDateFormat")
        fun conver15CardTo18(idCard: String): String {
            var idCard18 = ""
            if (idCard.length != CHINA_ID_MIN_LENGTH) {
                return ""
            }
            if (isNum(idCard)) {
                // 获取出生年月日
                val birthday = idCard.substring(6, 12)
                var birthDate: Date? = null
                try {
                    birthDate = SimpleDateFormat("yyMMdd").parse(birthday)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                val cal = Calendar.getInstance()
                if (birthDate != null) {
                    cal.time = birthDate
                }
                // 获取出生年(完全表现形式,如：2010)
                val sYear = cal.get(Calendar.YEAR).toString()
                idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8)
                // 转换字符数组
                val cArr = idCard18.toCharArray()
                if (cArr != null) {
                    val iCard = converCharToInt(cArr)
                    val iSum17 = getPowerSum(iCard)
                    // 获取校验位
                    val sVal = getCheckCode18(iSum17)
                    if (sVal.length > 0) {
                        idCard18 += sVal
                    } else {
                        return ""
                    }
                }
            } else {
                return ""
            }
            return idCard18
        }

        /**
         * 根据身份编号获取生日月
         *
         * @param idCard 身份编号
         * @return 生日(MM)
         */
        fun getMonthByIdCard(idCard: String): Short? {
            var idCard = idCard
            val len = idCard.length
            if (len < CHINA_ID_MIN_LENGTH) {
                return null
            } else if (len == CHINA_ID_MIN_LENGTH) {
                idCard = conver15CardTo18(idCard)
            }
            return java.lang.Short.valueOf(idCard.substring(10, 12))
        }

        /**
         * 根据身份编号获取生日天
         *
         * @param idCard 身份编号
         * @return 生日(dd)
         */
        fun getDateByIdCard(idCard: String): Short? {
            var idCard = idCard
            val len = idCard.length
            if (len < CHINA_ID_MIN_LENGTH) {
                return null
            } else if (len == CHINA_ID_MIN_LENGTH) {
                idCard = conver15CardTo18(idCard)
            }
            return java.lang.Short.valueOf(idCard.substring(12, 14))
        }

        /**
         * 根据身份证号，自动获取对应的星座
         *
         * @param idCard 身份证号码
         * @return 星座
         */
        fun getConstellationById(idCard: String): Int {
            if (isEmpty(idCard)) {
                return -1
            }
            if (!matchID(idCard)) {
                return -1
            }
            val month = getMonthByIdCard(idCard)!!.toInt()
            val day = getDateByIdCard(idCard)!!.toInt()
            var constellation = -1

            if (month == 1 && day >= 20 || month == 2 && day <= 18) {
                // 水瓶座
                constellation = 10
            } else if (month == 2 && day >= 19 || month == 3 && day <= 20) {
                // 双鱼座
                constellation = 11
            } else if (month == 3 && day > 20 || month == 4 && day <= 19) {
                // 白羊座
                constellation = 0
            } else if (month == 4 && day >= 20 || month == 5 && day <= 20) {
                // 金牛座
                constellation = 1
            } else if (month == 5 && day >= 21 || month == 6 && day <= 21) {
                // 双子座
                constellation = 2
            } else if (month == 6 && day > 21 || month == 7 && day <= 22) {
                // 巨蟹座
                constellation = 3
            } else if (month == 7 && day > 22 || month == 8 && day <= 22) {
                // 狮子座
                constellation = 4
            } else if (month == 8 && day >= 23 || month == 9 && day <= 22) {
                // 处女座
                constellation = 5
            } else if (month == 9 && day >= 23 || month == 10 && day <= 23) {
                // 天秤座
                constellation = 6
            } else if (month == 10 && day > 23 || month == 11 && day <= 22) {
                // 天蝎座
                constellation = 7
            } else if (month == 11 && day > 22 || month == 12 && day <= 21) {
                // 射手座
                constellation = 8
            } else if (month == 12 && day > 21 || month == 1 && day <= 19) {
                // 魔羯座
                constellation = 9
            }
            return constellation
        }
    }


    fun changerStringToDouble(value: String): Double {
        if (TextUtils.isEmpty(value)) {
            return 0.00
        }
        if (value == "null") {
            return 0.00
        }
        try {
            return java.lang.Double.parseDouble(value)
        } catch (e: Exception) {
            return 0.00
        }

    }

    fun changerValueNum(mDouble: Double?): Double {
        val b = BigDecimal(mDouble!!)
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    fun isValueNull(str: String?): Boolean {
        if (str == null || str.length == 0) {
            return true
        } else if (str == "null") {
            return true
        }
        return false
    }

    /**
     * 验证是否全为数字
     *
     * @param input
     * @return
     */
    fun isNum(input: String): Boolean {
        val p = compile("[0-9]+")
        val m = p.matcher(input)
        return m.matches()
    }


    /**
     * 已知内容
     * 替换的内容
     * 替换的颜色
     */
    fun getReplaceSpannableString(mContext: Context, details: String, keyWorld: String, colorId: Int): SpannableString {
        val mSpannableString = SpannableString(details)
        val beginIndex = details.indexOf(keyWorld)
        val endIndex = beginIndex + keyWorld.length
        if (beginIndex != -1 && endIndex != -1) {
            mSpannableString.setSpan(
                ForegroundColorSpan(mContext.resources.getColor(colorId)), beginIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return mSpannableString
    }

    fun friendlyNum(num: Float): String {
        return if (num == num.toInt().toFloat()) num.toInt().toString() else num.toString()
    }

    fun friendlyNumString(num: String): String {
        var value = 0.00
        if (!TextUtils.isEmpty(num)) {
            try {
                value = java.lang.Double.parseDouble(num)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

        }
        return if (value == value.toInt().toDouble()) value.toInt().toString() else value.toString()
    }

    fun friendlyNum(num: Double): String {
        return if (num == num.toInt().toDouble()) num.toInt().toString() else num.toString()
    }

    /**
     * 复制文字到粘贴板
     *
     * @param context
     * @param text
     */
    fun copyToClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < 11) {
            val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 粘贴
            //clip.getText();
            // 复制
            clip.text = text
        } else {
            // 获取系统剪贴板
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
            val clipData = ClipData.newPlainText("EakaySeller", text)
            // 把数据集设置（复制）到剪贴板
            clipboard.primaryClip = clipData
        }
        //        ToastUtil.showShort("复制成功");
    }

    /**
     * 获取系统粘贴板的数据
     *
     * @param context
     * @return
     */
    fun getClipboardText(context: Context): String? {
        // 获取系统剪贴板
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val primaryClip = clipboard.hasPrimaryClip()
        if (primaryClip) {
            val clip = clipboard.primaryClip
            val item = clip!!.getItemAt(0)
            return item.text.toString()
        }
        return null
    }

    /**
     * 连接方法 类似于javascript
     *
     * @param join   连接字符串
     * @param strAry 需要连接的集合
     * @return
     */
    fun join(join: String, strAry: Array<String>): String {
        val sb = StringBuffer()
        var i = 0
        val len = strAry.size
        while (i < len) {
            if (i == len - 1) {
                sb.append(strAry[i])
            } else {
                sb.append(strAry[i]).append(join)
            }
            i++
        }
        return sb.toString()
    }

    /**
     * 连接方法 类似于javascript
     *
     * @param join    连接字符串
     * @param listStr 需要连接的集合
     * @return
     */
    fun join(join: String, listStr: List<String>): String {
        val sb = StringBuffer()
        var i = 0
        val len = listStr.size
        while (i < len) {
            if (i == len - 1) {
                sb.append(listStr[i])
            } else {
                sb.append(listStr[i]).append(join)
            }
            i++
        }
        return sb.toString()
    }

    /**
     * 判断手机号码格式
     *
     * @param mobiles
     * @return
     */
    fun checkMobileNumber(mobiles: String): Boolean {
        val p = compile("^((13[0-9])|(14[5-9])|(15[0-3,5-9])|(16[6])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$")
        val mat = p.matcher(mobiles)
        return mat.matches()
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    fun checkBankCard(cardId: String): Boolean {
        val bit = getBankCardCheckCode(
            cardId
                .substring(0, cardId.length - 1)
        )
        return if (bit == 'N') {
            false
        } else cardId[cardId.length - 1] == bit
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    fun getBankCardCheckCode(nonCheckCodeCardId: String?): Char {
        if (nonCheckCodeCardId == null
            || nonCheckCodeCardId.trim { it <= ' ' }.length == 0
            || !nonCheckCodeCardId.matches("\\d+".toRegex())
        ) {
            // 如果传的不是数据返回N
            return 'N'
        }
        val chs = nonCheckCodeCardId.trim { it <= ' ' }.toCharArray()
        var luhmSum = 0
        var i = chs.size - 1
        var j = 0
        while (i >= 0) {
            var k = chs[i] - '0'
            if (j % 2 == 0) {
                k *= 2
                k = k / 10 + k % 10
            }
            luhmSum += k
            i--
            j++
        }
        return if (luhmSum % 10 == 0) '0' else (10 - luhmSum % 10 + '0'.toInt()).toChar()
    }

    /**
     * 拆分以","拼接的String字符串并返回array
     *
     * @param imagePaths
     * @return
     */
    fun splitString(imagePaths: String): List<String> {
        val paths = ArrayList<String>()
        if (TextUtils.isEmpty(imagePaths)) {
            return paths
        }
        if (imagePaths.contains(",")) {
            val split = imagePaths.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in split.indices) {
                val path = split[i]
                paths.add(path)
            }
        } else {
            paths.add(imagePaths)
        }
        return paths
    }

    /**
     * 连接方法 类似于javascript
     *
     * @param join    连接字符串
     * @param listStr 需要连接的集合
     * @return
     */
//    fun joinString(join: String, listStr: List<PictureOrderMessage>): String {
//        val sb = StringBuffer()
//        var i = 0
//        val len = listStr.size
//        while (i < len) {
//            if (i == len - 1) {
//                sb.append(listStr[i].getRemotePath())
//            } else {
//                sb.append(listStr[i].getRemotePath()).append(join)
//            }
//            i++
//        }
//        return sb.toString()
//    }

    /**
     * 拆分以","拼接的String字符串并返回array
     *
     * @param imagePaths
     * @return
     */
//    fun splitStringToList(imagePaths: String): List<PictureOrderMessage> {
//        val paths = ArrayList<PictureOrderMessage>()
//        if (TextUtils.isEmpty(imagePaths)) {
//            return paths
//        }
//        if (imagePaths.contains(",")) {
//            val split = imagePaths.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            for (i in split.indices) {
//                val path = split[i]
//                val message = PictureOrderMessage(Constants.NUMBER_ONE, null, path)
//                paths.add(message)
//            }
//        } else {
//            val message = PictureOrderMessage(Constants.NUMBER_ONE, null, imagePaths)
//            paths.add(message)
//        }
//        return paths
//    }

    /**
     * 获取请求list相关的请求数据
     *
     * @param currentPage 第几页
     * @param pageSize    大小
     * @param bean      date 里面的数据
     * @return
     */
    fun getListParams(currentPage: Int, pageSize: Int, bean: JSONObject): JSONObject {
        val total = JSONObject()
        val pageObject = JSONObject()
        pageObject.put("currentPage", currentPage)
        pageObject.put("pageSize", pageSize)
        val sortObject = JSONObject()
        sortObject.put("sort", "desc")
        sortObject.put("orderBy", "id")
        total.put("data", `bean`)
        total.put("page", pageObject)
        total.put("sort", sortObject)
        return total
    }

    /**
     * 获取数值精度格式化字符串
     *
     * @param precision
     * @return
     */
    fun getPrecisionFormat(precision: Int): String {
        return "%." + precision + "f"
    }

    /**
     * 反转数组
     *
     * @param arrays
     * @param <T>
     * @return
    </T> */
    fun <T> reverse(arrays: Array<T>?): Array<T>? {
        if (arrays == null) {
            return null
        }
        val length = arrays.size
        for (i in 0 until length / 2) {
            val t = arrays[i]
            arrays[i] = arrays[length - i - 1]
            arrays[length - i - 1] = t
        }
        return arrays
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    fun div(v1: Double, v2: Double, scale: Int): Double {
        if (scale < 0) {
            throw IllegalArgumentException(
                "The scale must be a positive integer or zero"
            )
        }
        val b1 = BigDecimal(v1.toString())
        val b2 = BigDecimal(v2.toString())
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    /**
     * 根据不同的情况获取对应的中文
     *
     * @param key
     * @return
     */

    fun getChinese(key: Int): String {
        return when (key) {
            1 -> "一"
            2 -> "二"
            3 -> "三"
            4 -> "四"
            5 -> "五"
            6 -> "六"
            7 -> "七"
            8 -> "八"
            9 -> "九"
            0 -> "零"
            else -> "零"
        }
    }

    /**
     * 根据不同的情况获取对应的中文
     *
     * @param key
     * @return
     */
    fun getChinese(key: String): String {
        if (key.isNullOrEmpty()) {
            return "零"
        }
        return when (key) {
            "1" -> "一"
            "2" -> "二"
            "3" -> "三"
            "4" -> "四"
            "5" -> "五"
            "6" -> "六"
            "7" -> "七"
            "8" -> "八"
            "9" -> "九"
            "0" -> "零"
            else -> "零"
        }
    }

    /**
     * 获取订单状态
     *
     * @param status
     * @return
     */
    fun getOrderStatus(context: Context, status: String, type: String): String {
        if (TextUtils.isEmpty(status)) {
            return context.getString(R.string.order_status_zero)
        }
        when (status) {
            context.getString(R.string.number_0) -> return context.getString(R.string.order_status_zero)
            context.getString(R.string.number_1) -> return context.getString(R.string.order_status_one)
            context.getString(R.string.number_2) -> return context.getString(R.string.order_status_two)
            context.getString(R.string.number_3) -> return context.getString(R.string.order_status_three)
            context.getString(R.string.number_4) -> return if (context.getString(R.string.number_1) == type) {
                context.getString(R.string.order_status_four_rescue)
            } else {
                context.getString(R.string.order_status_four)
            }
            context.getString(R.string.number_5) -> return context.getString(R.string.order_status_five)
            context.getString(R.string.number_6) -> return context.getString(R.string.order_status_six)
            context.getString(R.string.number_7) -> return context.getString(R.string.order_status_seven)
            context.getString(R.string.number_8) -> return context.getString(R.string.order_status_eight)
            context.getString(R.string.number_9) -> return context.getString(R.string.order_status_nine)
            context.getString(R.string.number_10) -> return if (context.getString(R.string.number_1) == type) {
                context.getString(R.string.order_status_four_rescue)
            } else {
                context.getString(R.string.order_status_ten)
            }
            context.getString(R.string.number_11) -> return context.getString(R.string.order_status_eleven)
            context.getString(R.string.number_12) -> return context.getString(R.string.order_status_twelve)
            context.getString(R.string.number_13) -> return if (context.getString(R.string.number_1) == type) {
                context.getString(R.string.order_status_four_rescue)
            } else {
                context.getString(R.string.order_status_thirteen)
            }
            context.getString(R.string.number_14) -> return if (context.getString(R.string.number_1) == type) {
                context.getString(R.string.order_status_fourteen)
            } else {
                context.getString(R.string.order_status_four_rescue)
            }
            context.getString(R.string.number_15) -> return context.getString(R.string.order_status_fifteen)
            else -> return context.getString(R.string.order_status_zero)
        }
    }

    /** Returns true if two possibly-null objects are equal.  */
    fun equal(a: Any?, b: Any): Boolean {
        return a == b || (a != null && a == b)
    }

    fun createBody(param: JSONObject): RequestBody {
        if (!param.containsKey(Constants.KEY_REQUEST_DEVICE_TOKEN)) {
            val deviceToken = LSPUtils.get(Constants.KEY_DEVICE_TOKEN, "")
            if (deviceToken.isNotEmpty()) {
                param[Constants.KEY_REQUEST_DEVICE_TOKEN] = deviceToken
            }
        }
        if (!param.containsKey(Constants.KEY_REQUEST_ACCESS_TOKEN)) {
            val accessToken = LSPUtils.get(Constants.KEY_AUTN_TOKEN, "")
            if (accessToken.isNotEmpty()) {
                param[Constants.KEY_REQUEST_ACCESS_TOKEN] = accessToken
            }
        }
        return RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull()!!, param.toJSONString())
    }

}
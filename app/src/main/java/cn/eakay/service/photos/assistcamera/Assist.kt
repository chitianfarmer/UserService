package cn.eakay.service.photos.assistcamera

/**
 * @packageName: UserService
 * @fileName: Assist
 * @author: chitian
 * @date: 2019-11-28 10:08
 * @description: 自定义相机的辅助配置
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
interface Assist {
    /**
     * 是否支持自定义模版
     */
    fun supportAssistMask(): Boolean

    /**
     * 是否有示例图片
     */
    fun hasExamplePicFun(): Boolean

    /**
     * 显示示例图片
     */
    fun showExamplePic()

    /**
     * 隐藏示例图片
     */
    fun hideExamplePic()
}
package cn.eakay.service.photos

import cn.eakay.service.base.BaseActivity

/**
 * @packageName: UserService
 * @fileName: PickPicActivity
 * @author: chitian
 * @date: 2019-11-28 10:05
 * @description:  拍照界面
 *
 * 有些系统厂商的 ROM 会给自带相机应用做优化，当某个 app 通过 intent 进入相机拍照界面时，
 * 系统会把这个 app 当前最上层的 Activity 销毁回收。（注意：猜测系统会不会回收，可能与当前内存情况有关，所以导致有时回收，有时不回收）
 * 如果被回收了，Activity的生命周期重走时，一些非UI的变量就会为空，出现{@link NullPointerException}的错误。
 * 为了避免出现这种错误，必须得在{@link Activity#onSaveInstanceState(Bundle)}
 * 和{@link Activity#onRestoreInstanceState(Bundle)} 保存和还原一些必要成员变量。
 * 但是这样的话，与选图相关的页面多了，就比较麻烦，复用性，可维护性比较差。
 * 所以，就希望用此页做中间代理。省去保存/还原 业务变量的麻烦。
 * <p>
 * >>>since 2017-04-14
 * BusinessActivity -> PickPicActivity -> CameraActivity -> CropActivity
 * 由于一些手机启动相机，是强制横屏的，拍照之后，跳转的确认界面是不限制方向的或者是竖屏的，在返回时，是会引起两次屏幕旋转的。
 * 又因拿到结果之后，这些Activity（除了BusinessActivity）都是在onActivityResult中返回结果，finish，
 * 所以不会在{@link PickPicActivity}中响应这两次屏幕旋转事件，转而在BusinessActivity中去响应，
 * 造成BusinessActivity重新创建(如果没有配置android:configChanges)需要关心 还原/保存 等事情。
 * 为了避免BusinessActivity重建，需要在Manifest文件中配置BusinessActivity的
 * android:configChanges="orientation|screenSize|keyboardHidden"属性
 * <p>
 * >>>since 2017-10-23 v4.2.5
 * 添加启动取还车自定义相机功能,需要传递{@link AssistConfig}
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class PickPicActivity :BaseActivity(){

    override fun getLayoutId(): Int =INVALID_CONTENT_ID
}
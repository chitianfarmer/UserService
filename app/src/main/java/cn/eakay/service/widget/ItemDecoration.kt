package cn.eakay.service.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @packageName: UserService
 * @fileName: ItemDecoration
 * @author: chitian
 * @date: 2019-07-16 10:33
 * @description:
 * @org: http://www.eakay.cn (芜湖恒天易开软件科技有限公司)
 *
 */
class ItemDecoration :RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(0,10,0,10)
    }
}
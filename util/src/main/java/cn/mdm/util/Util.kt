package cn.mdm.util

import android.content.Context
import android.text.SpannableString
import android.text.style.LeadingMarginSpan
import android.view.ViewTreeObserver
import android.widget.TextView

/**
 * @author qlZou
 * @desc  通用工具类
 * @create 2022/7/1
 */
object Util{

    /**
     * Android TextView 缩进指定距离
     * 方案二：动态设置缩进距离的方式
     * https://www.jianshu.com/p/03ec9865c942
     */
     fun calculateTag2(tag: TextView, title: TextView, text: String?) {
        val observer: ViewTreeObserver = tag.viewTreeObserver
        observer.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val spannableString = SpannableString(text)
                //这里没有获取margin的值，而是直接写死的
                val what: LeadingMarginSpan.Standard =
                    LeadingMarginSpan.Standard(tag.getWidth() + dip2px(tag.context, 10.0), 0)
                spannableString.setSpan(
                    what,
                    0,
                    spannableString.length,
                    SpannableString.SPAN_INCLUSIVE_INCLUSIVE
                )
                title.text = spannableString
                tag.viewTreeObserver.removeOnPreDrawListener(this)
                return false
            }
        })
    }

     fun dip2px(context: Context, dpValue: Double): Int {
        val density: Float = context.getResources().getDisplayMetrics().density
        return (dpValue * density + 0.5).toInt()
    }
}

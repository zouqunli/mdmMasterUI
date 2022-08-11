package cn.mdm.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.aspsine.fragmentnavigator.FragmentNavigator
import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter

/**
 * 自定义Fragment导航
 */
class MyFragNavigator {
    companion object{
        const val TAB1 = 0
        const val TAB2 = 1
        const val TAB3 = 2
        const val TAB4 = 3
        const val TAB5 = 4
        const val TAB6 = 5
    }
    private var mNavigator: FragmentNavigator? = null
    private var count = 0
    private var fragments :List<Fragment>
    constructor(savedInstanceState: Bundle?,fragmentManager: FragmentManager?,containerViewId:Int,fragments:List<Fragment>,startIndex:Int = TAB1){
        mNavigator = FragmentNavigator(fragmentManager, FragmentAdapter(fragments),containerViewId)
        mNavigator?.onCreate(savedInstanceState)
        mNavigator?.setDefaultPosition(startIndex)
        mNavigator?.showFragment(mNavigator?.currentPosition?:startIndex)
        count = fragments.size
        this.fragments = fragments
    }

    class FragmentAdapter(private var fragments:List<Fragment>): FragmentNavigatorAdapter {
        private var tagArr = mutableListOf<String>()
        init {
            for(index in fragments.indices) tagArr.add("frg$index")
        }
        override fun onCreateFragment(position: Int): Fragment {
            return fragments[position]
        }

        override fun getTag(position: Int): String {
            return tagArr[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
    //显示数据
    fun switchTab(position: Int,isRest:Boolean = false,bundle: Bundle? = null){
        if(position > count) return
        if(bundle != null) getFragment(position).arguments = bundle
        if(isRest) {
            //重新加载界面页面会重新走onCreate方法
            mNavigator?.resetFragments(position)
        }
        //切换页面只会在第一次走onCreate方法之后都是onHiddenChanged方法
        else mNavigator?.showFragment(position)
    }
    //获取fragment数量
    fun getCount():Int{
        return count
    }

    //上一个页面
    fun preTab(isRest:Boolean = false){
        var position = mNavigator?.currentPosition?:0
        position--
        if(position < 0){
            position = 0
        }
        switchTab(position,isRest)
    }

    //下一个页面
    fun nextTab(isRest:Boolean = false){
        var position = mNavigator?.currentPosition?:0
        position++
        if(position >= count){
            //长度减1是因为集合从0开始
            position = count - 1
        }
        switchTab(position,isRest)
    }

    //显示首页
    fun showFirst(isRest:Boolean = false){
        switchTab(TAB1,isRest)
    }

    //获取当前的fragment
    fun getCurrentFragment():Fragment{
        return getFragment(getCurrentIndex())
    }

    //获取当前的fragment
    fun getFragment(position:Int):Fragment{
        return this.fragments[position]
    }
    //获取当前给的序号
    fun getCurrentIndex(): Int {
        return mNavigator?.currentPosition?:0
    }
}
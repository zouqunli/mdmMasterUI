package cn.mdm.masterui.wiget.viewpage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * TODO 复制过来的，暂未学习和具体研究代码
 * 自定义类似ViewPage图片效果页
 */
public class MyViewPage extends ViewGroup {
    private int mLastX;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaxVelocity;
    /**
     * 当前显示的是第几个屏幕
     */
    private int mCurrentPage = 0;

    public MyViewPage(Context context) {
        super(context);
        init(context);
    }

    public MyViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyViewPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledPagingTouchSlop();
        mMaxVelocity = config.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for(int i = 0; i < count; i++){
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        Log.d("TAG","--l-->"+l+",--t-->"+t+",-->r-->"+r+",--b-->"+b);
        for(int i = 0; i < count; i++){
            View child = getChildAt(i);
            child.layout(i * getWidth(), t, (i + 1) * getWidth(), b);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(ev);
        int x = (int) ev.getX();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
 /* 注释的里面是使用startScroll（）来进行滑动的
 int oldScrollX = getScrollX();//原来的偏移量
 int preScrollX = oldScrollX + dx;//本次滑动后形成的偏移量
 if (preScrollX > (getChildCount() - 1) * getWidth()) {
  preScrollX = (getChildCount() - 1) * getWidth();
  dx = preScrollX - oldScrollX;
 }
 if (preScrollX < 0) {
  preScrollX = 0;
  dx = preScrollX - oldScrollX;
 }
 mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, 0);
 //注意，使用startScroll后面一定要进行invalidate刷新界面，触发computeScroll()方法，因为单纯的startScroll()是属于Scroller的，只是一个辅助类，并不会触发界面的绘制
 invalidate();
 */
                //但是一般在ACTION_MOVE中我们直接使用scrollTo或者scrollBy更加方便
                scrollBy(dx,0);
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int initVelocity = (int) velocityTracker.getXVelocity();
                if(initVelocity > mMaxVelocity && mCurrentPage > 0){//如果是快速的向右滑，则需要显示上一个屏幕
                    Log.d("TAG","----------------快速的向右滑--------------------");
                    scrollToPage(mCurrentPage - 1);
                }else if(initVelocity < -mMaxVelocity && mCurrentPage < (getChildCount() - 1)){//如果是快速向左滑动，则需要显示下一个屏幕
                    Log.d("TAG","----------------快速的向左滑--------------------");
                    scrollToPage(mCurrentPage + 1);
                }else{//不是快速滑动的情况，此时需要计算是滑动到
                    Log.d("TAG","----------------慢慢的滑动--------------------");
                    slowScrollToPage();
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    /**
     * 缓慢滑动抬起手指的情形，需要判断是停留在本Page还是往前、往后滑动
     */
    private void slowScrollToPage() {
        //当前的偏移位置
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        //判断是停留在本Page还是往前一个page滑动或者是往后一个page滑动
        int whichPage = (getScrollX() + getWidth() / 2 ) / getWidth() ;
        scrollToPage(whichPage);
    }

    /**
     * 滑动到指定屏幕
     * @param indexPage
     */
    private void scrollToPage(int indexPage) {
        mCurrentPage = indexPage;
        if(mCurrentPage > getChildCount() - 1){
            mCurrentPage = getChildCount() - 1;
        }
        //计算滑动到指定Page还需要滑动的距离
        int dx = mCurrentPage * getWidth() - getScrollX();
        mScroller.startScroll(getScrollX(),0,dx,0,Math.abs(dx) * 2);//动画时间设置为Math.abs(dx) * 2 ms
        //记住，使用Scroller类需要手动invalidate
        invalidate();
    }

    @Override
    public void computeScroll() {
        Log.d("TAG", "---------computeScrollcomputeScrollcomputeScroll--------------");
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
    }
}

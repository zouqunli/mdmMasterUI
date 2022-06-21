package cn.mdm.masterui.wiget.progress

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import cn.mdm.masterui.R
import kotlin.properties.Delegates

class MyProgress :View {

    /**
     * 进度条颜色
     */
    var progressColor:Int by Delegates.observable(Color.parseColor("#FFBF00")){ _, _, newValue->

    }

    /**
     * 进度条背景颜色
     */
    var progressBgColor:Int by Delegates.observable(Color.TRANSPARENT){ _, _, newValue->

    }

    /**
     * 角的圆弧半径
     */
    var cornerRadius:Float by Delegates.observable(0f){ _, _, newValue->

    }

    /**
     * 最大进度
     */
    var maxValue:Float by Delegates.observable(100f){ _, _, newValue->
        if(newValue <= 0f){
            maxValue = 100f
        }
        //重新计算值
        initData()
    }

    /**
     * 当前进度
     */
    var progress:Float by Delegates.observable(0f){ _, _, newValue->
        if(newValue > maxValue){
            progress = maxValue
        }
        postInvalidate()
    }

    private var mWidth = 100f
    private var cellWidth = 1f
    private var mHeight = 20f
    private var marginTop = 0f
    private lateinit var mPaint:Paint
    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initView(attrs)
    }
    @SuppressLint("Recycle")
    private fun initView(attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.MyProgress).apply {
            progressColor = getColor(R.styleable.MyProgress_progress_color,Color.parseColor("#FFBF00"))
            progressBgColor = getColor(R.styleable.MyProgress_progress_bg_color,Color.TRANSPARENT)
            cornerRadius = getFloat(R.styleable.MyProgress_corner_radius,0f)
            maxValue = getFloat(R.styleable.MyProgress_max_value,100f)
            progress = getFloat(R.styleable.MyProgress_progress,0f)
        }
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //初始化计算值
        initData()
    }

    private fun initData(){
        mWidth = measuredWidth/1f
        //算出单位宽度
        cellWidth = mWidth/maxValue
        //默认3分之一的高度
        mHeight = measuredHeight/3f
        //算出顶部距离
        marginTop = (measuredHeight/2f) - (mHeight/2f)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        //画进度背景
        canvas.translate(0f,marginTop)
        val rectBg = RectF(0f,0f,mWidth,mHeight)
        mPaint.color = progressBgColor
        canvas.drawRoundRect(rectBg,cornerRadius,cornerRadius,mPaint)
        //画进度
        val rect = RectF(0f,0f,cellWidth * progress,mHeight)
        mPaint.color = progressColor
        canvas.drawRoundRect(rect,cornerRadius,cornerRadius,mPaint)
        Log.e("MyProgress","rectf:left(${rect.left}),top(${rect.top}),right(${rect.right}),bottom(${rect.bottom})")
    }

    //开启动画
    fun startAnim(){
        ObjectAnimator.ofFloat(this,"progress",0f,progress).apply {
            interpolator = AnticipateInterpolator()
            duration = 2000
            start()
        }
    }

}
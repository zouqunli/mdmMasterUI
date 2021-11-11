package cn.mdm.masterui.wiget.progress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author qlzou
 * @desc 自定义圆形进度条
 * @create 2021/9/26
 */
public class MyCircleProgressView extends View {

    private Paint paint;
    //圆的整体宽度
    private int circleWidth;
    //剩余圆环的进度的底色
    private int roundBackgroundColor;
    //中间文字内容
    private String centerText = "Center\nText";
    //中间文字的颜色
    private int textColor;
    //中间文字的字体大小
    private float textSize;
    //圆环的厚度
    private float roundWidth;
    //当前进度数据
    private float progress = 77.5f;
    //渐进色的色值数组
    private int[] colors = {0xffff4639, 0xffCDD513,0xffff0000 };//0xff3CDF5F
    //圆半径
    private int radius;
    //画圆的矩形数据
    private RectF oval;
    //文字画笔
    private TextPaint mPaintText;
    //刻度线分割的数量
    private int maxColorNumber = 100;
    //关于刻度线的
    private float singlePoint = 9;
    //刻度线宽
    private float lineWidth = 0.3f;
    //圆心
    private int circleCenter;
    //渐进色shader
    private SweepGradient sweepGradient;
    //是否开启线模式
    private boolean isLine = false;

    /**
     * 分割的数量
     *
     * @param maxColorNumber 数量
     */
    public void setMaxColorNumber(int maxColorNumber) {
        this.maxColorNumber = maxColorNumber;
        singlePoint = (float) 360 / (float) maxColorNumber;
        invalidate();
    }

    /**
     * 是否是线条
     *
     * @param line true 是 false否
     */
    public void setLine(boolean line) {
        isLine = line;
        invalidate();
    }

    public int getCircleWidth() {
        return circleWidth;
    }

    public MyCircleProgressView(Context context) {
        this(context, null);
    }

    public MyCircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MyCircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        maxColorNumber = 40;
        circleWidth = getDpValue(100);
        roundBackgroundColor = 0xff4F598F;
        textColor = 0xff999999;
        roundWidth = 5;
        textSize =getDpValue(8);
        colors[0] = 0xffFF8355;
        colors[1] = 0xffFF9360;
        colors[2] = 0xffFF6A33;
//        initView();
    }


    /**
     * 空白出颜色背景
     * @param roundBackgroundColor
     */
    public MyCircleProgressView setRoundBackgroundColor(int roundBackgroundColor) {
        this.roundBackgroundColor = roundBackgroundColor;
        paint.setColor(roundBackgroundColor);
        invalidate();
        return this;
    }

    /**
     * 设置中间文字
     * @param text
     * @return
     */
    public MyCircleProgressView setText(String text){
        if(TextUtils.isEmpty(text))text = centerText;
        centerText = text;
        return this;
    }

    /**
     * 圆环中间文字字体颜色
     * @param textColor
     */
    public MyCircleProgressView setTextColor(int textColor) {
        this.textColor = textColor;
        mPaintText.setColor(textColor);
        invalidate();
        return this;
    }

    /**
     * 圆环中间文字字体大小
     * @param textSize
     */
    public MyCircleProgressView setTextSize(float textSize) {
        this.textSize = textSize;
        mPaintText.setTextSize(textSize);
        invalidate();
        return this;
    }

    /**
     * 渐变颜色
     * @param colors
     */
    public MyCircleProgressView setColors(int[] colors) {
        if (colors.length < 2) {
            throw new IllegalArgumentException("colors length < 2");
        }
        this.colors = colors;
        sweepGradientInit();
        invalidate();
        return this;
    }


    /**
     * 间隔角度大小
     *
     * @param lineWidth
     */
    public MyCircleProgressView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        invalidate();
        return this;
    }


    private int getDpValue(int w) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, w, getContext().getResources().getDisplayMetrics());
    }

    /**
     * 圆环宽度
     *
     * @param roundWidth 宽度
     */
    public MyCircleProgressView setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
        if (roundWidth > circleCenter) {
            this.roundWidth = circleCenter;
        }
        radius = (int) (circleCenter - this.roundWidth / 2); // 圆环的半径
        oval.left = circleCenter - radius;
        oval.right = circleCenter + radius;
        oval.bottom = circleCenter + radius;
        oval.top = circleCenter - radius;
        paint.setStrokeWidth(this.roundWidth);
        invalidate();
        return this;
    }

    /**
     * 圆环的直径
     *
     * @param circleWidth 直径
     */
    public MyCircleProgressView setCircleWidth(int circleWidth) {
        this.circleWidth = circleWidth;
        circleCenter = circleWidth / 2;

        if (roundWidth > circleCenter) {
            roundWidth = circleCenter;
        }
        setRoundWidth(roundWidth);
        sweepGradient = new SweepGradient(this.circleWidth / 2, this.circleWidth / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.circleWidth / 2, this.circleWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
        return this;
    }

    /**
     * 渐变初始化
     */
    public void sweepGradientInit() {
        //渐变颜色
        sweepGradient = new SweepGradient(this.circleWidth / 2, this.circleWidth / 2, colors, null);
        //旋转 不然是从0度开始渐变
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, this.circleWidth / 2, this.circleWidth / 2);
        sweepGradient.setLocalMatrix(matrix);
    }

    public void initView() {

        circleCenter = circleWidth / 2;//半径
        singlePoint = (float) 360 / (float) maxColorNumber;
        radius = (int) (circleCenter - roundWidth / 2); // 圆环的半径
        sweepGradientInit();
        mPaintText = new TextPaint();
        mPaintText.setColor(textColor);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(textSize);
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(Color.WHITE);

        paint = new Paint();
        paint.setColor(roundBackgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(roundWidth);
        paint.setAntiAlias(true);

        // 用于定义的圆弧的形状和大小的界限
        oval = new RectF(circleCenter - radius, circleCenter - radius, circleCenter + radius, circleCenter + radius);

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        //是否是线条模式
        if (isLine) {
            float start = -90f;
            float p = ((float) maxColorNumber / (float) 100);
            p = (int) (progress * p);
            for (int i = 0; i < p; i++) {
                paint.setColor(roundBackgroundColor);
                canvas.drawArc(oval, start + singlePoint - lineWidth, lineWidth, false, paint); // 绘制间隔快
                start = (start + singlePoint);
            }
        }
        //绘制剩下的空白区域
        paint.setColor(roundBackgroundColor);
        canvas.drawArc(oval, -90, (float) (-(100 - progress) * 3.6), false, paint);

        //绘制当前进度背景渐变颜色
        paint.setShader(sweepGradient);
        canvas.drawArc(oval, -90, (float) (progress * 3.6), false, paint);
        paint.setShader(null);

        //文字写在居中
        StaticLayout layout = new StaticLayout(centerText,mPaintText, (int) (oval.width()-2*roundWidth)
                , Layout.Alignment.ALIGN_NORMAL,1,0,true);
        canvas.save();
        //平移画布
        canvas.translate(circleCenter,circleCenter-layout.getHeight()/2);
        layout.draw(canvas);
        canvas.restore();

//        //绘制文字刻度
//        for (int i = 1; i <= 10; i++) {
//            canvas.save();// 保存当前画布
//            canvas.rotate(360 / 10 * i, circleCenter, circleCenter);
//            canvas.drawText(i * 10 + "", circleCenter, circleCenter - radius + roundWidth / 2 + getDpValue(4) + textSize, mPaintText);
//            canvas.restore();//
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            circleWidth = widthSize > heightSize?heightSize:widthSize;
        }
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //测量好数据初始化画笔等数据
        initView();
    }

    OnProgressScore onProgressScore;

    public interface OnProgressScore {
        void setProgressScore(float score);

    }

    /**
     * 设置圆环当前进度
     * @param p
     * @return
     */
    public synchronized MyCircleProgressView setProgress(float p) {
        return setProgress(p,100);
    }

    /**
     * 设置圆环当前进度
     * @param p   当前值
     * @param max 设定最大值
     * @return
     */
    public synchronized MyCircleProgressView setProgress(float p,float max) {
        return setProgress(p,max,null);
    }

    /**
     * 设置圆环当前进度
     * @param p  当前值
     * @param max  设定最大值
     * @param onProgressScore 监听
     * @return
     */
    public synchronized MyCircleProgressView setProgress(float p,float max, OnProgressScore onProgressScore) {
        this.onProgressScore = onProgressScore;
        if(p > max)p = max;
        float percent = p / max;
        progress = percent * 100;
        postInvalidate();
        return this;
    }

    /**
     * 是否开启动画
     * @param isAnimator
     * @return
     */
    public synchronized MyCircleProgressView isAnimator(boolean isAnimator){
        if(!isAnimator)return this;
        ObjectAnimator.ofFloat(this,"progress",0,progress)
        .setDuration(2000).start();
        return this;
    }

}

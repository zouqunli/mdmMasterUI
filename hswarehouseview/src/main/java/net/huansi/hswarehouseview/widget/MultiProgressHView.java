package net.huansi.hswarehouseview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * @date 创建时间 2022/6/8
 * @author qlzou
 * @Description  多层横向进度条
 * @Version 2.0
 */
public class MultiProgressHView<T extends MultiProgressHView.IProgressInfo> extends View {


    private float itemWidth = 310;
    private float itemHeight = 310;

    private float defaultItemWidth = 310;

    private float barWidth = 20; //默认柱子的宽度
    private float barHeight = 50; //柱子的高度
    private float cellWidth = 1;  //按照list的最大值算出的1点的 横向看宽度
    private Paint colorBarPaint; //柱状图画笔
    private Paint borderPaint;  //边框画笔
    private TextPaint textPaint; //文字画笔
    private float maxNum = 0;
    public float margin = 0;
    private float rectRadius = 0f;
    protected boolean isShowBorder = false;
    protected  boolean isBorderOver = false;

    //柱状图的柱形矩形
    private RectF barRectF;
    private List<T> mList;
    private List<T> mDefaultList;

    public MultiProgressHView(Context context) {
        this(context,null);
    }
    public MultiProgressHView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MultiProgressHView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init() {
        //初始化画笔
        colorBarPaint = new Paint();
        colorBarPaint.setAntiAlias(true);
        //Paint.Style.FILL_AND_STROKE 就是内容和轮廓一起画
        colorBarPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        //设置矩形
        barRectF = new RectF();

        textPaint = new TextPaint();

        //默认初始化数据
        mDefaultList = new ArrayList<>();
//        mDefaultList.add((T) new ProgressInfo(Color.RED,10));
//        mDefaultList.add((T) new ProgressInfo(Color.GREEN,20));
//        mDefaultList.add((T) new ProgressInfo(Color.YELLOW,30));
//        mDefaultList.add((T) new ProgressInfo(Color.WHITE,40));
        mList = mDefaultList;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        itemHeight = getMeasuredHeight();
        if(getMeasuredWidth() != defaultItemWidth){
            defaultItemWidth = getMeasuredWidth();
            //重新计算
            setList(mList,defaultItemWidth,maxNum);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculate();
        float borderWidth = borderPaint.getStrokeWidth();
        if(isShowBorder){
            //因为drawRoundRect圆角矩形，设置圆角后我们画出来的矩形的线宽只有我们 setStrokeWidth设置的一半宽。
            if(isBorderOver) canvas.drawRoundRect(new RectF(borderWidth/2 + margin,borderWidth/2 + margin,itemWidth - borderWidth/2-margin,itemHeight-borderWidth/2-margin),rectRadius,rectRadius,borderPaint);
            drawRect(canvas,borderWidth);
            if(!isBorderOver) canvas.drawRoundRect(new RectF(borderWidth/2+ margin,borderWidth/2+ margin,itemWidth - borderWidth/2-margin,itemHeight-borderWidth/2-margin),rectRadius,rectRadius,borderPaint);
        }else{
            drawRect(canvas,0);
        }
        Log.i("MtulProgressHView",mList.get(0).toString());
    }

    //画中间进度矩形
    private void drawRect(Canvas canvas,float borderWidth){
        float firstLeft = margin;
//        borderWidth += 1;
        for (int j = 0; j < mList.size(); j++) {
            //计算出高度
            barWidth = cellWidth * mList.get(j).getNum();
            barWidth = barWidth <= 0 ? 0 : barWidth;

            //设置颜色
            colorBarPaint.setColor(mList.get(j).getColor());

            float barLeft = firstLeft;
            float top = margin;
            float right = firstLeft + barWidth;
            float bottom = barHeight;
            if(barWidth > 0){
                //因为drawRoundRect圆角矩形，设置圆角后我们画出来的矩形的线宽只有我们 setStrokeWidth设置的一半宽。
                barRectF.set(barLeft + borderWidth/2, top + borderWidth/2, right- borderWidth/2, bottom- borderWidth/2);
                canvas.drawRoundRect(barRectF,rectRadius,rectRadius, colorBarPaint);
            }
            firstLeft += barWidth;
        }
    }






    //画文字居中
    private void drawText(Canvas canvas, RectF rectF, String text, int color, boolean isTitle){
        if(color == 0) {
            textPaint.setColor(Color.BLACK);
        }else{
            textPaint.setColor(color);
        }
        float textSize = barWidth/44 * 18 > 20 ? 20 : 18 * (barWidth / 44);
        textSize = textSize < 15 ? 15 : textSize;
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (rectF.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式

        if(isTitle) {
            //换行 拆开换行
            int group = (int) (4 * (barWidth / 44));
            if (text.length() > group) {
                int count = text.length() % group >= 1 ? text.length() / group + 1 : text.length() / group;
                for (int i = 0; i < count; i++) {
                    if ((i + 1) * group < text.length()) {
                        canvas.drawText(text.substring(i * group, (i + 1) * group), rectF.centerX(), baseLineY + (i * rectF.height() / 2), textPaint);
                    } else {
                        canvas.drawText(text.substring(i * group, text.length()), rectF.centerX(), baseLineY + (i * rectF.height() / 2), textPaint);
                    }
                }
            } else {
                canvas.drawText(text, rectF.centerX(), baseLineY, textPaint);
            }
        }else{
            canvas.drawText(text, rectF.centerX(), baseLineY, textPaint);
        }
    }


    public static class ProgressInfo implements IProgressInfo {
        private int color;
        private String subTitle;
        private float num;
        private String keyInfo; //记录关键信息

        public ProgressInfo(){}

        public ProgressInfo(int color,float num){
            this.color = color;
            this.num = num;
        }

        public ProgressInfo(int color, String subTitle, float num){
            this.color = color;
            this.subTitle = subTitle;
            this.num = num;
        }

        public ProgressInfo(int color, String subTitle, float num, String keyInfo){
            this.color = color;
            this.subTitle = subTitle;
            this.num = num;
            this.keyInfo = keyInfo;
        }

        @Override
        public int getColor() {
            return color;
        }

        @Override
        public String getSubTitle() {
            return subTitle;
        }

        @Override
        public float getNum() {
            return num;
        }

        @Override
        public String getKeyInfo() {
            return keyInfo;
        }
        @Override
        public String toString() {
            return color+"," + subTitle + ","+ num + "," +keyInfo;
        }
    }

    /**
     * 优化建议：
     * 可以采用接口的方式进行获取数据，这样的话可以避免转换对象
     */
    public interface IProgressInfo{
        //获取颜色
        @ColorInt
        int getColor();
        //获取标题
        String getSubTitle();
        //获取数量
        float getNum();
        //记录关键信息
        String getKeyInfo();
    }



    /**
     * 设置单个数据源
     * @param info
     */
    public void setOneData(T info, float itemWidth,float maxNum){
        setList(new ArrayList<T>(){{add(info);}},itemWidth,maxNum);
    }
    /**
     * 设置数据源
     * @param list
     */
    public void setList(List<T> list, float itemWidth){
        setList(list,itemWidth,0);
    }

    /**
     * 设置数据源
     * @param list  数据源
     * @param maxNum 设置默认最大数据值
     */
    public void setList(List<T> list, float width, float maxNum){
        synchronized (this) {
            //大于等于0的才设置值
            if(width > 0){
                this.itemWidth = width;
            }else{
                this.itemWidth = defaultItemWidth;
            }
            if (list == null || list.size() <= 0) {
                mList = mDefaultList;
            } else {
                mList = list;
            }
            barWidth = this.itemWidth - (2 * margin);  //获取柱状的宽度
            barHeight = itemHeight - (2 * margin); //获取柱状的高度 减去边框的高度
            int subSum = 0;
            for (IProgressInfo info : mList) {
                subSum += info.getNum();
            }
            if (subSum > maxNum) {
                maxNum = subSum;
            }
            if (maxNum == 0) {
                maxNum = 100;
            }
            cellWidth = barWidth / maxNum; //平均每num = 1 时的宽度
//            this.itemWidth = width;
            this.maxNum = maxNum;
            postInvalidate();
        }
    }

    /**
     * 边框宽度
     * @param borderWidth
     */
    public MultiProgressHView setBorderWidth(float borderWidth){
        if(borderWidth < 0)borderWidth = 0;
        borderPaint.setStrokeWidth(borderWidth);
        colorBarPaint.setStrokeWidth(borderWidth);
        postInvalidate();
        return this;
    }

    public MultiProgressHView setMargin(float margin){
        if(margin < 0)margin = 0;
        this.margin = margin;
        postInvalidate();
        return this;
    }

    //是否显示边框
    public MultiProgressHView isShowBoarder(boolean isShow){
        this.isShowBorder = isShow;
        postInvalidate();
        return this;
    }

    //是否边框可以被进度覆盖
    public MultiProgressHView isBorderOver(boolean isOver){
        this.isBorderOver = isOver;
        postInvalidate();
        return this;
    }

    //设置边框颜色
    public MultiProgressHView setBorderColor(@ColorInt int borderColor){
        if(borderColor != 0){
            borderPaint.setColor(borderColor);
        }
        postInvalidate();
        return this;
    }

    public MultiProgressHView setRectRadius(float radius){
        if(radius < 0) radius = 0f;
        this.rectRadius = radius;
        postInvalidate();
        return this;
    }



    /**
     * 重新requestLayout时调用
     */
    public void calculate(){
        barWidth = itemWidth - (2 * margin);  //获取柱状的宽度
        barHeight = itemHeight - (2 * margin); //获取柱状的高度 减去margin
        int subSum = 0;
        for (IProgressInfo info : mList) {
            subSum += info.getNum();
        }
        if (subSum > maxNum) {
            maxNum = subSum;
        }
        if (maxNum == 0) {
            maxNum = 100;
        }
        cellWidth = barWidth / maxNum; //平均每num = 1 时的宽度
//        postInvalidate();
    }
}

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

import androidx.annotation.Nullable;

/**
 * @date 创建时间 2018/3/11
 * @author qlzou
 * @Description  多层横向进度条
 * @Version 1.0
 */
public class MultiProgressHView extends View {


    private boolean isLoadNewData = false;
    private float itemWidth = 310;
    private float itemHeight = 310;

    private float barWidth = 20; //默认柱子的宽度
    private float barHeight = 50; //柱子的高度
    private float cellWidth = 1;  //按照list的最大值算出的1点的 横向看宽度
    private Paint colorBarPaint; //柱状图画笔
    private TextPaint textPaint; //文字画笔
    private float maxNum = 0;
    public float borderWidth;

    //柱状图的柱形矩形
    private RectF barRectf;
    private List<ProgressInfo> mList;
    private List<ProgressInfo> mDefaultList;

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
        //设置矩形
        barRectf = new RectF();

        textPaint = new TextPaint();

        //默认初始化数据
        mDefaultList = new ArrayList<>();
        mDefaultList.add(new ProgressInfo(Color.RED,10));
        mDefaultList.add(new ProgressInfo(Color.GREEN,20));
        mDefaultList.add(new ProgressInfo(Color.YELLOW,30));
        mDefaultList.add(new ProgressInfo(Color.WHITE,40));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(!isLoadNewData) {
            isLoadNewData = true;
//            setInfo(mDefaultList,10);
        }
//        setList(mDefaultList);
        itemHeight = getMeasuredHeight();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculate();
        float firstLeft = 0f;
        for(int j = 0; j < mList.size();j++){

            //计算出高度
            barWidth = cellWidth * mList.get(j).num;
            barWidth = barWidth <= 0?1 : barWidth;

            //设置颜色
            colorBarPaint.setColor(mList.get(j).color);
            barRectf = new RectF();

            float barLeft = firstLeft;
            float top = borderWidth;
            float right = firstLeft + barWidth;
            float bottom = barHeight - borderWidth;
            barRectf.set(barLeft,top,right,bottom);
            canvas.drawRect(barRectf,colorBarPaint);
            firstLeft += barWidth;

        }
        Log.i("MtulProgressHView",mList.get(0).toString());
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


    public static class ProgressInfo{
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
        public String toString() {
            return color+"," + subTitle + ","+ num + "," +keyInfo;
        }
    }


    /**
     * 设置数据源
     * @param list
     */
    public void setList(List<ProgressInfo> list, float itemWidth){
        setList(list,itemWidth,0);
    }

    /**
     * 设置数据源
     * @param list  数据源
     * @param maxNum 设置默认最大数据值
     */
    public void setList(List<ProgressInfo> list, float itemWidth, float maxNum){
        synchronized (this) {
            if (list == null || list.size() <= 0) {
                mList = mDefaultList;
            } else {
                mList = list;
            }
            barWidth = itemWidth;  //获取柱状的宽度
            barHeight = itemHeight - (2 * borderWidth); //获取柱状的高度 减去边框的高度
            int subSum = 0;
            for (ProgressInfo info : mList) {
                subSum += info.num;
            }
            if (subSum > maxNum) {
                maxNum = subSum;
            }
            if (maxNum == 0) {
                maxNum = 100;
            }
            cellWidth = barWidth / maxNum; //平均每num = 1 时的宽度
            this.itemWidth = itemWidth;
            this.maxNum = maxNum;
            postInvalidate();
        }
    }

    /**
     * 边框宽度
     * @param borderWidth
     */
    public void setBorderWidth(float borderWidth){
        this.borderWidth = borderWidth;
        postInvalidate();
    }


    /**
     * 重新requestLayout时调用
     */
    public void calculate(){
        barWidth = itemWidth;  //获取柱状的宽度
        barHeight = itemHeight; //获取柱状的高度
        int subSum = 0;
        for (ProgressInfo info : mList) {
            subSum += info.num;
        }
        if (subSum > maxNum) {
            maxNum = subSum;
        }
        if (maxNum == 0) {
            maxNum = 100;
        }
        cellWidth = barWidth / maxNum; //平均每num = 1 时的宽度
    }
}

package cn.mdm.masterui.wiget.pathmeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Arrays;


public class PathView extends View {
    //线画笔
    private Paint linePaint;
    //背景线颜色
    private int darkLineColor;
    //前景线颜色
    private int lightLineColor;

    //背景线区间点
    private float[] darkLinePoints;
    //前景线区间点集合
    private float[] lightLinePoints;

    //整条线的区间点集合
    private float[] points;

    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.STROKE);

        darkLineColor = Color.GRAY;
        lightLineColor = Color.GREEN;
    }

    public PathView setPath(Path path){
        initPath(path);
        return this;
    }

    /**
     * 设置背景线颜色
     * @return
     */
    public PathView setDarkLineColor(int color){
        if(color == 0) darkLineColor = Color.GRAY;
        darkLineColor = color;
        return this;
    }

    /**
     * 设置前景线颜色
     * @return
     */
    public PathView setLightLineColor(int color){
        if(color == 0) lightLineColor = Color.GREEN;
        lightLineColor = color;
        return this;
    }


    /**
     * 设置线宽
     * @param lineWidth
     * @return
     */
    public PathView setLineWidth(float lineWidth){
        if(lineWidth < 0) lineWidth = 2;
        linePaint.setStrokeWidth(lineWidth);
        return this;
    }

    /**
     * 设置背景进度
     * @param start 百分比 起始位置 0-1f
     * @param end 百分比 结束位置  0-1f
     * @return
     */
    public PathView setDarkLineProgress(float start,float end){
        setLineProgress(start,end,true);
        return this;
    }

    /**
     * 设置前景进度
     * @param start 百分比 起始位置 0-1f
     * @param end 百分比 结束位置  0-1f
     * @return
     */
    public PathView setLightLineProgress(float start,float end){
        setLineProgress(start,end,false);
        return this;
    }

    /**
     * 设置线的区间
     * @param start 百分比 起始位置 0-1f
     * @param end 百分比 结束位置  0-1f
     */
    private void setLineProgress(float start,float end,boolean isDarkLine){
        if(points == null)return;
        if(isDarkLine){
            //背景线
            darkLinePoints = getRangeValue(start,end);
        }else{
            lightLinePoints = getRangeValue(start,end);
        }
        postInvalidate();
    }


    private void initPath(Path path) {
        if(path == null)return;
        PathMeasure pm = new PathMeasure(path,false);
        points = new float[(int) (pm.getLength() * 2) + 2];
        float[] position = new float[2];
        int index = 0;
        for (int i = 0;i < pm.getLength();i++){
            boolean isSuccess = pm.getPosTan(i,position,null);
            if(!isSuccess) continue;
            points[index] = position[0];
            points[index + 1] = position[1];
            index += 2;
        }
    }

    /**
     * 截取区域的path线段点值
     * @param start 百分比 起始位置 0-1f
     * @param end 百分比 结束位置  0-1f
     * @return
     */
    private float[] getRangeValue(float start,float end){
        if(start >= end)return null;
        int startIndex = (int) (points.length * start);
        int endIndex = (int) (points.length * end);

        //是奇数的话就把他变成偶数，意义在于points中的坐标必须是成对的，
        //只有偶数才能成对
        if(startIndex % 2 != 0){
            //不用怕减到小于0 因为startIndex最小为1才能进入这个条件 因为0是进不来的
            startIndex--;
        }
        if(endIndex % 2 != 0){
            //这里增加1不用怕数组越界，因为下面的copy方法，越界了也只会截取数组最大下标
            endIndex++;
        }
        return Arrays.copyOfRange(points,startIndex,endIndex);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        linePaint.setColor(darkLineColor);
        if(darkLinePoints != null)
            canvas.drawPoints(darkLinePoints,linePaint);
        linePaint.setColor(lightLineColor);
        if(lightLinePoints != null){
            canvas.drawPoints(lightLinePoints,linePaint);
        }
//        linePaint.setColor(lightLineColor);
//        canvas.drawPath(getMapPath(),linePaint);
    }
}

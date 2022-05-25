package cn.mdm.masterui.wiget.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mdm
 * @desc  简单折线图
 * @create 2021/12/15
 */
public class MdmLineChart extends View {

    //坐标轴路径
    private Path mAxisPath;
    //折线路径
    private Path mPath;
    //折线下方背景图路径
    private Path mBgPath;
    //画笔
    private Paint mAxisPaint;
    private Paint mLinePaint;
    private Paint mBgPaint;
    private Paint mPointPaint;
    private TextPaint mTextPaint;


    //整个画布颜色 默认黑色
    private int canvasBgColor = 0xFF000000;
    //坐标轴颜色 默认黄色
    @ColorInt
    private int mAxisColor = 0xFFFFFF00;
    //折线颜色 默认绿色
    private int mLineColor = 0xFF04CF57;
    //默认折线区域颜色 渐进色
    private int[] mRegionColor = {0XAA2AFADF,0xAA4C83FF};

    private int mPointColor = 0xFF3F7F60;
    //数据集大小
    private int mPointLength = 20;
    //数据集
    private List<Float> mData;
    //view的宽高
    private float viewWidth;
    private float viewHeight;
    //坐标箭头的长度
    private float arrowLength = 10;

    //padding
    private float padding = 50;
    private float widthPadding = padding;
    private float heightPadding = padding;

    //图表整体显示区域
    private RectF originRectF;

    //折线图数值最大高度值
    private float maxValue = 0;
    //单位宽高
    private float cellHeight = 1;
    private float cellWidth = maxValue / mPointLength;
    //居中x轴偏移 算出了单位宽度 点的位置是单位宽度的最左边 所以计算单位宽度的中间偏移宽度
    private float dxCellWidth = 0;

    //是否自动计算当前数据中最大值
    private boolean isAutoMaxValue = false;
    //是否画折线的节点
    private boolean isShowCircle = false;
    private float circleRadius = 0f;

    public MdmLineChart(Context context) {
        this(context,null);
    }
    public MdmLineChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public MdmLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mAxisPath = new Path();
        mPath = new Path();
        mBgPath = new Path();
        mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAxisPaint.setStrokeWidth(3);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(3);
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(15);
        mTextPaint.setColor(Color.YELLOW);
        mData = new ArrayList<Float>(mPointLength){{
            add(10f);add(20f);add(50f);add(90f);add(15f);
            add(25f);add(75f);add(40f);add(15f);add(10f);
            add(75f);add(50f);add(90f);add(15f);add(25f);
            add(75f);add(40f);add(25f);add(75f);add(75f);
        }};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY){
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        }else if(widthMode == MeasureSpec.AT_MOST){
            viewWidth = resolveSize(200,widthMeasureSpec);
        }
        if(heightMode == MeasureSpec.EXACTLY){
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        }else{
            viewHeight = resolveSize(300,widthMeasureSpec);
        }
        setMeasuredDimension((int)viewWidth,(int)viewHeight);
        //计算出显示区域的宽高
        viewWidth -= widthPadding*2;
        viewHeight -= heightPadding*2;
        getOriginRectF();
    }
    private void getOriginRectF(){
        originRectF = new RectF(widthPadding,heightPadding,widthPadding+viewWidth,heightPadding + viewHeight);
    }

    private void getMaxLength(){
        if(isAutoMaxValue) {
            if (mData == null || mData.size() == 0) {
                maxValue = 100;
            } else if(maxValue <= 0){
                maxValue = mData.get(0);
                for (float data : mData) {
                    if (maxValue < data) {
                        maxValue = data;
                    }
                }
                maxValue += maxValue * 0.2f;
            }else{
                if(maxValue < mData.get(mData.size()-1)){
                    maxValue = mData.get(mData.size()-1);
                    maxValue += maxValue * 0.2f;
                }
            }
        }else{
            maxValue = 100;
        }
    }
    private void getCellHeight(){
        cellHeight = viewHeight / maxValue;
        cellWidth = viewWidth / mData.size();
        dxCellWidth = cellWidth / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(canvasBgColor);
        //画坐标
        drawAxis(canvas);
        //画折线
        drawLine(canvas);
    }

    //画坐标
    private void drawAxis(Canvas canvas) {
//        canvas.save();
//        mPath.reset();
        mAxisPaint.setColor(mAxisColor);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPath.moveTo(originRectF.left,originRectF.top);
        mAxisPath.lineTo(originRectF.left,originRectF.bottom);
        mAxisPath.lineTo(originRectF.right,originRectF.bottom);

        //画Y轴箭头
        mAxisPath.moveTo(originRectF.left - arrowLength,originRectF.top + arrowLength);
        mAxisPath.lineTo(originRectF.left,originRectF.top);
        mAxisPath.lineTo(originRectF.left+arrowLength,originRectF.top + arrowLength);

        //画X轴箭头
        mAxisPath.moveTo(originRectF.right - arrowLength,originRectF.bottom - arrowLength);
        mAxisPath.lineTo(originRectF.right,originRectF.bottom);
        mAxisPath.lineTo(originRectF.right - arrowLength,originRectF.bottom + arrowLength);

        canvas.drawPath(mAxisPath, mAxisPaint);
//        canvas.restore();
    }

    //画折线
    private void drawLine(Canvas canvas) {
//        canvas.save();
        mPath.reset();
        mBgPath.reset();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setColor(mPointColor);
        getMaxLength();
        getCellHeight();
        //由于背景需要封闭区域，必须从x轴的初始点位置
        mBgPath.moveTo(originRectF.left + dxCellWidth,originRectF.bottom);
        for(int i = 0;i < mData.size();i++){
            if(i == 0){
                //折线不是封闭图形所以不需要从原点开始，而是从第一个高度点开始
                mPath.moveTo(originRectF.left + i * cellWidth + dxCellWidth,originRectF.bottom - mData.get(i) * cellHeight);
            }else{
                mPath.lineTo(originRectF.left + i * cellWidth + dxCellWidth,originRectF.bottom - mData.get(i) * cellHeight);
            }
            //记录背景路径
            mBgPath.lineTo(originRectF.left + i * cellWidth + dxCellWidth,originRectF.bottom - mData.get(i) * cellHeight);
            //画节点
            if(isShowCircle)
            canvas.drawCircle(originRectF.left + i * cellWidth + dxCellWidth,originRectF.bottom - mData.get(i) * cellHeight,circleRadius, mPointPaint);
            //画节点值
            drawValue(canvas,mData.get(i),originRectF.left + i * cellWidth + dxCellWidth,originRectF.bottom - mData.get(i) * cellHeight);
        }
        //最后需要画个垂线到x轴上
        mBgPath.lineTo(originRectF.left + (mData.size()-1) * cellWidth + dxCellWidth,originRectF.bottom);
        mBgPath.close();//关闭图形形成封闭图形
        canvas.drawPath(mPath, mLinePaint);
        //画背景 新建一个画笔专门给bgPath用
        mBgPaint.setStyle(Paint.Style.FILL);
        //横向的渐进色色 左上角 到 右下角
        mBgPaint.setShader(new LinearGradient(originRectF.left,originRectF.bottom,originRectF.left,originRectF.top,mRegionColor,null, Shader.TileMode.CLAMP));
        mBgPaint.setAlpha(0xCC);
        canvas.drawPath(mBgPath, mBgPaint);
//        canvas.restore();
    }

    //画值
    private void drawValue(Canvas canvas, float value,float x,float y) {
        canvas.save();
        //文字写在居中
        StaticLayout layout = new StaticLayout(String.valueOf(value),mTextPaint, (int) (cellWidth)
                , Layout.Alignment.ALIGN_NORMAL,1,0,true);
        float tWidth = mTextPaint.measureText(value+"");
        //平移画布
        canvas.translate(x - tWidth/2,y-layout.getHeight());
        canvas.clipRect(0,0,tWidth,layout.getHeight());
        canvas.drawColor(0xAAFF0000);
        layout.draw(canvas);
        canvas.restore();
    }

    //设置数据
    public MdmLineChart setData(List<Float> list){
        if(list == null)return this;
        mData.clear();
        mData.addAll(list);
        return this;
    }

    //添加数据
    public MdmLineChart addData(float data){
        if(mData == null)mData = new ArrayList<>();
        if(mData.size() >= mPointLength){
            mData.remove(0);
        }
        mData.add(data);
        postInvalidate();
        return this;
    }

    //清除数据
    public MdmLineChart clearData(){
        if(mData != null)mData.clear();
        return this;
    }

    //设置显示列表的个数
    public MdmLineChart setPointLength(int length){
        if(length < 0){
            length = 10;
        }
        mPointLength = length;
        return this;
    }
    //是否自动取最大值
    public MdmLineChart isAutoMaxValue(boolean isAutoMaxVaule){
        this.isAutoMaxValue = isAutoMaxVaule;
        return this;
    }

    //是否画节点，并配置节点的半径
    public MdmLineChart isShowCircle(boolean isShowCircle,float radius){
        this.isShowCircle = isShowCircle;
        this.circleRadius = radius;
        return this;
    }

    //设置背景颜色
    public MdmLineChart setCanvasBg(int color){
        if(color == 0)return this;
        this.canvasBgColor = color;
        return this;
    }

    //设置坐标轴颜色
    public MdmLineChart setAxisColor(int color){
        if(color == 0)return this;
        this.mAxisColor = color;
        return this;
    }

    //设置折线颜色
    public MdmLineChart setLineColor(int color){
        if(color == 0)return this;
        this.mLineColor = color;
        return this;
    }

    //设置折线底部背景颜色
    public MdmLineChart setLineRegionColor(int[] regionColors){
        if(regionColors == null)return this;
        this.mRegionColor = regionColors;
        return this;
    }
}

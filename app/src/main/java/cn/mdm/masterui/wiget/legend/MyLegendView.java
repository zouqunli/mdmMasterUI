package cn.mdm.masterui.wiget.legend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @date 创建时间 2021/9/26
 * @author qlzou
 * @Description  新自定义图例view
 * @Version 1.0
 */
public class MyLegendView extends View {

    private float itemWidth = 200;
    private float itemHeight = 100;

    private Paint rectFPaint;
    private Paint blockPaint;
    private Paint textPaint;
    private RectF rectF;

    //色块边长
    private float colorBlockLength = 10;
    //色块和文字间隙长度
    private float spaceLength = 5;
    //两个色块之间的间隙长度
    private float spaceHLength = 10;
    //两个色块之间的行间隙长度
    private float spaceVLength = 10;
    //文字大小
    private int textSize = 12;

    private int defaultColor = Color.WHITE; //默认黑色


    private List<List<LegendInfo>> mList = new ArrayList<>();

    public MyLegendView(Context context) {
        this(context,null);
    }
    public MyLegendView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MyLegendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void init() {
        //测试用来看边框使用的画笔
        blockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blockPaint.setStyle(Paint.Style.STROKE);
        blockPaint.setColor(Color.WHITE);
        blockPaint.setStrokeWidth(2);
        //初始化画笔
        rectFPaint = new Paint();
        rectFPaint.setAntiAlias(true);
        rectFPaint.setStyle(Paint.Style.FILL);
        textPaint = new Paint();
        textPaint.setColor(defaultColor);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);
//        textPaint.setTypeface(CKLookApplication.MIRCOSOFT_YAHEI_LIGHT);
        //设置矩形
        rectF = new RectF();

//        List<LegendInfo> list = new ArrayList<>();
//        list.add(new LegendInfo(Color.RED,"item1"));
//        list.add(new LegendInfo(Color.GREEN,"item2"));
//        list.add(new LegendInfo(Color.BLUE,"item3"));
//        list.add(new LegendInfo(Color.YELLOW,"item4"));
//        mList = splitList(list,4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //下面这行不能注释否则会崩溃
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY){
            itemWidth = widthSize;
        }
        if(heightMode == MeasureSpec.EXACTLY){
            itemHeight = heightSize;
        }
        //自适应宽度
        if(widthMode == MeasureSpec.AT_MOST){
            //当width设置是wrap_content时
            if(mList != null || mList.size() != 0 || (map != null&&map.size()>0)){
                float width = 0;
                for (Float item:map.values()){
                    width += item + colorBlockLength + spaceLength + spaceHLength;
                }
                widthSize = (int) width;
            }
            setMeasuredDimension(widthSize,heightSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float cellHeight = itemHeight/mList.size();
        float top = 0;
        for(int i = 0 ; i < mList.size();i++){
            List<LegendInfo> list = mList.get(i);
            for (int j = 0; j < list.size();j++ ){
                //rcf是色块和文字的区域大小
                RectF rcf = new RectF();
                rcf.set(0,0,colorBlockLength + spaceLength + map.get(j) ,cellHeight);
                //recrf是整体的位移距离
                rectF.set(rectF.right,top,rectF.right + rcf.width() + spaceHLength,top + rcf.height());
                drawColorText(canvas,list.get(j).color,list.get(j).title,rcf);
            }
            top += cellHeight + spaceVLength;
            rectF.set(0,0,0,0);//每一行都重置位置
        }

    }

    /**
     * 画色块和文字
     * @param canvas
     * @param color
     * @param text
     * @param rcf
     */
    private void drawColorText(Canvas canvas,int color, String text,RectF rcf){
        canvas.save();
        canvas.translate(rectF.left,rectF.top);
        RectF colorBlockRf = new RectF();
        colorBlockRf.set(0,0,colorBlockLength,colorBlockLength);
        if(rcf.height() > colorBlockLength){
            //垂直间隙
            float spaceVer =(rcf.height() - colorBlockLength)/2;
            //为了让色块居中显示
            colorBlockRf.set(0,spaceVer,colorBlockLength,colorBlockLength + spaceVer);
        }
        //画色块
        rectFPaint.setColor(color);
        canvas.drawRect(colorBlockRf,rectFPaint);
//        canvas.drawRect(colorBlockRf,blockPaint);

        //画文字
        RectF textRf = new RectF();
        textRf.set(colorBlockRf.right + spaceLength,0,rcf.right,rcf.bottom);

        //计算baseline
        Paint.FontMetrics fontMetrics=textPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=textRf.centerY()+distance;
        canvas.drawText(text, textRf.left, baseline, textPaint);
//        canvas.drawRect(textRf,blockPaint);
        canvas.restore();
    }


    public MyLegendView setTextSize(int textSize){
        this.textSize = textSize;
        return this;
    }

    public MyLegendView setTextColor(int color){
        try{
            textPaint.setColor(color);
        }catch (Exception e){
            textPaint.setColor(defaultColor);
        }
        return this;
    }

    //设置颜色色块边长
    public MyLegendView setColorBlockLength(float length){
        if(length < 1)length = 10;
        this.colorBlockLength = length;
        return this;
    }

    //设置文字色块间隙长度
    public MyLegendView setSpaceLength(float length){
        if(length < 1)length = 10;
        this.spaceLength = length;
        return this;
    }

    //设置横向整个item的间隙长度
    public MyLegendView setSpaceHLength(float length){
        if(length < 1)length = spaceHLength;
        this.spaceHLength = length;
        return this;
    }

    //设置垂直行的间隙长度
    public MyLegendView setSpaceVLength(float length){
        if(length < 1)length = 5;
        this.spaceHLength = length;
        return this;
    }


    /**
     * 设置
     * @param list
     */
    public MyLegendView setList(List<LegendInfo> list){
        return setList(list,list.size());
    }

    /**
     * 设置数据放在最后
     * @param list
     */
    public MyLegendView setList(List<LegendInfo> list, int cols){
        if (list == null || list.size() <= 0){
            return this;
        }
        if(cols < 1)cols = 2;
        this.mList = splitList(list,cols);
        initData(mList);
        postInvalidate();
        return this;
    }


    private LinkedHashMap<Integer,Float> map;
    /**
     * 初始化数据
     * @param list
     */
    private void initData(List<List<LegendInfo>> list) {
        if(list == null || list.size() == 0){
            return;
        }
        map = new LinkedHashMap<>();
        textPaint.setTextSize(textSize);
        //算出每个item最大宽度
        for(int i = 0; i < list.size();i++){
            for (int j = 0; j < list.get(i).size();j++){
                if(map.get(j) == null || map.get(j) == 0
                        || map.get(j) < getTextWidth(list.get(i).get(j).title,textPaint)){
                    map.put(j,getTextWidth(list.get(i).get(j).title,textPaint));
                }
            }
        }
    }


    //获取文字的宽度
    public static float getTextWidth(String text, Paint paint){
        return paint.measureText(text);
    }

    //获取文字的区域高度
    public static float getTextHeight(String text, Paint paint){
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    public static class LegendInfo {
        public LegendInfo(int color, String title){
            this.color = color;
            this.title = title;
        }
        public int color;
        public String title;
    }

    /**
     * 分割list
     * @param list
     * @param pageSize 每个list的大小 分页
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        List<List<T>> listArray = new ArrayList<List<T>>();
        List<T> subList = null;
        for (int i = 0; i < list.size(); i++) {
            if (i % pageSize == 0) {//每次到达页大小的边界就重新申请一个subList
                subList = new ArrayList<T>();
                listArray.add(subList);
            }
            subList.add(list.get(i));
        }
        return listArray;
    }
}

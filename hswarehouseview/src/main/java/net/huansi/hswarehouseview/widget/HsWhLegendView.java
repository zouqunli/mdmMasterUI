package net.huansi.hswarehouseview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import net.huansi.hswarehouseview.entity.HsWarehouseLegend;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

/**
 * @date 创建时间 2018/3/11
 * @author qlzou
 * @Description  自定义view图示
 * @Version 1.0
 */
public class HsWhLegendView extends View {

    //图例的总宽度
    protected float viewWidth = 100;
    //图例的总高度
    protected float viewHeight = 100;
    //每个图例的图和文字的宽度
    protected float cellWidth;
    //每个图例的高度，也就是每行的高度
    protected float cellHeight;

    //显示图例时的左边距
    protected float showWidthLeftSpace;

    //每个图例的文字和图的比例 默认1:2
    protected float chartWeight = 1;
    protected float textWeight = 2;

    //每个图例的文字和图宽高
    protected float chartWidth;
    protected float textWidth;

    //是否开启自动跟随色块颜色模式
    protected boolean isAutoTextColor = false;


    protected float top_bottom_space;
    protected float left_right_space;

    protected Paint rectFPaint;
    protected Paint textPaint;
    protected RectF rectF;
    protected RectF chartRectF;
    protected RectF textRectF;

    protected List<List<HsWarehouseLegend>> mList = new ArrayList<>();

    public HsWhLegendView(Context context) {
        this(context,null);
    }
    public HsWhLegendView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public HsWhLegendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void init() {
        //初始化画笔
        rectFPaint = new Paint();
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);  //默认黑色
        textPaint.setTextSize(18);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setAntiAlias(true);
        //设置矩形
        rectF = new RectF();
        textRectF = new RectF();
        chartRectF = new RectF();
//        List<HsWarehouseLegend> list = new ArrayList<>();
//        list.add(new HsWarehouseLegend(Color.RED,"item1"));
//        list.add(new HsWarehouseLegend(Color.GREEN,"item2"));
//        list.add(new HsWarehouseLegend(Color.BLUE,"item3"));
//        list.add(new HsWarehouseLegend(Color.YELLOW,"item4"));
//        setList(list);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY){
            viewWidth = widthSize;
        }
        if(heightMode == MeasureSpec.EXACTLY){
            viewHeight = heightSize;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mList == null || mList.size()==0) return;
        initWidth();
        for (int i = 0; i < mList.size(); i++){
            if(mList.get(i) == null)continue;
            for (int j = 0; j < mList.get(i).size(); j++){
                if(mList.get(i).get(j) == null) continue;
                //画矩形
                HsWarehouseLegend info = mList.get(i).get(j);
                rectFPaint.setColor(info.getColor());
                rectF.set(showWidthLeftSpace + cellWidth * j,cellHeight * i,showWidthLeftSpace + (cellWidth * j) + chartWidth,cellHeight * (i + 1));
                chartRectF.set(rectF.left + left_right_space,rectF.top + top_bottom_space,rectF.right - left_right_space,rectF.bottom-top_bottom_space);
                canvas.drawRect(chartRectF,rectFPaint);

                //画标题
                textRectF.set(rectF.right,cellHeight * i,rectF.right + textWidth,cellHeight * (i + 1));
                Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                int baseLineY = (int) (textRectF.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式
                textPaint.setColor(isAutoTextColor?info.getColor():Color.BLACK);
                canvas.drawText(info.getTitle(),textRectF.left,baseLineY,textPaint);
            }
        }




//        for (int i = 0;i < mList.size(); i++){
//            if(mList.get(i) != null){
//                HsWarehouseLegend fistrInfo = mList.get(i).get(0);
//                rectFPaint.setColor(fistrInfo.getColor());
//                rectF.set(cellWidth ,cellHeight,cellWidth/2)*3,(cellHeight/4)*3 + (i*cellHeight));
//                canvas.drawRect(rectF,rectFPaint);
//
//                //画标题
//                RectF titleRect = new RectF();
//                titleRect.set(cellWidth * 2,i*cellHeight,cellWidth * 4,(i+1)*cellHeight);
//                Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
//                float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
//                float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
//                int baseLineY = (int) (titleRect.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式
////                canvas.drawText(fistrInfo.title,titleRect.centerX(),baseLineY,textPaint); //居中
//                canvas.drawText(fistrInfo.getTitle(),titleRect.left,baseLineY,textPaint);
//
//                try {
//                    HsWarehouseLegend secondInfo = mList.get(i).get(1);
//                    rectFPaint.setColor(secondInfo.getColor());
//                    rectF.set((cellWidth*5)-(cellWidth/2) ,(cellHeight/4)+(i*cellHeight),(cellWidth*5) + (cellWidth/2),(cellHeight/4)*3 + (i*cellHeight));
//                    canvas.drawRect(rectF,rectFPaint);
//                    //画标题
//                    RectF secondTitleRect = new RectF();
//                    secondTitleRect.set(cellWidth * 6,i*cellHeight,cellWidth * 8,(i+1)*cellHeight);
//                    Paint.FontMetrics sfontMetrics = textPaint.getFontMetrics();
//                    float stop = sfontMetrics.top;//为基线到字体上边框的距离,即上图中的top
//                    float sbottom = sfontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
//                    int sbaseLineY = (int) (secondTitleRect.centerY() - stop/2 - sbottom/2);//基线中间点的y轴计算公式
//                    canvas.drawText(secondInfo.getTitle(),secondTitleRect.left,sbaseLineY,textPaint);
//                }catch (Exception e){
//
//                }
//            }
//        }
    }

    /**
     * 设置字体大小
     * @param size
     */
    public void setTextSize(int size){
        textPaint.setTextSize(size);
        postInvalidate();
    }

    /**
     * 设置字体颜色
     * @param color
     */
    public void setTextColor(int color){
        textPaint.setColor(color);
        postInvalidate();
    }

    /**
     * 设置字体颜色自动模式 随色块改变而改变
     * @param isAutoTextColor
     */
    public void setAutoTextColor(boolean isAutoTextColor){
        this.isAutoTextColor = isAutoTextColor;
        postInvalidate();
    }

    /**
     * 设置数据
     * @param list
     */
    public void setList(List<HsWarehouseLegend> list){
        setList(list,0);
    }

    /**
     * 设置数据
     * @param list
     */
    public void setList(List<HsWarehouseLegend> list,int legendColNum){
        if (list == null || list.size() <= 0)return;
        this.mList = splitList(list,legendColNum > 0?legendColNum :list.size());
        postInvalidate();
    }


    private void initWidth(){
        cellHeight = viewHeight / mList.size();
        cellWidth = viewWidth / (mList.get(0).size() + 1);
        showWidthLeftSpace = cellWidth / 2;
        chartWidth = (chartWeight/(chartWeight + textWeight)) * cellWidth;
        textWidth = (textWeight/(chartWeight + textWeight)) * cellWidth;
        top_bottom_space = cellHeight / 3;
        left_right_space = chartWidth / 6;
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

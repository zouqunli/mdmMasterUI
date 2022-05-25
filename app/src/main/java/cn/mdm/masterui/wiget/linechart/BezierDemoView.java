package cn.mdm.masterui.wiget.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 贝塞尔曲线 demo
 */
public class BezierDemoView extends View {
    private Paint linePaint;
    private Path path;
    private PointF controlPoint = new PointF();
    private PointF start = new PointF();
    private PointF end = new PointF();
    private float viewWidth;
    private float viewHeight;
    public BezierDemoView(Context context) {
        this(context,null);
    }

    public BezierDemoView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierDemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        path = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = this.getMeasuredWidth();
        viewHeight = this.getMeasuredHeight();
        start.x = viewWidth / 8;
        start.y = viewHeight / 2;
        end.x = viewWidth - start.x;
        end.y = viewHeight / 2;
        controlPoint.x = start.x + (end.x - start.x)/2;
        controlPoint.y = start.y + (end.y - start.y)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.reset();
        linePaint.setColor(Color.BLACK);
        //画起始点和终止点
        canvas.drawCircle(start.x,start.y,5,linePaint);
        canvas.drawCircle(end.x,end.y,5,linePaint);

        //画贝塞尔曲线
        path.moveTo(start.x,start.y);
        path.quadTo( controlPoint.x, controlPoint.y,end.x,end.y);
        linePaint.setColor(Color.BLUE);
        canvas.drawPath(path,linePaint);
        //画控制点
        canvas.drawCircle(controlPoint.x,controlPoint.y,10,linePaint);

        //画起始点和终止点的连线
        linePaint.setColor(Color.BLACK);
        canvas.drawLine(start.x,start.y,controlPoint.x,controlPoint.y,linePaint);
        canvas.drawLine(end.x,end.y,controlPoint.x,controlPoint.y,linePaint);
    }

    //起始点和重点坐标
    private PointF getBezierControl(float x1, float y1, float x2, float y2){
        PointF pointF = new PointF();
        pointF.x = x1 + 0.5f*(x2 - x1);
        pointF.y = y1 + 0.5f*(y2 - y1);
        Log.e("ControlPoint","ox1y1 == " + x1 +","+ y1 +"-ox2y2 == " + x2 +","+ y2+"  //////  x - " + pointF.x + "---" + pointF.y);
        return pointF;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                controlPoint.x = event.getX();
                controlPoint.y = event.getY();
                postInvalidate();
                //防止父view滑动拦截
                getParent().requestDisallowInterceptTouchEvent(true);
                break;

        }
        return true;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        //我们设定多阶的贝塞尔的点
//        //一阶2个点，两阶3个点，3阶4个点这里用了4阶5个点  n阶n+1个点
//        //(0,0)(300,300)(200,700)(500,1200)(700,1200)
//        //实现4阶贝塞尔曲线
//        float[] pointX = {0,300,200,500,700};
//        float[] pointY = {0,300,700,1200,1200};
//
//        //我们取多少个点，点越多线也越平滑,当然计算的时间越长
//        int pointNum = 1000;
//        for(int i = 0; i< pointNum;i++){
//            //取当前点的进度
//            float progress = i / pointNum;
//            //获取x当前进度的x坐标
//            float bezierX = getBezierValue(progress,pointX);
//            float bezierY = getBezierValue(progress,pointY);
//            //画线，点越多距离越小，越接近平滑曲线
//            path.lineTo(bezierX,bezierY);
//        }
//        canvas.drawPath(path,linePaint);
//    }


    //算出多阶贝塞曲线的点 跟系统的2阶3阶的Api算法差不多
    //因为系统没有提供超过3阶贝塞尔的公式，这里有实现方法，可以实现任意阶数
    private float getBezierValue(float t,float... value){
        final int len = value.length;
        for (int i=len - 1 ;i > 0;i--){
            for (int j = 0;j < i ;j++){
                //B(t)=P0+(P1-P0)t=(1-t)P0+tP1,t∈[0,1] 公式的点计算
                value[j] = value[j] + (value[j+1] -value[j])*t;
            }
        }
        //最后算出获取到最后一位的贝塞尔曲线点
        return value[0];
    }
}

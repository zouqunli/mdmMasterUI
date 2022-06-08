package cn.mdm.masterui.wiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * 利用Region类检测任意图形的点击区域
 */
public class ClickRegionView extends View {

    private Region mViewRegion = new Region();
    private Path mPath = new Path();
    private Paint paint = new Paint();
    private Context mContext;
    public ClickRegionView(Context context) {
        this(context,null);
    }

    public ClickRegionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClickRegionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    private void init(Context context) {
        mContext = context;
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        mPath.moveTo(100,100);
        mPath.lineTo(50,100);
        mPath.lineTo(100,120);
        mPath.rLineTo(50,150);
        mPath.rLineTo(150,100);
        mPath.close();
        RectF rectF = new RectF();
        mPath.computeBounds(rectF,true);

        mViewRegion.setPath(mPath,new Region((int)rectF.left,(int)rectF.top,(int)rectF.right,(int)rectF.bottom));
        canvas.drawPath(mPath,paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mViewRegion != null) {
                    if(mViewRegion.contains((int)event.getX(),(int)event.getY())){
                        Toast.makeText(mContext,"已点击 x=" + event.getX() + ",y=" + event.getY(),Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        return true;
    }
}

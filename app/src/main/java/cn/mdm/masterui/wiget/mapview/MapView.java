package cn.mdm.masterui.wiget.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import cn.mdm.masterui.R;

/**
 * @author mdm
 * @desc  简单地图控件
 * @create 2021/12/15
 */
public class MapView extends View {
//    Paint paint = new Paint();

    //底部map图片宽度
    private float picMapWidth = 1190;
    //底部map图片高度
    private float picMapHeight = 666;

    //显示位置的图片宽度
    private float pointWidth = 45;
    //显示位置的图片高度
    private float pointHeight = 48;

    //控件的宽高
    private float viewWidth;
    private float viewHeight;

    //点的矩形
//    private RectF pointRectF;
    //点的集合
    private List<MapBean> mList;

    private TextPaint textPaint;

    private IMapViewListener mListener;
    private Matrix matrix;

    BitmapFactory.Options bfoOptions;
    private Bitmap mapBmp;
    private Bitmap point1Bmp;
    private Bitmap point2Bmp;
    private Bitmap unClickPoint1Bmp;
    private Bitmap unClickPoint2Bmp;
    private Paint bitmapPaint;

    /**
     * 动态效果
     */
    private float postValue = 1f;
    private Paint circlePaint;


    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
//        pointRectF = new RectF();
        matrix = new Matrix();
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(15);
        textPaint.setStrokeWidth(3);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        bitmapPaint=new Paint();
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setDither(true);
        bitmapPaint.setFilterBitmap(true);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.parseColor("#51C467"));


        bfoOptions = new BitmapFactory.Options();
        bfoOptions.inScaled = false;
        mapBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_map,bfoOptions);
        point1Bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_point_db,bfoOptions);
        point2Bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_point_jb,bfoOptions);
        unClickPoint1Bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_point_db_an,bfoOptions);
        unClickPoint2Bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_point_jb_an,bfoOptions);
        mList = new ArrayList<MapBean>(){{
            add(new MapBean("1",new Point(916,85),"王一博  肖战"));
            add(new MapBean("2",new Point(109,242),"洋洋  李现"));
            add(new MapBean("3",new Point(420,440),"王一博  肖战"));
            add(new MapBean("3",new Point(420,440),"洋洋  李现"));
            add(new MapBean("3",new Point(420,440),"王一博  肖战"));
            add(new MapBean("3",new Point(750,85),0,true,"洋洋  李现"));
//            add(new MapBean("4",new Point(426,280),false));
//            add(new MapBean("5",new Point(400,280)));
//
//            add(new MapBean("6",new Point(445,280)));
//            add(new MapBean("7",new Point(465,280)));
//            add(new MapBean("8",new Point(485,280)));
//            add(new MapBean("9",new Point(505,280)));
//            add(new MapBean("10",new Point(575,280)));
        }};
//        mList = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        if(viewWidth == 0 && viewHeight ==0) {
            measure(0,0);
        }
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float xRatio = getXRatio();
        float yRatio = getYRatio();
        //画背景
        matrix.reset();
        matrix.postScale(xRatio,yRatio);
        canvas.drawBitmap(mapBmp,matrix,bitmapPaint);
        if(point1Bmp.getWidth()!=(int)(pointWidth * xRatio)) {
            point1Bmp = Bitmap.createScaledBitmap(point1Bmp, (int) (pointWidth * xRatio), (int) (pointHeight * yRatio), true);
        }
        if(point2Bmp.getWidth() != (int)(pointWidth * xRatio)) {
            point2Bmp = Bitmap.createScaledBitmap(point2Bmp,(int)(pointWidth * xRatio) , (int)(pointHeight * yRatio), true);
        }
        if(unClickPoint1Bmp.getWidth() != (int)(pointWidth * xRatio)) {
            unClickPoint1Bmp = Bitmap.createScaledBitmap(unClickPoint1Bmp,(int)(pointWidth * xRatio) , (int)(pointHeight * yRatio), true);
        }
        if(unClickPoint2Bmp.getWidth() != (int)(pointWidth * xRatio)) {
            unClickPoint2Bmp = Bitmap.createScaledBitmap(unClickPoint2Bmp,(int)(pointWidth * xRatio) , (int)(pointHeight * yRatio), true);
        }
//        int index = 1;
        for(MapBean bean : mList){
            Point point = bean.getPoint();
            RectF pointRectF = new RectF();
            pointRectF.set((point.x - (pointWidth/2))*xRatio
                    ,(point.y - pointHeight )*yRatio
                    ,(point.x - (pointWidth/2))*xRatio + (pointWidth * xRatio)
                    ,(point.y - pointHeight)*yRatio + (pointHeight * yRatio));
            bean.setRectF(pointRectF);
            drawPointBmp(canvas,bean,pointRectF);
//            drawText(canvas,pointRectF,String.valueOf(index++)); //暂时没用传进来的序号
            drawText(canvas,pointRectF,bean); //
        }
        if(postValue > 50){
            postValue = 1f;
            postInvalidateDelayed(1500);
        }else{
            postValue+=1f;
            postInvalidateDelayed(20);
        }
    }

    //画文字居中
    private void drawText(Canvas canvas, RectF rectF, MapBean bean){
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (rectF.centerY() - top/3 - bottom/3);//基线中间点的y轴计算公式
        if(bean.getRoleType() == -1) {
            textPaint.setColor(0xffCC7832);
        } else {
            textPaint.setColor(Color.WHITE);
        }
        canvas.drawText(bean.getTitle(), rectF.centerX(), baseLineY, textPaint);
    }

    /**
     * 画波纹
     * @param canvas
     * @param rectf
     */
    private void drawCircle(Canvas canvas, RectF rectf){
        Point center = new Point();
        center.x = (int) (rectf.right - rectf.width() / 2);
        center.y = (int) (rectf.bottom - rectf.height() / 2);
        circlePaint.setAlpha(255 - (int) (5*postValue));
        canvas.drawCircle(center.x,center.y,postValue,circlePaint);
        Log.i("tag","Alpha ==> " + circlePaint.getAlpha() + "postValue ==> " + postValue);
    }

    /**
     * 画点的
     * @param canvas
     * @param pointRectF
     */
    private void drawPointBmp(Canvas canvas, MapBean bean, RectF pointRectF){
        if(bean.getRoleType() == 0){ // 待办
            if(bean.isFlash()) { //可点击
                canvas.drawBitmap(point1Bmp, pointRectF.left, pointRectF.top, bitmapPaint);
                drawCircle(canvas,pointRectF);
            }
            else  //不可点击
            {
                canvas.drawBitmap(unClickPoint1Bmp,pointRectF.left,pointRectF.top,bitmapPaint);
            }
        }else if(bean.getRoleType() == 1){  //警报
            if(bean.isFlash()) {  //可点击
                canvas.drawBitmap(point2Bmp, pointRectF.left, pointRectF.top, bitmapPaint);
                drawCircle(canvas,pointRectF);
            }
            else  //不可点击
            {
                canvas.drawBitmap(unClickPoint2Bmp,pointRectF.left,pointRectF.top,bitmapPaint);
            }
        }
    }

    /**
     * 设置背景图片的宽高
     * @param width
     * @param height
     * @return
     */
    public MapView setPicMapWH(float width, float height){
        picMapWidth = width;
        picMapHeight = height;
        return this;
    }


    /**
     * 设置显示点击图片的宽高
     */
    public MapView setPicPointWH(float width, float height){
        pointWidth = width;
        pointHeight = height;
        return this;
    }

    /**
     * 设置点的坐标
     * @return
     */
    public MapView setPointLocation(List<MapBean> list){
        this.mList = list;
        return this;
    }

    /**
     * 增加点的坐标
     * @param addList
     */
    public void addPointLocation(List<MapBean> addList){
        if(mList == null) {
            return;
        }
        mList.addAll(addList);
    }


    /**
     * 设置监听事件
     * @param listener
     * @return
     */
    public MapView setListener(IMapViewListener listener){
        this.mListener = listener;
        return this;
    }

    /**
     * 设置地图图片
     * @param drawableId
     * @return
     */
    public MapView setMapPic(int drawableId){
        if (mapBmp != null) {
            mapBmp.recycle();
            mapBmp = null;
        }
        mapBmp = BitmapFactory.decodeResource(getResources(), drawableId,bfoOptions);
        return this;
    }

    /**
     * 设置点击图片
     * @param drawableId
     * @return
     */
    public MapView setPointPic(int drawableId){
        if (point1Bmp != null) {
            point1Bmp.recycle();
            point1Bmp = null;
        }
        point1Bmp = BitmapFactory.decodeResource(getResources(), drawableId,bfoOptions);
        return this;
    }

    /**
     * 设置不能点击的图片
     * @param drawableId
     * @return
     */
    public MapView setUnPointPic(int drawableId){
        if (point2Bmp != null) {
            point2Bmp.recycle();
            point2Bmp = null;
        }
        point2Bmp = BitmapFactory.decodeResource(getResources(), drawableId,bfoOptions);
        return this;
    }

    /**
     * 设置波纹颜色
     * @param color
     * @return
     */
    public MapView setBoWenColor(int color){
        circlePaint.setColor(color);
        return this;
    }


    /**
     * 获取X与原图的大小比例
     * @return
     */
    private float getXRatio(){
        if(picMapWidth == 0) {
            return 0;
        }
        if(viewWidth == 0) {
            viewWidth = getMeasuredWidth();
        }
        return viewWidth/picMapWidth;
    }

    /**
     * 获取Y与原图的大小比例
     * @return
     */
    private float getYRatio(){
        if(picMapHeight == 0) {
            return 0;
        }
        if(viewHeight == 0) {
            viewHeight = getMeasuredHeight();
        }
        return viewHeight/picMapHeight;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                MapBean selectBean = null;
                for (MapBean bean : mList){
                    if(x > bean.getRectF().left && x < bean.getRectF().right &&
                       y > bean.getRectF().top && y < bean.getRectF().bottom){
                        selectBean = bean;
                    }
                }
                if(selectBean != null){
                    if(mListener != null) {
                        mListener.clickItem(selectBean);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mapBmp.recycle();
        mapBmp = null;
        point1Bmp.recycle();
        point1Bmp = null;
        point2Bmp.recycle();
        point2Bmp = null;
        unClickPoint1Bmp.recycle();
        unClickPoint1Bmp = null;
        unClickPoint2Bmp.recycle();
        unClickPoint2Bmp = null;
        System.gc();
    }

    public static class MapBean{
        //附带的对象
        private Object obj;
        //原坐标
        private Point point;
        //修正坐标矩形
        private RectF rectF;

        //是否高光显示 高光就是可点击
        private boolean isFlash = true;

        //0待办  1警报  -1不画显示图形
        private int roleType = 0;

        //标题
        private String title;

        public MapBean(){}
        public MapBean(Object obj, Point point, String title) {
           this(obj,point,-1,false,title);
        }
        public MapBean(Object obj, Point point, int roleType, boolean isFlash) {
            this(obj,point,roleType,isFlash,"");
        }

        public MapBean(Object obj, Point point, int roleType, boolean isFlash, String title){
            this.obj = obj;
            this.point = point;
            this.roleType = roleType;
            this.isFlash = isFlash;
            this.title = title;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public RectF getRectF() {
            return rectF;
        }

        public void setRectF(RectF rectF) {
            this.rectF = rectF;
        }

        public int getRoleType() {
            return roleType;
        }

        public void setRoleType(int roleType) {
            this.roleType = roleType;
        }

        public boolean isFlash() {
            return isFlash;
        }

        public void setFlash(boolean flash) {
            isFlash = flash;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    public interface IMapViewListener{
        void clickItem(MapBean bean);
    }


}

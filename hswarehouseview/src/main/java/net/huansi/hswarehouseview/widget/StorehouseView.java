package net.huansi.hswarehouseview.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.huansi.hswarehouseview.R;
import net.huansi.hswarehouseview.entity.HsWarehouseItemInfo;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * @date 创建时间 2018/3/11
 * @author qlzou
 * @Description  单个库位view
 * @Version 1.0
 */
public class StorehouseView extends LinearLayout {

    public enum WareHouseMode{
        ONLY_TITLE,
        ONLY_SUBTITLE,
        ONLY_PROGRESS, //只有进度条
        BOTH,
    }
    WareHouseMode mode = WareHouseMode.ONLY_TITLE;
    protected LinearLayout mStoreHouseView;
    protected TextView mTvTitle;
    protected TextView mSubTitle;
    protected View mHBar;
    protected MultiProgressHView mMtulProView;
    public StorehouseView(Context context) {
        super(context);
        init(context);
    }

    public StorehouseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StorehouseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        View view = inflate(context, R.layout.view_progress_view_group,this);
        mStoreHouseView = view.findViewById(R.id.storeHouseView);
        mTvTitle = view.findViewById(R.id.tvTitle);
        mSubTitle = view.findViewById(R.id.subTitle);
        mHBar = view.findViewById(R.id.hBar);
        mMtulProView = view.findViewById(R.id.cvView);
        mMtulProView.setBorderWidth(1);

//        mMtulProView.setOneData(new MultiProgressHView.ProgressInfo(Color.RED,90f),-1,90f);
//        mMtulProView.setBorderColor(Color.GREEN).isBorderOver(false).isShowBoarder(true);
//        mMtulProView.setRectRadius(4f).setBorderWidth(3f);
    }

    /**
     * 模式设置一定要在设置标题前，不然不起效果
     * @param mode
     * @return
     */
    public StorehouseView showMode(WareHouseMode mode){
        if(mode == null)mode = WareHouseMode.ONLY_TITLE;
        this.mode = mode;
        switch (mode){
            case ONLY_TITLE:
                mTvTitle.setVisibility(VISIBLE);
                mSubTitle.setVisibility(GONE);
                break;
            case ONLY_SUBTITLE:
                mTvTitle.setVisibility(GONE);
                mHBar.setVisibility(GONE);
                mSubTitle.setVisibility(VISIBLE);
                break;
            case ONLY_PROGRESS:
                mTvTitle.setVisibility(GONE);
                mHBar.setVisibility(GONE);
                mSubTitle.setVisibility(GONE);
                break;
            case BOTH:
                mTvTitle.setVisibility(VISIBLE);
                mSubTitle.setVisibility(VISIBLE);
                break;
        }
        return this;
    }

    //设置分割线的颜色
    public StorehouseView setHBarColor(@ColorInt int color){
        mHBar.setBackgroundColor(color);
        return this;
    }

    //设置分割线的厚度
    public StorehouseView setHBarWidth(float width){
        mHBar.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, (int) width));
        return this;
    }

    //设置标题文字
    public StorehouseView setTitle(String title){
        if(title == null) title = "";
        mTvTitle.setText(title);
        return this;
    }

    //设置副标题文字
    public StorehouseView setSubTitle(String subTitle){
        if(subTitle == null) subTitle ="";
        mSubTitle.setText(subTitle);
        return this;
    }

    //设置标题字体大小
    public StorehouseView setTitleTextSize(float size){
        mTvTitle.setTextSize(size);
        return this;
    }

    //设置副标题字体大小
    public StorehouseView setSubTitleTextSize(float size){
        mSubTitle.setTextSize(size);
        return this;
    }

    //统一设置字体颜色
    public StorehouseView setTextColor(int color){
        setTitleTextColor(color);
        setSubTitleTextColor(color);
        return this;
    }

    //设置标题的颜色
    public StorehouseView setTitleTextColor(int color){
        mTvTitle.setTextColor(color);
        return this;
    }

    //设置进度条上的标题颜色
    public StorehouseView setSubTitleTextColor(int color){
        mSubTitle.setTextColor(color);
        return this;
    }


    //整个view的背景图
    public StorehouseView setStoreBackground(Drawable background){
        mStoreHouseView.setBackground(background);
        return this;
    }

    //设置进度条的边框的宽度
    public StorehouseView setPVBorderWidth(float borderWidth){
        if(borderWidth < 0)borderWidth = 0;
        mMtulProView.setBorderWidth(borderWidth);
        return this;
    }

    //设置进度条的边距
    public StorehouseView setPVMargin(float margin){
        if(margin < 0)margin = 0;
        mMtulProView.setMargin(margin);
        return this;
    }

    public MultiProgressHView getPV(){
        return mMtulProView;
    }

    public TextView getTitle(){
        return mTvTitle;
    }

    public TextView getSubTitle(){
        return mSubTitle;
    }

    /**
     * 设置信息
     * @param info
     */
    public void setInfo(HsWarehouseItemInfo info, float proViewWidth){
        if(info == null){
            setTitle("");
            setSubTitle("");
            mMtulProView.setList(null,proViewWidth); //减的2是左右两边的边框的宽度 左1右1
        }else {
            setTitle(info.getTitle());
            setSubTitle(info.getSubTitle());
            mMtulProView.setList(info.getProgressInfos(),proViewWidth,info.getMaxValue());
        }
    }

    public <T extends MultiProgressHView.IProgressInfo> StorehouseView setDataList(List<T> list,float max){
        if(list == null || list.isEmpty()){
            mMtulProView.setList(null,-1,max);
        }else{
            mMtulProView.setList(list,-1,max);
        }
        return this;
    }

    public <T extends MultiProgressHView.IProgressInfo> StorehouseView setDataInfo(T info,float max){
        if(info == null){
            mMtulProView.setList(null,-1,max);
        } else{
            mMtulProView.setOneData(info,-1,max);
        }
        return this;
    }
}

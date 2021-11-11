package net.huansi.hswarehouseview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.huansi.hswarehouseview.R;
import net.huansi.hswarehouseview.entity.HsWarehouseItemInfo;

import androidx.annotation.Nullable;

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
    protected TextView mTvTitle;
    protected TextView mSubTitle;
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
        mTvTitle = view.findViewById(R.id.tvTitle);
        mSubTitle = view.findViewById(R.id.subTitle);
        mMtulProView = view.findViewById(R.id.cvView);
        mMtulProView.setBorderWidth(1);
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
                mSubTitle.setVisibility(VISIBLE);
                break;
            case ONLY_PROGRESS:
                mTvTitle.setVisibility(GONE);
                mSubTitle.setVisibility(GONE);
                break;
            case BOTH:
                mTvTitle.setVisibility(VISIBLE);
                mSubTitle.setVisibility(VISIBLE);
                break;
        }
        return this;
    }


    public StorehouseView setTitle(String title){
        if(title == null) title = "";
        mTvTitle.setText(title);
        return this;
    }

    public StorehouseView setSubTitle(String subTitle){
        if(subTitle == null) subTitle ="";
        mSubTitle.setText(subTitle);
        return this;
    }

    public StorehouseView setTextColor(int color){
        setTitleTextColor(color);
        setSubTitleTextColor(color);
        return this;
    }

    public StorehouseView setTitleTextColor(int color){
        mTvTitle.setTextColor(color);
        return this;
    }

    public StorehouseView setSubTitleTextColor(int color){
        mSubTitle.setTextColor(color);
        return this;
    }

    public StorehouseView setBorderWidth(float borderWidth){
        if(borderWidth < 0)borderWidth = 0;
        mMtulProView.setBorderWidth(borderWidth);
        return this;
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
}

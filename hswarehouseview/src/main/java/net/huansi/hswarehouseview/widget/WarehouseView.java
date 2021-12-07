package net.huansi.hswarehouseview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import net.huansi.hswarehouseview.R;
import net.huansi.hswarehouseview.adapter.HsWhGeneralAdapter;
import net.huansi.hswarehouseview.adapter.HsWhViewHolder;
import net.huansi.hswarehouseview.entity.HsWarehouseItemInfo;
import net.huansi.hswarehouseview.entity.HsWarehouseLegend;
import net.huansi.hswarehouseview.widget.StorehouseView.WareHouseMode;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;

/**
 * @date 创建时间 2019/8/2
 * @author qlzou
 * @Description  仓库view
 * @Version 1.0
 */
public class WarehouseView extends LinearLayout {

    protected GridView mGvData;
    protected HsWhLegendView mLegendView;
    private int numColumns = 3; //默认3列
    private int numRows = 10; //默10行
    protected WareHouseMode mode = null;

    private HsWhGeneralAdapter<HsWarehouseItemInfo> mAdapter;
    private List<HsWarehouseItemInfo> listGroup = new ArrayList<>();

    public WarehouseView(Context context) {
        super(context);
    }

    public WarehouseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressViewLargeGroup_styleable);
            numColumns = ta.getInt(R.styleable.ProgressViewLargeGroup_styleable_numCols,3);
            numRows = ta.getInt(R.styleable.ProgressViewLargeGroup_styleable_numRows,10);
        }
        init(context);
    }

    public WarehouseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        initView(context);
        mGvData.setNumColumns(numColumns); //设置列数
        mAdapter = new HsWhGeneralAdapter<HsWarehouseItemInfo>(context,listGroup,R.layout.item_ware_house) {
            @Override
            public void initItemView(HsWhViewHolder hsWhViewHolder, HsWarehouseItemInfo info, int position) {
                if(info != null){
                    if(numRows > 0) {
                        ViewGroup.LayoutParams param = hsWhViewHolder.getConvertView().getLayoutParams();
                        param.height = Math.round(mGvData.getMeasuredHeight() * 1.0f / numRows);
                        hsWhViewHolder.getConvertView().setLayoutParams(param);
                    }
                    StorehouseView storehouseView = hsWhViewHolder.getView(R.id.cvgView);
                    storehouseView.showMode(mode).setTextColor(getColor(info.getTextColor()))
                            .setInfo(info,mGvData.getMeasuredWidth()/numColumns);
                }
            }
        };
        mGvData.setAdapter(mAdapter);
    }

    private void initView(Context context) {
        View view = inflate(context, R.layout.view_progress_view_large_group,this);
        mGvData = view.findViewById(R.id.gvData);
        mLegendView = view.findViewById(R.id.hsLegend);
    }

    private int getColor(String color){
        try{
            return Color.parseColor(color);
        }catch (Exception e){
            return Color.BLACK;
        }
    }

    /**
     * 获取列数
     * @return 当前类
     */
    public float getCols(){
        return numColumns;
    }

    /**
     * 获取行数
     * @return 当前类
     */
    public float getRows(){
        return numRows;
    }


    /**
     * 设置数据
     * @param list 库位列表数据
     * @return 当前类
     */
    public WarehouseView setList(List<HsWarehouseItemInfo> list){
        listGroup = list;
        mAdapter.setList(listGroup);
        return this;
    }

    /**
     * 设置图例数据
     * @param legendList 图例数据
     * @return 当前类
     */
    public WarehouseView setLegendList(List<HsWarehouseLegend> legendList){
        mLegendView.setList(legendList);
        return this;
    }

    /**
     * 设置图例数据
     * @param legendList 图例数据
     * @param legendColNum 显示图例列数
     * @return 当前类
     */
    public WarehouseView setLegendList(List<HsWarehouseLegend> legendList,int legendColNum){
        mLegendView.setList(legendList,legendColNum);
        return this;
    }

    /**
     * 设置列和行
     * @param cols 列数
     * @param rows 行数
     * @return 当前类
     */
    public WarehouseView setColAndRow(int cols,int rows){
        if(cols <= 0)cols = 3;
        if(rows == 0)rows = 10;
        this.numRows = rows;
        this.numColumns = cols;
        if(mGvData == null) return this;
        mGvData.setNumColumns(numColumns);
        if(mAdapter == null) return this;
        mGvData.setAdapter(mAdapter);
        requestLayout();
        return this;
    }

    /**
     * 设置列数
     * @param cols  列数
     * @return 当前类
     */
    public WarehouseView setCols(int cols){
        if(cols <= 0)cols = 3;
        this.numColumns = cols;
        if(mGvData == null)return this;
        mGvData.setNumColumns(cols);
        if(mAdapter == null)return this;
        mGvData.setAdapter(mAdapter);
        requestLayout();
        return this;
    }

    /**
     * 设置行数
     * @param rows 行数
     * @return 当前类
     */
    public WarehouseView setRows(int rows){
        if(rows == 0)rows = 10;
        this.numRows = rows;
        if(mAdapter == null)return this;
        mGvData.setAdapter(mAdapter);
        requestLayout();
        return this;
    }

    /**
     * 设置图例权重
     * @param weight 图例权重值
     * @return 当前类
     */
    public WarehouseView setLegendWeight(float weight){
        if(weight <= 0){
            mLegendView.setVisibility(View.GONE);
        }
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,weight);
        mLegendView.setLayoutParams(params);
        requestLayout();
        return this;
    }

    /**
     * 设置仓库列表权重
     * @param weight 库位列表权重值
     * @return 当前类
     */
    public WarehouseView setWarehouseWeight(float weight){
        if(weight <= 0){
            mGvData.setVisibility(View.GONE);
            requestLayout();
            return this;
        }
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,weight);
        mGvData.setLayoutParams(params);
        requestLayout();
        return this;
    }

    /**
     * 设置仓库和图例权重
     * @param legendW  图例权重
     * @param warehouseW 库位权重
     * @return 当前类
     */
    public WarehouseView setLegendAndWarehouseWeight(float legendW,float warehouseW){
        setLegendWeight(legendW);
        setWarehouseWeight(warehouseW);
        return this;
    }

    /**
     * 设置图例文字统一颜色
     * @param color 颜色
     * @return 当前类
     */
    public WarehouseView setLegendTextColor(int color){
        mLegendView.setTextColor(color);
        requestLayout();
        return this;
    }

    /**
     * 设置图例文本统一字体大小
     * @param size 文字大小
     * @return 当前类
     */
    public WarehouseView setLegendTextSize(int size){
        mLegendView.setTextSize(size);
        requestLayout();
        return this;
    }

    /**
     * 设置自动获取颜色
     * @param isAutColor 是否
     * @return 当前类
     */
    public WarehouseView setLegendAutoColor(boolean isAutColor){
        mLegendView.setAutoTextColor(isAutColor);
        requestLayout();
        return this;
    }

    /**
     * 设置模式
     * @param mode 模式
     * @return 当前类
     */
    public WarehouseView setMode(WareHouseMode mode){
        this.mode = mode;
        requestLayout();
        return this;
    }
}
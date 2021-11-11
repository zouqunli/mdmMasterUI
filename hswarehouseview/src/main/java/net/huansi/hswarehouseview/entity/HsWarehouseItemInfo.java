package net.huansi.hswarehouseview.entity;

import android.graphics.Color;

import net.huansi.hswarehouseview.widget.MultiProgressHView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @date 创建时间 2019/8/2
 * @author qlzou
 * @Description  每个库位的信息
 * @Version 1.0
 */
public class HsWarehouseItemInfo implements Serializable {

    //标题
    protected String title;

    protected String subTitle;

    protected String textColor = "#000000";
    //颜色
    protected List<Integer> colors;

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    //颜色对应的值
    protected List<Float> values;

    //进度最大值
    protected float maxValue = 0f;

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    protected List<MultiProgressHView.ProgressInfo> progressInfos;

    public List<MultiProgressHView.ProgressInfo> getProgressInfos() {
        if(progressInfos == null){
            progressInfos = new ArrayList<>();
            parseList();
        }
        return progressInfos;
    }
    /**
     * 格式化数据
     */
    public void parseList(){
        progressInfos.clear();
        if(colors == null && values == null) return;
        int length = 0;
        if(colors.size() >= values.size())length = values.size();
        else length = colors.size();
        for(int i = 0 ; i < length ; i++){
            progressInfos.add(getProgressInfo(colors.get(i),values.get(i)));
        }
    }

    private MultiProgressHView.ProgressInfo getProgressInfo(int color, float num){
        try{
            return new MultiProgressHView.ProgressInfo(color,num);
        }catch (Exception e){
            return new MultiProgressHView.ProgressInfo(Color.parseColor("#000000"),num);
        }
    }
}
package net.huansi.hswarehouseview.entity;

/**
 * @date 创建时间 2019/8/2
 * @author qlzou
 * @Description  图例信息
 * @Version 1.0
 */
public class HsWarehouseLegend {

    public HsWarehouseLegend(int color,String title){
        this.color = color;
        this.title = title;
    }
    protected String title;
    protected int color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

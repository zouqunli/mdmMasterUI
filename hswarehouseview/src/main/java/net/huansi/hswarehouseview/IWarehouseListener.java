package net.huansi.hswarehouseview;

import net.huansi.hswarehouseview.entity.HsWarehouseItemInfo;
import net.huansi.hswarehouseview.widget.StorehouseView;

public interface IWarehouseListener {
    //配置库位view
    void configStorehouseView(StorehouseView sView, HsWarehouseItemInfo info);
}

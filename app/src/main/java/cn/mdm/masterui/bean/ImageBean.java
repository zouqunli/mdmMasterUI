package cn.mdm.masterui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageBean implements Serializable {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    //测试
    public static List<ImageBean> getImageList(){
        List<ImageBean> list = new ArrayList<>();
        for (int i = 0 ;i < 5;i++){
            ImageBean bean = new ImageBean();
            bean.imageUrl = imgs[i%imgs.length];
            list.add(bean);
        }
        return list;
    }

    private static transient String[] imgs = {
            "https://img2.baidu.com/it/u=4084621093,2971972319&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2Fe5%2Fee%2F39%2Fe5ee394171595fe762878968bf537f0c.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658394332&t=268227024deddaae6c577208c96b0291",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2F1114%2F022221105922%2F210222105922-7-1200.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658394332&t=92075aeb9c946e39e7befc30a9122c74",
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fup.enterdesk.com%2Fedpic%2Ff4%2F6c%2F1c%2Ff46c1c4d5dbee45fade6a85de81edba9.jpg&refer=http%3A%2F%2Fup.enterdesk.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1658394332&t=35a305416190ceb183ee40233237633a",
    };
}

package cn.mdm.masterui;

import static net.huansi.hswarehouseview.widget.StorehouseView.WareHouseMode.ONLY_SUBTITLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import net.huansi.hswarehouseview.entity.HsWarehouseItemInfo;
import net.huansi.hswarehouseview.widget.MultiProgressHView;

import java.util.ArrayList;
import java.util.List;

import cn.mdm.masterui.adapter.GeneralAdapter;
import cn.mdm.masterui.adapter.ViewHolder;
import cn.mdm.masterui.bean.BaseBean;
import cn.mdm.masterui.bean.ImageBean;
import cn.mdm.masterui.databinding.ActivityMainBinding;
import cn.mdm.masterui.wiget.linechart.LineChartActivity;
import cn.mdm.masterui.wiget.nettv.NetTvActivity;
import cn.mdm.masterui.wiget.step.StepActivity;
import cn.mdm.masterui.wiget.step.StepperView;
import cn.mdm.masterui.wiget.pathmeasure.PathMeasureAct;

public class MainActivity extends AppCompatActivity {

    private List<BaseBean> btnList = new ArrayList<BaseBean>(){{
        add(new BaseBean("PathMeasure测试",1));
        add(new BaseBean("editSpinner测试",2));
        add(new BaseBean("仓库控件测试",3));
        add(new BaseBean("新图例控件测试",4));
        add(new BaseBean("自定义圆形进度条测试",5));
        add(new BaseBean("NetTextView加载Html测试",6));
        add(new BaseBean("自定义圆形进度条测试",7));
        add(new BaseBean("步进器测试",8));
        add(new BaseBean("自定义动态折线图",9));
    }};
    private GeneralAdapter<BaseBean> adapter;
    private ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        adapter = new GeneralAdapter<BaseBean>(this,btnList,R.layout.item_btn) {
            @Override
            public void initItemView(ViewHolder viewHolder, BaseBean info, int position) {
                if(info == null)return;
                Button btn = viewHolder.getView(R.id.btn);
                btn.setText(info.getName());
                btn.setOnClickListener(v -> switchPage(info.getId()));
            }
        };
        mBinding.lvBtn.setAdapter(adapter);
        mBinding.imgHeader.setOnClickListener(v -> {
            mBinding.drawerLayout.openDrawer(Gravity.LEFT);
        });

//        mBinding.progress.setOneData(new MultiProgressHView.ProgressInfo(Color.RED,90f),-1,90f);
//        mBinding.progress.setBorderColor(Color.GREEN).isBorderOver(false).isShowBoarder(true);
//        mBinding.progress.setRectRadius(4f).setBorderWidth(3f);

        mBinding.sv.showMode(ONLY_SUBTITLE).setSubTitle("hehahahh").getPV().setRectRadius(5f).setOneData(new MultiProgressHView.ProgressInfo(Color.RED,90),-1,100);
        useBanner();
    }

    public void useBanner() {
        //—————————————————————————如果你想偷懒，而又只是图片轮播————————————————————————
        mBinding.banner.setAdapter(new BannerImageAdapter<ImageBean>(ImageBean.getImageList()) {
                    @Override
                    public void onBindView(BannerImageHolder holder, ImageBean data, int position, int size) {
                        //图片加载自己实现
                        Glide.with(holder.itemView)
                                .load(data.getImageUrl())
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                                .into(holder.imageView);
                    }
                })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .setIndicator(new CircleIndicator(this),false) //false是不显示指示器
                .setUserInputEnabled(false) //禁止滑动
                .isAutoLoop(false)
                .setCurrentItem(2);
        //更多使用方法仔细阅读文档，或者查看demo

        mBinding.btnNext.setOnClickListener(v -> {
            mBinding.banner.setCurrentItem((mBinding.banner.getCurrentItem() + 1)%mBinding.banner.getItemCount());
        });
    }

    private void switchPage(int index){
        switch (index){
            case 1:
                startActivity(new Intent(this, PathMeasureAct.class));
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                startActivity(new Intent(this, NetTvActivity.class));
                break;
            case 7:
                break;
            case 8:
                startActivity(new Intent(this, StepActivity.class));
                break;
            case 9:
                startActivity(new Intent(this, LineChartActivity.class));
                break;
            default:
                break;
        }
    }
}
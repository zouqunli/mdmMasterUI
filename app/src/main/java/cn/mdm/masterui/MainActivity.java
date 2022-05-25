package cn.mdm.masterui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.mdm.masterui.adapter.GeneralAdapter;
import cn.mdm.masterui.adapter.ViewHolder;
import cn.mdm.masterui.bean.BaseBean;
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
package cn.mdm.masterui.wiget.linechart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.mdm.masterui.R;
import cn.mdm.masterui.databinding.ActivityLineChartBinding;

/**
 * 贝塞尔学习测试页
 */
public class LineChartActivity extends AppCompatActivity {


    private ActivityLineChartBinding mBinding;
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_line_chart);

        mBinding.lineChart.clearData()
                .setPointLength(20)
                .isAutoMaxValue(true)
                .isShowCircle(true,5f)
                .setLineRegionColor(new int[]{0xFFffadad,0xFFffd6a5,0xFFfdffb6,0xFFcaffbf,0xFF9bf6ff,0xFFa0c4ff})
                .setLineColor(Color.BLUE)
                .setAxisColor(Color.YELLOW)
                .setCanvasBg(Color.BLACK);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mBinding.lineChart.addData((int)(new Random().nextFloat() * 500f));
                index++;
                if(index > 10) this.cancel();
            }
        },0,500);

        mBinding.lineBezierChart.clearData()
                .setPointLength(20)
                .isAutoMaxValue(true)
                .isShowCircle(true,5f)
                .setLineRegionColor(new int[]{0xFFffadad,0xFFffd6a5,0xFFfdffb6,0xFFcaffbf,0xFF9bf6ff,0xFFa0c4ff})
                .setLineColor(Color.BLUE)
                .setAxisColor(Color.YELLOW)
                .setCanvasBg(Color.BLACK);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mBinding.lineBezierChart.addData((int)(new Random().nextFloat() * 500f));
                index++;
                if(index > 10) this.cancel();
            }
        },0,500);
    }
}
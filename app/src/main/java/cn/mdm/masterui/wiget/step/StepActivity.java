package cn.mdm.masterui.wiget.step;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import cn.mdm.masterui.R;
import cn.mdm.masterui.databinding.ActivityStepBinding;

public class StepActivity extends AppCompatActivity {


    private ActivityStepBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_step);
        mBinding.first.setOnClickListener(v -> mBinding.stv.stepSet(StepperView.Step.FIRST));
        mBinding.next.setOnClickListener(v -> mBinding.stv.stepSet(StepperView.Step.NEXT));
        mBinding.pre.setOnClickListener(v -> mBinding.stv.stepSet(StepperView.Step.PREV));
        mBinding.setFive.setOnClickListener(v -> mBinding.stv.stepSet(StepperView.Step.SET,5));
    }
}
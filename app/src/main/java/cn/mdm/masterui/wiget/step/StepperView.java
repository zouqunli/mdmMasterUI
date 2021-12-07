package cn.mdm.masterui.wiget.step;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import lombok.experimental.var;

/**
 * @author qlZou
 * @desc  步进器View
 * @create 2021/12/3
 */
public class StepperView extends LinearLayout {

    public enum Step{
        FIRST,PREV,NEXT,CURRENT,SET
    }
    List<Checkable> childView = new ArrayList<>();
    private StepListener listener;
    public StepperView(Context context) {
        super(context);
    }

    public StepperView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public interface StepListener{
        public void onStep(int stepIndex);
    }

    public void setListener(StepListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        childView.clear();
        addCheckChildView(this);
    }

    public void addCheckChildView(ViewGroup viewGroup){
        if(viewGroup == null) return;
        for(int i = 0 ; i < viewGroup.getChildCount();i++){
            View view = viewGroup.getChildAt(i);
            if (view instanceof Checkable){
                childView.add((Checkable) view);
            }else if(view instanceof ViewGroup){
                addCheckChildView((ViewGroup) view);
            }
        }
    }
    //设置步骤
    public int stepSet(Step step){
        return stepSet(step,-1,true);
    }

    //设置步骤
    public int stepSet(Step step,int stepIndex){
        return stepSet(step,stepIndex,true);
    }
    //设置步骤
    public int stepSet(Step step,boolean isEnable){
        return stepSet(step,-1,isEnable);
    }
    //设置步骤
    public int stepSet(Step step,int stepIndex,boolean isEnable){
        int index = -1;
        stepIndex -= 1; //因为是从零开始的，所以想要用户设置到5其实就是设置到0..4
        for (Checkable checkable:childView) if (checkable.isChecked()){index++;}
        switch (step){
            case FIRST:
                index = 0;
                break;
            case PREV:
                if(index > 0)index--;
                break;
            case NEXT:
                index++;
                if(index >= childView.size())index = childView.size()-1;
                break;
            case SET:
                if(stepIndex<0) index = 0;
                else if(stepIndex>= childView.size()) index = 0;
                else index = stepIndex;
                break;
            case CURRENT: break;
        }
        showStep(index,isEnable);
        if(listener != null)listener.onStep(index);
        return index;
    }

    private void showStep(int index,boolean isEnable){
        if(!isEnable)return;
        for (Checkable checkable:childView) checkable.setChecked(false);
        for (int i = 0; i <= index;i++){
            childView.get(i).setChecked(true);
        }
    }
}

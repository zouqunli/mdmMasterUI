package cn.mdm.masterui.wiget.pathmeasure;

import android.graphics.Path;
import android.os.Bundle;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import cn.mdm.masterui.R;
import cn.mdm.masterui.wiget.TestView;

public class PathMeasureAct extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    TestView pathView;
    private float dStart;
    private float dEnd;
    private float lStart;
    private float lEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pathmeasure);

        pathView = findViewById(R.id.pathView);

        SeekBar darkStart = findViewById(R.id.darkStart);
        SeekBar darkEnd = findViewById(R.id.darkEnd);
        SeekBar lightStart = findViewById(R.id.lightStart);
        SeekBar lightEnd = findViewById(R.id.lightEnd);


        darkStart.setOnSeekBarChangeListener(this);
        darkEnd.setOnSeekBarChangeListener(this);
        lightStart.setOnSeekBarChangeListener(this);
        lightEnd.setOnSeekBarChangeListener(this);


        pathView.setPath(getMapPath());

        int[] nums = {1,4,2,7};
        System.out.print(countPairs(nums,2,6));
    }

    private Path getMapPath(){
        Path path = new Path();
        path.moveTo(100,0);
        path.rLineTo(-25,25);
        path.rLineTo(0,25);
        path.rLineTo(25,25);
        path.rLineTo(-25,25);
        path.rLineTo(0,25);
        path.rLineTo(25,25);
        path.rLineTo(-25,25);
        path.rLineTo(0,25);
        path.rLineTo(25,25);
        path.rLineTo(-25,25);
        path.rLineTo(0,25);
        path.rLineTo(25,25);
        path.rLineTo(-25,25);
        path.rLineTo(0,25);
        path.rLineTo(25,25);
        path.rLineTo(-25,25);
        path.rLineTo(0,25);
        path.rLineTo(25,25);
        path.rLineTo(-25,25);
        path.rLineTo(0,25);
        path.rLineTo(25,25);
        return path;
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.darkStart:
                dStart = progress/100f;
                break;
            case R.id.darkEnd:
                dEnd = progress/100f;
                break;
            case R.id.lightStart:
                lStart = progress/100f;
                break;
            case R.id.lightEnd:
                lEnd = progress/100f;
                break;
        }
        pathView.setDarkLineProgress(dStart,dEnd)
                .setLightLineProgress(lStart,lEnd);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public static int countPairs(int[] nums, int low, int high) {
        int sum = 0;
        StringBuffer sb = new StringBuffer();
        for(int i = 0 ;i < nums.length;i++){
            for(int j = i+1; j < nums.length ;j++){
                int temp = nums[i] ^ nums[j];
                if(low <= temp && temp <= high){
                    sb.append("("+i+","+j+"):nums["+i+"] XOR nums["+j+"] = " + temp + "\n");
                    sum++;
                }
            }
        }
        System.out.print(sb.toString());
        return sum;
    }
}
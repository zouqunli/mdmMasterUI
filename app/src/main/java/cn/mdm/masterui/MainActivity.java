package cn.mdm.masterui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Path;
import android.os.Bundle;
import android.widget.SeekBar;

import cn.mdm.masterui.wiget.TestView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    TestView testView;
    private float dStart;
    private float dEnd;
    private float lStart;
    private float lEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testView = findViewById(R.id.testView);


        SeekBar darkStart = findViewById(R.id.darkStart);
        SeekBar darkEnd = findViewById(R.id.darkEnd);
        SeekBar lightStart = findViewById(R.id.lightStart);
        SeekBar lightEnd = findViewById(R.id.lightEnd);


        darkStart.setOnSeekBarChangeListener(this);
        darkEnd.setOnSeekBarChangeListener(this);
        lightStart.setOnSeekBarChangeListener(this);
        lightEnd.setOnSeekBarChangeListener(this);


        testView.setPath(getMapPath());
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
        testView.setDarkLineProgress(dStart,dEnd)
                .setLightLineProgress(lStart,lEnd);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
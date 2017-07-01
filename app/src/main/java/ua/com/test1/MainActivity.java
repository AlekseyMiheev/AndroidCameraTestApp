package ua.com.test1;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import ua.com.test1.presenter.CameraPresenter;
import ua.com.test1.view.CameraView;
import ua.com.test1.view.IView;
import ua.com.test1.view.ObjectView;

public class MainActivity extends AppCompatActivity implements IView {

    private FrameLayout preview;
    private CameraPresenter presenter;
    private ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        preview = (FrameLayout) findViewById(R.id.preview);
        preview.setDrawingCacheEnabled(true);
        presenter = new CameraPresenter(this);
        presenter.loadCamera();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.savePicture();
            }
        });
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setCameraView(CameraView cameraView) {
        preview.addView(cameraView);
    }

    @Override
    public void setObjectView(ObjectView objectView) {
        preview.addView(objectView, 0);
    }

    @Override
    public void setScaleGestureDetector(ObjectView objectView) {
        scaleGestureDetector = new ScaleGestureDetector(getContext(), objectView);

        preview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public FrameLayout getRoot() {
        return preview;
    }
}

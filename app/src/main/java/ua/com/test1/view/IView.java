package ua.com.test1.view;

import android.content.Context;
import android.widget.FrameLayout;

/**
 * Created by Aleksey on 30.06.2017.
 */

public interface IView {

    Context getContext();
    void setCameraView(CameraView cameraView);
    void setObjectView(ObjectView objectView);
    void setScaleGestureDetector(ObjectView objectView);
    FrameLayout getRoot();
}

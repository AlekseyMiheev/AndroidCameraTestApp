package ua.com.test1.view;

import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

import ua.com.test1.CameraHolder;

/**
 * Created by Aleksey on 30.06.2017.
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private int cameraId = 0;

    private SurfaceHolder holder;
    private Camera camera;
    private Context context;

    public CameraView(Context context) {
        super(context);
        this.context = context;
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setupCamera();
    }

    private void setupCamera() {
        CameraHolder holder = new CameraHolder();
        holder.createCamera();
        holder.setupCameraParams();
        camera = holder.getCamera();
        cameraId = holder.getCameraId();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            setCameraOrientation(cameraId, camera);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setCameraOrientation(cameraId, camera);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.release();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCameraOrientation(int cameraId, android.hardware.Camera camera) {
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        try {
            Camera.Parameters params = camera.getParameters();
            camera.setDisplayOrientation(result);
            params.setRotation(result);
            camera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package ua.com.test1;

import android.graphics.ImageFormat;
import android.hardware.Camera;

/**
 * Created by Aleksey on 01.07.2017.
 */

public class CameraHolder {

    private Camera camera;
    private Camera.Parameters parameters;
    private int cameraId = -1;

    public void createCamera() {
        cameraId = getAvailableCameraId();
        if (cameraId > -1) {
            camera = Camera.open(cameraId);
            parameters = camera.getParameters();
        }
    }

    private int getAvailableCameraId() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public void setupCameraParams() {
         /* Choose best resolution
       List<Camera.Size> supportedSizes = params.getSupportedPictureSizes();
        int w = 0, h = 0;
        for (Camera.Size size : supportedSizes) {
            if (size.width > w || size.height > h) {
                w = size.width;
                h = size.height;
            }
        }*/
        int w = camera.getParameters().getPreviewSize().width;
        int h = camera.getParameters().getPreviewSize().height;

        parameters.setPictureSize(w, h);

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);

        if (parameters.isAutoWhiteBalanceLockSupported())
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);

        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setJpegQuality(100);

        camera.setParameters(parameters);
    }

    public Camera getCamera() {
        return camera;
    }

    public int getCameraId() {
        return cameraId;
    }

}

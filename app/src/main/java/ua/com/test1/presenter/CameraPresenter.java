package ua.com.test1.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ua.com.test1.R;
import ua.com.test1.view.CameraView;
import ua.com.test1.view.IView;
import ua.com.test1.view.ObjectView;

/**
 * Created by Aleksey on 30.06.2017.
 */

public class CameraPresenter implements ICameraPresenter {

    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/Capture/";

    private IView view;
    private CameraView cameraView;
    private ObjectView objectView;

    public CameraPresenter(IView view) {
        this.view = view;
        cameraView = new CameraView(view.getContext());
        objectView = new ObjectView(view.getContext());
        objectView.setObject(R.drawable.sample);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap resultImage = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            Bitmap tmp =  Bitmap.createBitmap(resultImage.getWidth(), resultImage.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(tmp);
            canvas.drawBitmap(resultImage, 0, 0, null);
            canvas.drawBitmap(objectView.getObject(), objectView.getDrawMatrix(), null);

            File myDir = new File(IMAGE_PATH);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageName = "Image_" + timeStamp + ".png";
            File file = new File (myDir, imageName);

            OutputStream fOutputStream;
            try {
                fOutputStream = new FileOutputStream(file);

                tmp.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

                fOutputStream.flush();
                fOutputStream.close();
            } catch (FileNotFoundException e) {
                return;
            } catch (IOException e) {
                return;
            }
            camera.startPreview();
        }
    };

    @Override
    public void savePicture() {
        cameraView.getCamera().takePicture(null, null, mPicture);
    }

    @Override
    public void loadCamera() {
        view.setObjectView(objectView);
        view.setCameraView(cameraView);
        view.setScaleGestureDetector(objectView);
    }

    @Override
    public void releaseCamera() {

    }
}

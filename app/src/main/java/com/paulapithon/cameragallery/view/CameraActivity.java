package com.paulapithon.cameragallery.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.paulapithon.cameragallery.R;
import com.paulapithon.cameragallery.util.Constants;
import com.paulapithon.cameragallery.util.FolderManager;
import com.paulapithon.cameragallery.util.view.ImageSurfaceView;
import com.paulapithon.cameragallery.util.view.SquareImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.camera_preview)
    public FrameLayout cameraPreviewLayout;
    @BindView(R.id.captured_image)
    public SquareImageView capturedImageHolder;

    private Camera mCamera;
    private int currentIndex;
    private boolean isCameraConnected = false;
    private Camera.Parameters mCameraParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        //Defines image preview as latest image taken
        currentIndex = FolderManager.getListSize() - 1;
        Bitmap bitmap = FolderManager.getBitmapByIndex(currentIndex);
        if(bitmap != null) { capturedImageHolder.setImageBitmap(bitmap); }

        checkDeviceCamera();
        ImageSurfaceView mImageSurfaceView = new ImageSurfaceView(CameraActivity.this, mCamera);
        cameraPreviewLayout.addView(mImageSurfaceView);
    }

    @OnClick(R.id.snap_picture)
    public void onSnapClick () {
        mCamera.takePicture(null, null, pictureCallback);
    }

    @OnClick(R.id.captured_image_button)
    public void onCapturedImageClick () {
        if (currentIndex >= 0) {
            Intent intent = new Intent(CameraActivity.this, DetailsActivity.class);
            intent.putExtra(Constants.INDEX_IMAGE, currentIndex);
            startActivity(intent);
            finish();
        }
    }
    
    private void checkDeviceCamera(){
        Camera camera = null;

        try {
            camera = Camera.open(0);
            if(camera != null){
                isCameraConnected = true;
            }
        } catch (RuntimeException e){ isCameraConnected = false; }

        if (isCameraConnected){
            mCamera = camera;
            setCameraParameters();
        } else {
            Toast.makeText(this, "Not possible to open camera.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setCameraParameters () {

        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        setCameraDisplayOrientation(display);

        mCameraParameters = mCamera.getParameters();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        setBestPreviewSize(metrics.widthPixels, metrics.heightPixels);
        setBestPictureSize();

        mCamera.setParameters(mCameraParameters);
    }

    private void setBestPreviewSize(int width, int height){

        float EMPIRICAL_TOLERANCE = 0.4f;
        Camera.Size bestPreviewSize = null;

        float ratio = Math.max(width, height) / Math.min(width, height);
        float bestArea = 0;

        float previewRatio;
        float previewArea;

        for(Camera.Size previewSize : mCameraParameters.getSupportedPreviewSizes()){
            previewRatio = (float) previewSize.width / previewSize.height;
            previewArea = previewSize.width * previewSize.height;

            if ((width == previewSize.height && height == previewSize.width)){
                bestPreviewSize = previewSize;
                break;
            }else if(Math.abs(previewRatio - ratio) <= EMPIRICAL_TOLERANCE && previewArea >= bestArea){
                bestArea = previewArea;
                bestPreviewSize = previewSize;
            }
        }

        if(bestPreviewSize != null){
            mCameraParameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        }
    }

    private void setBestPictureSize(){

        float EMPIRICAL_TOLERANCE = 0.4f;
        Camera.Size bestPictureSize = null;

        int width = mCameraParameters.getPreviewSize().width;
        int height = mCameraParameters.getPreviewSize().height;

        float ratio = width / height;
        float bestArea = 0;

        float pictureRatio;
        float pictureArea;

        for(Camera.Size pictureSize : mCameraParameters.getSupportedPictureSizes()){
            pictureRatio = (float) pictureSize.width / pictureSize.height;
            pictureArea = pictureSize.width * pictureSize.height;

            if(Math.abs(pictureRatio - ratio) <= EMPIRICAL_TOLERANCE && pictureArea >= bestArea){
                bestArea = pictureArea;
                bestPictureSize = pictureSize;
            }
        }

        if(bestPictureSize != null){
            mCameraParameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);
        }
    }

    private void setCameraDisplayOrientation(Display display) {

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                mCamera.setDisplayOrientation(90);
                break;
            case Surface.ROTATION_90:
                break;
            case Surface.ROTATION_180:
                break;
            case Surface.ROTATION_270:
                mCamera.setDisplayOrientation(180);
                break;
        }
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            if(bitmap==null){
                Toast.makeText(CameraActivity.this, "Captured image is empty", Toast.LENGTH_LONG).show();
                return;
            }
            //Updates current index on preview image
            currentIndex = FolderManager.saveNewPicture(bitmap);
            //Updates preview image
            capturedImageHolder.setImageBitmap(bitmap);
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CameraActivity.this, GalleryActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.padoon.cameratest;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class CameraActivity extends AppCompatActivity {
    private boolean isClick = true;
    private static final String PATH_IMAGES = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "easy_check";
    private CameraSurfaceView mCameraSurfaceView;
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    private Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };
    private Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mCameraSurfaceView.startPreview();
            saveFile(data);
            Toast.makeText(CameraActivity.this, "拍照成功", Toast.LENGTH_SHORT).show();
            isClick = true;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img_take_photo = (ImageView) findViewById(R.id.img_take_photo);
        mCameraSurfaceView = (CameraSurfaceView) findViewById(R.id.sv_camera);
        img_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    public void takePhoto() {
        if (isClick) {
            isClick = false;
            mCameraSurfaceView.takePicture(mShutterCallback, rawPictureCallback, jpegPictureCallback);
        }
    }

    public void saveFile(byte[] data) {
        String fileName = UUID.randomUUID().toString() + ".jpg";
        FileOutputStream outputStream = null;
        try {
            File file = new File(PATH_IMAGES);
            if (!file.exists()) {
                file.mkdirs();
            }
            outputStream = new FileOutputStream(PATH_IMAGES + File.separator + fileName);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.example.gcrown.textrecognition.module;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gcrown.textrecognition.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chaochaowu
 * @Description : 主界面Activity,处理View部分
 * @class : MainActivity
 * @time Create at 6/4/2018 4:24 PM
 */


public class MainActivity extends AppCompatActivity implements MainContract.View{

    private Context mContext;

    private ImageView imageView,conversion,translation,reset;
    private EditText editText;
    private TextView textView,cn,en;
    private Button button;

    private MainPresenter mPresenter;
    File mTmpFile;
    Uri imageUri;

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        imageView = findViewById(R.id.imageView);
        conversion = findViewById(R.id.conversion);
        translation = findViewById(R.id.translation);
        reset = findViewById(R.id.reset);
        editText = findViewById(R.id.editview);
        textView = findViewById(R.id.textView);
        en = findViewById(R.id.en);
        cn = findViewById(R.id.cn);
        button = findViewById(R.id.button);
        mPresenter = new MainPresenter(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                /*Resources r = mContext.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(r, R.drawable.test);
                mPresenter.getRecognitionResultByImage(bmp);*/
            }
        });

        conversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String a = cn.getText().toString();
                 String b = en.getText().toString();
                 String c = "中文";
                if (a.equals(c)) {
                    cn.setText("英文");
                    en.setText("中文");
                }if (b.equals(c)){
                    cn.setText("中文");
                    en.setText("英文");
                }

            }
        });

        translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString();
                MainContract.TranslateCallback translateCallback = new MainContract.TranslateCallback() {
                    @Override
                    public void onTranslateDone(String result) {
                        Log.e("wasd",result);
                        Log.e("wasd","66666666");
                        textView.setText(result);
                        // result是翻译结果，在这里使用翻译结果，比如使用对话框显示翻译结果
                    }
                };
                String a = cn.getText().toString();
                String b = en.getText().toString();
                String c = "中文";
                if (a.equals(c)) {
                    new TranslateUtil().translate(MainActivity.this, "auto", "cn", s, translateCallback);
                }if (b.equals(c)){
                    new TranslateUtil().translate(MainActivity.this, "auto", "en", s, translateCallback);
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                textView.setText("");
            }
        });
    }


    @Override
    public void updateUI(String s) {
        editText.setText(s);
        MainContract.TranslateCallback translateCallback = new MainContract.TranslateCallback() {
            @Override
            public void onTranslateDone(String result) {
                Log.e("wasd",result);
                Log.e("wasd","66666666");
                textView.setText(result);
                // result是翻译结果，在这里使用翻译结果，比如使用对话框显示翻译结果
            }
        };
        String a = cn.getText().toString();
        String b = en.getText().toString();
        String c = "中文";
        if (a.equals(c)) {
            new TranslateUtil().translate(MainActivity.this, "auto", "cn", s, translateCallback);
        }if (b.equals(c)){
            new TranslateUtil().translate(MainActivity.this, "auto", "en", s, translateCallback);
        }




    }

    private void takePhoto(){

        if (!hasPermission()) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/img";
        if (new File(path).exists()) {
            try {
                new File(path).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mTmpFile = new File(path, filename + ".jpg");
        mTmpFile.getParentFile().mkdirs();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String authority = getPackageName() + ".provider";
            imageUri = FileProvider.getUriForFile(this, authority, mTmpFile);
        } else {
            imageUri = Uri.fromFile(mTmpFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }


    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE);
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        return;
                    }
                }
                takePhoto();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap photo = BitmapFactory.decodeFile(mTmpFile.getAbsolutePath());
                mPresenter.getRecognitionResultByImage(photo);
                imageView.setImageBitmap(photo);
        }
    }



}

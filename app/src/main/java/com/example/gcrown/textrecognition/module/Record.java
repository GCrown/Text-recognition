package com.example.gcrown.textrecognition.module;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gcrown.textrecognition.R;

import java.io.File;
import java.io.FileInputStream;



public class Record extends AppCompatActivity{
    private  TextView textView;
    public Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        textView = findViewById(R.id.record1);
        button = findViewById(R.id.delete);
        readFileData(null,"wasd.txt",this);
        click();

    }

    //打开指定文件，读取其数据，返回字符串对象
    public String readFileData(String type,String filename,Context context){
        File file = context.getExternalFilesDir(type);
        final String FILE_NAME = file + "/" + filename;
        String result="";
        try {
            FileInputStream fin = new FileInputStream(FILE_NAME);
            //获取文件长度
            int lenght = fin.available();
            byte[] buffer = new byte[lenght];
            fin.read(buffer);
            //将byte数组转换成指定格式的字符串
            result = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("wasd00000",result);
        textView.setText(result);
        Log.e("wasd000001",result);
        return result;
    }

    public void click(){
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FileStorage.removeFileFromSDCard(null,"wasd.txt",Record.this);
                textView.setText("");
            }
        });

    }
}

package com.example.yls.zxing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private EditText context;
    private ImageView view;
    private Button btn_produce;
    private Button btn_searchSelf;
    private TextView tv_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initsViews();
    }

    private void initsViews() {
        context= (EditText) findViewById(R.id.message);
        view= (ImageView) findViewById(R.id.img);
        tv_message= (TextView) findViewById(R.id.tv_result);
        btn_produce= (Button) findViewById(R.id.btn_produce);

        btn_searchSelf= (Button) findViewById(R.id.btn_searchSelf);
        btn_produce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=context.getText().toString().trim();
                if(str.length()==0){
                    Toast.makeText(MainActivity.this,"输入为空白,请再试一次",Toast.LENGTH_LONG).show();
                    return;
                }
              view.setImageBitmap(encodeAsBitmap(str));
            }
        });
        btn_searchSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this)
                        .setOrientationLocked(false)
                        .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {

                Toast.makeText(this,"内容为空",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"扫描成功",Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                tv_message.setText(ScanResult);
            }
        } else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    private Bitmap encodeAsBitmap(String str){
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            try {
                str=new String(str.getBytes("ISO-8859-1"),"UTF-8");//zxing乱码解决方法
            } catch (UnsupportedEncodingException e) {
            }
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 200, 200);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e){
            e.printStackTrace();
        } catch (IllegalArgumentException iae){ //
            return null;
        }
        return bitmap;
          }
    }

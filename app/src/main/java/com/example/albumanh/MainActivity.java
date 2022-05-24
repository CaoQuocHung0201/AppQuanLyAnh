package com.example.albumanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnChupanh, btnLuuanh,btnHuy, btnXemAlbum, btnTaoAlbum;
    final int REQUEST_CODE_CAMERA=123;
    final int REQUEST_CODE_FOLDER=456;
    ImageView imgHinh;


    boolean check=false;
    Spinner spinner;
    public static Database database;

    public static String tenTable="anhmacdinh";

    ArrayList<String> listspinner = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        database=new Database(this, "test.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS '"+tenTable+"' (Id INTEGER PRIMARY KEY AUTOINCREMENT, HinhAnh BLOB, Ngay VARCHAR(150))");
        //database.QueryData("CREATE TABLE IF NOT EXISTS anhgiadinh (Id INTEGER PRIMARY KEY AUTOINCREMENT, HinhAnh BLOB, Ngay VARCHAR(150))");
        //database.QueryData("CREATE TABLE IF NOT EXISTS anhcongviec (Id INTEGER PRIMARY KEY AUTOINCREMENT, HinhAnh BLOB, Ngay VARCHAR(150))");
        AnhXa();


        listspinner.add("Ảnh Mặc định");listspinner.add("Ảnh gia đình");listspinner.add("Ảnh công việc");

        ArrayAdapter arrspin=new ArrayAdapter(this, android.R.layout.simple_spinner_item,listspinner);
        arrspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrspin);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String tenchuanhoa=listspinner.get(i);
                String ten1=tenchuanhoa.toLowerCase(Locale.ROOT);
                String ten2=ten1.replaceAll("[\\s|\\u00A0]+", "");
                String ten3=VNCharacterUtils.removeAccent(ten2);
                tenTable=ten3;
                database.QueryData("CREATE TABLE IF NOT EXISTS '"+tenTable+"' (Id INTEGER PRIMARY KEY AUTOINCREMENT, HinhAnh BLOB, Ngay VARCHAR(150))");
                Toast.makeText(MainActivity.this, "Đã chọn album: "+listspinner.get(i), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat dinhdangNgay=new SimpleDateFormat("dd/MM/yyyy");
        String ngay=dinhdangNgay.format(calendar.getTime())+"\n";

        btnXemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, AlbumAnh.class);
                startActivity(intent);
            }
        });

        btnChupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_CAMERA
                );
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgHinh.setImageBitmap(null);
                check=false;
            }
        });
        btnLuuanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check==false)
                    Toast.makeText(MainActivity.this, "Bạn Chưa chụp ảnh!", Toast.LENGTH_SHORT).show();
                else {
                BitmapDrawable bitmapDrawable= (BitmapDrawable) imgHinh.getDrawable();
                Bitmap bitmap=bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                byte[] hinhAnh= byteArray.toByteArray();

                database.insert_hinh(
                        hinhAnh,
                        ngay,
                        tenTable);

                Toast.makeText(MainActivity.this, "Đã thêm hình vào album!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnTaoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogThemAlbum();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case  REQUEST_CODE_CAMERA:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }else Toast.makeText(this, "Bạn không cho phép mở camera!", Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_CODE_FOLDER:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent=new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                }else Toast.makeText(this, "Bạn không cho phép mở thư mục ảnh!", Toast.LENGTH_SHORT).show();
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_CAMERA && resultCode==RESULT_OK && data!=null){
            Bitmap bitmap= (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
            check=true;
        }
        if(requestCode==REQUEST_CODE_FOLDER && resultCode==RESULT_OK && data!=null){
            Uri uri=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imgHinh.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void AnhXa(){
        btnChupanh=(Button) findViewById(R.id.btnChupAnh);
        btnLuuanh=(Button) findViewById(R.id.btnLuu);
        btnHuy=(Button) findViewById(R.id.btnHuy);
        btnXemAlbum=(Button) findViewById(R.id.btnXemalbum);
        btnTaoAlbum=(Button) findViewById(R.id.btnTaoalbum);
        imgHinh=(ImageView) findViewById(R.id.hinhsetup);
        spinner=(Spinner) findViewById(R.id.spinneralbum);
    }

    public void DialogThemAlbum() {

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themalbum);

        EditText editTenalbum = (EditText) dialog.findViewById(R.id.edtTenvi);

        ImageView btnXacNhan=(ImageView)dialog.findViewById(R.id.imgXacNhanThem);
        ImageView btnHuy=(ImageView)dialog.findViewById(R.id.imgHuyThem);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten1=editTenalbum.getText().toString().trim();
                String ten2=ten1.toLowerCase(Locale.ROOT);
                String ten3=ten2.replaceAll("[\\s|\\u00A0]+", "");
                String ten4=VNCharacterUtils.removeAccent(ten3);;
                tenTable=ten4;
                database.QueryData("CREATE TABLE IF NOT EXISTS '"+tenTable+"' (Id INTEGER PRIMARY KEY AUTOINCREMENT, HinhAnh BLOB, Ngay VARCHAR(150))");
                Toast.makeText(MainActivity.this, "Đã thêm", Toast.LENGTH_SHORT).show();
                listspinner.add(editTenalbum.getText().toString().trim());
                dialog.dismiss();

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }




}


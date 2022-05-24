package com.example.albumanh;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlbumAnh extends AppCompatActivity {

    GridView GridHinhAnh;
    ArrayList<ObjectImage> arrayList;
    ImageAdapter adapter;
    Button sorttang,sortgiam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_anh);

        sorttang=(Button)findViewById(R.id.btnSapxepthapdencao) ;
        sortgiam=(Button)findViewById(R.id.btnSapxepcaodenthap) ;

        sorttang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(arrayList, new Comparator<ObjectImage>() {
                    @Override
                    public int compare(ObjectImage o1, ObjectImage o2) {
                        return o1.getNgayluu().compareTo(o2.getNgayluu());
                    }

                });
                adapter.notifyDataSetChanged();
            }
        });
        sortgiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(arrayList, new Comparator<ObjectImage>() {
                    @Override
                    public int compare(ObjectImage o1, ObjectImage o2) {
                        return o2.getNgayluu().compareTo(o1.getNgayluu());
                    }

                });
                adapter.notifyDataSetChanged();
            }
        });

        GridHinhAnh=(GridView) findViewById(R.id.gvAlbumanh);
        arrayList=new ArrayList<>();
        adapter=new ImageAdapter(this, R.layout.grid_item, arrayList);
        GridHinhAnh.setAdapter(adapter);

        GetData();
    }
    private void GetData(){

        Cursor cursor=MainActivity.database.GetData("SELECT * FROM '"+MainActivity.tenTable+"'");
        while (cursor.moveToNext()){
            arrayList.add(new ObjectImage(
                    cursor.getInt(0),
                    cursor.getBlob(1),
                    cursor.getString(2)
            ));
        }
        adapter.notifyDataSetChanged();
    }
    private void GetDataSort(){

        Cursor cursor=MainActivity.database.GetData("SELECT * FROM '"+MainActivity.tenTable+"'");
        while (cursor.moveToNext()){
            arrayList.add(new ObjectImage(
                    cursor.getInt(0),
                    cursor.getBlob(1),
                    cursor.getString(2)
            ));
        }


    }
}
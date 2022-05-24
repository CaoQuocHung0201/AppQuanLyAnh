package com.example.albumanh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    Context context;
    private int layout;
    private List<ObjectImage> list;

    public ImageAdapter(Context context, int layout, List<ObjectImage> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        ImageView imgHinhAnh;
        TextView tvNgayluu;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(layout,null);
            viewHolder.tvNgayluu=(TextView)convertView.findViewById(R.id.ngaychup);
            viewHolder.imgHinhAnh=(ImageView)convertView.findViewById(R.id.hinhtronggrid);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        ObjectImage hinh=list.get(position);

        viewHolder.tvNgayluu.setText(hinh.getNgayluu());
        //chuuyển mảng byte[] -> bitmap

        byte[]hinhAnh=hinh.getHinh();
        Bitmap bitmap= BitmapFactory.decodeByteArray(hinhAnh,0,hinhAnh.length);
        viewHolder.imgHinhAnh.setImageBitmap(bitmap);

        return convertView;
    }
}

package com.paulapithon.cameragallery.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paulapithon.cameragallery.R;
import com.paulapithon.cameragallery.util.FolderManager;

import java.util.ArrayList;

/**
 * Created by paula on 08/12/17.
 */

public class GridViewAdapter extends ArrayAdapter<ImageItem>{

    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<>();

    public GridViewAdapter (Context context, int layoutResourceId, ArrayList<ImageItem> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = row.findViewById(R.id.text);
            holder.image = row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        ImageItem item = data.get(position);
        holder.image.setImageBitmap(FolderManager.getBitmapByIndex(item.getIndex()));
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}

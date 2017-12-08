package com.paulapithon.cameragallery.view;

import android.Manifest;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.paulapithon.cameragallery.util.Constants;
import com.paulapithon.cameragallery.util.FolderManager;
import com.paulapithon.cameragallery.view.adapter.GridViewAdapter;
import com.paulapithon.cameragallery.R;
import com.paulapithon.cameragallery.view.adapter.ImageItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class GalleryActivity extends AppCompatActivity {

    @BindView(R.id.gridView)
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        requestPermissions();

        //Creates grid and adds images on Musical Photo gallery
        GridViewAdapter adapter = new GridViewAdapter(this, R.layout.grid_item_layout, FolderManager.getImageItems());
        mGridView.setAdapter(adapter);
    }

    @OnItemClick(R.id.gridView)
    public void onGridClick (AdapterView<?> parent, int position) {
        //Image listener to see details of clicked image
        ImageItem item = (ImageItem) parent.getItemAtPosition(position);
        Intent intent = new Intent(GalleryActivity.this, DetailsActivity.class);
        intent.putExtra(Constants.INDEX_IMAGE, item.getIndex());
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.camera_button)
    public void onCameraClick () {
        Intent intent = new Intent(GalleryActivity.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestPermissions () {
        final String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        //Request read memory permission
        ActivityCompat.requestPermissions(this, permissions, 0);
    }
}

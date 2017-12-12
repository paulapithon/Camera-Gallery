package com.paulapithon.cameragallery.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.paulapithon.cameragallery.R;
import com.paulapithon.cameragallery.util.Constants;
import com.paulapithon.cameragallery.util.FolderManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.image)
    PhotoView mImageView;

    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        currentIndex = getIntent().getIntExtra(Constants.INDEX_IMAGE, 0);
        mImageView.setImageBitmap(FolderManager.getBitmapByIndex(currentIndex));
    }

    @OnClick(R.id.previous_button)
    public void onPreviousClick () {
        Bitmap bitmap = FolderManager.getBitmapByIndex(currentIndex - 1);
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
            currentIndex--;
        }
    }

    @OnClick(R.id.next_button)
    public void onNextClick () {
        Bitmap bitmap = FolderManager.getBitmapByIndex(currentIndex + 1);
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
            currentIndex++;
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(DetailsActivity.this, GalleryActivity.class);
        startActivity(intent);
        finish();
    }
}

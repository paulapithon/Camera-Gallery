package com.paulapithon.cameragallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.paulapithon.cameragallery.view.adapter.ImageItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by paula on 08/12/17.
 */

public class FolderManager {

    private static ArrayList<ImageItem> imageItems;

    public static Bitmap getBitmapByIndex (int index) {
        if (imageItems == null) { getImageItems(); }
        try {
            String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + Constants.FOLDER_PATH + File.separator + imageItems.get(index).getPath();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            return BitmapFactory.decodeFile(imagePath, options);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static int getListSize () {
        return imageItems.size();
    }

    //Initializes list with images from external storage
    public static ArrayList<ImageItem> getImageItems () {
        imageItems = new ArrayList<>();
        //Gets path for Musical Folder inside DCIM
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Constants.FOLDER_PATH);
        if(!path.exists()) {
            //If the path doesn't exist yet, create it
            path.mkdir();
        }
        String[] fileNames = path.list();
        if(fileNames != null) {
            for (int i = 0; i < fileNames.length; i++) {
                //Create ImageItem and add to list that will be added to the grid
                String imagePath = fileNames[i];
                imageItems.add(new ImageItem(imagePath, i));
            }
        }
        return imageItems;
    }

    public static int saveNewPicture (Bitmap bitmap) {
        int index = imageItems.size();
        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Constants.FOLDER_PATH);
        if(!path.exists()) {
            path.mkdir();
        }
        String fileName = "Image-" + new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date()) + ".jpg";
        File file = new File(path, fileName);
        if (file.exists()) {
            file.delete();
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            imageItems.add(new ImageItem(fileName, index));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return index;
    }
}

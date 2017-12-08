package com.paulapithon.cameragallery.view.adapter;

/**
 * Created by paula on 08/12/17.
 */

public class ImageItem {
    private String path;
    private int index;

    public ImageItem(String path, int index) {
        super();
        this.path = path;
        this.index = index;
    }

    public String getPath() {
        return path;
    }
    public int getIndex() {
        return index;
    }
}

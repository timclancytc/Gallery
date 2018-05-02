package edu.vt.cs.cs5254.gallery;

import android.graphics.drawable.Drawable;

public class GalleryItem {

    private String mCaption;
    private String mId;
    private String mUrl;
    private double mLat;
    private double mLon;
    private Drawable mDrawable;

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }

    public boolean hasDrawable() {
        return !(mDrawable == null);
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}

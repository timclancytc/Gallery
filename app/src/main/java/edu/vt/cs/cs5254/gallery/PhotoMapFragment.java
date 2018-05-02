package edu.vt.cs.cs5254.gallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class PhotoMapFragment extends SupportMapFragment
        implements GalleryItemLab.OnRefreshItemsListener {

    private static final String TAG = "PhotoMapFragment";
    private GoogleMap mMap;
    private GalleryItemLab mItemLab = GalleryItemLab.get();
    ThumbnailDownloader<GalleryItem> mThumbnailDownloader;

    public static PhotoMapFragment newInstance() {
        return new PhotoMapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (!mItemLab.hasGalleryItems()) {
            mItemLab.refreshItems(this);
        }

        getMapAsync(googleMap -> {
            mMap = googleMap;
            updateUI();
        });

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                (GalleryItem mGalleryItem, Bitmap bitmap) -> {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    mGalleryItem.setDrawable(drawable);
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "Background thread started");
    }

    @Override
    public void onRefreshItems(List<GalleryItem> items) {
        updateUI();
    }

    private void updateUI() {
        if (mMap == null || !mItemLab.hasGalleryItems()) {return;}

        mMap.clear();

        for (GalleryItem item : mItemLab.getGalleryItems()) {
            LatLng itemLatLng = new LatLng(item.getLat(), item.getLon());
            MarkerOptions options = new MarkerOptions()
                    .position(itemLatLng)
                    .title(item.getCaption());
            if (!item.hasDrawable()) {
                mThumbnailDownloader.queueThumbnail(item, item.getUrl());
            }
            mMap.setInfoWindowAdapter(new MarkerInfoWindow(getContext()));
            Marker marker = mMap.addMarker(options);
            marker.setTag(item);
        }
    }

}

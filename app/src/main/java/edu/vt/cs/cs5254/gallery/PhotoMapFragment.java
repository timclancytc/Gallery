package edu.vt.cs.cs5254.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class PhotoMapFragment extends SupportMapFragment {

    private static final String TAG = "PhotoMapFragment";
    private List<GalleryItem> mItems = new ArrayList<>();
    private GoogleMap mMap;

    public static PhotoMapFragment newInstance() {
        return new PhotoMapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new FetchItemsTask().execute();

        getMapAsync(googleMap -> {
            mMap = googleMap;
            updateUI();
        });
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetchr().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            Log.i(TAG, galleryItems.size() + " items have been retrieved");
            updateUI();
        }
    }

    private void updateUI() {
        if (mMap == null || mItems == null || mItems.isEmpty()) {return;}

        mMap.clear();

        for (GalleryItem item : mItems) {
            LatLng itemLatLng = new LatLng(item.getLat(), item.getLon());
            MarkerOptions options = new MarkerOptions()
                    .position(itemLatLng)
                    .title(item.getCaption());
            mMap.addMarker(options);
        }
    }

}

package edu.vt.cs.cs5254.gallery;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GalleryItemLab {

    private List<GalleryItem> mItems;
    private static GalleryItemLab sGalleryItemLab;
    private static final String TAG = "GalleryItemLab";

    public static GalleryItemLab get() {
        if (sGalleryItemLab == null) {
            sGalleryItemLab = new GalleryItemLab();
        }

        return sGalleryItemLab;
    }

    private GalleryItemLab() {
        mItems = new ArrayList<>();
    }

    public interface OnRefreshItemsListener {
        void onRefreshItems(List<GalleryItem> items);
    }

    private class FetchItemsTask extends AsyncTask <Void, Void, List<GalleryItem>> {
        private OnRefreshItemsListener mListener;

        public FetchItemsTask(OnRefreshItemsListener listener) {
            mListener = listener;
        }
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetchr().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            Log.i(TAG, galleryItems.size() + " items have been retrieved");
            mListener.onRefreshItems(mItems);
        }

    }

    public boolean hasGalleryItems() {
        if (mItems == null || mItems.isEmpty()) {
            return false;
        }

        return true;
    }

    public List<GalleryItem> getGalleryItems() {
        return mItems;
    }

    public GalleryItem getGalleryItem(int position) {
        return mItems.get(position);
    }

    public void refreshItems(OnRefreshItemsListener listener) {
        new FetchItemsTask(listener).execute();
    }
}

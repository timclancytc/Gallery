package edu.vt.cs.cs5254.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThumbnailDownloader<H> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    private Handler mRequestHandler;
    private ConcurrentMap<H, String> mRequestMap = new ConcurrentHashMap<>();

    private Handler mResponseHandler;
    private ThumbnailDownloadListener<H> mThumbnailDownloadListener;

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestMap.clear();
    }

    public interface ThumbnailDownloadListener<H> {
        void onThumbnailDownloaded(H holder, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<H> listener) {
        mThumbnailDownloadListener = listener;
    }



    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public void queueThumbnail(H holder, String url) {
        Log.i(TAG, "Got a URL: " + url);
        if (url == null) {
            mRequestMap.remove(holder);
        } else {
            mRequestMap.put(holder, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, holder).sendToTarget();
        }
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    H holder = (H) msg.obj;
                    Log.i(TAG, "Got a request for url:" + mRequestMap.get(holder));
                    handleRequest(holder);
                }
            }
        };
    }

    private void handleRequest(final H holder) {
        try {
            final String url = mRequestMap.get(holder);
            if (url == null) {
                return;
            }
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            mResponseHandler.post(() -> {
                if (mRequestMap.get(holder) != url || mHasQuit) {return;}
                mRequestMap.remove(holder);
                mThumbnailDownloadListener.onThumbnailDownloaded(holder, bitmap);
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }
}

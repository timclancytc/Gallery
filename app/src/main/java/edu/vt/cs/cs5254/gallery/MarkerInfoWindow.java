package edu.vt.cs.cs5254.gallery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public MarkerInfoWindow(Context context) {
        mContext = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View view = inflater.inflate(R.layout.info_window_marker, null);
        GalleryItem galleryItem = (GalleryItem) marker.getTag();
        TextView title = view.findViewById(R.id.marker_title);
        title.setText(galleryItem.getCaption());
        ImageView thumbnail = view.findViewById(R.id.marker_thumbnail);
        if (galleryItem.hasDrawable()) {
            thumbnail.setImageDrawable(galleryItem.getDrawable());
        } else {
            thumbnail.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_photo));
        }

        return view;
    }
}

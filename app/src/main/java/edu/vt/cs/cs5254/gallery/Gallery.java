package edu.vt.cs.cs5254.gallery;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Gallery extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GalleryFragment.newInstance();
    }
}

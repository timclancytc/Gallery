package edu.vt.cs.cs5254.gallery;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FlickrFetchrTest {

    private static final String TAG = "FlickrFetchrTest";


    @Test
    public void parseItemsTest() {
        FlickrFetchr ff = new FlickrFetchr();
        List<GalleryItem> items = new ArrayList<>();

        try {
            InputStream in = FlickrFetchrTest.class.getResourceAsStream("/json/flickr.json");
            String testString = getString(in);

            JSONObject jsonBodyTest = new JSONObject(testString);

            ff.parseItems(items, jsonBodyTest);
        } catch (JSONException je) {
            System.out.println(TAG + "JSON Exception");
        }


        assertEquals(8, items.size());
        GalleryItem item0 = items.get(0);
        assertEquals("41681227201", item0.getId());
        assertEquals("Sunrise looking east", item0.getCaption());
        assertEquals("https://farm1.staticflickr.com/827/41681227201_1aa55c9586_m.jpg", item0.getUrl());
        assert(-37.805257 == item0.getLat());
        assert(144.953023 == item0.getLon());

    }

    private String getString(InputStream in) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                sb.append(line);
            }

        } catch (IOException ioe) {
            System.out.println(TAG + ": IO Exception");
        }

        return sb.toString();
    }
}
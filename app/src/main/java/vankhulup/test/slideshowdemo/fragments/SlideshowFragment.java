package vankhulup.test.slideshowdemo.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;

import java.util.ArrayList;
import java.util.List;

import vankhulup.test.slideshowdemo.R;
import vankhulup.test.slideshowdemo.adapter.FlipperAdapter;

/**
 * Created by Vans on 27.07.2014.
 */
public class SlideshowFragment extends Fragment {
    public static final String TAG = "SlideshowListener";

    AdapterViewFlipper mSlideShowFlipper;
    List<String> availableImages = new ArrayList<String>();
    int slidePointer = 0;
    String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor c = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            availableImages.add(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)));
            c.moveToNext();
        }
        System.out.println();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slideshow, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSlideShowFlipper = (AdapterViewFlipper) view.findViewById(R.id.slideshow_flipper);
        prepareFlipperSettings();
    }

    private void prepareFlipperSettings() {
        mSlideShowFlipper.setAutoStart(true);
        mSlideShowFlipper.setFlipInterval(2000);
        mSlideShowFlipper.setAdapter(new FlipperAdapter(getActivity(), availableImages));
    }
}

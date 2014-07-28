package vankhulup.test.slideshowdemo.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

    ImageButton nextButton;
    ImageButton previousButton;
    ImageButton playButton;

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
        nextButton = (ImageButton)view.findViewById(R.id.nex_button);
        previousButton = (ImageButton)view.findViewById(R.id.previous_button);
        playButton = (ImageButton)view.findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlideShowFlipper.isFlipping()) {
                    mSlideShowFlipper.stopFlipping();
                    playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                    Toast.makeText(getActivity(), "Stopped auto slide show", Toast.LENGTH_SHORT).show();
                } else {
                    mSlideShowFlipper.startFlipping();
                    playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                    Toast.makeText(getActivity(), "Start auto slide show", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlideShowFlipper.isFlipping()){
                    mSlideShowFlipper.showNext();
                } else {
                    Toast.makeText(getActivity(), "Fuck off, it's automated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlideShowFlipper.isFlipping()){
                    mSlideShowFlipper.showPrevious();
                } else {
                    Toast.makeText(getActivity(), "Fuck off, it's automated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        prepareFlipperSettings();
    }

    private void prepareFlipperSettings() {
        mSlideShowFlipper.setAutoStart(true);
        mSlideShowFlipper.setFlipInterval(2000);
        mSlideShowFlipper.setAdapter(new FlipperAdapter(getActivity(), availableImages));
        mSlideShowFlipper.setInAnimation(getActivity(), R.anim.slide_in_right);
        mSlideShowFlipper.setOutAnimation(getActivity(), R.anim.slide_out_left);
    }
}

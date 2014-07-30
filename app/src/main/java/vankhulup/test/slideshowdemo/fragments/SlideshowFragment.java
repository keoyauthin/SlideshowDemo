package vankhulup.test.slideshowdemo.fragments;

import android.animation.Animator;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vankhulup.test.slideshowdemo.R;
import vankhulup.test.slideshowdemo.adapter.FlipperAdapter;

/**
 * Created by Vans on 27.07.2014.
 */
public class SlideshowFragment extends Fragment {
    public static final String TAG = "SlideshowListener";
    private ShareActionProvider mShareActionProvider;

    AdapterViewFlipper mSlideShowFlipper;
    List<String> availableImages = new ArrayList<String>();
    String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
    };

    ImageButton nextButton;
    ImageButton previousButton;
    ImageButton playButton;
    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            int currentIndex = mSlideShowFlipper.getDisplayedChild();
            String fileName = availableImages.get(currentIndex);
            File tmpFile = new File(fileName);
            Uri photoUri = Uri.fromFile(tmpFile);

            mShareActionProvider.setShareIntent(updateSearchIntent(fileName, photoUri.toString()));
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
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
        c.close();

        BroadcastReceiver localEventsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateSlideShowView(intent);
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localEventsReceiver,
                new IntentFilter("settings-change"));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_slideshow, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSlideShowFlipper = (AdapterViewFlipper) view.findViewById(R.id.slideshow_flipper);
        nextButton = (ImageButton) view.findViewById(R.id.nex_button);
        previousButton = (ImageButton) view.findViewById(R.id.previous_button);
        playButton = (ImageButton) view.findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSlideShowFlipper.isFlipping()) {
                    mSlideShowFlipper.stopFlipping();
                    playButton.setBackgroundResource(android.R.drawable.ic_media_play);
                    Toast.makeText(getActivity(), "Stopped auto slide show", Toast.LENGTH_SHORT).show();
                } else {
                    mSlideShowFlipper.startFlipping();
                    playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                    Toast.makeText(getActivity(), "Start auto slide show", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlideShowFlipper.isFlipping()) {
                    mSlideShowFlipper.showNext();
                } else {
                    Toast.makeText(getActivity(), "Fuck off, it's automated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlideShowFlipper.isFlipping()) {
                    mSlideShowFlipper.showPrevious();
                } else {
                    Toast.makeText(getActivity(), "Fuck off, it's automated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        prepareFlipperSettings();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.slideshow_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider)
                shareItem.getActionProvider();
        mShareActionProvider.setShareIntent(getDefaultIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private void prepareFlipperSettings() {
        mSlideShowFlipper.setAutoStart(true);
        mSlideShowFlipper.setFlipInterval(2000);
        mSlideShowFlipper.setAdapter(new FlipperAdapter(getActivity(), availableImages));
        mSlideShowFlipper.setInAnimation(getActivity(), R.animator.slide_in_right);
        mSlideShowFlipper.setOutAnimation(getActivity(), R.animator.slide_out_left);
        mSlideShowFlipper.getInAnimation().addListener(animationListener);
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "Test share");
        return intent;
    }

    private Intent updateSearchIntent(String text, String filePath){
        Intent searchIntent = getDefaultIntent();
        searchIntent.putExtra(Intent.EXTRA_TEXT, text);
        searchIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
        return searchIntent;
    }

    private void updateSlideShowView(Intent intent) {
        if (intent.hasExtra("newAnimation")) {
            SettingsFragment.AnimationEnum newAnimation = (SettingsFragment.AnimationEnum) intent.getSerializableExtra("newAnimation");
            changeAnimation(newAnimation);
        }
        if (intent.hasExtra("newFrequency")) {
            mSlideShowFlipper.setFlipInterval(intent.getIntExtra("newFrequency", 0));
        }
    }

    private void changeAnimation(SettingsFragment.AnimationEnum animation) {
        switch (animation) {
            case FADE:
                mSlideShowFlipper.setInAnimation(getActivity(), R.animator.fade_in);
                mSlideShowFlipper.setOutAnimation(getActivity(), R.animator.fade_out);
                break;
            case ROTATE:
                mSlideShowFlipper.setOutAnimation(getActivity(), R.animator.rotate_in);
                mSlideShowFlipper.setInAnimation(getActivity(), R.animator.rotate_out);
                break;
            case SLIDE:
                mSlideShowFlipper.setInAnimation(getActivity(), R.animator.slide_in_right);
                mSlideShowFlipper.setOutAnimation(getActivity(), R.animator.slide_out_left);
                break;
        }
    }
}

package vankhulup.test.slideshowdemo.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import vankhulup.test.slideshowdemo.R;

/**
 * Created by Vans on 27.07.2014.
 */
public class SettingsFragment extends Fragment {

    TextView currentFrequencyLabel;
    SeekBar mFrequencySeekBar;
    FrameLayout mainContainer;
    LinearLayout contentContainer;
    RadioGroup animationSetGroup;
    private boolean isAnimationChanged = false;
    private AnimationEnum currentAnimation;
    private boolean isFrequencyChanged = false;
    private int frequencyChange;

    public enum AnimationEnum {
        SLIDE,
        ROTATE,
        FADE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainContainer = (FrameLayout)view.findViewById(R.id.settings_main_container);
        contentContainer = (LinearLayout)view.findViewById(R.id.settings_views_container);
        applyBlurToBackground();

        animationSetGroup = (RadioGroup)view.findViewById(R.id.settings_animation_group);
        currentFrequencyLabel = (TextView)view.findViewById(R.id.slide_frequency_indicator);
        mFrequencySeekBar = (SeekBar)view.findViewById(R.id.slide_frequency_picker);

        mFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float frequencyInSeconds = progress / 1000f;
                isFrequencyChanged = true;
                currentFrequencyLabel.setText(String.format("Current frequency - %.1f seconds", frequencyInSeconds));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //NO-OP
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                frequencyChange = seekBar.getProgress();
            }
        });
        animationSetGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isAnimationChanged = true;
                switch (checkedId){
                    case R.id.settings_animation_fade:
                        currentAnimation = AnimationEnum.FADE;
                        break;
                    case R.id.settings_animation_rotate:
                        currentAnimation = AnimationEnum.ROTATE;
                        break;
                    case R.id.settings_animation_slide:
                        currentAnimation = AnimationEnum.SLIDE;
                        break;
                }
            }
        });
    }

    private void applyBlurToBackground() {
        mainContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mainContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                mainContainer.buildDrawingCache();

                Bitmap bitmap = mainContainer.getDrawingCache();
                blur(bitmap, contentContainer);
                return false;
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bitmap, View view) {
        float radius = 20;
        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bitmap, 0, 0, null);
        RenderScript rs = RenderScript.create(getActivity());
        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        view.setBackground(new BitmapDrawable(
                getResources(), overlay));
        rs.destroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent = new Intent("settings-change");
        if(isAnimationChanged)
            intent.putExtra("newAnimation", currentAnimation);
        if (isFrequencyChanged)
            intent.putExtra("newFrequency", frequencyChange);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }
}

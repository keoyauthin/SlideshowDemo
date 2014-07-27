package vankhulup.test.slideshowdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Vans on 27.07.2014.
 */
public class SlideItem extends RelativeLayout {

    ImageView slideImage;
    TextView slideTitle;
    Bitmap image;

    public SlideItem(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.item_slide, this, true);
        slideImage = (ImageView)findViewById(R.id.slide_image);
        slideTitle = (TextView) findViewById(R.id.slide_title);

    }

    public void setSlideData(String title, Bitmap image){
        Bitmap d = new BitmapDrawable(getContext().getResources(), image).getBitmap();
        int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
        image = Bitmap.createScaledBitmap(d, 512, nh, true);
        slideImage.setImageBitmap(image);
//        slideImage.setBackground(new BitmapDrawable(getResources(), image));
        slideTitle.setText(title);
        d.recycle();
    }
}

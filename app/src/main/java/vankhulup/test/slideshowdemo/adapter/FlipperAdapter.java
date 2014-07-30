package vankhulup.test.slideshowdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import vankhulup.test.slideshowdemo.R;

/**
 * Created by Vans on 27.07.2014.
 */
public class FlipperAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<String> filesAddresses = new ArrayList<String>();

    static class ViewHolder {
        ImageView slideImage;
        TextView slideTitle;
    }

    public FlipperAdapter(Context context, List<String> addresses) {
        this.inflater = LayoutInflater.from(context);
        this.filesAddresses = addresses;
    }
    @Override
    public int getCount() {
        return filesAddresses.size();
    }

    @Override
    public Object getItem(int position) {
        return filesAddresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_slide, parent, false);
            viewHolder.slideImage = (ImageView)convertView.findViewById(R.id.slide_image);
            viewHolder.slideTitle = (TextView)convertView.findViewById(R.id.slide_title);
            convertView.setTag(viewHolder);
        } else {
           viewHolder = (ViewHolder)convertView.getTag();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filesAddresses.get(position), options);

        options.inSampleSize = calculateInSimpleSize(options, 512, 512);

        options.inJustDecodeBounds = false;
        WeakReference<Bitmap> imageRef = new WeakReference<>(BitmapFactory.decodeFile(filesAddresses.get(position), options));
        viewHolder.slideImage.setImageBitmap(imageRef.get());
        viewHolder.slideTitle.setText(filesAddresses.get(position));

        return convertView;
    }

    private int calculateInSimpleSize(BitmapFactory.Options options, int requiredWidth, int requiredHeight) {
        final int rawHeight = options.outHeight;
        final int rawWidth = options.outWidth;
        int inSampleSize = 1;

        if (rawHeight > requiredHeight|| rawWidth> requiredWidth) {
            int halfHeight = rawHeight / 2;
            int halfWidth = rawWidth / 2;

            while ((halfHeight / inSampleSize) > requiredHeight
                    && (halfWidth / inSampleSize)> requiredWidth)
                inSampleSize *= 2;
        }
        return inSampleSize;
    }
}

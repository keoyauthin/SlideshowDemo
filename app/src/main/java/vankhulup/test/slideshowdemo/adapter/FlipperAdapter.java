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
        Bitmap originalBitmap = BitmapFactory.decodeFile(filesAddresses.get(position));
        int nh = (int) ( originalBitmap.getHeight() * (512.0 / originalBitmap.getWidth()) );
        Bitmap recycledImage = Bitmap.createScaledBitmap(originalBitmap, 512, nh, true);
        viewHolder.slideImage.setImageBitmap(recycledImage);
        viewHolder.slideTitle.setText(filesAddresses.get(position));

        return convertView;
    }
}

package in.poolclues.www.poolclues;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Shyam Yadav on 24-Oct-15.
 */
public class CustomGrid extends BaseAdapter {


    private ArrayList<String> productname;
    private ArrayList<Integer> listFlag;
    private Activity activity;

    public CustomGrid(Activity activity, ArrayList<String> productname, ArrayList<Integer> listFlag) {
        super();
        this.productname = productname;
        this.listFlag = listFlag;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return productname.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return productname.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder {
        public ImageView imgViewFlag;
        public TextView txtViewTitle;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder view;
        LayoutInflater inflator = activity.getLayoutInflater();

        if (convertView == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.activity_single_grid, null);

            view.txtViewTitle = (TextView) convertView.findViewById(R.id.grid_text);
            view.imgViewFlag = (ImageView) convertView.findViewById(R.id.grid_image);
            view.imgViewFlag.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.imgViewFlag.setPadding(4,4,4,4);
            convertView.setTag(view);

        } else {
            view = (ViewHolder) convertView.getTag();
        }

        view.txtViewTitle.setText(productname.get(position));
        view.imgViewFlag.setImageResource(listFlag.get(position));
        return convertView;
    }



}
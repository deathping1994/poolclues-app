package in.poolclues.www.poolclues;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Shyam Yadav on 06-Nov-15.
 */
public class ProductAdapter extends ArrayAdapter<Products> {
    List<Products> mylist;

    public ProductAdapter(Context _context, List<Products> _mylist) {
        super(_context, R.layout.activity_single_grid, _mylist);
        this.mylist = _mylist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
        convertView = vi.inflate(R.layout.activity_single_grid, parent, false);
        // Product object
        Products product = getItem(position);
        //
        TextView txtTitle = (TextView) convertView.findViewById(R.id.grid_text);
        txtTitle.setText(product.title);
        // show image
        ImageView img = (ImageView) convertView.findViewById(R.id.grid_image);
        // download image
        imgdownload imageDownloader = new imgdownload();
        imageDownloader.download(product.img_url, img);

        return convertView;
    }
}
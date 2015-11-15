package in.poolclues.www.poolclues;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Shyam Yadav on 06-Nov-15.
 */
public class Products{

    public String id;
    public String title;
    public String img_url;
    public Products(String p_title, String p_img_url) {
        title = p_title;
        img_url = p_img_url;
    }

}
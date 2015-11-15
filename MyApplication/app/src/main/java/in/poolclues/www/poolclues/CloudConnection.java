package in.poolclues.www.poolclues;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Shyam Yadav on 20-Oct-15.
 */
public class CloudConnection extends Activity {

    public boolean isConnected()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null&&networkInfo.isConnected())
            return true;
        else
            return false;
    }
}

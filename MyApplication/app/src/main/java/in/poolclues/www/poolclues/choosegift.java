package in.poolclues.www.poolclues;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class choosegift extends Activity implements AbsListView.OnScrollListener {

    DBConnection dbConn= new DBConnection();
    String authtoken;
    GridView grid;
    List products;
    ListView lvProducts;
    GridView gvProducts = null;
    ProductAdapter adapterProducts;
    private boolean lvBusy = false;
     int[] imageId= {R.drawable.gift,R.drawable.gift };
   // int[] imageId;
  //  private ArrayList<String> p_productName;
    private ArrayList<Integer> listFlag;
    CustomGrid customGrid;
    public static final String URL1 ="http://theopentutorials.com/totwp331/wp-content/uploads/totlogo.png";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosegift);
        Intent intent=getIntent();
        Bundle intentBundle=intent.getExtras();
        authtoken=intentBundle.getString("authtoken");
        // populate data

        new HttpAsyncTask().execute(dbConn.URLProductList);

}


    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                lvBusy = false;
                adapterProducts.notifyDataSetChanged();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                lvBusy = true;
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                lvBusy = true;
                break;
        }
    }


    public boolean isLvBusy(){
        return lvBusy;
    }





        // get image from here //

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }


// end  image from here //





    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            ClsProductList clsProductList = new ClsProductList();
            clsProductList.setAuthtoken(authtoken.toString());
            return GetProductList(urls[0], clsProductList);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result.equals("") || result == null){
                Toast.makeText(choosegift.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }
            returnParsedJsonObject(result);
          //  Toast.makeText(getBaseContext(), "Gift:" +result, Toast.LENGTH_LONG).show();
        }
    }
    private void returnParsedJsonObject(String result){
        List products;
        products = new ArrayList();
        List p_imges= new ArrayList();
        JSONObject resultObject = null;
        ArrayList<String> p_productName= new ArrayList<String>();
        ArrayList<String> p_productImage= new ArrayList<String>();
        ArrayList<Integer> p_id= new ArrayList<Integer>();
        ArrayList<Integer> p_price= new ArrayList<Integer>();
        String img=null;
        try {
            resultObject = new JSONObject(result);
            JSONArray array=resultObject.getJSONArray("products");
            int value=0;
            for(int i=0;i<array.length();i++) {
           // for(int i=0;i<=2;i++) {
                String line=array.getString(i);
                resultObject= new JSONObject(line);
                String imageURL=resultObject.getString("image");
                int price=resultObject.getInt("price");
                String name=resultObject.getString("name");
                name=name+"at Price(₹)"+price;
                int id=resultObject.getInt("id");
                p_productName.add(name);
                p_productImage.add(imageURL);
                p_id.add(id);
                p_price.add(price);
                value=array.length();
                img=imageURL;
                products.add(new Products(name,imageURL));
            }

         //   products = new ArrayList();
            //products.add(new Products("Orange","http://farm5.staticflickr.com/4142/4787427683_3672f1db9a_s.jpg"));
            //
            lvProducts = (ListView) findViewById( R.id.list_products);
            lvProducts.setAdapter(new ProductAdapter(choosegift.this, products) );


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void prepareList(int value)
    {


        listFlag = new ArrayList<Integer>();
        for(int i=0;i<value;i++) {
            listFlag.add(R.drawable.gift);
        }
 //       listFlag.add(R.drawable.gift);
   //     listFlag.add(R.drawable.gift);
    //    listFlag.add(R.drawable.gift);


    }


    public static String GetProductList(String url, ClsProductList clsProductList)
    {
        InputStream inputStream=null;
        String result="";

        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";
            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("authtoken",clsProductList.getAuthtoken());
            json=jsonObject.toString();
            StringEntity se= new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept","application/json");
            httpPost.setHeader("Content-type","application/json");
            HttpResponse httpResponse=httpClient.execute(httpPost);
            inputStream=httpResponse.getEntity().getContent();
            if (inputStream!=null)
            {
                result = convertInputStreamToString(inputStream);
            }
            else
            {
                result = "Did not work";
            }
        }
        catch (Exception ex)
        {

        }
        return  result;
    }
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choosegift, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

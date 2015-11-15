package in.poolclues.www.poolclues;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class create_event extends Activity {

    DBConnection dbConn= new DBConnection();
    String authtoken;
    String emailid;
    ListView lvProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // get values from another page that is below //

        Intent intent=getIntent();
        Bundle intentBundle=intent.getExtras();
        authtoken=intentBundle.getString("authtoken");
        emailid=intentBundle.getString("emailid");
        // get value from another page end here //

        // list events add here //


        final ListView listview = (ListView) findViewById(R.id.list_events);
        String[] values = new String[] { "AndroidnAndroid ", "iPhonen" +
                "Android", "WindowsMobilen" +
                "Android",
                "Blackberryn" +
                        "Androidn" +
                        "Android", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
       // listview.setAdapter(adapter);


        // list events end here //



        // click event for create event group //

        Button V_btn_createeventgroup=(Button)findViewById(R.id.btn_createeventgroup);
        V_btn_createeventgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1= new Intent(create_event.this,groupcreate.class);
                intent1.putExtra("authtoken", authtoken);
                intent1.putExtra("emailid", emailid);
                startActivity(intent1);
            }
        });

        // click event end here //

        Toast.makeText(this,""+authtoken.toString(),Toast.LENGTH_LONG).show();
        new HttpAsyncFetchList().execute(dbConn.APIURL+emailid.toString()+dbConn.URLFetchPoolList+"all");

    }




    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }


    // Fetch list from API start from here //
    private class HttpAsyncFetchList extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            ClsFetchEvents clsFetchEvents= new ClsFetchEvents();
            clsFetchEvents.setAuthCode(authtoken.toString());
            return FetchEvents(urls[0], clsFetchEvents);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            super.onPostExecute(result);
            returnParsedJsonObject(result);
            Toast.makeText(getBaseContext(), "" + result, Toast.LENGTH_LONG).show();
        }
    }

    private void returnParsedJsonObject(String result){
        List products;
        products = new ArrayList();
        List p_imges= new ArrayList();
        JSONObject resultObject = null;
        String img=null;
      //  try {
        //    Toast.makeText(this,"3",Toast.LENGTH_LONG).show();
        //    resultObject = new JSONObject(result);
        //    JSONArray array=resultObject.getJSONArray("products");
          //  int value=0;
          //  for(int i=0;i<array.length();i++) {
          //      String line=array.getString(i);
          //      resultObject= new JSONObject(line);
          //      String imageURL=resultObject.getString("event_list");
         //       String name="name";
        //        imageURL=imageURL+ "http://farm5.staticflickr.com/4142/4787427683_3672f1db9a_s.jpg";
            //    products.add(new Products(name,imageURL));
        //    }
            products = new ArrayList();
            products.add(new Products("Orange","http://farm5.staticflickr.com/4142/4787427683_3672f1db9a_s.jpg"));
            lvProducts = (ListView) findViewById( R.id.list_events);
            lvProducts.setAdapter(new EventGroupAdapter(create_event.this, products) );

     //   } catch (JSONException e) {
     //       e.printStackTrace();
     //   }

    }

    public static String FetchEvents(String url, ClsFetchEvents clsFetchEvents)
    {
        InputStream inputStream=null;
        String result="";
        result=url;
        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";
            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("authtoken",clsFetchEvents.getAuthCode());
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


    // Fetch list from API end from here //

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
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
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

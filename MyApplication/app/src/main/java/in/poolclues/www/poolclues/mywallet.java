package in.poolclues.www.poolclues;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class mywallet extends Activity {


    String authtoken;
    String emailid;
    protected EditText txt_amount;
    DBConnection dbConn= new DBConnection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        Intent intent=getIntent();
        Bundle intentBundle=intent.getExtras();
        authtoken=intentBundle.getString("authtoken");
        emailid=intentBundle.getString("emailid");

        // get balance //

        new GetBalanceDetails().execute(dbConn.APIURL+emailid.toString()+dbConn.URLWalletBalance);

        // end balance  here //
        GetRechargeDetails();
        final Button recharge=(Button)findViewById(R.id.btnrecharge);
        Button addmoney=(Button)findViewById(R.id.btnaddmoney);
        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_amount=(EditText)findViewById(R.id.txt_amount);
                txt_amount.setVisibility(View.VISIBLE);
                recharge.setVisibility(View.VISIBLE);


            }
        });

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getBaseContext(), emailid.toString()+authtoken.toString()+txt_amount.getText().toString(), Toast.LENGTH_LONG).show();
                new HttpAsyncTaskRecharge().execute(dbConn.APIURL+emailid.toString()+dbConn.URLAddMoneyToWallet);
                GetRechargeDetails();
            }
        });

    }

    private void GetRechargeDetails()
    {
        // list events add here //


     //   final ListView listview = (ListView) findViewById(R.id.list_events);
     //   String[] values = new String[] { "AndroidnAndroid ", "iPhonen" +
      //          "Android", "WindowsMobilen" +
      //          "Android",
      //          "Blackberryn" +
      //                  "Androidn" +
     //                   "Android", "WebOS", "Ubuntu", "Windows7", "Max OS X",
     //           "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
      //          "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
      //          "Android", "iPhone", "WindowsMobile" };

//        final ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < values.length; ++i) {
 //           list.add(values[i]);
 //       }
  //      final StableArrayAdapter adapter = new StableArrayAdapter(this,
   //             android.R.layout.simple_list_item_1, list);
   //     listview.setAdapter(adapter);

        new HttpAsyncGetPaymentDetails().execute(dbConn.URLServerSts+emailid.toString()+dbConn.URLPaymentDetails);
        // list events end here //

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


    // get payment detaisl from this function //


    private class HttpAsyncGetPaymentDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            ClsAuthToken clsAuthToken= new ClsAuthToken();
            clsAuthToken.setAuth(authtoken.toString());
            return GetPaymentDetails(urls[0], clsAuthToken);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), "" + result, Toast.LENGTH_LONG).show();
            GetFilterDetails(result);
        }
    }

    private void GetFilterDetails(String result){
        List products;
        products = new ArrayList();
        int amt=0;
        JSONObject resultObject = null;
        final ArrayList<String> list = new ArrayList<String>();
        try {
            resultObject = new JSONObject(result);
            JSONArray array=resultObject.getJSONArray("transactions");
            int value=0;
            for(int i=0;i<array.length();i++) {
                String line=array.getString(i);
                resultObject= new JSONObject(line);
                String date=resultObject.getString("date");
                String amount=resultObject.getString("amount");
                String details="Date: "+date+"\nRecharge Amount(₹): "+amount;
                list.add(details);

            }
           //  TextView ttlamt= (TextView)findViewById(R.id.lblttlamt);
           //  ttlamt.setText(amt);
             final ListView listview = (ListView) findViewById(R.id.list_rechargehis);
             final StableArrayAdapter adapter = new StableArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
             listview.setAdapter(adapter);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static String GetPaymentDetails(String url, ClsAuthToken clsAuthToken)
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
            jsonObject.accumulate("authtoken",clsAuthToken.getAuth());

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
                // result="json"+json+"input"+inputStream;

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


    // end payment details from here  //


    // wallet balance show here //


    private class GetBalanceDetails extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            ClsAuthToken clsAuthToken= new ClsAuthToken();
            clsAuthToken.setAuth(authtoken.toString());
            return GetBalance(urls[0], clsAuthToken);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            GetFilterBalance(result);
        }
    }

    public static String GetBalance(String url, ClsAuthToken clsAuthToken)
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
            jsonObject.accumulate("authtoken",clsAuthToken.getAuth());
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

    private void GetFilterBalance(String result){
        JSONObject resultObject = null;
        final ArrayList<String> list = new ArrayList<String>();
        try {
            resultObject = new JSONObject(result);
            String amount="₹: "+resultObject.getString("amount");
              TextView ttlamt= (TextView)findViewById(R.id.lblttlamt);
              ttlamt.setText(amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // waller balance end here //


    private class HttpAsyncTaskRecharge extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            ClsRecharge clsRecharge= new ClsRecharge();
            clsRecharge.setAuth(authtoken.toString());
            clsRecharge.setAmt(Integer.parseInt(txt_amount.getText().toString()));
            return CreateRecharge(urls[0], clsRecharge);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), "" + result, Toast.LENGTH_LONG).show();
        }
    }


    public static String CreateRecharge(String url, ClsRecharge clsRecharge)
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
            jsonObject.accumulate("authtoken",clsRecharge.getAuth());
            jsonObject.accumulate("amount",clsRecharge.getAmt());

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
               // result="json"+json+"input"+inputStream;

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
        getMenuInflater().inflate(R.menu.menu_mywallet, menu);
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

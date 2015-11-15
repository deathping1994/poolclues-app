package in.poolclues.www.poolclues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class signup extends Activity {

    protected EditText txtfirst_name,txtmiddle_name,txtlast_name,txtemail_id,txtpassword,txthouse_no,txtstreet,txtcity,txtstate,txtcountry,txtphone;

    InputStream is=null;
    String s_txtfirstname,s_txtlastname,s_txtmobileno,s_txtemailid,s_txtaddress;
    String line=null;
    String result=null;
    int code;
    DBConnection dbConn= new DBConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        txtfirst_name=(EditText)findViewById(R.id.txt_firstname);
        txtmiddle_name=(EditText)findViewById(R.id.txt_middlename);
        txtlast_name=(EditText)findViewById(R.id.txt_lastname);
        txtemail_id=(EditText)findViewById(R.id.txt_emailid);
        txtpassword=(EditText)findViewById(R.id.txt_password);
        txthouse_no=(EditText)findViewById(R.id.txt_houseno);
        txtstreet=(EditText)findViewById(R.id.txt_street);
        txtcity=(EditText)findViewById(R.id.txt_city);
        txtstate=(EditText)findViewById(R.id.txt_state);
        txtcountry=(EditText)findViewById(R.id.txt_couuntry);
        txtphone=(EditText)findViewById(R.id.txt_mobile);
      //  txtfirstname=(EditText)findViewById(R.id.txtfirstname);
      //  txtlastname=(EditText)findViewById(R.id.txtlastname);
        Button btnclick=(Button)findViewById(R.id.btnsignup);
        btnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

      //          s_txtfirstname=txtfirstname.getText().toString();
       //         s_txtlastname=txtlastname.getText().toString();
       //         Insert();

                // progress bar from here //
        final ProgressDialog progressDialog= new ProgressDialog(signup.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                },5000);
            new HttpAsyncTask().execute(dbConn.URLReg);

                // progress bar end here //

           // Toast.makeText(signup.this,"Sign Up Successfully",Toast.LENGTH_LONG).show();
            }
        });

    }


    public void Insert()
    {
     //   ArrayList<NameValuePair> nameValuePairs= new ArrayList<NameValuePair>();
     //   nameValuePairs.add(new BasicNameValuePair("first_name",s_txtfirstname));
     //   nameValuePairs.add(new BasicNameValuePair("last_name",s_txtlastname));
     //   try {
     //       HttpClient httpClient= new DefaultHttpClient();
     //       HttpPost httpPost= new HttpPost(dbConn.SignUpURL);
     //       httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
     //       HttpResponse response=httpClient.execute(httpPost);
     //       HttpEntity entity=response.getEntity();
     //       is=entity.getContent();
     //      EditText fn=(EditText)findViewById(R.id.txtfirstname);
     //       fn.setText("");
     //       EditText ln=(EditText)findViewById(R.id.txtlastname);
      //      ln.setText("");
       //     Toast.makeText(signup.this,"Sign Up Successfully",Toast.LENGTH_LONG).show();

     //   }
     //   catch (Exception e)
      //  {
       //     Toast.makeText(signup.this,"IC"+e.toString(),Toast.LENGTH_LONG).show();
      //  }
      //  try
      //  {
       //     BufferedReader reader= new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
       //     StringBuilder sb= new StringBuilder();
       //     while ((line=reader.readLine())!=null)
       //     {
       //         sb.append(line+"\n");
       //     }
       //     is.close();
       //     result=sb.toString();
      //  }
      //  catch (Exception e)
      //  {
      //      Toast.makeText(signup.this,e.toString(),Toast.LENGTH_LONG).show();
      //  }
     //   try
     //   {
     //       JSONObject jsonObject= new JSONObject(result);
     //       code=(jsonObject.getInt("code"));
     //       if(code==1)
     //       {
     //           EditText fn=(EditText)findViewById(R.id.txtfirstname);
     //           EditText ln=(EditText)findViewById(R.id.txtlastname);
     //           fn.setText("");
      //          ln.setText("");
      //          Toast.makeText(signup.this,"Sign Up Successfully",Toast.LENGTH_LONG).show();

      //      }
      //      else
      //      {
      //          Toast.makeText(signup.this,"Sorry Try Again",Toast.LENGTH_LONG).show();
      //      }
     //   }
      //  catch (Exception e)
     //   {
     //       Toast.makeText(signup.this,e.toString(),Toast.LENGTH_LONG).show();
     //   }
    }



    public static String Register(String url, ClsSignup clsSignup)
    {
        InputStream inputStream=null;
        String result="";
        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";

            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("first_name",clsSignup.getFirst_name());
            jsonObject.accumulate("middle_name",clsSignup.getMiddle_name());
            jsonObject.accumulate("last_name",clsSignup.getLast_name());
            jsonObject.accumulate("email_id",clsSignup.getEmail_id());
            jsonObject.accumulate("password",clsSignup.getPassword());
            jsonObject.accumulate("house_no",clsSignup.getHouse_no());
            jsonObject.accumulate("street",clsSignup.getStreet());
            jsonObject.accumulate("city",clsSignup.getCity());
            jsonObject.accumulate("state",clsSignup.getState());
            jsonObject.accumulate("country",clsSignup.getCountry());
            jsonObject.accumulate("phone",clsSignup.getPhone());

            json=jsonObject.toString();
            StringEntity se= new StringEntity(json);
            httpPost.setEntity(se);

            httpPost.setHeader("Accept","application/json");
            httpPost.setHeader("Content-type","application/json");

            HttpResponse httpResponse=httpClient.execute(httpPost);
            inputStream=httpResponse.getEntity().getContent();

            if (inputStream!=null)
               result=convertInputStreamToString(inputStream);

                else
                result ="Did not work";


        }
        catch (Exception ex)
        {

        }
        //Toast.makeText(signup.this,"Sign Up Successfully",Toast.LENGTH_LONG).show();
        return  result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            ClsSignup clsSignup = new ClsSignup();
            clsSignup.setFirst_name(txtfirst_name.getText().toString());
            clsSignup.setMiddle_name(txtmiddle_name.getText().toString());
            clsSignup.setLast_name(txtlast_name.getText().toString());
            clsSignup.setEmail_id(txtemail_id.getText().toString());
            clsSignup.setPassword(txtpassword.getText().toString());
            clsSignup.setHouse_no(txthouse_no.getText().toString());
            clsSignup.setStreet(txtstreet.getText().toString());
            clsSignup.setCity(txtcity.getText().toString());
            clsSignup.setState(txtstate.getText().toString());
            clsSignup.setCountry(txtcountry.getText().toString());
            clsSignup.setPhone(txtphone.getText().toString());
            return Register(urls[0],clsSignup);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            EditText  txtfirst_name=(EditText)findViewById(R.id.txt_firstname);
            txtfirst_name.setText("");
            EditText txtmiddle_name=(EditText)findViewById(R.id.txt_middlename);
            txtmiddle_name.setText("");
            EditText txtlast_name=(EditText)findViewById(R.id.txt_lastname);
            txtlast_name.setText("");
            EditText txtemail_id=(EditText)findViewById(R.id.txt_emailid);
            txtemail_id.setText("");
            EditText txtpassword=(EditText)findViewById(R.id.txt_password);
            txtpassword.setText("");
            EditText txthouse_no=(EditText)findViewById(R.id.txt_houseno);
            txthouse_no.setText("");
            EditText txtstreet=(EditText)findViewById(R.id.txt_street);
            txtstreet.setText("");
            EditText txtcity=(EditText)findViewById(R.id.txt_city);
            txtcity.setText("");
            EditText txtstate=(EditText)findViewById(R.id.txt_state);
            txtstate.setText("");
            EditText txtcountry=(EditText)findViewById(R.id.txt_couuntry);
            txtcountry.setText("");
            EditText txtphone=(EditText)findViewById(R.id.txt_mobile);
            txtphone.setText("");
            //Toast.makeText(getBaseContext(), ""+result, Toast.LENGTH_LONG).show();
            TextView lblmsg=(TextView)findViewById(R.id.lblmessage);
            lblmsg.setText(result);

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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

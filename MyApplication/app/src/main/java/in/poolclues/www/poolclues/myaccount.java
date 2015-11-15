package in.poolclues.www.poolclues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class myaccount extends Activity {

    protected EditText txtemailidverified,txtmobilenoverified,txtverificationcode,txtupdatepassword;
    DBConnection dbConn= new DBConnection();
    String loggeduserid;
    String authtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        // session events start from here //
        Intent intent=getIntent();
        Bundle intentBundle=intent.getExtras();
        loggeduserid=intentBundle.getString("userid");
        authtoken=intentBundle.getString("authtoken");
        EditText txtuserid=(EditText)findViewById(R.id.txtemailidverify);
        txtuserid.setText(loggeduserid);

        // session end from here //

        txtupdatepassword=(EditText)findViewById(R.id.txtupdatepassword);
        txtemailidverified=(EditText)findViewById(R.id.txtemailidverify);
        txtmobilenoverified=(EditText)findViewById(R.id.txtmobilenoverify);
        txtverificationcode=(EditText)findViewById(R.id.txtverificationcode);

        // button event start from here//

        Button btnverify=(Button)findViewById(R.id.btnverify);
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   Toast.makeText(myaccount.this, "AUTH"+txtemailidverified+"TOKE"+txtverificationcode.getText().toString(), Toast.LENGTH_LONG).show();
                final ProgressDialog progressDialog= new ProgressDialog(myaccount.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },5000);

                if (txtupdatepassword.getText().toString() != null)
                {
                    if (txtupdatepassword.getText().toString().equals(""))
                    {
                        Toast.makeText(myaccount.this, "Please Enter New Password", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(myaccount.this, "New Password Updating...", Toast.LENGTH_LONG).show();
                        new HttpAsyncTask().execute(dbConn.APIURL + loggeduserid.toString() + dbConn.URLPwdUpdEnd);
                    }
                }
                else if (txtverificationcode.getText().toString()!=null)
                {
                    if (txtverificationcode.getText().toString().equals(""))
                    {
                        Toast.makeText(myaccount.this, "Please Enter Verification Code", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(myaccount.this, "Verifying...", Toast.LENGTH_LONG).show();
                        new HttpAsyncTask().execute(dbConn.APIURL + loggeduserid.toString() + dbConn.URLEmailVerify);
                    }
                }
                else if (txtmobilenoverified.getText().toString()!=null)
                {
                    if(txtmobilenoverified.getText().toString().equals(""))
                    {
                        Toast.makeText(myaccount.this, "Please Enter Phone No", Toast.LENGTH_LONG).show();
                    }
                    else
                    {   Toast.makeText(myaccount.this, "Contact No Updating...", Toast.LENGTH_LONG).show();
                        new UpdateContactNo().execute(dbConn.APIURL+loggeduserid.toString()+dbConn.URLAddPhoneNO+txtmobilenoverified.getText().toString());
                    }
                }
                else
                {
                    Toast.makeText(myaccount.this, "Awwww....", Toast.LENGTH_LONG).show();
                }

            }
        });


        // button event end from here //
    }

    // start here for cont//

    private class UpdateContactNo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

                ClsAuthToken clsAuthToken = new ClsAuthToken();
                clsAuthToken.setAuth(authtoken.toString());
                return UpdateContact(urls[0], clsAuthToken);

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            Toast.makeText(getBaseContext(), "" +result, Toast.LENGTH_LONG).show();

        }
    }


    public static String UpdateContact(String url, ClsAuthToken clsAuthToken)
    {
        InputStream inputStream=null;
        String result="";

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


    // end here for cont no //


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            if (txtupdatepassword.getText().toString().equals("")) {
                ClsEmailMoVerify clsEmailMoVerify = new ClsEmailMoVerify();
                clsEmailMoVerify.setAuthtoken(authtoken.toString());
                clsEmailMoVerify.setVerification_code(txtverificationcode.getText().toString());
                return EmailVerify(urls[0], clsEmailMoVerify);
            }
            else
            {
                ClsPwdUpdate clsPwdUpdate = new ClsPwdUpdate();
                clsPwdUpdate.setAuthtoken(authtoken.toString());
                clsPwdUpdate.setNew_password(txtupdatepassword.getText().toString());
                return UpdatePwd(urls[0], clsPwdUpdate);
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result.equals("") || result == null){
                Toast.makeText(myaccount.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }
          //  int jsonResult = returnParsedJsonObject(result);
          //  if(jsonResult == 0){
          //      Toast.makeText(login.this, "Invalid username or password or email"+jsonResult, Toast.LENGTH_LONG).show();
          //      return;
        //    }
        //    if(jsonResult==1){
         //       Intent intent = new Intent(login.this,main.class);
         //       intent.putExtra("userid", enteredEmailid);
         //       startActivity(intent);
        //    }
            Toast.makeText(getBaseContext(), "" +result, Toast.LENGTH_LONG).show();

        }
    }

    public static String EmailVerify(String url, ClsEmailMoVerify clsEmailMoVerify)
    {
        InputStream inputStream=null;
        String result="";

        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";
            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("verification_code",clsEmailMoVerify.getVerification_code());
            jsonObject.accumulate("authtoken",clsEmailMoVerify.getAuthtoken());
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

    public static String UpdatePwd(String url, ClsPwdUpdate clsPwdUpdate)
    {
        InputStream inputStream=null;
        String result="";

        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";
            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("authtoken",clsPwdUpdate.getAuthtoken());
            jsonObject.accumulate("new_password",clsPwdUpdate.getNew_password());
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
                //result="PWDUP"+json;

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
        getMenuInflater().inflate(R.menu.menu_myaccount, menu);
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

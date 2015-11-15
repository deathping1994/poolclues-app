package in.poolclues.www.poolclues;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class login extends Activity {

    protected EditText txtuserid,txtpassword;
    String enteredEmailid;
    String enteredPassword;
    String authtoken;
    DBConnection dbConn= new DBConnection();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //final int res=  ServerResponse(dbConn.URLServerSts);







        txtuserid=(EditText)findViewById(R.id.txtusername);
        txtpassword=(EditText)findViewById(R.id.txtpasswrod);
        Button btnsendtosignup=(Button)findViewById(R.id.btnsignup);
        Button btnlogin=(Button)findViewById(R.id.btnlogin);

        final CheckBox chk;
        chk=(CheckBox)findViewById(R.id.chkforgotpasssword);
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chk.isChecked()) {
                Intent in= new Intent(login.this,forgotpwd.class);
                startActivity(in);
                }
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               enteredEmailid=txtuserid.getText().toString();
               enteredPassword=txtpassword.getText().toString();

                    if (enteredEmailid.equals("") || enteredPassword.equals("")) {
                        Toast.makeText(login.this, "User Id and Password must be filled", Toast.LENGTH_LONG).show();
                        return;
                    }
            // loading bar here
          //      Toast.makeText(login.this,"Server Code"+res,Toast.LENGTH_LONG).show();
                final ProgressDialog progressDialog= new ProgressDialog(login.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                new Handler().postDelayed( new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },3000);

            // end loading bar here
      //     Intent intent= new Intent(login.this,main.class);
      //     intent.putExtra("userid", enteredEmailid);
      //     startActivity(intent);
            new HttpAsyncTask().execute(dbConn.URLLogin);
            }
        });

        btnsendtosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(login.this,signup.class);
                startActivity(intent);
            }
        });
    }


    public static String Login(String url, ClsLogin clsLogin)
    {
        InputStream inputStream=null;
        String result="";

        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";
            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("email_id",clsLogin.getEmail_id());
            jsonObject.accumulate("password",clsLogin.getPassword());

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

            ClsLogin clsLogin = new ClsLogin();
            clsLogin.setEmail_id(txtuserid.getText().toString());
            clsLogin.setPassword(txtpassword.getText().toString());
            return Login(urls[0],clsLogin);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result.equals("") || result == null){
                Toast.makeText(login.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if(jsonResult == 0){
                Toast.makeText(login.this, "Invalid username or password or email"+jsonResult, Toast.LENGTH_LONG).show();
                return;
            }
            if(jsonResult==1){
               Intent intent = new Intent(login.this,main.class);
               intent.putExtra("userid", enteredEmailid);
               intent.putExtra("authtoken", authtoken);
               startActivity(intent);
            }
          //  Toast.makeText(getBaseContext(), "" +result, Toast.LENGTH_LONG).show();
        }
    }

    private int ServerResponse(String API)
    {
       int code=0;
        try {

            URL url = new URL(API);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            code = connection.getResponseCode();
            return  code;
        }
        catch (Exception e)
        {

        }
        return  code;
    }

    private int returnParsedJsonObject(String result){
        JSONObject resultObject = null;
        int returnedResult = 0;
        String s=null;
        try {
            resultObject = new JSONObject(result);
            String res=result.toString();
            String success=resultObject.getString("success");
            String authtoken=resultObject.getString("authtoken");
            String[] afterlogin ={authtoken,success};
            s=success.replaceAll("\\s+","");
            if (s.equals("SuccessfullyLoggedin!"))
            {
                afterloginreturn(afterlogin);
                returnedResult=1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    private String[] afterloginreturn(String[] p_value)
    {
        String[] ret_value=p_value;
        String au_th=ret_value[0];
        authtoken=au_th;
      //  Toast.makeText(getBaseContext(), "authtoken" +authtoken, Toast.LENGTH_LONG).show();
        return ret_value;
    }








    public void onBackPressed() {
        //do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

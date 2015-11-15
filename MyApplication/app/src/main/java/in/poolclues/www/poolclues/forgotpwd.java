package in.poolclues.www.poolclues;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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


public class forgotpwd extends Activity {

    protected EditText txtuserid;
    String enteredEmailid;
    DBConnection dbConn= new DBConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);
        txtuserid=(EditText)findViewById(R.id.txtuserid);
        Button btnForgotPwd=(Button)findViewById(R.id.btnforgotpwd);
        btnForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enteredEmailid=txtuserid.getText().toString();
                if (enteredEmailid.equals(""))
                {
                    Toast.makeText(forgotpwd.this, "User Id must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    new HttpAsyncTaskForgotpwd().execute(dbConn.URLForgotPwd+enteredEmailid);
                }
            }
        });

    }


    // forgot password methods start from here //

    private class HttpAsyncTaskForgotpwd extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            ClsForgotPwd clsForgotPwd = new ClsForgotPwd();
            clsForgotPwd.setEmail_id(txtuserid.getText().toString());
            return ForgotPwd(urls[0], clsForgotPwd);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result.equals("") || result == null){
                Toast.makeText(forgotpwd.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }
     //       int jsonResult = returnParsedJsonObject(result);
       //     if(jsonResult == 0){
        //        Toast.makeText(forgotpwd.this, "Invalid username or password or email"+jsonResult, Toast.LENGTH_LONG).show();
        //        return;
        //    }
        //    if(jsonResult==1){
                //  Intent intent = new Intent(login.this,main.class);
                //   intent.putExtra("userid", enteredEmailid);
                //   startActivity(intent);
        //    }
            Toast.makeText(getBaseContext(), "" +result, Toast.LENGTH_LONG).show();
        }
    }



    public static String ForgotPwd(String url, ClsForgotPwd clsForgotPwd)
    {
        InputStream inputStream=null;
        String result="";

        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";
            JSONObject jsonObject= new JSONObject();
            //jsonObject.accumulate("emailid",clsForgotPwd.getEmail_id());
            jsonObject.accumulate("emailid",clsForgotPwd.getEmail_id());
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





    // forgot password methods end from here //



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgotpwd, menu);
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

package in.poolclues.www.poolclues;

import android.app.Activity;
import android.content.Intent;
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


public class invitation extends Activity {

    EditText txtemailid, txtamount;
    Button sendinvitation;
    DBConnection dbConn= new DBConnection();
    String authtoken;
    String emailid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);

        Intent intent=getIntent();
        Bundle intentBundle=intent.getExtras();
        authtoken=intentBundle.getString("authtoken");
        emailid=intentBundle.getString("emailid");

        txtemailid = (EditText) findViewById(R.id.txtemailid);
        txtamount = (EditText) findViewById(R.id.txtamount);
        sendinvitation = (Button) findViewById(R.id.btnsendinvitation);
        sendinvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new HttpAsyncCreateInvite().execute(dbConn.APIURL+dbConn.URLAddContributors);
            }
        });
    }


    private class HttpAsyncCreateInvite extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            ClsEmailAmt clsEmailAmt = new ClsEmailAmt();
            clsEmailAmt.setEmail(txtemailid.getText().toString());
            clsEmailAmt.setAmt(txtamount.getText().toString());
            return CreateInvitation(urls[0], clsEmailAmt);

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), "" + result, Toast.LENGTH_LONG).show();

        }
    }

    public static String CreateInvitation(String url, ClsEmailAmt clsEmailAmt) {
            InputStream inputStream = null;
            String result = "";
            result = url;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                String json = "";
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email_id", clsEmailAmt.getEmail());
                jsonObject.accumulate("amount", clsEmailAmt.getAmt());
                json = jsonObject.toString();
                StringEntity se = new StringEntity(json);
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                } else {
                    result = "Did not work";
                }
            } catch (Exception ex) {

            }
            return result;
        }


        private static String convertInputStreamToString(InputStream inputStream) throws IOException {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_invitation, menu);
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

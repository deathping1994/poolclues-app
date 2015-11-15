package in.poolclues.www.poolclues;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class groupcreate extends Activity implements View.OnClickListener {


    protected EditText txt_event_name,txt_target_date,txt_target_amount,txt_description,txt_invites,txt_products,txt_msg,txt_public,txtcontributor,txtmycontribution,txtcontiamount;
    DBConnection dbConn= new DBConnection();
    private ImageButton ib;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    String authtoken;
    String emailid;
    String finaldate;
    String searchable="true";
    private TextView lblmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupcreate);

        // get values from another page that is below //

        Intent intent=getIntent();
        Bundle intentBundle=intent.getExtras();
        authtoken=intentBundle.getString("authtoken");
        emailid=intentBundle.getString("emailid");
        // get vlaue from another page end here //


        ib = (ImageButton) findViewById(R.id.btnCalendar);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        txt_target_date = (EditText)findViewById(R.id.txttargetdate);
        ib.setOnClickListener(this);

        txt_event_name=(EditText)findViewById(R.id.txteventname);
        txt_target_date=(EditText)findViewById(R.id.txttargetdate);
        txt_target_amount=(EditText)findViewById(R.id.txttargetamt);
        txt_description=(EditText)findViewById(R.id.txtdescription);
        lblmessage=(TextView)findViewById(R.id.lblfooter);
        txt_msg=(EditText)findViewById(R.id.txtmessage);
        txtcontributor=(EditText)findViewById(R.id.txtcontributor);
        txtmycontribution=(EditText)findViewById(R.id.txtmycontribution);
        txtcontiamount=(EditText)findViewById(R.id.txtcontiamount);
        finaldate=txt_target_date.getText().toString();
        Button btncreate=(Button)findViewById(R.id.btnCreateEvent);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpAsyncCreateGroup().execute(dbConn.URLCreatPool);
           //     Toast.makeText(groupcreate.this,"ja"+ja,Toast.LENGTH_LONG).show();
            }
        });
    }

    private class HttpAsyncCreateGroup extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
             JSONArray j=new JSONArray();
            for(int i=0;i<=1;i++) {
               try {
                   j.put(new JSONObject(GetContributionDetails()));
               }
               catch (Exception e)
               {
                   System.out.println(e.toString());
               }
               }
            ClsCreate_Event clsEvent= new ClsCreate_Event();
            clsEvent.setEmail_id(emailid);
            clsEvent.setEvent_name(txt_event_name.getText().toString());
            clsEvent.setTarget_date(txt_target_date.getText().toString().replaceAll("/", ""));
            clsEvent.setTarget_amount(Integer.parseInt(txt_target_amount.getText().toString()));
            clsEvent.setDescription(txt_description.getText().toString());
            clsEvent.setContributor(j);
            clsEvent.setMsg(txt_msg.getText().toString());
            clsEvent.setAuthtoken(authtoken);
            clsEvent.setSearchable(searchable);
            return CreateEvent(urls[0],clsEvent);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), "" + result, Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
            int jsonResult = returnParsedJsonObject(result);
            if(jsonResult == 1)
            {

                String msg="Event Group successfully created !";
                lblmessage.setText(msg);
               // return;
           }
            else
            {
                String msg="Opps Error !"+result;
                lblmessage.setText(msg);
              //  return;
            }
        }
    }

    private String GetContributionDetails()
    {
        ClsContri clsContri = new ClsContri();
        clsContri.setEmail_id(txtcontributor.getText().toString());
        clsContri.setAmount(txtcontiamount.getText().toString());
        return  ContriDetails(clsContri);
    }

    public static String ContriDetails(ClsContri clsContri)
    { String result;
        try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("email_id" , clsContri.getEmail_id());
                jsonObject.accumulate("amount" , clsContri.getAmount());
                return jsonObject.toString();
        }
        catch (Exception e)
        {
          result="ex"+e.toString();

        }
        return result;
    }

    public static String CreateEvent(String url, ClsCreate_Event clsCreate_event)
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
            jsonObject.accumulate("email_id",clsCreate_event.getEmail_id());
            jsonObject.accumulate("pool_name",clsCreate_event.getEvent_name());
            jsonObject.accumulate("target_date",clsCreate_event.getTarget_date());
            jsonObject.accumulate("target_amount",clsCreate_event.getTarget_amount());
            jsonObject.accumulate("description",clsCreate_event.getDescription());
            jsonObject.put("contributors", clsCreate_event.getContributor());
            jsonObject.accumulate("msg",clsCreate_event.getMsg());
            jsonObject.accumulate("authtoken",clsCreate_event.getAuthtoken());
            jsonObject.accumulate("searchable",clsCreate_event.getSearchable());
            json=jsonObject.toString();
            System.out.println(json);

//            json=json.replaceAll("\\\\","");
//            json=json.replaceAll("\\"{,"{");
            StringEntity se= new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept","application/json");
            httpPost.setHeader("Content-type","application/json");
            HttpResponse httpResponse=httpClient.execute(httpPost);
            inputStream=httpResponse.getEntity().getContent();
            if (inputStream!=null)
            {
                result = convertInputStreamToString(inputStream);
               //result=""+clsCreate_event.getContributor().toString().substring(1,clsCreate_event.getContributor().toString().length()-1);
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


    private int returnParsedJsonObject(String result){
        JSONObject resultObject = null;
        int returnedResult = 0;
        String s=null;
        try {
            resultObject = new JSONObject(result);
            String res=result.toString();
            String success=resultObject.getString("success");
            s=success.replaceAll("\\s+","");
            if (s.equals("Eventcreatedsuccessfully."))
            {
                returnedResult=1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }

    @Override
    public void onClick(View v) {
        showDialog(0);

    }

    // @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            //txt_target_date.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
            //      + selectedYear);
            txt_target_date.setText(selectedDay +"" +(selectedMonth + 1) +""+  selectedYear);
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groupcreate, menu);
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

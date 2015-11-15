package in.poolclues.www.poolclues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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


public class main extends Activity {

    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=1;
    Uri imageUri = null;
    static TextView imageDetails= null;
    public static ImageView showImg=null;
    main CameraActivity= null;
    String loggeduserid;
    String authtokenreceived;
    DBConnection dbConn= new DBConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera();


        // all session intent here //

        Intent intent=getIntent();
        Bundle intentBundle=intent.getExtras();
        loggeduserid=intentBundle.getString("userid");
        authtokenreceived=intentBundle.getString("authtoken");
        TextView lbluser=(TextView)findViewById(R.id.lblwelcomeuser);
        TextView lblauthtoken=(TextView)findViewById(R.id.lblauthtoken);
        lbluser.setText(loggeduserid);
        lblauthtoken.setText(authtokenreceived);

        // all session intent end here //



        // click events start from here //

        Button btnmyProfile=(Button)findViewById(R.id.btn_myprofile);
        btnmyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(main.this,myaccount.class);
                intent.putExtra("userid",loggeduserid);
                intent.putExtra("authtoken",authtokenreceived);
                startActivity(intent);
            }
        });

        Button btnCreatEvent=(Button)findViewById(R.id.btn_createevent);
        btnCreatEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(main.this,create_event.class);
                intent.putExtra("authtoken", authtokenreceived);
                intent.putExtra("emailid", loggeduserid);
                startActivity(intent);
            }
        });

        Button btnChoosegift=(Button)findViewById(R.id.btn_choosegift);
        btnChoosegift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(main.this,choosegift.class);
                intent1.putExtra("authtoken", authtokenreceived);
                startActivity(intent1);
            }
        });

        Button btninvitation=(Button)findViewById(R.id.btn_invitefriend);
        btninvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invi= new Intent(main.this,invitation.class);
                invi.putExtra("authtoken", authtokenreceived);
                invi.putExtra("emailid", loggeduserid);
                startActivity(invi);
            }
        });

        Button btncontri=(Button)findViewById(R.id.btn_contribute);
        btncontri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent contri= new Intent(main.this,mywallet.class);
                contri.putExtra("authtoken", authtokenreceived);
                contri.putExtra("emailid", loggeduserid);
                startActivity(contri);
            }
        });

        Button btnLogout=(Button)findViewById(R.id.btnlogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new HttpAsyncTask().execute(dbConn.URLLogOut+loggeduserid);
                final ProgressDialog progressDialog= new ProgressDialog(main.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        },4000);

                Intent intent =new Intent(main.this,login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }


    // code for log out start from here //


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            ClsLogout clsLogout = new ClsLogout();
            clsLogout.setVerification_code(authtokenreceived.toString());
            return Logout(urls[0], clsLogout);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if(result.equals("") || result == null){
                Toast.makeText(main.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }
          //  int jsonResult = returnParsedJsonObject(result);
          //  if(jsonResult == 0){
           //     Toast.makeText(login.this, "Invalid username or password or email"+jsonResult, Toast.LENGTH_LONG).show();
           //     return;
          //  }
         //   if(jsonResult==1){
         //       Intent intent = new Intent(login.this,main.class);
         //       intent.putExtra("userid", enteredEmailid);
         //       startActivity(intent);
         //   }
            Toast.makeText(getBaseContext(), "" +result, Toast.LENGTH_LONG).show();
        }
    }


    public static String Logout(String url, ClsLogout clsLogout)
    {
        InputStream inputStream=null;
        String result="";

        try
        {
            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);
            String json="";
            JSONObject jsonObject= new JSONObject();
            jsonObject.accumulate("authtoken",clsLogout.getVerification_code());

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



    // code for log out end form here //




// camera code start from here //

    public void camera()
    {
        CameraActivity=this;
        Button btnCamera=(Button)findViewById(R.id.btn_myselfie);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename="Camera_Poolclues.jpg";
                ContentValues values= new ContentValues();
                values.put(MediaStore.Images.Media.TITLE,filename);
                values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
                imageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    protected void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if ( resultCode == RESULT_OK) {
                String imageId = convertImageUriToFile( imageUri,CameraActivity);
                new LoadImagesFromSDCard().execute(""+imageId);
            } else if ( resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String convertImageUriToFile( Uri imageUri, Activity activity )
    {
        Cursor cursor = null;
        int imageID = 0;
        try {
            String [] proj={MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID,MediaStore.Images.Thumbnails._ID,MediaStore.Images.ImageColumns.ORIENTATION};
            cursor =activity.managedQuery(imageUri,proj,null,null,null);
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int size = cursor.getCount();
            if (size == 0) {
                imageDetails.setText("No Image");
            }
            else
            {
                int thumbID = 0;
                if (cursor.moveToFirst()) {
                    imageID= cursor.getInt(columnIndex);
                    thumbID= cursor.getInt(columnIndexThumb);
                    String Path = cursor.getString(file_ColumnIndex);
                    String CapturedImageDetails = " PoolCluesImageDetails : \n\n"+" ImageID :"+imageID+"\n"+" ThumbID :"+thumbID+"\n"+" Path :"+Path+"\n";
                    imageDetails.setText( CapturedImageDetails );
                }
            }
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return""+imageID;
    }


    public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(main.this);
        Bitmap mBitmap;

        protected void onPreExecute() {
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }

        protected Void doInBackground(String... urls) {
            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;
            try {
                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                if (bitmap != null) {
                    newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);
                    bitmap.recycle();
                    if (newBitmap != null) {
                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {

                cancel(true);
            }
            return null;
        }

        protected void onPostExecute(Void unused) {

            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if (mBitmap != null) {
                // Set Image to ImageView

                showImg.setImageBitmap(mBitmap);
            }

        }
    }


// camera code end here //











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

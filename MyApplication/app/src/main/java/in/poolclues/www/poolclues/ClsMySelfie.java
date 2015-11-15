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
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Shyam Yadav on 20-Oct-15.
 */
public class ClsMySelfie extends Activity {

    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE=1;
    Uri imageUri = null;
    static TextView imageDetails= null;
    public static ImageView showImg=null;
    ClsMySelfie CameraActivity= null;


    public void Camera()
    {
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
        private ProgressDialog Dialog = new ProgressDialog(ClsMySelfie.this);
        Bitmap mBitmap;

        protected void onPreExecute() {
            Dialog.setMessage("Loading Image from SDcard..");
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

}

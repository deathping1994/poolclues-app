package in.poolclues.www.poolclues;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.IOException;


/**
 * Created by Shyam Yadav on 05-Nov-15.
 */
public class ClsNotification extends Activity {

    private NotificationManager notificationManager;
    //public void sendBasicNotification(String message) {

       // notificationManager = getNotificationManager();

     //   Notification.Builder build = new Notification.Builder(this)  //using the Notification.Builder class
      //          .setContentTitle("New JellyBean Notification")
      //          .setContentText("This is just an example")
     //           .setSmallIcon(R.drawable.noti)
     //           .setPriority(Notification.PRIORITY_HIGH)
      //          .setSmallIcon(R.drawable.noti);

        //  .addAction(
        //          android.R.drawable.ic_btn_speak_now,
        //          "Speak",
        //          PendingIntent.getActivity(getApplicationContext(), 0,getIntent(), 0, null))

        //    .addAction(
        //           android.R.drawable.ic_dialog_email,
        //          "Email",
        //          PendingIntent.getActivity(getApplicationContext(), 0,getIntent(), 0, null))

        //            .addAction(
//                        android.R.drawable.ic_dialog_info,"Info",
        //                      PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), 0, null));

 //       Notification notification = new Notification.BigPictureStyle(build)
   //             .bigPicture
   //                     (
     //                           BitmapFactory.decodeResource(getResources(), R.drawable.noti)).build();  //show image along with the notification
     //   Intent notificationIntent = new Intent(this, login.class);
     //   notificationManager.notify(0, notification);

 //   }
  //  private NotificationManager getNotificationManager()
  //  {
   //     return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 //   }

}

package robert.sysc3010;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class NotifyClass extends AppCompatActivity {//GARBAGE CLASS REMOVE WHEN DONE
    //ONLY HERE SO I CAN REMEBER THE METHODS


    public static NotificationCompat.Builder builder;
    public static Intent intent;
    public static TaskStackBuilder stackBuilder;
    public static  PendingIntent pendingIntent;
    public static NotificationManager notificationManager ;
    Context ctx;

    NotifyClass (){
        ctx = NotifyClass.this;

    }


       @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);

    }
    public void showNotifications(){//Creates a notification that leads to the NotifyClass when pushed

        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.alert);
        builder.setContentTitle("Click Here to Notify Contacts");
        builder.setContentText("You have been in a minor crash, clicking here will allow you to notify your contacts");
        //when the notification is pressed
        intent = new Intent(this,GPS.class);
        stackBuilder = TaskStackBuilder.create(NotifyClass.this);
        stackBuilder.addParentStack(Login.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


    }
    public void runIt() {
        notificationManager.notify(0, builder.build());
    }



}

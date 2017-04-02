
package robert.sysc3010;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.system.Os;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class BackgroundTask extends AsyncTask<String,String,String> {

    private static final String TAG = Register.class.getSimpleName();
    //TODO on post execute we should check if the username is in use, then TOAST them and send them back to MainMenu
       /*
        If using physical device you need the server ip + server listen port
        apache's listen port is 80
        if using emulator, you can use "localhost" or "127.0.0.1" with no port
         */
    String reg_url = "http://192.168.1.146:80/register.php";
    String log_url = "http://192.168.1.146:80/checkDB.php";
    String gps_url = "http://192.168.1.146:80/gps.php";
    String eventCheck_url = "http://192.168.1.146:80/eventCheck.php";

    public String testing;
    public String mainSignIn = "Robert";
    Context ctx;
    public boolean check = false;
    public boolean finished =false;

    public boolean checkDone = false;
    public String eventType="1";
    public String prevEventType="1";


    public Object lock = new Object();



    BackgroundTask(Context ctx){

        this.ctx = ctx;
    }
    @Override
    protected void onPostExecute(String result) {

        finished = true;
        if (testing == "done") {
            Toast.makeText(ctx, "User Registered!", Toast.LENGTH_LONG).show();
        }
        if (check){
            Toast.makeText(ctx, "Successful Login!", Toast.LENGTH_LONG).show();
            //TODO move to next activity
        }
        else if(!check){
            Toast.makeText(ctx, "Login Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String doInBackground(String... params) {


        String option = params[0];
        if(option.equals("login")){
            String username = params[1];
            String password = params[2];
            Log.d(TAG,""+password);
            mainSignIn=username;
            check = checkDB(username,password);//TODO THIS WILL RETURN THE PASSWORD!
            Log.d(TAG,""+check);
            finished=true;

        }
        if(option.equals("register")) {
            String username = params[1];
            String password = params[2];
            mainSignIn = username;
            sqlCommunicateHelper(reg_url, username, password, "register");
        }
        else if(option.equals("GPS")) {
            String latitude = params[1];
            String longitude = params[2];
            sqlCommunicateHelper(gps_url, latitude, longitude, "GPS");
        }
        else if(option.equals("Event")){

                synchronized (lock) {
                    while (true) {
                        listenForEvents();
                        if (!eventType.equals(prevEventType)) {
                            if (eventType.equals("2")) {
                                Log.d(TAG, "NOTIFICATION 2 BEING CREATED");
                                publishProgress(eventType);
                                //Login nc = new Login();
                                //nc.showNotifications();
                            }
                            if (eventType.equals("3")) {
                                Log.d(TAG, "NOTIFICATION 3 BEING CREATED");
                                publishProgress(eventType);
                                break;
                            }
                        }
                        try {
                            lock.wait(5000);
                            lock.notify();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


        }
        return null;
    }//end method


    public void listenForEvents(){
        try {
            URL url = new URL(eventCheck_url);//Create url for registering users
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Create connection with URL
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String result = br.readLine();//contains the password for the user
            Log.d(TAG, "" + "EVENT IS OF TYPE: " + result);

            prevEventType=eventType;
            Log.d(TAG, "" + "PREVIOUS EVENT IS OF TYPE: " + prevEventType);
            if(result.equals("1")){
                eventType="1";
                checkDone = true;
                }
            else if(result.equals("2")){
                eventType="2";
                checkDone = true;
                }
            else if(result.equals("3")){
                //stop everything and send SMS to contacts
                eventType="3";
                checkDone = true;

                }
            inputStream.close();
            br.close();

        }
        catch (MalformedURLException e){
            //URL MISTAKE
            e.printStackTrace();
        }
        catch (IOException e ){
            e.printStackTrace();
        }

    }


    public boolean checkDB(String username,String password){
        try {
            URL url = new URL(log_url);//Create url for registering users
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Create connection with URL
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream OS = connection.getOutputStream();//Transmit data by writing to this stream
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data = URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8");
            bw.write(data);
            bw.flush();

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String result = br.readLine();//contains the password for the user
            Log.d(TAG,""+result);
            if(result.equals("false")){
                //Username not in Database
            }
            else if(result.equals(password)){//right password
                return true;
            }
            OS.close();
            bw.close();
            inputStream.close();
            br.close();
            return false;
        }
        catch (MalformedURLException e){//Thrown by URL,
            e.printStackTrace();
            Log.d(TAG,"MalformException occured");
        }
        catch (IOException e){
            e.printStackTrace();
            Log.d(TAG,"IOEception Occured");
        }
        return false;
    }


    public void sqlCommunicateHelper(String urlInput, String input1,String input2,String mode ){
        String username=null;
        String password = null;
        String latitude = null;
        String longitude = null;

        if(mode.equals("register")){
            username = input1;
            password = input2;
        }
        else if(mode.equals("login")){
            username = input1;
            password = input2;
        }
        else if(mode.equals("GPS")){
            latitude = input1;
            longitude = input2;
        }

        try{
            String data = "";
            String dataReg ="";
            URL url = new URL(urlInput);//Create url for registering users
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();//Create connection with URL
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream OS = connection.getOutputStream();//Transmit data by writing to this stream
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));

            if(mode.equals("register")){
                Log.d(TAG,"register selected");
                data = URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8")+ "&"+URLEncoder.encode("user_password","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8");
            }
            else if(mode.equals("login")){
                Log.d(TAG,"Login selected");
                data = URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8")+ "&"+URLEncoder.encode("user_password","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8");
                dataReg = URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8");//Check if username is in use;
            }
            else if(mode.equals("GPS")){
                Log.d(TAG,"GPS selected");
                data = URLEncoder.encode("lat","UTF-8") + "=" + URLEncoder.encode(latitude,"UTF-8")+ "&"+URLEncoder.encode("long","UTF-8") + "=" + URLEncoder.encode(longitude,"UTF-8");
            }

            bw.write(data);
            bw.flush();
            OS.close();
            bw.close();
            InputStream IS = connection.getInputStream();
            IS.close();

        }
        catch (MalformedURLException e){//Thrown by URL,
            e.printStackTrace();
            Log.d(TAG,"MalformException occured");
        }
        catch (IOException e){
            e.printStackTrace();
            Log.d(TAG,"IOEception Occured");
        }



    }


    @Override
    protected void onProgressUpdate(String... values) {

            if (eventType.equals("2")) {
                Log.d(TAG, "event 2 notification");
                showNotifications();


            } else if (eventType.equals("3")) {
                Log.d(TAG, "event 3 notification");
                showNotifications();
                notifyNow();

                //immediately change to gps.class and notify everyone
            }
        }

    public void showNotifications(){//Creates a notification that leads to the NotifyClass when pushed

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setSmallIcon(R.drawable.alert);
        builder.setContentTitle("Click Here to Notify Contacts");
        builder.setContentText("You have been in a minor crash, clicking here will allow you to notify your contacts");
        //when the notification is pressed
        Intent intent = new Intent(ctx,GPS.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(NotifyClass.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());

    }
    public void notifyNow(){
        Intent intent = new Intent(ctx,GPS.class);
        ctx.startActivity(intent);
        GPS myGPS = new GPS();
        myGPS.sendNow = true;

    }

}

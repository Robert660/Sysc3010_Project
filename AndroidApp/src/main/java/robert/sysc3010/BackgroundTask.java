
package robert.sysc3010;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class BackgroundTask extends AsyncTask<String,String,String> {

    private static final String TAG = Register.class.getSimpleName();
    //TODO on post execute we should check if the username is in use, then TOAST them and send them back to MainMenu
       /*
        If using physical device you need the server ip + server listen port
        apache's listen port is 80
        if using emulator, you can use "localhost" or "127.0.0.1" with no port
         */
    static String reg_url = "http://192.168.43.18:80/register.php"; //php to register users
    static String log_url = "http://192.168.43.18:80/checkDB.php";//php to check db for username
    static String gps_url = "http://192.168.43.18:80/gps.php"; // php to add gps location to the username
    static String eventCheck_url = "http://192.168.43.18:80/eventCheck.php"; // polls the event type

    static String usernameMaster ;

    public String testing;
    Context ctx;
    public boolean check = false;
    public boolean finished =false;
    public boolean checkDone = false;

    public String eventType="1";//Starting event is of type 1
    public String prevEventType="1";//the starting event is of type 1

    public Object lock = new Object();
    public boolean logInToast = false;




    BackgroundTask(Context ctx){

        this.ctx = ctx;
    }
    @Override
    protected void onPostExecute(String result) {
        //once the doInBackground is complete, onPostExecute gets ran
        finished = true;
        if (testing == "done") {
            Toast.makeText(ctx, "User Registered!", Toast.LENGTH_LONG).show();
        }
        if (check && logInToast){
            Toast.makeText(ctx, "Successful Login!", Toast.LENGTH_LONG).show();
        }
        else if(!check && logInToast){
            Toast.makeText(ctx, "Login Failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String doInBackground(String... params) {

        Log.d(TAG,reg_url);

        String option = params[0];
        if(option.equals("login")){
            logInToast = true;
            String username = params[1];
            String password = params[2];
            usernameMaster = username;

            Log.d(TAG,""+password);

            check = checkDB(username,password);//checkDB returns the password of the user
            finished=true;

        }
        if(option.equals("register")) {
            String username = params[1];
            String password = params[2];
            usernameMaster = username;

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
                                publishProgress(eventType);//interupts the main thread to run method on progressUpdate(String)

                            }
                            if (eventType.equals("3")) {
                                Log.d(TAG, "NOTIFICATION 3 BEING CREATED");
                                publishProgress(eventType);
                                break;//Stop running and sms contacts
                            }
                        }
                        try {
                            lock.wait(2000);//poll the database every 5 seconds
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
        //method that polls the table Events, there is only 1 row that has 1 value for event
        //event 1 is normal execution
        //event 2 is notify the user with push notification
        //event 3 is notify + send sms to all the selected contacts
        synchronized (lock) {
            try {
                URL url = new URL(eventCheck_url);//Create url for registering users
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Create connection with URL
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String result = br.readLine();//contains the password for the user
                
                /*Since we are trying to connect to a databse on a rpi, the connection will be slow
                Therefore when reading the line we may see a null value which is <br />, 
                in this case we wait 1 sec then try reading the line again                
                */
                
                while (result.equals("<br />")) { 
                    try {
                        wait(1000);
                        lock.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result = br.readLine();//contains the password for the user
                }
                if(result.contains("failed")){//This could be cause since the connection timed out
                    inputStream.close();
                    br.close();
                    listenForEvents();
                }

                Log.d(TAG, "" + "EVENT IS OF TYPE: " + result);

                prevEventType = eventType;
                //prevEventType holds the previous event,
                // i will compare the difference between the two and if it changes
                //then we do something.

                Log.d(TAG, "" + "PREVIOUS EVENT IS OF TYPE: " + prevEventType);
                if (result.equals("1")) {
                    eventType = "1";
                    checkDone = true;
                } else if (result.equals("2")) {
                    eventType = "2";
                    checkDone = true;
                } else if (result.equals("3")) {
                    //stop everything and send SMS to contacts
                    eventType = "3";
                    checkDone = true;

                }
                inputStream.close();
                br.close();

            } catch (MalformedURLException e) {
                //URL MISTAKE
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public boolean checkDB(String username,String password){
        //This method calls the php to poll the Users table for the username,  then check if
        //it matches the password, it will return true if that is the case
        //or it will return false otherwise
        String username1 = username;
        String password1 = password;
        synchronized (lock) {
            try {
                URL url = new URL(log_url);//Create url for registering users
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Create connection with URL
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream OS = connection.getOutputStream();//Transmit data by writing to this stream
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                bw.write(data);
                bw.flush();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String result = br.readLine();//contains the password for the user
                while (result.equals("<br />")) {
                    try {
                        wait(1000);
                        lock.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result = br.readLine();//contains the password for the user
                }
                if (result.contains("failed")) {
                    OS.close();
                    bw.close();
                    inputStream.close();
                    br.close();
                    checkDB(username1, password1);
                }
                Log.d(TAG, "" + result);
                if (result.equals("false")) {
                    //Username not in Database
                } else if (result.equals("retry") || result.equals("retryFalse")) {
                    OS.close();
                    bw.close();
                    inputStream.close();
                    br.close();
                    checkDB(username, password);

                } else if (result.equals(password)) {//right password
                    return true;
                }
                OS.close();
                bw.close();
                inputStream.close();
                br.close();
                return false;
            } catch (MalformedURLException e) {//Thrown by URL,
                e.printStackTrace();
                Log.d(TAG, "MalformException occured");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOEception Occured");
            }
        }
        return false;
    }


    public void sqlCommunicateHelper(String urlInput, String input1,String input2,String mode ){
        //helper method to communicate with db

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
        synchronized (lock) {
            try {
                String data = "";
                String dataReg = "";
                URL url = new URL(urlInput);//Create url for registering users
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();//Create connection with URL
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream OS = connection.getOutputStream();//Transmit data by writing to this stream
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);


                if (mode.equals("register")) {
                    Log.d(TAG, "register selected");
                    data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                } else if (mode.equals("login")) {
                    Log.d(TAG, "Login selected");
                    data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" + URLEncoder.encode("user_password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                    dataReg = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");//Check if username is in use;
                } else if (mode.equals("GPS")) {//use usernameMaster
                    Log.d(TAG, "GPS selected");

                    data = URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") + "&" + URLEncoder.encode("long", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8") + "&"
                            + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(usernameMaster, "UTF-8");
                }

                String result = br.readLine();//contains the password for the user
                Log.d(TAG, "" + result);
                Log.d("TESTING:BT",""+result);
                while (result.contains("<br />")) {
                    try {
                        lock.wait(1000);
                        lock.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    result = br.readLine();//contains the password for the user
                }
                if (result.contains("failed")) {
                    OS.close();
                    bw.close();
                    inputStream.close();
                    br.close();
                    sqlCommunicateHelper(urlInput,input1,input2,mode);
                }
                if (result.equals("retry") || result.contains("retryFalse")) {
                    OS.close();
                    bw.close();
                    inputStream.close();
                    br.close();
                    sqlCommunicateHelper(urlInput, input1, input2, mode);

                }
                bw.write(data);
                bw.flush();
                OS.close();
                bw.close();
                InputStream IS = connection.getInputStream();
                IS.close();

            } catch (MalformedURLException e) {//Thrown by URL,
                e.printStackTrace();
                Log.d(TAG, "MalformException occured");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOEception Occured");
            }
        }



    }


    @Override
    protected void onProgressUpdate(String... values) {
        //this gets run when publish gets invoked.

            if (eventType.equals("2")) {
                Log.d(TAG, "event 2 notification");
                showNotifications("Minor");


            } else if (eventType.equals("3")) {
                Log.d(TAG, "event 3 notification");
                showNotifications("MAJOR");
                //notifyNow();

                //immediately change to gps.class and notify everyone
            }
        }

    public void showNotifications(String event){//Creates a notification that leads to the NotifyClass when pushed

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setSmallIcon(R.drawable.alert);
        builder.setContentTitle("Click Here to Notify Contacts");
        builder.setContentText(event+" crash, clicking here will allow you to notify your contacts");
        //when the notification is pressed
        Intent intent = new Intent(ctx,GPS.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(AddContacts.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setVibrate(new long[]{1000,1000});

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());

    }
    public void notifyNow(){
        // this is called for event 3
        //this will create a new GPS instance
        // since we cannot invoke any of the systemServices from here
        //i will instead set a static flag in GPS where gps can check for if true, then it will send SMS
        Intent intent = new Intent(ctx,GPS.class);
        ctx.startActivity(intent);
        GPS myGPS = new GPS();
        myGPS.sendNow = true;

    }

}

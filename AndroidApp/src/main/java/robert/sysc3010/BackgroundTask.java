
package robert.sysc3010;


import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import android.content.Intent;
import android.os.AsyncTask;
import android.system.Os;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class BackgroundTask extends AsyncTask<String,Void,String> {

    //TODO on post execute we should check if the username is in use, then TOAST them and send them back to MainMenu

    Context ctx;
    private static final String TAG = Register.class.getSimpleName();
    public String testing;
    public String mainSignIn = "Robert";
    BackgroundTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPostExecute(String result) {
        if (testing == "done") {
            Toast.makeText(ctx, "User Registered!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        /*
        If using physical device you need the server ip + server listen port
        apache's listen port is 80
        if using emulator, you can use "localhost" or "127.0.0.1" with no port
         */
        String reg_url = "http://192.168.1.146:80/register.php";
        String log_url = "http://192.168.1.146:80/login.php";
        String gps_url = "http://192.168.1.146:80/gps.php";

        String option = params[0];
        if(option.equals("login")){
            String username = params[1];
            String password = params[2];
            mainSignIn=username;


        }
        if(option.equals("register")){
            String username = params[1];
            String password = params[2];
            mainSignIn=username;


            try{//Log.d will output to logcat so i can see at what step in the process we are at
                Log.d(TAG,"Registration starting");
                URL url = new URL(reg_url);//Create url for registering users
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();//Create connection with URL
                Log.d(TAG,"Connecting to URL..."+connection.toString());
                connection.setRequestMethod("POST");
                Log.d(TAG,"Request Method set to POST");
                connection.setDoOutput(true);
                Log.d(TAG,"Do output set to True");
                Log.d(TAG, "Creating output Stream");
                OutputStream OS = connection.getOutputStream();//Transmit data by writing to this stream
                Log.d(TAG, "Oputput Stream Created");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("username","UTF-8") + "=" + URLEncoder.encode(username,"UTF-8")+ "&"+URLEncoder.encode("user_password","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8");
                Log.d(TAG,"Data Encoded and Sent");
                bw.write(data);
                bw.flush();
                bw.close();
                OS.close();
                InputStream IS = connection.getInputStream();
                IS.close();
                Log.d(TAG,"Registration Finished");
                testing="done";
                return "Registration Success...";
            }//end try

            catch (MalformedURLException e){//Thrown by URL
                e.printStackTrace();
                Log.d(TAG,"MalformException occured");
                return null;
            }
            catch (IOException e){
                e.printStackTrace();
                Log.d(TAG,"IOEception Occured");
                return null;
            }
        }//end if register
        else if(option.equals("GPS")){
            String latitude = params[1];
            String longitude = params[2];
            try{
                Log.d(TAG,"GPS updating");
                URL url = new URL(gps_url);//Create url for registering users
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();//Create connection with URL
                Log.d(TAG,"Connecting to URL..."+connection.toString());
                connection.setRequestMethod("POST");
                Log.d(TAG,"Request Method set to POST");
                connection.setDoOutput(true);
                Log.d(TAG,"Do output set to True");
                Log.d(TAG, "Creating output Stream");
                OutputStream OS = connection.getOutputStream();//Transmit data by writing to this stream
                Log.d(TAG, "Oputput Stream Created");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("lat","UTF-8") + "=" + URLEncoder.encode(latitude,"UTF-8")+ "&"+URLEncoder.encode("long","UTF-8") + "=" + URLEncoder.encode(longitude,"UTF-8");
                     //   +"&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(mainSignIn,"UTF-8");
                    //for now we will use a master HC username stored in the PHP
                Log.d(TAG,"Data Encoded and Sent");
                bw.write(data);
                bw.flush();
                bw.close();
                OS.close();
                InputStream IS = connection.getInputStream();
                IS.close();
                Log.d(TAG,"GPS UPDATED");
                return "Registration Success...";
            }
            catch (MalformedURLException e){//Thrown by URL,
                e.printStackTrace();
                Log.d(TAG,"MalformException occured");
                return null;
            }
            catch (IOException e){
                e.printStackTrace();
                Log.d(TAG,"IOEception Occured");
                return null;
            }
        }
        return null;
    }//end method

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}

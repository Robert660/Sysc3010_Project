


package robert.sysc3010;


import android.content.Context;
import android.os.AsyncTask;
import android.system.Os;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String,Void,Void> {

    Context ctx;

    BackgroundTask(Context ctx){
        this.ctx = ctx;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(String... params) {
        String reg_url = "http://10.0.2.2/webapp/register.php";//TODO change these and write them
        String log_url = "http://10.0.2.2/webapp/login.php";

        String option = params[0];
        if(option.equals("register")){
            String username = params[1];
            String password = params[2];
            try{
                URL url = new URL(reg_url);
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream OS = connection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(OS));
               // String data = URLEncoder.encode("user","UTF-8") + " =" + URLEncoder.encode(username,"UTF-8")+ "&"


            }
            catch (MalformedURLException e){

            }
            catch (IOException e1){

            }





        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}

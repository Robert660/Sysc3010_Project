package robert.sysc3010;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HiddenChangeBT extends AppCompatActivity{
    //hidden class to update ip:port so that testing is easier.

    Button hiddenChange;
    EditText ipPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_change_bt);
        ipPort = (EditText) findViewById(R.id.ipPort);
        hiddenChange = (Button) findViewById(R.id.hiddenChange);

        hiddenChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.hiddenChange://if it was the login button that was pressed
                        changeBT();
                        finish();
            }
        }

    });
    }



    public void changeBT(){

        BackgroundTask bt = new BackgroundTask(this);
        String ipAndPort = ipPort.getText().toString();
        String reg_url = "http://"+ipAndPort+"/register.php";
        String log_url = "http://"+ipAndPort+"/checkDB.php";
        String event_url = "http://"+ipAndPort+"/eventCheck.php";
        String gps_url = "http://"+ipAndPort+"/gps.php";
        Log.d("BT",""+reg_url);
        Log.d("BT",""+log_url);
        Log.d("BT",""+event_url);
        Log.d("BT",""+gps_url);
        bt.reg_url = reg_url;
        bt.log_url = log_url;
        bt.eventCheck_url = event_url;
        bt.gps_url = gps_url;

    }
}

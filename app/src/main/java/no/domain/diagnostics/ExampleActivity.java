package no.domain.diagnostics;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import no.domain.diagnostics.AlertDialogs.CustomAlertDialog;

/**
 * This class is not intended for inclusion in the final version of the app. Use it to test/store
 * any code that might be of some use later.
 **/
public class ExampleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        Intent intent=registerReceiver(null,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale=intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        if(25.0f<((float)level/(float)scale)*100.0f){
            ((CheckBox)findViewById(R.id.battery)).setChecked(true);
        }
        else{
            CustomAlertDialog.newInstance("WARNING", "Your battery is below 25%. This may lead to camera malfunctions in some phone models.", "Okay").show(getFragmentManager(),"alerts");
        }

        PackageManager packageManager=this.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            ((CheckBox)findViewById(R.id.camera)).setChecked(true);
        }
        else{
            CustomAlertDialog.newInstance("WARNING", "This app requires a camera.", "Okay").show(getFragmentManager(),"alerts");
        }

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo.isConnected()){
            ((CheckBox)findViewById(R.id.wifi)).setChecked(true);
        }
        else{
            CustomAlertDialog.newInstance("WARNING", "You are not connected to the internet. This app requires an internet connection for some features.", "Okay").show(getFragmentManager(),"alerts");
        }
    }

    /**
     * This function starts the next activity.
     *
     * @param view
     **/
    public void Continue(View view){
        if(((CheckBox)findViewById(R.id.battery)).isChecked()&&((CheckBox)findViewById(R.id.camera)).isChecked()&&((CheckBox)findViewById(R.id.wifi)).isChecked()){
            startActivity(new Intent(ExampleActivity.this, HeaterActivity.class));
        }
    }
}
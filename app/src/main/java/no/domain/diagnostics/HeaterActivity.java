package no.domain.diagnostics;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import no.domain.diagnostics.AlertDialogs.CustomAlertDialog;
import no.domain.diagnostics.AlertDialogs.EditAlertDialog;

public class HeaterActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    public boolean timerStarted=false;
    public boolean canSkip=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heater);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setScaleY(10f);
        CustomAlertDialog.newInstance("Instructions", "Please flip the left switch on the chassis labeled \"H\" on and tap \"Continue\" to start a timer.", "Okay").show(getFragmentManager(),"alerts");
    }

    /**
     * This function starts a timer for a given view and duration.
     *
     * @param progressBar  Time display of type ProgressBar.
     * @param duration     Timer duration of type float.
     **/
    public void StartTimer(final ProgressBar progressBar, final float duration){
        if(progressBar==null||duration<0){
            return;
        }
        progressBar.setMax((int)duration);
        new CountDownTimer((long)duration*1000,1000){
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int)millisUntilFinished/1000);
            }
            public void onFinish() {
                Timer(progressBar);
            }
        }.start();
    }

    /**
     * This function starts a 5 minute timer. This function should be called by a TextView.
     *
     * @param view  Activated element, should be of type Button.
     **/
    public void Timer(View view){
        if(timerStarted){
            if(canSkip) {
                startActivity(new Intent(HeaterActivity.this, AnalysisActivity.class));
            }
            else{
                canSkip=true;
                CustomAlertDialog.newInstance("Instructions", "Please flip the right switch labeled \"L\" on and the left switch labeled \"H\" off on the chassis. Then press \"Continue\" to proceed.", "Okay").show(getFragmentManager(), "alerts");
            }
        }
        else{
            timerStarted = true;
            StartTimer(progressBar, 300);
            Toast.makeText(this,"Please use this time to fill in some information.",Toast.LENGTH_LONG).show();
            EditAlertDialog dialog = new EditAlertDialog();
            dialog.show(getFragmentManager(), "alert");
        }
    }
}

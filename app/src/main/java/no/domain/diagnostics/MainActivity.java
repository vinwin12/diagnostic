package no.domain.diagnostics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * This class is the app's first/main activity.
 **/
public class MainActivity extends AppCompatActivity{
    /**
     * This function is called every time the app is launched. Use it to initiate variables and call
     * any functions that need to be called.
     *
     * @param savedInstanceState    This variable is unused (mostly).
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Jury-rigging for demo version
        // At the moment, this causes the app to skip all instructions
        // Remove this line in order to force the user to view all the intermediate activities
        // startActivity(new Intent(MainActivity.this,AnalysisActivity.class));
    }

    /**
     * This function starts the next activity.
     *
     * @param view  Activated element.
     */
    public void Start(View view){
        startActivity(new Intent(MainActivity.this,ExampleActivity.class));
    }
}
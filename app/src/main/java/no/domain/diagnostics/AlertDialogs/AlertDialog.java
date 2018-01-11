package no.domain.diagnostics.AlertDialogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import no.domain.diagnostics.R;

public class AlertDialog extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.alert_dialog);
    }

    public void showEditDialog(){
        EditAlertDialog dialog = new EditAlertDialog();
        dialog.show(getFragmentManager(), "alert");
    }

    public void showStringDialog(){
       CustomAlertDialog.newInstance("Title", "message", "Button").show(getFragmentManager(),"alerts");
    }
}
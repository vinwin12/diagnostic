package no.domain.diagnostics.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class CustomAlertDialog extends DialogFragment {
    public static CustomAlertDialog newInstance(String title, String message, String button){
        CustomAlertDialog custom = new CustomAlertDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("message",message);
        args.putString("button",button);
        custom.setArguments(args);
        return custom;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        String button = getArguments().getString("button");
        Context context = getActivity();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title)
                .setMessage(message)
                .setPositiveButton(button,null);

        return alert.create();
    }
}
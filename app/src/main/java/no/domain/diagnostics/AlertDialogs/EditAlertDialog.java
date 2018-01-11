package no.domain.diagnostics.AlertDialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import no.domain.diagnostics.R;

public class EditAlertDialog extends DialogFragment {
    final String[] information = new String[5];

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
      //  final View view = inflater.inflate(R.layout.dialog_edit,null);
       // builder.setView(view).setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                  //  @Override
         //           public void onClick(DialogInterface dialog, int id) {
           //             EditText name = (EditText) view.findViewById(R.id.name);
//                        EditText identity = (EditText) view.findViewById(R.id.identity);
//                        EditText sex = (EditText) view.findViewById(R.id.sex);
//                        EditText species = (EditText) view.findViewById(R.id.species);
//                        EditText symptoms = (EditText) view.findViewById(R.id.symptoms);

                        //TODO:Piotr: you can code anything you want to from this line to parse the data. This is just an example
                       // doPositiveClick(name,identity,sex,species,symptoms);
                    //}
              //  });

   //     return builder.create();
    return builder.create();
    }

    public  void doPositiveClick(EditText name, EditText identity, EditText sex, EditText species, EditText symptoms){
        information[0] = name.getText().toString();
        information[1] = identity.getText().toString();
        information[2] = sex.getText().toString();
        information[3] = species.getText().toString();
        information[4] = symptoms.getText().toString();

        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Alert_Dialogs");
            if (!file.exists()) {
                file.mkdirs();
            }

            int index = 0;
            if (new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Alert_Dialogs/Alert_Dialog_Info.txt").exists()) {
                FileReader fileReader = new FileReader(Environment.getExternalStorageDirectory() + "/Diagnostics/Alert_Dialogs/Alert_Dialog_Info.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String string = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    string += line;
                }
                index = Integer.parseInt(string);
                bufferedReader.close();
                fileReader.close();
            }

            file = new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Alert_Dialogs/Alert_Dialog_" + index + ".txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append(information[0]+'\n');
            fileWriter.append(information[1]+'\n');
            fileWriter.append(information[2]+'\n');
            fileWriter.append(information[3]+'\n');
            fileWriter.append(information[4]);
            fileWriter.flush();
            fileWriter.close();

            file = new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Alert_Dialogs/Alert_Dialog_Info.txt");
            fileWriter = new FileWriter(file);
            fileWriter.append("" + (index + 1));
            fileWriter.flush();
            fileWriter.close();
        }
        catch (Exception e){

        }
    }
}
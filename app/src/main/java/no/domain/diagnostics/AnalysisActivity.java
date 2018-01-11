package no.domain.diagnostics;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import no.domain.diagnostics.AlertDialogs.CustomAlertDialog;
import no.domain.diagnostics.PS.PSsBlackBox;
import no.domain.diagnostics.VV.BlackBoxMarkerAlgo;

import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.android.Utils.matToBitmap;
import static org.opencv.core.CvType.CV_8UC1;

public class AnalysisActivity extends AppCompatActivity{
    //private static TextView result;
    private static ImageView result;
    private static String results="";
    private Bitmap bitmap;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        //result=(TextView)this.findViewById(R.id.result);
        result=(ImageView)this.findViewById(R.id.results);
        //Camera();
        //Toast.makeText(this,"Please take a picture of the chip.",Toast.LENGTH_LONG).show();
    }

    public void Camera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Chips");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            int index = 0;
            if (new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Chips/Chip_Info.txt").exists()) {
                FileReader fileReader = new FileReader(Environment.getExternalStorageDirectory() + "/Diagnostics/Chips/Chip_Info.txt");
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

            File file = new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Chips/", "Chip_"+index+".jpg");
            uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            file = new File(Environment.getExternalStorageDirectory() + "/Diagnostics/Chips/Chip_Info.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.append("" + (index + 1));
            fileWriter.flush();
            fileWriter.close();
        }
        catch(Exception e){

        }
        startActivityForResult(intent,1);
    }

    public void Analyze(View view){
        if(bitmap==null) {
            while (bitmap == null) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Analyze(null);
                    }
                }, 100);
            }
        }
        else {
            Bitmap brightnessBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, false);
            int brightness = Color.green(brightnessBitmap.getPixel(0, 0));
            Toast.makeText(this,"Brightness: "+brightness,Toast.LENGTH_LONG).show();
            if(brightness<75){
                Toast.makeText(this,"Using the PS algorithm.",Toast.LENGTH_LONG).show();
                PS();
            }
            else{
                Toast.makeText(this,"Using the VV algorithm.",Toast.LENGTH_LONG).show();
                VV();
                // Not necessarily true, but it helps to know which algorithm is being used
                // CustomAlertDialog.newInstance("WARNING", "Image brightness might be suboptimal for this algorithm.", "Okay").show(getFragmentManager(),"alerts");
            }
        }
    }

    public void PS(){
        if(bitmap==null){
            Gallery(null);
        }
        else{
            PSsBlackBox.Input(bitmap);
        }
    }

    public void VV(){
        if(bitmap==null){
            Gallery(null);
        }
        else{
            if(bitmap.getHeight()<bitmap.getWidth()){
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            int[] results=new BlackBoxMarkerAlgo(bitmap).findValuesAlgo();
            if(results==null){
                return;
            }

            int cutoff=(int)((results[0]-results[1])*0.375f)+results[1];

            // Old code that adds HTML for color coding the results
            // This code does not work with the alert dialogs
            /*
            String newResult="<font color='red'>CHANNEL INTENSITIES:\n(CUTOFF: "+cutoff+")</font><br>";
            for(int i=0;i<results.length;i++){
                if(results[i]<cutoff) {
                    newResult += "<br><font color='red'>CHANNEL " + i + ":</font> ";
                    newResult += "<font color='blue'>" + results[i] + "</font>";
                }
                else{
                    newResult += "<br><font color='red'>CHANNEL " + i + ":</font> ";
                    newResult += "<font color='green'>" + results[i] + "</font>";
                }
            }
            */

            String newResult="CHANNEL INTENSITIES:\n(CUTOFF: "+cutoff+")\n";
            for(int i=0;i<results.length;i++){
                newResult += "CHANNEL " + i + ": ";
                newResult += results[i] + "\n";
            }

            this.results=newResult;
            Results(null);
        }
    }

    public static void Result(Bitmap bitmap,String string){
        // The following line formals the string if it is given in HTML format --- this is derelict code
        // result.setText(Html.fromHtml(newResult),TextView.BufferType.SPANNABLE);
        result.setImageBitmap(bitmap);
        results=string;
    }

    public void Results(View view){
        if(results!="") {
            CustomAlertDialog.newInstance("Results", results, "Okay").show(getFragmentManager(), "alerts");
        }
    }

    // Code under construction --- this code reads/writes decoded/encoded data
    // RSA encryption and file saving is not a priority at the moment, so this code is unused
    public void SaveResult(String newResult){
        try {
            FileInputStream fileInputStream = openFileInput("data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] inputBuffer = new char[100];
            int newChar;
            String string = "Recovered String: ";
            while ((newChar = inputStreamReader.read(inputBuffer)) > 0) {
                string += String.copyValueOf(inputBuffer, 0, newChar);
            }

            Key publicKey = null;
            Key privateKey = null;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            byte[] encodedString = null;
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedString = cipher.doFinal((string+'\n'+newResult).getBytes());

            FileOutputStream fileOutputStream=openFileOutput("data.txt",MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(encodedString.toString());
            outputStreamWriter.close();

            /*
            To decode use the following snippet of code:

            FileInputStream fileInputStream = openFileInput("data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] inputBuffer = new char[100];
            int newChar;
            String string = "Recovered String: ";
            while ((newChar = inputStreamReader.read(inputBuffer)) > 0) {
                string += String.copyValueOf(inputBuffer, 0, newChar);
            }

            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decodedString = cipher.doFinal(string.getBytes());
            string=decodedString.toString();
             */
        }
        catch(Exception e){
            Toast.makeText(this,"Result saving failed.",Toast.LENGTH_LONG).show();
        }
    }

    // Old code
    public Bitmap CompressBitmap(Bitmap input){
        OpenCVLoader.initDebug();

        Mat mat = new Mat();
        bitmapToMat(bitmap, mat);
        List<Mat> mats=new ArrayList<>(3);
        Core.split(mat,mats);
        mats.set(0,Mat.zeros(mat.rows(),mat.cols(),CV_8UC1));
        mats.set(2,Mat.zeros(mat.rows(),mat.cols(),CV_8UC1));
        Core.merge(mats,mat);
        matToBitmap(mat,input);

        return input;
    }

    public void Gallery(View view){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        InputStream inputStream;
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==1){
                if(bitmap!=null){
                    bitmap.recycle();
                }
                getContentResolver().notifyChange(uri,null);
                ContentResolver contentResolver=getContentResolver();
                try{
                    bitmap = android.provider.MediaStore.Images.Media.getBitmap(contentResolver, uri);
                    result.setImageBitmap(bitmap);
                    //Analyze();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(requestCode==2){
                try{
                    if(bitmap!=null){
                        bitmap.recycle();
                    }
                    uri=data.getData();
                    inputStream=getContentResolver().openInputStream(data.getData());
                    bitmap=BitmapFactory.decodeStream(inputStream);
                    result.setImageBitmap(bitmap);
                    //Analyze();
                }
                catch(FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    }
}

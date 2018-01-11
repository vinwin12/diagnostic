package no.domain.diagnostics;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class BarcodeActivity extends AppCompatActivity{
    private Bitmap bitmap;
    private Uri uri;
    //private int stepsToOverride=1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        Camera();
        Toast.makeText(this,"Please take a picture of the chip's QR code.",Toast.LENGTH_LONG).show();
    }

    public void QR(){
        if(bitmap==null){
            while(bitmap==null){
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        QR();
                    }
                },100);
            }

            // The following code adds an override to skip the barcode
            /*
            stepsToOverride-=1;
            if(stepsToOverride==0){
                Toast.makeText(this,"Override initiated.",Toast.LENGTH_LONG).show();
                startActivity(new Intent(BarcodeActivity.this,AnalysisActivity.class));
            }
            else{
                Camera();
                Toast.makeText(this, "Please take a picture of the chip's QR code.", Toast.LENGTH_LONG).show();
            }
            */
        }
        else{
            QR(bitmap);
        }
    }

    public void Camera(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File folder=new File(Environment.getExternalStorageDirectory()+"/Diagnostics/QR_Codes");
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file=new File(Environment.getExternalStorageDirectory()+"/Diagnostics/QR_Codes/","QR_Code_Capture.jpg");
        uri=Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,1);
    }

    public void QR(Bitmap input){
        try {
            BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE).build();
            if (!detector.isOperational()) {
                Toast.makeText(this, "QR Code Detection Failed", Toast.LENGTH_LONG).show();
                return;
            }
            Frame frame = new Frame.Builder().setBitmap(input).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);
            Barcode barcode = barcodes.valueAt(0);

            int index=0;
            if(new File(Environment.getExternalStorageDirectory()+"/Diagnostics/QR_Codes/QR_Code_Info.txt").exists()) {
                FileReader fileReader = new FileReader(Environment.getExternalStorageDirectory() + "/Diagnostics/QR_Codes/QR_Code_Info.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String string = "";
                String line;
                while ((line=bufferedReader.readLine())!=null) {
                    string += line;
                }
                index= Integer.parseInt(string);
                bufferedReader.close();
                fileReader.close();
            }

            File file=new File(Environment.getExternalStorageDirectory()+"/Diagnostics/QR_Codes/QR_Code_"+index+".txt");
            FileWriter fileWriter=new FileWriter(file);
            fileWriter.append(barcode.rawValue);
            fileWriter.flush();
            fileWriter.close();

            file=new File(Environment.getExternalStorageDirectory()+"/Diagnostics/QR_Codes/QR_Code_Info.txt");
            fileWriter=new FileWriter(file);
            fileWriter.append(""+(index+1));
            fileWriter.flush();
            fileWriter.close();

            bitmap.recycle();
            startActivity(new Intent(BarcodeActivity.this,AnalysisActivity.class));
        }
        catch(Exception e){
            Camera();
            Toast.makeText(this,"QR Code Detection Failed\n\nPlease take a picture of the chip's QR code.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==1){
                if(bitmap!=null){
                    bitmap.recycle();
                }
                getContentResolver().notifyChange(uri,null);
                ContentResolver contentResolver=getContentResolver();
                try{
                    bitmap=android.provider.MediaStore.Images.Media.getBitmap(contentResolver,uri);
                    bitmap=Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/16, bitmap.getHeight()/16, true);
                    QR();
                    //((ImageView)findViewById(R.id.qrCode)).setImageBitmap(bitmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}

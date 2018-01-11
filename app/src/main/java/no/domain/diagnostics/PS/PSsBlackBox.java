package no.domain.diagnostics.PS;

import android.graphics.Bitmap;

import no.domain.diagnostics.AnalysisActivity;

/**
 * Please use this class as a black box interface for my code - at least until I comment it.
 **/

public class PSsBlackBox {
    public static Bitmap originalBitmap;
    public static Bitmap bitmap;

    public static double intensitySensitivity = 0.375;
    public static double greenSensitivity = 0.25;
    public static double lineSensitivity = 0.25;
    public static int pixelSensitivity = 150;
    public static int rangeSensitivity = 42;

    public static double[] pixelIntensities = {0, 0, 0, 0 ,0 ,0, 0, 0, 0, 0};
    public static int[] greenPixels = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public static int[] lines = {-1,-1,-1,-1};

    public static String result;

    /**
     * Call this function to analyze a bitmap.
     **/
    public static void Input(Bitmap input) {
        originalBitmap = input;
        bitmap = input;

        for(int i = 0; i < 10; i ++) {
            pixelIntensities[i] = 0;
            greenPixels[i] = 0;
        }
        for(int i = 0; i < 4; i ++) {
            lines[i] = -1;
        }
        result = "";

        new no.domain.diagnostics.PS.AnalysisAsyncTask().execute();
    }

    /**
     * UPDATE THIS FUNCTION!
     * This function is called once processing is finished.
     * UPDATE THIS FUNCTION!
     **/
    public static void Output(){
        AnalysisActivity.Result(originalBitmap,result);
    }

    /**
     * The following functions should be placed in an activity for the sake of convenience.
     **/
    /*
    public void Camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    public void Gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 2);
    }

    public void QR(Bitmap input){
        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext()).setBarcodeFormats(Barcode.DATA_MATRIX|Barcode.QR_CODE).build();
        if(!detector.isOperational()){
            Toast.makeText(this,"QR Code Detection Failed",Toast.LENGTH_LONG).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(input).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);
        Barcode barcode = barcodes.valueAt(0);
        Toast.makeText(this,barcode.rawValue,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream inputStream;
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            try {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    */
}

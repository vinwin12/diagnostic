package no.domain.diagnostics.VV;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class BlackBoxMarkerAlgo {
    ImageView imageView;
    public Uri mMediaUri;
    int rangX;
    int rangY;
    int x;
    int y;
    public static final String TAG = BlackBoxMarkerAlgo.class.getSimpleName();

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
    //THE FOLLOWING LINE OF CODE WERE EDITED BY PWS//
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
    public Bitmap inputBitmap;


    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
    //THE FOLLOWING LINE OF CODE WERE EDITED BY PWS//
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
    /*
    public BlackBoxMarkerAlgo(Uri uri){
        mMediaUri = uri;
    }
    */
    public BlackBoxMarkerAlgo(Bitmap bitmap){
        inputBitmap=bitmap;
    }



    public int [] findValuesAlgo(){
        OpenCVLoader.initDebug();

        int[] values;

        if (mMediaUri == null) {
            Log.d(TAG, "ImageUri does not exist");
        }


        rangX = 900;
        rangY = 1200;


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
        //THE FOLLOWING LINE WAS EDITED BY PWS//
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
        //Bitmap bm = decodeSampledBitmapFromResource(mMediaUri, 900, 1200);
        //100, 100
        Bitmap bm=inputBitmap;

        bm = Bitmap.createScaledBitmap(bm, 900, 1200, false);

        if (bm == null) {
            prints("This doesn't work");

        }

        /*
        Bitmap bright = Bitmap.createScaledBitmap(bm,390,520,false);
        
        int brightness = no.domain.diagnostics.VV.BrightnessAlgo.findBrightness(bright);

        if(brightness >= 20) {

            x = 100;
            y = 200;

        }

        else if(brightness > 11){
            x = 50;
            y = 150;
        }

        else if(brightness <= 11){
            return null;
        }
        */

        x=100;
        y=200;



        Bitmap grayImage = detectEdges(bm);

        if (grayImage == null) {
            prints("Gray is not working");

        }

        grayImage = Bitmap.createScaledBitmap(grayImage, 900, 1200, false);

        if (grayImage == null) {
            prints("Cannot convert Gray");
        }

        no.domain.diagnostics.VV.LabAware lab = new no.domain.diagnostics.VV.LabAware();
        values = lab.calculateChip(grayImage, bm);


        return values;
    }















    public Bitmap detectEdges(Bitmap bitmap) {

        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);
        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, x, y, 3, false);

        //TODO: intially set to true, check to see if false makes sense

        Bitmap image = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);

        Utils.matToBitmap(edges, image);

        return image;
    }



    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }

        }

        return inSampleSize;

    }


    public void prints(String message) {

        Log.d(TAG, message);

    }

    public void printInt(int num) {

        String number = Integer.toString(num);
        Log.d(TAG, number);

    }

    public static Bitmap decodeSampledBitmapFromResource(Uri uri,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(uri.getPath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.getPath(), options);
    }


}

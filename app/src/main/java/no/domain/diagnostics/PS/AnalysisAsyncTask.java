package no.domain.diagnostics.PS;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import static no.domain.diagnostics.PS.PSsBlackBox.bitmap;
import static no.domain.diagnostics.PS.PSsBlackBox.greenPixels;
import static no.domain.diagnostics.PS.PSsBlackBox.greenSensitivity;
import static no.domain.diagnostics.PS.PSsBlackBox.intensitySensitivity;
import static no.domain.diagnostics.PS.PSsBlackBox.lineSensitivity;
import static no.domain.diagnostics.PS.PSsBlackBox.lines;
import static no.domain.diagnostics.PS.PSsBlackBox.originalBitmap;
import static no.domain.diagnostics.PS.PSsBlackBox.pixelIntensities;
import static no.domain.diagnostics.PS.PSsBlackBox.pixelSensitivity;
import static no.domain.diagnostics.PS.PSsBlackBox.rangeSensitivity;
import static no.domain.diagnostics.PS.PSsBlackBox.result;
import static org.opencv.android.Utils.bitmapToMat;
import static org.opencv.core.Core.inRange;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2HSV;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;

public class AnalysisAsyncTask extends AsyncTask<String, Void, String> {
    private void Rescale(){
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
        originalBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth()/2, originalBitmap.getHeight()/2, true);
    }

    private void Threshold(){
        OpenCVLoader.initDebug();

        Mat mat = new Mat();
        bitmapToMat(bitmap, mat);
        Size size = new Size(3, 3);
        GaussianBlur(mat, mat, size, 0);
        cvtColor(mat, mat, COLOR_RGB2HSV);
        inRange(mat, new Scalar(60-rangeSensitivity, 100, 12.5), new Scalar(60+rangeSensitivity, 255, 255), mat);
        dilate(mat, mat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(9, 9)));

        bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
    }

    private void Rotate270(){
        Matrix matrix = new Matrix();
        matrix.postRotate(270);

        bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        originalBitmap = Bitmap.createBitmap(originalBitmap , 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
    }

    private void Rotate90(){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void DetectLines(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if(bitmap != null && width > 300 && height > 300) {
            Bitmap croppedBitmap=Bitmap.createBitmap(bitmap, 100, 100, width-200, height-200);
            bitmap = croppedBitmap;
            croppedBitmap=Bitmap.createBitmap(originalBitmap, 100, 100, width-200, height-200);
            originalBitmap = croppedBitmap;
        }

        DetectLine();
        Rotate90();

        DetectLine();
        Rotate90();

        DetectLine();
        Rotate90();

        DetectLine();
        Rotate90();

        if(-lines[2]-lines[0]+100 < 0 && -lines[3]-lines[1]+100 < 0) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            Bitmap croppedBitmap=Bitmap.createBitmap(bitmap, lines[0]-50, lines[3]-50, width-lines[2]-lines[0]+100, height-lines[3]-lines[1]+100);
            bitmap = croppedBitmap;
            croppedBitmap=Bitmap.createBitmap(originalBitmap, lines[0]-50, lines[3]-50, width-lines[2]-lines[0]+100, height-lines[3]-lines[1]+100);
            originalBitmap = croppedBitmap;

            for(int i = 0; i < 4; i ++) {
                lines[i] = 50;
            }
        }
    }

    private void DetectLine(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap bitmapCopy = bitmap.copy(bitmap.getConfig(),true);

        int[] pixels = new int[width*height];
        int[] pixelsCopy = new int[width*height];
        bitmapCopy.getPixels(pixels,0,width,0,0,width,height);
        int pixel;
        int localSensitivity = (int)(height*lineSensitivity);
        if(height < width) {
            localSensitivity *= 0.50;
        }
        int whitePixels;
        int maxWhitePixels;
        boolean lineFound = false;
        for(int i = 0; i < width; i ++) {
            whitePixels = 0;
            maxWhitePixels = 0;
            for(int j = 1; j < height; j ++) {
                pixel = pixels[i + j*width];
                if(pixel == Color.WHITE) {
                    whitePixels += 1;
                }
                else {
                    if(whitePixels > maxWhitePixels) {
                        maxWhitePixels = whitePixels;
                        whitePixels = 0;
                    }
                }
                pixelsCopy[i + j*width] = pixel;
            }
            if(maxWhitePixels >= localSensitivity && !lineFound) {
                lineFound = true;
                for(int j = 0; j < 4; j ++) {
                    if(lines[j] == -1) {
                        lines[j] = i;
                        break;
                    }
                }
                for(int j = 1; j < height; j ++) {
                    pixelsCopy[i+j*width] = Color.RED;
                }
            }
        }

        bitmapCopy.setPixels(pixelsCopy,0,width,0,0,width,height);
        bitmap=Bitmap.createBitmap(bitmapCopy);
    }

    private void Analyze(){
        Rescale();

        Threshold();

        Rescale();
        Rescale();

        if(bitmap.getHeight() > bitmap.getWidth()) {
            Rotate270();
        }

        DetectLines();

        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap bitmapCopy = originalBitmap.copy(originalBitmap.getConfig(),true);

        int[] pixels = new int[width*height];
        int[] pixelsCopy = new int[width*height];
        bitmapCopy.getPixels(pixels,0,width,0,0,width,height);
        int pixel;
        float greenPixel = 0;
        float nonGreenPixel = 0;
        for(int i = (lines[0]+width-lines[2])/2+1; i < width; i ++) {
            if(i < (lines[0]+(width-lines[2])*3)/4) {
                pixel = pixels[i + (lines[3]+(int)Math.round((height-lines[3]-lines[1])*0.91))*width];
                greenPixel += Color.green(pixel)/((lines[0]+(width-lines[2])*3)/4-(lines[0]+width-lines[2])/2);
                pixel = pixels[i + (lines[3]+(int)Math.round((height-lines[3]-lines[1])*0.82))*width];
                nonGreenPixel += Color.green(pixel)/((lines[0]+(width-lines[2])*3)/4-(lines[0]+width-lines[2])/2);
            }
            else {
                break;
            }
        }
        greenPixel = Math.round(nonGreenPixel + (greenPixel - nonGreenPixel)*greenSensitivity);

        int intensityMultiplier=0;
        for(int i = 0; i < width; i ++) {
            for(int j = 1; j < height; j ++) {
                pixel = pixels[i + j*width];
                if(i == lines[0] || height-j == lines[1] || width-i == lines[2] || j == lines[3] || i == (lines[0]+width-lines[2])/2 || i == (lines[0]+(width-lines[2])*3)/4) {
                    pixelsCopy[i + j*width] = Color.RED;
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.11)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[0] += 1;
                        }
                        pixelIntensities[0] += Color.green(pixel);
                        intensityMultiplier++;
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.20)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[1] += 1;
                        }
                        pixelIntensities[1] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.29)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[2] += 1;
                        }
                        pixelIntensities[2] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.38)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[3] += 1;
                        }
                        pixelIntensities[3] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.47)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[4] += 1;
                        }
                        pixelIntensities[4] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.55)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[5] += 1;
                        }
                        pixelIntensities[5] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.64)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[6] += 1;
                        }
                        pixelIntensities[6] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.73)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[7] += 1;
                        }
                        pixelIntensities[7] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.82)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[8] += 1;
                        }
                        pixelIntensities[8] += Color.green(pixel);
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.91)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(Color.green(pixel) > greenPixel) {
                            greenPixels[9] += 1;
                        }
                        pixelIntensities[9] += Color.green(pixel);
                    }
                }
                pixelsCopy[i + j*width] = pixel;
            }
        }

        int minIntensity = (int)Math.round(pixelIntensities[8]/9)+(int)((Math.round(pixelIntensities[9]/9)-Math.round(pixelIntensities[8]/9))*intensitySensitivity);
        for(int i = 0; i < width; i ++) {
            for(int j = 1; j < height; j ++) {
                if(i == lines[0] || height-j == lines[1] || width-i == lines[2] || j == lines[3] || i == (lines[0]+width-lines[2])/2 || i == (lines[0]+(width-lines[2])*3)/4) {
                    pixelsCopy[i + j*width] = Color.RED;
                }
                if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.11)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[0] > pixelSensitivity && Math.round(pixelIntensities[0]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.20)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[1] > pixelSensitivity && Math.round(pixelIntensities[1]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.29)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[2] > pixelSensitivity && Math.round(pixelIntensities[2]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.38)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[3] > pixelSensitivity && Math.round(pixelIntensities[3]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.47)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[4] > pixelSensitivity && Math.round(pixelIntensities[4]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.55)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[5] > pixelSensitivity && Math.round(pixelIntensities[5]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.64)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[6] > pixelSensitivity && Math.round(pixelIntensities[6]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.73)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[7] > pixelSensitivity && Math.round(pixelIntensities[7]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.82)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[8] > pixelSensitivity && Math.round(pixelIntensities[8]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
                else if(Math.abs(lines[3]+Math.round((height-lines[3]-lines[1])*0.91)-j) < 4) {
                    if((lines[0]+width-lines[2])/2 < i && i < (lines[0]+(width-lines[2])*3)/4) {
                        if(greenPixels[9] > pixelSensitivity && Math.round(pixelIntensities[9]/9) > minIntensity) {
                            pixelsCopy[i + j * width] = Color.GREEN;
                        }
                        else {
                            pixelsCopy[i + j * width] = Color.BLUE;
                        }
                    }
                }
            }
        }

        bitmapCopy.setPixels(pixelsCopy,0,width,0,0,width,height);
        originalBitmap=Bitmap.createBitmap(bitmapCopy);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        originalBitmap = Bitmap.createBitmap(originalBitmap , 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);

        /*
        result = "<font color='red'>CHANNEL INTENSITIES:\n(CUTOFF: " + minIntensity + ")</font><br>";
        for(int i = 9; i > -1; i --) {
            result += "<br><font color='red'>CHANNEL " + (10-i) + ":</font> ";
            if(greenPixels[i] > pixelSensitivity && Math.round(pixelIntensities[i]/9) > minIntensity) {
                result += "<font color='green'>" + Math.round(pixelIntensities[i]/9) + "</font>";
            }
            else {
                result += "<font color='blue'>" + Math.round(pixelIntensities[i]/9) + "</font>";
            }
        }
        */
        result = "CHANNEL INTENSITIES:\n";
        for(int i = 9; i > -1; i --) {
            result += "CHANNEL " + (10-i) + ": ";
            if(greenPixels[i] > pixelSensitivity && Math.round(pixelIntensities[i]/9) > minIntensity) {
                result += Math.round(pixelIntensities[i]/intensityMultiplier)+"\n";
            }
            else {
                result += Math.round(pixelIntensities[i]/intensityMultiplier)+"\n";
            }
        }
    }

    @Override
    protected String doInBackground(String[] parameters) {
        Analyze();
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        PSsBlackBox.Output();
    }
}
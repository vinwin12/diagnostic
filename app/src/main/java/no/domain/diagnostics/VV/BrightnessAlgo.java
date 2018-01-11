package no.domain.diagnostics.VV;

import android.graphics.Bitmap;
import android.graphics.Color;

public class BrightnessAlgo {

    public static int findBrightness(Bitmap bm){

        int red = 0;
        int blue = 0;
        int green = 0;


        for (int i : no.domain.diagnostics.VV.Functions.range(0,520,false)){
            for (int j : no.domain.diagnostics.VV.Functions.range(0,390,false)){
                int pixel = bm.getPixel(j,i);

                red += Color.red(pixel);
                blue += Color.blue(pixel);
                green += Color.green(pixel);

            }

        }

        int r = red/(390*520);
        int b = blue/(390*520);
        int g = green/(390*520);

        int result = (int) Math.sqrt(0.241*(r*r) + 0.691*(g*g) + 0.068*(b*b));

       // int result2 = (int) (0.299*r + 0.587*g + 0.114*b);

      //  int result3 = (int) (0.2126*r + 0.7152*g + 0.0722*b);


        return result;
    }

}

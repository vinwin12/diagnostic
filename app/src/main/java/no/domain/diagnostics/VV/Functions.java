package no.domain.diagnostics.VV;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.google.common.collect.DiscreteDomain;
//import com.google.common.collect.Range;
//import org.opencv.core.Mat;

public class  Functions {


    //TODO:fix offset similair to python in all cases and usages


    public static int greenPixel(int pixel){


            return Color.green(pixel);


    }





    public static int[] range(int start, int length, boolean reverse){


        ArrayList<Integer> ranges = new ArrayList<Integer>();

        if(reverse){
            for (int i = 0; i < start - length ; i++){

                ranges.add(i,start - i);

            }
        }

        else{

            for (int i = 0; i < length - start;i++){

                ranges.add(i,start + i);
            }


        }

        int[] result = no.domain.diagnostics.VV.SquareFunctions.convertIntegers(ranges);

        return result;

    }


    public static int[] getLineFunctions(int[] XList, String function){

        int[] var = new int[2];

        if(function.equals( "coordinates")){
            var[0] = XList[0];
            var[1] = XList[(XList.length) -1 ];

        }

        if (function.equals("length")){

            var[0] = XList[XList.length - 1] - XList[0];
            var[1] = 0;

        }

        if (function.equals( "midpointCoordinate")){
            var[0] = Math.round(((XList[XList.length - 1] - XList[0])/2 + XList[0])); //TODO:CHECK TYPE
            var[1] = 0;

        }



        return var;
    }





    //TODO: getmode
    public  static ArrayList<Integer> mode(final int[] numbers) {

        final ArrayList<Integer> modes = new ArrayList<Integer>();
        final Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();


        int max = -1;

        for(final int n: numbers){
            int count = 0;

            if(countMap.containsKey(n)){
                count = countMap.get(n) + 1;

            }
            else{
                count = 1;

            }
            countMap.put(n,count);

            if(count> max){
                max = count;
            }
        }


        for(final  Map.Entry<Integer,Integer> tuple: countMap.entrySet()){

            if(tuple.getValue() == max){
                modes.add(tuple.getKey());
            }

        }



        return modes;
    }

    //TODO: getMinSlope
    public static int getMode(ArrayList<Integer> list){
        int mode = 0;


        if(list.size() == 0){
            return 0;
        }

        else{

            int small = list.get(0);
            int index = 0;

            for(int i = 0; i < list.size();i++){
                if(list.get(i) < small){
                    small = list.get(i);
                    index = i;
                }
            }

            mode = small;

        }

        return mode;
    }



    public static void colorLine(Bitmap picture, String type, int x, int y, int finalX, int finalY){

        if (type.equals("vert")) {
            for (int i = 0; i < range(x, finalX,false).length; i++) { //TODO: fix index
                picture.setPixel(y, i, Color.RED);
//TODO: changed index pair
            }
        }

        if (type.equals("hori")){
            for (int i = 0; i < range(y,finalY,false).length;i++){
                picture.setPixel(i,x,Color.RED);
            }
        }

    }

    public static void colorLines(Bitmap picture, String type, int x, int y, int finalX, int finalY){

        if (type.equals("vert")) {
            for (int i = 0; i < range(x, finalX,false).length; i++) { //TODO: fix index
                picture.setPixel(i, y, Color.WHITE);

            }
        }

        if (type.equals( "hori")){
            for (int i = 0; i < range(y,finalY,false).length;i++){
                picture.setPixel(x,i,Color.WHITE);
            }
        }

    }

    public static void colorRectangle(Bitmap picture, int x, int y, int finalX, int finalY){

        colorLines(picture,"hori",x,y,x,finalY); // top hoirzonal line
        colorLines(picture,"vert",x,y,finalX,y); // left vertical line
        colorLines(picture,"hori",finalX,y,finalX,finalY); // down horizontal line
        colorLines(picture,"vert",x,finalY,finalX,finalY); // right horizontal line

    }

    public static void colorPixel(Bitmap picture, int x, int y, String color){

        if (color.equals("red")){
            picture.setPixel(y,x,Color.RED);
        }

        if (color.equals("white")){
            picture.setPixel(y,x,Color.YELLOW);
        }

        if (color.equals("green")){
            picture.setPixel(y,x,Color.GREEN);
        }

        if (color.equals("wite")){
            picture.setPixel(y,x,Color.WHITE);
        }

    }




    public static int findingSlope(ArrayList<Integer> xList, ArrayList<Integer> yList,String function){



        ArrayList<Integer> slopeDifference = new ArrayList<Integer>();

        int number = 1;

        //int modeSlope = 0;
        int actualSlope = 0;


        if (function.equals("vertical")){
            for(int j : yList){

                int i = 0; //TODO: when using math functions should I double or ints
                if(number < yList.size()){
                    slopeDifference.add(i, Math.abs(j - yList.get(number)));
                    number = number + 1; // TODO: check if this should go outside or inside
                }
                i++;
            }
        }

        if (function.equals("horizontal")) {
            for (int j : xList) {

                int i = 0;
                if (number < xList.size()) {
                    slopeDifference.add(i, Math.abs(j - xList.get(number)));
                    number = number + 1;
                }
                i++;
            }
        }

        try { //TODO: see how try catch nested works


            int[] slopeInt = no.domain.diagnostics.VV.SquareFunctions.convertIntegers(slopeDifference);


            ArrayList<Integer> modeSlope = mode(slopeInt);
            actualSlope = getMode(modeSlope);
        }
        catch(Exception e){
              actualSlope = 0;
        }

        return actualSlope;
    }


    public static int findY1(int slope, ArrayList<Integer> Ylist){

        int flag = 0;
        int Y1 = 1;

        for (int i : range(0,Ylist.size(),false)){


            if((i + 1) == ( Ylist.size())){

                break; //TODO: check if this is equal to java
            }


            int difference = Math.abs(Ylist.get(i) - Ylist.get(i+1));


            if (flag > 3){
                return Y1;

            }

            else if(slope == difference){

                flag = flag + 1;
                Y1 = Ylist.get(i);
            }

            else{
                Y1 = Y1 * 0;
                flag = flag*0;
            }

        }

        if (Y1 > 100){
            return Y1;
        }

        else{
            return 0;
        }



    }

    public static ArrayList<ArrayList<Integer>> vertLineList(Bitmap picture, int x1, int x2, int y1, int y2){

        int black = 0;
        int white = 255;
        ArrayList<ArrayList<Integer>> Length = new ArrayList<ArrayList<Integer>>();
        int num = 0;

        ArrayList<Integer> X = new ArrayList<Integer>(1);
        ArrayList<Integer> Y = new ArrayList<Integer>(1);



        for(int i: range(x1,x2,false)){


            boolean grayFlag = false;

             num = 0;

            for (int j : range(y1,y2,false)){

             int pixel = picture.getPixel(j,i);

             if (num > 0){
                 break;   //TODO: could be possible error, check
             }

             if (pixel == Color.BLACK && (grayFlag)){
                 break;
             }

             if (pixel == Color.WHITE){
                 X.add(i);
                 Y.add(j);
                 grayFlag = true;
                 num = num + 1;

             }



            }

        }

        Length.add(0,X);
        Length.add(1,Y);

        return Length;


    }


    public static ArrayList<ArrayList<Integer>> horiLineList(Bitmap picture, int x1, int x2, int y1, int y2){

        int black = 0;
        int white = 255;
        ArrayList<ArrayList<Integer>> Length = new ArrayList<ArrayList<Integer>>();
        int num = 0;

        ArrayList<Integer> X = new ArrayList<Integer>(1);
        ArrayList<Integer> Y = new ArrayList<Integer>(1);


        loop:
        for(int j: range(y1,y2,false)){

            boolean grayFlag = false;

            num = 0;

            for (int i : range(x1,x2,false)){

                int pixel = picture.getPixel(j,i);

                if (num > 0){
                    break;   //TODO: could be possible error, check make example files
                }

                if (pixel == Color.BLACK && (grayFlag == true)){
                    break;
                }

                if (pixel == Color.WHITE){
                    X.add(i);
                    Y.add(j);
                    grayFlag = true;
                    num = num + 1;

                }



            }

        }

        Length.add(0,X);
        Length.add(1,Y);

        return Length;




    }




    public static ArrayList<ArrayList<Integer>> horiLineListBelow(Bitmap picture, int x1, int x2, int y1, int y2){

        int black = 0;
        int white = 255;
        ArrayList<ArrayList<Integer>> Length = new ArrayList<ArrayList<Integer>>();
        int num = 0;

        ArrayList<Integer> X = new ArrayList<Integer>(1);
        ArrayList<Integer> Y = new ArrayList<Integer>(1);


        loop:
        for(int j: range(y1,y2,false)){

            boolean grayFlag = false;

            num = 0;

            for (int i : range(x2,x1,true)){  //TODO: make for backward portability also check about that -1

                int pixel = picture.getPixel(j,i);

                if (num > 0){
                    break;   //TODO: could be possible error, check make example files
                }

                if (pixel == Color.BLACK && (grayFlag == true)){
                    break;
                }

                if (pixel == Color.WHITE){
                    X.add(i);
                    Y.add(j);
                    grayFlag = true;
                    num = num + 1;

                }



            }

        }

        Length.add(0,X);
        Length.add(1,Y);

        return Length;







    }


    public static ArrayList<ArrayList<Integer>> vertLineListRight(Bitmap picture, int x1, int x2, int y1, int y2){


        int black = 0;
        int white = 255;
        ArrayList<ArrayList<Integer>> Length = new ArrayList<ArrayList<Integer>>();
        int num = 0;

        ArrayList<Integer> X = new ArrayList<Integer>(1);
        ArrayList<Integer> Y = new ArrayList<Integer>(1);


        loop:
        for(int i: range(x1,x2,false)){

            boolean grayFlag = false;

            num = 0;

            for (int j : range(y2,y1,true)){ //TODO: check

                int pixel = picture.getPixel(j,i);

                if (num > 0){
                    break;   //TODO: could be possible error, check
                }

                if (pixel == Color.BLACK && (grayFlag == true)){
                    break;
                }

                if (pixel == Color.WHITE){
                    X.add(i);
                    Y.add(j);
                    grayFlag = true;
                    num = num + 1;

                }



            }

        }

        Length.add(0,X);
        Length.add(1,Y);

        return Length;

    }


    public static int backUpfindY1(int slope, ArrayList<Integer> Ylist){

        int flag = 0;
        int Y1 = 1;

        for (int i : range(0,Ylist.size(),false)){

            if (i + 1 == Ylist.size()){
                break;
            }


            int difference = Math.abs(Ylist.get(i) - Ylist.get(i+1));



            if (flag > 1){
                return Y1;
            }

            else if(slope == difference){
                flag = flag + 1;
                Y1 = Ylist.get(i);
            }


            else {
                Y1 = Y1*0;
                flag = flag*0;
            }

        }


        if (Y1 > 100){
            return Y1;
        }

        else{
            return 0;
        }

    }

    public static int worstCasefindY1(int slope, ArrayList<Integer> Ylist){

        int flag = 0;
        int Y1 = 1;

        for(int i : range(0, Ylist.size(),false)){

            if(i + 1 == Ylist.size()){
                break;
            }

            int difference = Math.abs(Ylist.get(i) - Ylist.get(i+1));


            if (flag > 0){
                return Y1;
            }

            else if(slope == difference) {
                flag = flag + 1;
                Y1 = Ylist.get(i);
            }


            else{
                Y1 = Y1 * 0;
                flag = flag * 0;
            }


        }

        if(Y1 > 100){
            return Y1;
        }

        else{
            return 0;
        }


    }


    public static int findAxis(int slope, ArrayList<Integer> Ylist){

        int slope3 = findY1(slope,Ylist);

        int num = Ylist.get(0);  //TODO: check index

        if (slope3 > 100 || (slope3 != 0)){
            num = slope3;
        }

        int slope1 = backUpfindY1(slope,Ylist);

        if (slope1 > 100 || (slope1 != 0)){
            num = slope1;
        }

        int slope2 = worstCasefindY1(slope,Ylist);


        if (slope2 > 100 || (slope2 != 0)){
            num =  slope2;
        }

        if (Ylist.size() == 1){
            num = Ylist.get(0);
        }


        return num;

    }


} // end of class

package no.domain.diagnostics.VV;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;


public class LabAware {

    public  int green1;
    public  int green2;
    public  int green3;
    public  int green4;
    public  int green5;
    public  int green6;
    public  int green7;
    public  int green8;
    public  int green9;
    public  int green10;
   // public int[] firstOuterPoints;
   // public int[] secondOuterPoints;

    //this comment should be deleted

    public int[] calculateChip(Bitmap picture, Bitmap greenPicture) {




        ArrayList<ArrayList<Integer>> xyList = getRanges(picture);
        ArrayList<Integer> xList = xyList.get(0);
        ArrayList<Integer> yList = xyList.get(1);

        boolean bothPass = false;
        boolean OnlyfirstSquare = false;
        boolean OnlysecondSquare = false;

        int[] leftSquare = new int[2];
        int[] rightSquare = new int[2];

        try {
            leftSquare = no.domain.diagnostics.VV.SquareFunctions.firstSquare(picture, no.domain.diagnostics.VV.SquareFunctions.convertIntegers(xList));

            rightSquare = no.domain.diagnostics.VV.SquareFunctions.secondSquare(picture, no.domain.diagnostics.VV.SquareFunctions.convertIntegers(yList));
            bothPass = true;
        } catch (Exception e) {
            try {
                leftSquare = no.domain.diagnostics.VV.SquareFunctions.firstSquareOnly(picture, no.domain.diagnostics.VV.SquareFunctions.convertIntegers(xList));
                OnlyfirstSquare = true;
                bothPass = false;
            } catch (Exception v) {


                rightSquare = no.domain.diagnostics.VV.SquareFunctions.secondSquareOnly(picture, no.domain.diagnostics.VV.SquareFunctions.convertIntegers(yList));
                OnlysecondSquare = true;
                bothPass = false;

            }

        }



        if(bothPass || OnlyfirstSquare){
            calculateGreenChip(greenPicture,leftSquare[0],leftSquare[1],true);
        }

        if(OnlysecondSquare){
            calculateGreenChip(greenPicture,rightSquare[0],rightSquare[1],false);
        }


        int[] values = new int[10];

        values[0] = green1;
        values[1] = green2;
        values[2] = green3;
        values[3] = green4;
        values[4] = green5;
        values[5] = green6;
        values[6] = green7;
        values[7] = green8;
        values[8] = green9;
        values[9] = green10;

        return values;
    }


    public ArrayList<ArrayList<Integer>> getRanges (Bitmap picture){


            ArrayList<Integer> firstSquare = new ArrayList<Integer>();
            ArrayList<Integer> secondSquare = new ArrayList<Integer>();
            ArrayList<ArrayList<Integer>> xyRanges = new ArrayList<ArrayList<Integer>>();


            ArrayList<Integer> coordinate = new ArrayList<Integer>();
            ArrayList<Integer> coordinateTwo = new ArrayList<Integer>();


            //int x = 831;
            //int y = 451;




            //!!!!!!!!!!!!!!!!!!!!!!!DO NOT DELETE!!!!!!!!!!!!!!!!!!!!!!!!!!!

            int x = 737;
            int y = 370;


            boolean whiteFound = false;


            for (int j : Functions.range(x, 869, false)) {
                whiteFound = false;

                for (int i : Functions.range(y, 555, false)) {

                    int pixel = picture.getPixel(i, j);

                    if (pixel == Color.WHITE) {
                        whiteFound = true;
                        break;
                    }

                    if (whiteFound == false && i == 554) {
                        coordinate.add(j);
                    }


                }


            }


            x = coordinate.get(3);


            boolean firstWhite = false;


            for (int j : Functions.range(350, 515, false)) {
                for (int i : Functions.range(x, x + 50, false)) {

                    int pixel = picture.getPixel(j, i);


                    if (pixel == Color.WHITE) {
                        firstWhite = true;
                        break;
                    }

                    if (i == x + 49 && firstWhite == true) {
                        coordinateTwo.add(j);
                    }


                }


            }



            y = coordinateTwo.get(1);

            firstSquare.add(x);
            firstSquare.add(x + 46);
            firstSquare.add(y - 43);
            firstSquare.add(y - 10);
            firstSquare.add(x);
            firstSquare.add(x + 20);
            firstSquare.add(y - 45);
            firstSquare.add(y);
            firstSquare.add(x + 16);
            firstSquare.add(x + 46);
            firstSquare.add(y - 45);
            firstSquare.add(y);
            firstSquare.add(x);
            firstSquare.add(x + 46);
            firstSquare.add(y - 16);
            firstSquare.add(y);

            secondSquare.add(x);
            secondSquare.add(x + 46);
            secondSquare.add(y);
            secondSquare.add(y + 16);
            secondSquare.add(x);
            secondSquare.add(x + 20);
            secondSquare.add(y);
            secondSquare.add(y + 45);
            secondSquare.add(x + 16);
            secondSquare.add(x + 46);
            secondSquare.add(y);
            secondSquare.add(y + 45);
            secondSquare.add(x);
            secondSquare.add(x + 46);
            secondSquare.add(y + 12);
            secondSquare.add(y + 46);


            xyRanges.add(firstSquare);
            xyRanges.add(secondSquare);

            return xyRanges;


        }


    public void calculateGreenChip(Bitmap picture, int x, int y, boolean isFirst) {

        picture.setPixel(y,x,Color.YELLOW);
        picture.setPixel(y+1,x,Color.YELLOW);
        picture.setPixel(y+2,x,Color.YELLOW);
        picture.setPixel(y+3,x,Color.YELLOW);
        picture.setPixel(y+4,x,Color.YELLOW);


        if(isFirst) {
            findGreenValue(x, y, picture, isFirst);
        }
        else{
            findGreenValueSecondSquare(x,y,picture,isFirst);
        }



    }


    public void findGreenValue(int Xreferencepoint, int Yreferencepoint, Bitmap picture, boolean isFirst) {


        int[] greenChannelAverageValue;

        //1

        Channel firstChannel = new Channel(1, isFirst);
        firstChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);


        //2

        Channel secondChannel = new Channel(2, isFirst);
        secondChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        //3

        Channel thirdChannel = new Channel(3, isFirst);
        thirdChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        // for fourth channel

        Channel fourthChannel = new Channel(4, isFirst);
        fourthChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        // for fifth channel

        Channel fifthChannel = new Channel(5, isFirst);
        fifthChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        // for sixth channel ##

        Channel sixthChannel = new Channel(6, isFirst);
        sixthChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);

        // for seventh channel ##

        Channel seventhChannel = new Channel(7, isFirst);
        seventhChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);

        // for eigth channel ##

        Channel eigthChannel = new Channel(8, isFirst);
        eigthChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);

        //for ninth channel ##

        Channel ninthChannel = new Channel(9, isFirst);
        ninthChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);

        // for tenth channel ##

        Channel tenthChannel = new Channel(10, isFirst);
        tenthChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);
        //  System.gc();


        greenChannelAverageValue = findGreen(firstChannel, secondChannel, thirdChannel, fourthChannel, fifthChannel, sixthChannel, seventhChannel, eigthChannel, ninthChannel, tenthChannel, picture);

        //System.gc();
        green1 = greenChannelAverageValue[0];
        green2 = greenChannelAverageValue[1];
        green3 = greenChannelAverageValue[2];
        green4 = greenChannelAverageValue[3];
        green5 = greenChannelAverageValue[4];
        green6 = greenChannelAverageValue[5];
        green7 = greenChannelAverageValue[6];
        green8 = greenChannelAverageValue[7];
        green9 = greenChannelAverageValue[8];
        green10 = greenChannelAverageValue[9];
        //    System.gc();
    }


    public void findGreenValueSecondSquare(int Xreferencepoint, int Yreferencepoint, Bitmap picture, boolean isFirst) {

        int[] greenChannelAverageValue;

        //1

        Channel firstChannel = new Channel(1, isFirst);
        firstChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);


        //2

        Channel secondChannel = new Channel(2, isFirst);
        secondChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        //3

        Channel thirdChannel = new Channel(3, isFirst);
        thirdChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        // for fourth channel

        Channel fourthChannel = new Channel(4, isFirst);
        fourthChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        // for fifth channel

        Channel fifthChannel = new Channel(5, isFirst);
        fifthChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        // for sixth channel ##

        Channel sixthChannel = new Channel(6, isFirst);
        sixthChannel.calculatePosition(Yreferencepoint, "-", Xreferencepoint);

        // for seventh channel ##

        Channel seventhChannel = new Channel(7, isFirst);
        seventhChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);

        // for eigth channel ##

        Channel eigthChannel = new Channel(8, isFirst);
        eigthChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);

        //for ninth channel ##

        Channel ninthChannel = new Channel(9, isFirst);
        ninthChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);

        // for tenth channel ##

        Channel tenthChannel = new Channel(10, isFirst);
        tenthChannel.calculatePosition(Yreferencepoint, "+", Xreferencepoint);
        //  System.gc();


        greenChannelAverageValue = findGreen(firstChannel, secondChannel, thirdChannel, fourthChannel, fifthChannel, sixthChannel, seventhChannel, eigthChannel, ninthChannel, tenthChannel, picture);

        //System.gc();
        green1 = greenChannelAverageValue[0];
        green2 = greenChannelAverageValue[1];
        green3 = greenChannelAverageValue[2];
        green4 = greenChannelAverageValue[3];
        green5 = greenChannelAverageValue[4];
        green6 = greenChannelAverageValue[5];
        green7 = greenChannelAverageValue[6];
        green8 = greenChannelAverageValue[7];
        green9 = greenChannelAverageValue[8];
        green10 = greenChannelAverageValue[9];
        //    System.gc();


    }

    public int[] findGreen(Channel a, Channel b, Channel c, Channel d, Channel e, Channel f, Channel g, Channel h, Channel i, Channel j, Bitmap picture) {

        int[] values = new int[10];


        values[0] = findGreenArea(a, picture);
        values[1] = findGreenArea(b, picture);
        values[2] = findGreenArea(c, picture);
        values[3] = findGreenArea(d, picture);
        values[4] = findGreenArea(e, picture);
        values[5] = findGreenArea(f, picture);
        values[6] = findGreenArea(g, picture);
        values[7] = findGreenArea(h, picture);
        values[8] = findGreenArea(i, picture);
        values[9] = findGreenArea(j, picture);


        return values;

    }


    public int findGreenArea(Channel a, Bitmap picture) {

        ArrayList<Integer> green = new ArrayList<Integer>();

        int initialOffset = 1;

        int offset = getOffset(a.yposition - 5 - initialOffset,a.xposition - 200,picture);
        
        for (int i : Functions.range(a.yposition - 5 - offset, a.yposition + 6 - offset, false)) {
            for (int j : Functions.range(a.xposition, a.xposition - 201, true)) {

                int pixel = picture.getPixel(i, j);

                int greenValue = Color.green(pixel);

                green.add(greenValue);

                //picture.setPixel(i,j,Color.YELLOW);


            }


        }


        for (int j : Functions.range(a.xposition,a.xposition - 201,true)){

            int i = a.yposition - 6 - offset;
                picture.setPixel(i,j,Color.WHITE);


            int k = a.yposition + 6 - offset;
                picture.setPixel(k,j,Color.WHITE);

        }


        for (int i : Functions.range(a.yposition - 6 -offset, a.yposition + 6 - offset,false)){

            int j = a.xposition - 201;

                picture.setPixel(i,j,Color.WHITE);

            int k = a.xposition;

                picture.setPixel(i, k, Color.WHITE);



        }



        int TotalSum = 0;
        for (int i = 0; i < green.size(); i++) {
            TotalSum = TotalSum + green.get(i);

        }

        int value = Math.round(TotalSum / 2000);

        return value;
    }
    
    
    private int getOffset(int x, int y,Bitmap picture) {

        ArrayList<Integer> row1 =  new ArrayList<Integer> ();
        ArrayList<Integer> row2 =  new ArrayList<Integer> ();
        ArrayList<Integer> row3 =  new ArrayList<Integer> ();

        int offset1 = 0;
        int offset2 = 0;
        int offset3 = 0;

        //for row1
        for(int i : Functions.range(x,x + 10,false)){
            int pixel = picture.getPixel(i,y+50);
            int green = Color.green(pixel);
            row1.add(green);
        }

        row1.trimToSize();

        for(int i = 0; i < row1.size();i++){
            if(i == row1.size() - 1){
                break;
            }

            int num1 = row1.get(i);
            int num2 = row1.get(i+1);

            if((Math.abs(num1 - num2) > 9)){

                if(i < 7 && num1 > num2){
                    offset1 = i - 7;
                    break;
                }
                else if(num2 > num1 && i > 2 ){
                    offset1 = i - 2;
                    break;
                }

            }

        }

        ///end for row1


        //for row2
        for(int i : Functions.range(x,x + 10,false)){
            int pixel = picture.getPixel(i,y+100);
            int green = Color.green(pixel);
            row2.add(green);
        }

        row2.trimToSize();

        for(int i = 0; i < row2.size();i++){
            if(i == row2.size() - 1){
                break;
            }

            int num1 = row2.get(i);
            int num2 = row2.get(i+1);

            if((Math.abs(num1 - num2) > 9)){

                if(i < 7 && num1 > num2){
                    offset2 = i - 7;
                    break;
                }
                else if(num2 > num1 && i > 2 ){
                    offset2 = i - 2;
                    break;
                }

            }

        }

        ///end for row 2



        //for row3
        for(int i : Functions.range(x,x + 10,false)){
            int pixel = picture.getPixel(i,y+150);
            int green = Color.green(pixel);
            row3.add(green);
        }

        row3.trimToSize();

        for(int i = 0; i < row3.size();i++){
            if(i == row3.size() - 1){
                break;
            }

            int num1 = row3.get(i);
            int num2 = row3.get(i+1);

            if((Math.abs(num1 - num2) > 9)){

                if(i < 7 && num1 > num2){
                    offset3 = i - 7;
                    break;
                }
                else if(num2 > num1 && i > 2 ){
                    offset3 = i - 2;
                    break;
                }

            }

        }

        ///end for row3



        return (int) ((offset1+offset2+offset3)/3);
    }    





}

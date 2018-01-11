package no.domain.diagnostics.VV;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class SquareFunctions{

    public static ArrayList<ArrayList<Integer>>firstSquareOuterLines(Bitmap picture, String function, int Lx1, int Lx2,int Ly1,int Ly2, int Ux1, int Ux2, int Uy1, int Uy2, int Dx1, int Dx2, int Dy1, int Dy2, int Rx1,int Rx2, int Ry1, int Ry2 ){




        ArrayList<Integer> listOfPoints = new ArrayList<Integer>(16);

        ArrayList<Integer> otherPoints = new ArrayList<Integer>(18);

        ArrayList<ArrayList<Integer>> containment = new ArrayList<ArrayList<Integer>>(3);

        ArrayList<Integer> XMidpoint = new ArrayList<Integer>(1);
        ArrayList<Integer> YMidpoint = new ArrayList<Integer>(1); //TODO: check


        // for first square left vertical line

        ArrayList<Integer> LXList = Functions.vertLineList(picture,Lx1,Lx2,Ly1,Ly2).get(0);
        ArrayList<Integer> listy = Functions.vertLineList(picture,Lx1,Lx2,Ly1,Ly2).get(1);

        int slopeL1 = Functions.findingSlope(LXList,listy, "vertical");
        int LY1 = Functions.findAxis(slopeL1,listy);

        int[] listx1 = convertIntegers(LXList);

        int TestLx1 = Functions.getLineFunctions(listx1,"coordinates")[0];
        int TestLx2 = Functions.getLineFunctions(listx1,"coordinates")[1];
        Functions.colorLine(picture, "vert" , TestLx1,LY1,TestLx2,LY1);
        Functions.colorPixel(picture, Functions.getLineFunctions(listx1,"midpointCoordinate")[0] ,LY1 , "white");

        int LMidpoint = Functions.getLineFunctions(listx1,"midpointCoordinate")[0];

        XMidpoint.add(LMidpoint);
        YMidpoint.add(LY1);

        //For First square up horizontal line

        ArrayList<Integer> listxU = Functions.horiLineList(picture,Ux1, Ux2, Uy1,Uy2).get(0);
        ArrayList<Integer> LUList = Functions.horiLineList(picture,Ux1,Ux2,Uy1,Uy2).get(1);


        int slopeU1 = Functions.findingSlope(listxU,LUList,"horizontal");
        int LU1 = Functions.findAxis(slopeU1, listxU);

        int[] listy1 = convertIntegers(LUList);

        int TestUy1 = Functions.getLineFunctions(listy1,"coordinates")[0];
        int TestUy2 = Functions.getLineFunctions(listy1, "coordinates")[1];
        int UMidpoint = Functions.getLineFunctions(listy1, "midpointCoordinate")[0];
        Functions.colorLine(picture,"hori",LU1, TestUy1, LU1, TestUy2); //TODO: make sure midpoint returns int
        Functions.colorPixel(picture,LU1, UMidpoint,"white");

        XMidpoint.add(LU1);
        YMidpoint.add(UMidpoint);


        //FOr first square bottom horizontal line

        ArrayList<Integer> listxD = Functions.horiLineListBelow(picture,Dx1,Dx2,Dy1,Dy2).get(0);
        ArrayList<Integer> LDList = Functions.horiLineListBelow(picture,Dx1,Dx2,Dy1,Dy2).get(1);

        int slopeD1 = Functions.findingSlope(listxD,LDList,"horizontal");
        int LD1 = Functions.findAxis(slopeD1,listxD);

        int[] listy2 = convertIntegers(LDList);

        int TestDy1 = Functions.getLineFunctions(listy2,"coordinates")[0];
        int TestDy2 = Functions.getLineFunctions(listy2,"coordinates")[1];

        int DMidpoint = Functions.getLineFunctions(listy2,"midpointCoordinate")[0];
        Functions.colorLine(picture,"hori",LD1, TestDy1,LD1,TestDy2);
        Functions.colorPixel(picture,LD1,DMidpoint,"white");

        XMidpoint.add(LD1);
        YMidpoint.add(DMidpoint);

        //For first square right vertical line


        ArrayList<Integer> LRList = Functions.vertLineListRight(picture,Rx1,Rx2,Ry1,Ry2).get(0);
        ArrayList<Integer> listyR = Functions.vertLineListRight(picture,Rx1,Rx2,Ry1,Ry2).get(1);

        int slopeR1 = Functions.findingSlope(LRList,listyR,"vertical");
        int LR1 = Functions.findAxis(slopeR1,listyR);

        int[] listx2 = convertIntegers(LRList);

        int TestRx1 = Functions.getLineFunctions(listx2, "coordinates")[0];
        int TestRx2 = Functions.getLineFunctions(listx2,"coordinates")[1];

        int RMidpoint = Functions.getLineFunctions(listx2,"midpointCoordinate")[0];
        Functions.colorLine(picture,"vert",TestRx1,LR1,TestRx2,LR1);
        Functions.colorPixel(picture,RMidpoint,LR1,"white");

        XMidpoint.add(RMidpoint);
        YMidpoint.add(LR1);


        if(function.equals("midpoint")){
            containment.add(0,XMidpoint);
            containment.add(1,YMidpoint);
            containment.trimToSize();
        }

        else if(function.equals("inner")){

            listOfPoints.add(0,TestLx1);
            listOfPoints.add(1,TestLx2);
            listOfPoints.add(2,LY1);
            listOfPoints.add(3,LY1);
            listOfPoints.add(4,LU1);
            listOfPoints.add(5,LU1);
            listOfPoints.add(6,TestUy1);
            listOfPoints.add(7,TestUy2);
            listOfPoints.add(8,LD1);
            listOfPoints.add(9,LD1);
            listOfPoints.add(10,TestDy1);
            listOfPoints.add(11,TestDy2);
            listOfPoints.add(12,TestRx1);
            listOfPoints.add(13,TestRx2);
            listOfPoints.add(14,LR1);
            listOfPoints.add(15,LR1);
            containment.add(0,listOfPoints);
            containment.trimToSize();

        }

        else if(function.equals("both")){
            listOfPoints.add(0,TestLx1);
            listOfPoints.add(1,TestLx2);
            listOfPoints.add(2,LY1);
            listOfPoints.add(3,LY1);
            listOfPoints.add(4,LU1);
            listOfPoints.add(5,LU1);
            listOfPoints.add(6,TestUy1);
            listOfPoints.add(7,TestUy2);
            listOfPoints.add(8,LD1);
            listOfPoints.add(9,LD1);
            listOfPoints.add(10,TestDy1);
            listOfPoints.add(11,TestDy2);
            listOfPoints.add(12,TestRx1);
            listOfPoints.add(13,TestRx2);
            listOfPoints.add(14,LR1);
            listOfPoints.add(15,LR1);
            containment.add(0,listOfPoints);
            containment.add(1,XMidpoint);
            containment.add(2,YMidpoint);


        }

    return containment;
    }//end of function




    public static ArrayList<ArrayList<Integer>> secondSquareOuterLines(Bitmap picture, String function, int Lx1, int Lx2,int Ly1,int Ly2, int Ux1, int Ux2, int Uy1, int Uy2, int Dx1, int Dx2, int Dy1, int Dy2, int Rx1,int Rx2, int Ry1, int Ry2 ){


        ArrayList<Integer> listOfPoints = new ArrayList<Integer>(16);

        ArrayList<Integer> otherPoints = new ArrayList<Integer>(18);

        ArrayList<ArrayList<Integer>> containment = new ArrayList<ArrayList<Integer>>(3);

        ArrayList<Integer> XMidpoint = new ArrayList<Integer>(1);
        ArrayList<Integer> YMidpoint = new ArrayList<Integer>(1); //TODO: check


        // for first square left vertical line

        ArrayList<Integer> LXList = Functions.vertLineList(picture,Lx1,Lx2,Ly1,Ly2).get(0);
        ArrayList<Integer> listy = Functions.vertLineList(picture,Lx1,Lx2,Ly1,Ly2).get(1);

        int slopeL1 = Functions.findingSlope(LXList,listy, "vertical");
        int LY1 = Functions.findAxis(slopeL1,listy);

        int[] listx1 = convertIntegers(LXList);

        int TestLx1 = Functions.getLineFunctions(listx1,"coordinates")[0];
        int TestLx2 = Functions.getLineFunctions(listx1,"coordinates")[1];
        Functions.colorLine(picture, "vert" , TestLx1,LY1,TestLx2,LY1);
        Functions.colorPixel(picture, Functions.getLineFunctions(listx1,"midpointCoordinate")[0] ,LY1 , "white");

        int LMidpoint = Functions.getLineFunctions(listx1,"midpointCoordinate")[0];

        XMidpoint.add(LMidpoint);
        YMidpoint.add(LY1);

        //For First square up horizontal line

        ArrayList<Integer> listxU = Functions.horiLineList(picture,Ux1, Ux2, Uy1,Uy2).get(0);
        ArrayList<Integer> LUList = Functions.horiLineList(picture,Ux1,Ux2,Uy1,Uy2).get(1);


        int slopeU1 = Functions.findingSlope(listxU,LUList,"horizontal");
        int LU1 = Functions.findAxis(slopeU1, listxU);

        int[] listy1 = convertIntegers(LUList);

        int TestUy1 = Functions.getLineFunctions(listy1,"coordinates")[0];
        int TestUy2 = Functions.getLineFunctions(listy1, "coordinates")[1];
        int UMidpoint = Functions.getLineFunctions(listy1, "midpointCoordinate")[0];
        Functions.colorLine(picture,"hori",LU1, TestUy1, LU1, TestUy2); //TODO: make sure midpoint returns int
        Functions.colorPixel(picture,LU1, UMidpoint,"white");

        XMidpoint.add(LU1);
        YMidpoint.add(UMidpoint);


        //FOr first square bottom horizontal line

        ArrayList<Integer> listxD = Functions.horiLineListBelow(picture,Dx1,Dx2,Dy1,Dy2).get(0);
        ArrayList<Integer> LDList = Functions.horiLineListBelow(picture,Dx1,Dx2,Dy1,Dy2).get(1);

        int slopeD1 = Functions.findingSlope(listxD,LDList,"horizontal");
        int LD1 = Functions.findAxis(slopeD1,listxD);

        int[] listy2 = convertIntegers(LDList);

        int TestDy1 = Functions.getLineFunctions(listy2,"coordinates")[0];
        int TestDy2 = Functions.getLineFunctions(listy2,"coordinates")[1];

        int DMidpoint = Functions.getLineFunctions(listy2,"midpointCoordinate")[0];
        Functions.colorLine(picture,"hori",LD1, TestDy1,LD1,TestDy2);
        Functions.colorPixel(picture,LD1,DMidpoint,"white");

        XMidpoint.add(LD1);
        YMidpoint.add(DMidpoint);

        //For first square right vertical line


        ArrayList<Integer> LRList = Functions.vertLineListRight(picture,Rx1,Rx2,Ry1,Ry2).get(0);
        ArrayList<Integer> listyR = Functions.vertLineListRight(picture,Rx1,Rx2,Ry1,Ry2).get(1);

        int slopeR1 = Functions.findingSlope(LRList,listyR,"vertical");
        int LR1 = Functions.findAxis(slopeR1,listyR);

        int[] listx2 = convertIntegers(LRList);

        int TestRx1 = Functions.getLineFunctions(listx2, "coordinates")[0];
        int TestRx2 = Functions.getLineFunctions(listx2,"coordinates")[1];

        int RMidpoint = Functions.getLineFunctions(listx2,"midpointCoordinate")[0];
        Functions.colorLine(picture,"vert",TestRx1,LR1,TestRx2,LR1);
        Functions.colorPixel(picture,RMidpoint,LR1,"white");

        XMidpoint.add(RMidpoint);
        YMidpoint.add(LR1);


        if(function.equals("midpoint")){
            containment.add(0,XMidpoint);
            containment.add(1,YMidpoint);
            containment.trimToSize();
        }

        else if(function.equals("inner")){

            listOfPoints.add(0,TestLx1);
            listOfPoints.add(1,TestLx2);
            listOfPoints.add(2,LY1);
            listOfPoints.add(3,LY1);
            listOfPoints.add(4,LU1);
            listOfPoints.add(5,LU1);
            listOfPoints.add(6,TestUy1);
            listOfPoints.add(7,TestUy2);
            listOfPoints.add(8,LD1);
            listOfPoints.add(9,LD1);
            listOfPoints.add(10,TestDy1);
            listOfPoints.add(11,TestDy2);
            listOfPoints.add(12,TestRx1);
            listOfPoints.add(13,TestRx2);
            listOfPoints.add(14,LR1);
            listOfPoints.add(15,LR1);
            containment.add(0,listOfPoints);
            containment.trimToSize();

        }

        else if(function.equals("both")){
            listOfPoints.add(0,TestLx1);
            listOfPoints.add(1,TestLx2);
            listOfPoints.add(2,LY1);
            listOfPoints.add(3,LY1);
            listOfPoints.add(4,LU1);
            listOfPoints.add(5,LU1);
            listOfPoints.add(6,TestUy1);
            listOfPoints.add(7,TestUy2);
            listOfPoints.add(8,LD1);
            listOfPoints.add(9,LD1);
            listOfPoints.add(10,TestDy1);
            listOfPoints.add(11,TestDy2);
            listOfPoints.add(12,TestRx1);
            listOfPoints.add(13,TestRx2);
            listOfPoints.add(14,LR1);
            listOfPoints.add(15,LR1);
            containment.add(0,listOfPoints);
            containment.add(1,XMidpoint);
            containment.add(2,YMidpoint);


        }

        return containment;

    }




    public static int[] midpointFirstSquareOuterLines(Bitmap picture,int[] List){

        int[] dataSet = new int[2];

        int Lx1 = List[0];
        int Lx2 = List[1];
        int Ly1 = List[2];
        int Ly2 = List[3];
        int Ux1 = List[4];
        int  Ux2 = List[5];
        int  Uy1 = List[6];
        int  Uy2 = List[7];
        int Dx1 = List[8];
        int  Dx2 = List[9];
        int  Dy1 = List[10];
        int  Dy2 = List[11];
        int  Rx1 = List[12];
        int  Rx2 = List[13];
        int Ry1 = List[14];
        int  Ry2 = List[15];

        ArrayList<Integer>  XList = new ArrayList<Integer>();
        ArrayList<Integer>  YList = new ArrayList<Integer>();
        XList = firstSquareOuterLines(picture,"midpoint",Lx1, Lx2, Ly1, Ly2, Ux1,Ux2,Uy1,Uy2,Dx1,Dx2,Dy1,Dy2,Rx1, Rx2, Ry1, Ry2).get(0);
        YList = firstSquareOuterLines(picture,"midpoint",Lx1, Lx2, Ly1, Ly2, Ux1,Ux2,Uy1,Uy2,Dx1,Dx2,Dy1,Dy2,Rx1, Rx2, Ry1, Ry2).get(1);

        int Vx1 = XList.get(0);
        int Vy1 = YList.get(0);
        int Hx2 = XList.get(1);
        int  Hy2 = YList.get(1);
        int Hx3 = XList.get(2);
        int  Hy3 = YList.get(2);
        int Vx4 = XList.get(3);
        int Vy4 = YList.get(3);


        int  x1 = Vx1;
        int y1 = Hy2;
        int x2 = Vx1;
        int  y2 = Hy3;
        int  x3 = Vx4;
        int  y3 = Hy2;
        int x4 = Vx4;
        int  y4 = Hy3;

        Functions.colorPixel(picture, Math.round((x1+x2+x3+x4)/4),Math.round((y1+y2+y3+y4)/4),"white");

        dataSet[0] = Math.round((x1+x2+x3+x4)/4);
        dataSet[1] = Math.round((y1+y2+y3+y4)/4);

        return dataSet;

    }




    public static int[] midPointSecondSquareOuterLines(Bitmap picture, int[] List){
        int[] dataSet = new int[2];

        int Lx1 = List[0];
        int Lx2 = List[1];
        int Ly1 = List[2];
        int Ly2 = List[3];
        int Ux1 = List[4];
        int  Ux2 = List[5];
        int  Uy1 = List[6];
        int  Uy2 = List[7];
        int Dx1 = List[8];
        int  Dx2 = List[9];
        int  Dy1 = List[10];
        int  Dy2 = List[11];
        int  Rx1 = List[12];
        int  Rx2 = List[13];
        int Ry1 = List[14];
        int  Ry2 = List[15];

        ArrayList<Integer>  XList = new ArrayList<Integer>();
        ArrayList<Integer>  YList = new ArrayList<Integer>();
        XList = secondSquareOuterLines(picture,"midpoint",Lx1, Lx2, Ly1, Ly2, Ux1,Ux2,Uy1,Uy2,Dx1,Dx2,Dy1,Dy2,Rx1, Rx2, Ry1, Ry2).get(0);
        YList = secondSquareOuterLines(picture,"midpoint",Lx1, Lx2, Ly1, Ly2, Ux1,Ux2,Uy1,Uy2,Dx1,Dx2,Dy1,Dy2,Rx1, Rx2, Ry1, Ry2).get(1);

        int Vx1 = XList.get(0);
        int Vy1 = YList.get(0);
        int Hx2 = XList.get(1);
        int  Hy2 = YList.get(1);
        int Hx3 = XList.get(2);
        int  Hy3 = YList.get(2);
        int Vx4 = XList.get(3);
        int Vy4 = YList.get(3);


        int  x1 = Vx1;
        int y1 = Hy2;
        int x2 = Vx1;
        int  y2 = Hy3;
        int  x3 = Vx4;
        int  y3 = Hy2;
        int x4 = Vx4;
        int  y4 = Hy3;

        Functions.colorPixel(picture, Math.round((x1+x2+x3+x4)/4),Math.round((y1+y2+y3+y4)/4),"white");

        dataSet[0] = Math.round((x1+x2+x3+x4)/4);
        dataSet[1] = Math.round((y1+y2+y3+y4)/4);

        return dataSet;

    }



    public static int[] firstSquare(Bitmap picture ,int[] List){


        boolean innerHasPassed = false;

        int[] finalPoints = new int[2];

        int lx1 = List[0];
        int lx2 = List[1];
        int ly1 = List[2];
        int ly2 = List[3];
        int ux1 = List[4];
        int  ux2 = List[5];
        int uy1 = List[6];
        int  uy2 = List[7];
        int  dx1 = List[8];
        int  dx2 = List[9];
        int   dy1 = List[10];
        int   dy2 = List[11];
        int  rx1 = List[12];
        int  rx2 = List[13];
        int   ry1 = List[14];
        int  ry2 = List[15];



        ArrayList<ArrayList<Integer>> OuterPoints = SquareFunctions.firstSquareOuterLines(picture, "both",lx1, lx2, ly1, ly2, ux1,ux2,uy1,uy2,dx1,dx2,dy1,dy2,rx1, rx2, ry1, ry2 );

        ArrayList<Integer> listOfPoints = OuterPoints.get(0);
        ArrayList<Integer> OXList = OuterPoints.get(1);
        ArrayList<Integer> OYList = OuterPoints.get(2);


        int Lx1 = listOfPoints.get(0);
        int Lx2 = listOfPoints.get(1);
        int Ly1 = listOfPoints.get(2);
        int Ly2 = listOfPoints.get(3);
        int Ux1 = listOfPoints.get(4);
        int  Ux2 = listOfPoints.get(5);
        int  Uy1 = listOfPoints.get(6);
        int  Uy2 = listOfPoints.get(7);
        int Dx1 = listOfPoints.get(8);
        int  Dx2 = listOfPoints.get(9);
        int  Dy1 = listOfPoints.get(10);
        int  Dy2 = listOfPoints.get(11);
        int  Rx1 = listOfPoints.get(12);
        int  Rx2 = listOfPoints.get(13);
        int Ry1 = listOfPoints.get(14);
        int  Ry2 = listOfPoints.get(15);


        ArrayList<Integer> XList = new ArrayList<Integer>();
        ArrayList<Integer> YList = new ArrayList<Integer>();


        try {

            ArrayList<ArrayList<Integer>> innerListPoints = SquareFunctions.firstSquareOuterLines(picture, "midpoint", Lx1, Lx2, Ly1, Ly2 + 3, Ux1, Ux2 + 3, Uy1, Uy2, Dx1 - 3, Dx2, Dy1, Dy2, Rx1, Rx2, Ry1 - 3, Ry2);
            XList = innerListPoints.get(0);
            YList = innerListPoints.get(1);
            innerHasPassed = true;
        }
        catch (Exception e){
            innerHasPassed = false;
        }



        //For the outer square

        int OXMidpoint;
        int OYMidpoint;

        if (!innerHasPassed) {


            int OVx1 = OXList.get(0);
            int OVy1 = OYList.get(0);
            int OHx2 = OXList.get(1);
            int OHy2 = OYList.get(1);
            int OHx3 = OXList.get(2);
            int OHy3 = OYList.get(2);
            int OVx4 = OXList.get(3);
            int OVy4 = OYList.get(3);


            int Ox1 = OVx1;
            int Oy1 = OHy2;
            int Ox2 = OVx1;
            int Oy2 = OHy3;
            int Ox3 = OVx4;
            int Oy3 = OHy2;
            int Ox4 = OVx4;
            int Oy4 = OHy3;


             OXMidpoint = Math.round((Ox1 + Ox2 + Ox3 + Ox4) / 4);
             OYMidpoint = Math.round((Oy1 + Oy2 + Oy3 + Oy4) / 4);

            Functions.colorPixel(picture, OXMidpoint, OYMidpoint, "white");

            finalPoints[0] = OXMidpoint;
            finalPoints[1] = OYMidpoint;

            return finalPoints;

        }

        //for both the outer square and inner square coordinates
        else {

            int OVx1 = OXList.get(0);
            int OVy1 = OYList.get(0);
            int OHx2 = OXList.get(1);
            int OHy2 = OYList.get(1);
            int OHx3 = OXList.get(2);
            int OHy3 = OYList.get(2);
            int OVx4 = OXList.get(3);
            int OVy4 = OYList.get(3);


            int Ox1 = OVx1;
            int Oy1 = OHy2;
            int Ox2 = OVx1;
            int Oy2 = OHy3;
            int Ox3 = OVx4;
            int Oy3 = OHy2;
            int Ox4 = OVx4;
            int Oy4 = OHy3;


            OXMidpoint = Math.round((Ox1 + Ox2 + Ox3 + Ox4) / 4);
            OYMidpoint = Math.round((Oy1 + Oy2 + Oy3 + Oy4) / 4);

            Functions.colorPixel(picture, OXMidpoint, OYMidpoint, "white");

            int Vx1 = XList.get(0);
            int Vy1 = YList.get(0);
            int Hx2 = XList.get(1);
            int Hy2 = YList.get(1);
            int Hx3 = XList.get(2);
            int Hy3 = YList.get(2);
            int Vx4 = XList.get(3);
            int Vy4 = YList.get(3);


            int x1 = Vx1;
            int y1 = Hy2;
            int x2 = Vx1;
            int y2 = Hy3;
            int x3 = Vx4;
            int y3 = Hy2;
            int x4 = Vx4;
            int y4 = Hy3;


            int IXMidpoint = Math.round((x1 + x2 + x3 + x4) / 4);
            int IYMidpoint = Math.round((y1 + y2 + y3 + y4) / 4);

            Functions.colorPixel(picture, IXMidpoint, IYMidpoint, "white");


            //For final midpoint

            int x = Math.round((IXMidpoint + OXMidpoint) / 2);
            int y = Math.round((IYMidpoint + OYMidpoint) / 2);

            Functions.colorPixel(picture, x, y, "green");

            finalPoints[0] = x;
            finalPoints[1] = y;


            return finalPoints;
        }

    }



    public static int[] secondSquare(Bitmap picture, int[] List){


        boolean innerHasPassed = false;

        int[] finalPoints = new int[2];

        int lx1 = List[0];
        int lx2 = List[1];
        int ly1 = List[2];
        int ly2 = List[3];
        int ux1 = List[4];
        int  ux2 = List[5];
        int uy1 = List[6];
        int  uy2 = List[7];
        int  dx1 = List[8];
        int  dx2 = List[9];
        int   dy1 = List[10];
        int   dy2 = List[11];
        int  rx1 = List[12];
        int  rx2 = List[13];
        int   ry1 = List[14];
        int  ry2 = List[15];



        ArrayList<ArrayList<Integer>> OuterPoints = SquareFunctions.firstSquareOuterLines(picture, "both",lx1, lx2, ly1, ly2, ux1,ux2,uy1,uy2,dx1,dx2,dy1,dy2,rx1, rx2, ry1, ry2 );

        ArrayList<Integer> listOfPoints = OuterPoints.get(0);
        ArrayList<Integer> OXList = OuterPoints.get(1);
        ArrayList<Integer> OYList = OuterPoints.get(2);


        int Lx1 = listOfPoints.get(0);
        int Lx2 = listOfPoints.get(1);
        int Ly1 = listOfPoints.get(2);
        int Ly2 = listOfPoints.get(3);
        int Ux1 = listOfPoints.get(4);
        int  Ux2 = listOfPoints.get(5);
        int  Uy1 = listOfPoints.get(6);
        int  Uy2 = listOfPoints.get(7);
        int Dx1 = listOfPoints.get(8);
        int  Dx2 = listOfPoints.get(9);
        int  Dy1 = listOfPoints.get(10);
        int  Dy2 = listOfPoints.get(11);
        int  Rx1 = listOfPoints.get(12);
        int  Rx2 = listOfPoints.get(13);
        int Ry1 = listOfPoints.get(14);
        int  Ry2 = listOfPoints.get(15);


        ArrayList<Integer> XList = new ArrayList<Integer>();
        ArrayList<Integer> YList = new ArrayList<Integer>();

        try {
            ArrayList<ArrayList<Integer>> innerListPoints = SquareFunctions.firstSquareOuterLines(picture, "midpoint", Lx1, Lx2, Ly1, Ly2 + 3, Ux1, Ux2 + 3, Uy1, Uy2, Dx1 - 3, Dx2, Dy1, Dy2, Rx1, Rx2, Ry1 - 3, Ry2);
            XList = innerListPoints.get(0);
            YList = innerListPoints.get(1);
            innerHasPassed = true;

        }catch (Exception e){
            innerHasPassed = false;
        }


        //For the outer square

        int OXMidpoint;
        int OYMidpoint;

        if (!innerHasPassed) {


            int OVx1 = OXList.get(0);
            int OVy1 = OYList.get(0);
            int OHx2 = OXList.get(1);
            int OHy2 = OYList.get(1);
            int OHx3 = OXList.get(2);
            int OHy3 = OYList.get(2);
            int OVx4 = OXList.get(3);
            int OVy4 = OYList.get(3);


            int Ox1 = OVx1;
            int Oy1 = OHy2;
            int Ox2 = OVx1;
            int Oy2 = OHy3;
            int Ox3 = OVx4;
            int Oy3 = OHy2;
            int Ox4 = OVx4;
            int Oy4 = OHy3;


             OXMidpoint = Math.round((Ox1 + Ox2 + Ox3 + Ox4) / 4);
             OYMidpoint = Math.round((Oy1 + Oy2 + Oy3 + Oy4) / 4);

            Functions.colorPixel(picture, OXMidpoint, OYMidpoint, "white");

            finalPoints[0] = OXMidpoint;
            finalPoints[1] = OYMidpoint;

            return finalPoints;

        }


        //for both the outer square and inner square coordinates
        else {

            int OVx1 = OXList.get(0);
            int OVy1 = OYList.get(0);
            int OHx2 = OXList.get(1);
            int OHy2 = OYList.get(1);
            int OHx3 = OXList.get(2);
            int OHy3 = OYList.get(2);
            int OVx4 = OXList.get(3);
            int OVy4 = OYList.get(3);


            int Ox1 = OVx1;
            int Oy1 = OHy2;
            int Ox2 = OVx1;
            int Oy2 = OHy3;
            int Ox3 = OVx4;
            int Oy3 = OHy2;
            int Ox4 = OVx4;
            int Oy4 = OHy3;


            OXMidpoint = Math.round((Ox1 + Ox2 + Ox3 + Ox4) / 4);
            OYMidpoint = Math.round((Oy1 + Oy2 + Oy3 + Oy4) / 4);

            Functions.colorPixel(picture, OXMidpoint, OYMidpoint, "white");

            int Vx1 = XList.get(0);
            int Vy1 = YList.get(0);
            int Hx2 = XList.get(1);
            int Hy2 = YList.get(1);
            int Hx3 = XList.get(2);
            int Hy3 = YList.get(2);
            int Vx4 = XList.get(3);
            int Vy4 = YList.get(3);


            int x1 = Vx1;
            int y1 = Hy2;
            int x2 = Vx1;
            int y2 = Hy3;
            int x3 = Vx4;
            int y3 = Hy2;
            int x4 = Vx4;
            int y4 = Hy3;


            int IXMidpoint = Math.round((x1 + x2 + x3 + x4) / 4);
            int IYMidpoint = Math.round((y1 + y2 + y3 + y4) / 4);

            Functions.colorPixel(picture, IXMidpoint, IYMidpoint, "white");


            //For final midpoint

            int x = Math.round((IXMidpoint + OXMidpoint) / 2);
            int y = Math.round((IYMidpoint + OYMidpoint) / 2);

            Functions.colorPixel(picture, x, y, "green");

            finalPoints[0] = x;
            finalPoints[1] = y;


            return finalPoints;
        }


    }







    public static int[] convertIntegers(ArrayList<Integer> integers){
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length ; i++){
            ret[i] = integers.get(i).intValue();
        }


        return ret;
    }




    public static int[] firstSquareOnly(Bitmap picture, int[] List){

        int[] finalPoints = new int[2];


        int lx1 = List[0];
        int lx2 = List[1];
        int ly1 = List[2];
        int ly2 = List[3];
        int ux1 = List[4];
        int  ux2 = List[5];
        int uy1 = List[6];
        int  uy2 = List[7];
        int  dx1 = List[8];
        int  dx2 = List[9];
        int   dy1 = List[10];
        int   dy2 = List[11];
        int  rx1 = List[12];
        int  rx2 = List[13];
        int   ry1 = List[14];
        int  ry2 = List[15];


        ArrayList<ArrayList<Integer>> OuterPoints = SquareFunctions.firstSquareOuterLines(picture, "midpoint",lx1, lx2, ly1, ly2, ux1,ux2,uy1,uy2,dx1,dx2,dy1,dy2,rx1, rx2, ry1, ry2);
        ArrayList<Integer> XList = OuterPoints.get(0);
        ArrayList<Integer> YList = OuterPoints.get(1);


        int Vx1 = XList.get(0);
        int Vy1 = YList.get(0);
        int  Hx2 = XList.get(1);
        int Hy2 = YList.get(1);
        int  Hx3 = XList.get(2);
        int Hy3 = YList.get(2);
        int Vx4 = XList.get(3);
        int Vy4 = YList.get(3);


        int  x1 = Vx1;
        int  y1 = Hy2;
        int x2 = Vx1;
        int y2 = Hy3;
        int x3 = Vx4;
        int  y3 = Hy2;
        int x4 = Vx4;
        int y4 = Hy3;


        int IXMidpoint = Math.round((x1+x2+x3+x4)/4);
        int IYMidpoint = Math.round((y1+y2+y3+y4)/4);

        Functions.colorPixel(picture,IXMidpoint,IYMidpoint,"green");


        finalPoints[0] = IXMidpoint;
        finalPoints[1] = IYMidpoint;


        return finalPoints;

    }


    public static int[] secondSquareOnly(Bitmap picture, int[] List){
        int[] finalPoints = new int[2];


        int lx1 = List[0];
        int lx2 = List[1];
        int ly1 = List[2];
        int ly2 = List[3];
        int ux1 = List[4];
        int  ux2 = List[5];
        int uy1 = List[6];
        int  uy2 = List[7];
        int  dx1 = List[8];
        int  dx2 = List[9];
        int   dy1 = List[10];
        int   dy2 = List[11];
        int  rx1 = List[12];
        int  rx2 = List[13];
        int   ry1 = List[14];
        int  ry2 = List[15];


        ArrayList<ArrayList<Integer>> OuterPoints = SquareFunctions.firstSquareOuterLines(picture, "midpoint",lx1, lx2, ly1, ly2, ux1,ux2,uy1,uy2,dx1,dx2,dy1,dy2,rx1, rx2, ry1, ry2);
        ArrayList<Integer> XList = OuterPoints.get(0);
        ArrayList<Integer> YList = OuterPoints.get(1);


        int Vx1 = XList.get(0);
        int Vy1 = YList.get(0);
        int  Hx2 = XList.get(1);
        int Hy2 = YList.get(1);
        int  Hx3 = XList.get(2);
        int Hy3 = YList.get(2);
        int Vx4 = XList.get(3);
        int Vy4 = YList.get(3);


        int  x1 = Vx1;
        int  y1 = Hy2;
        int x2 = Vx1;
        int y2 = Hy3;
        int x3 = Vx4;
        int  y3 = Hy2;
        int x4 = Vx4;
        int y4 = Hy3;


        int IXMidpoint = Math.round((x1+x2+x3+x4)/4);
        int IYMidpoint = Math.round((y1+y2+y3+y4)/4);

        Functions.colorPixel(picture,IXMidpoint,IYMidpoint,"green");


        finalPoints[0] = IXMidpoint;
        finalPoints[1] = IYMidpoint;


        return finalPoints;
    }

}

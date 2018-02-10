package com.mckinsey.ckc.sf.utils;

import com.mckinsey.ckc.sf.constants.IConstants;
import com.mckinsey.ckc.sf.data.Coordinate;

public class Tools implements IConstants{
	
	//This function is used to calculate distance between two points based on their coordinates
	public static double getDirectDistance(Coordinate x, Coordinate y){
//		double t = Math.acos(Math.sin(x.getY()/180*Math.PI)*Math.sin(y.getY()/180*Math.PI)+
//				Math.cos(x.getY()/180*Math.PI)*Math.cos(y.getY()/180*Math.PI)*Math.cos(x.getX()/180*Math.PI-y.getX()/180*Math.PI));
//		double d = Math.floor(t*180*60/Math.PI*100000)/100000*1.852*1000;
		double t = Math.sqrt(Math.pow(y.getX()-x.getX(), 2)+Math.pow(y.getY()-x.getY(), 2));
		return t;
	}
	
	//This function is used to calculate directional vector between two points based on their coordinates
	public static Coordinate vectorize(Coordinate x, Coordinate y){
		return new Coordinate(y.getX()-x.getX(),y.getY()-x.getY());
	}
	
	//This function is used to calculate vector length
	public static double getVectorLength(Coordinate x, Coordinate y){
		return Math.sqrt(Math.pow(y.getX()-x.getX(), 2)+Math.pow(y.getY()-x.getY(), 2));
	}
	
	//This function is used to calculate angle between two vectors
	public static double getAngle(Coordinate vector1, Coordinate vector2){
		double angle = 180*Math.acos(elementProduct(vector1,vector2)/Math.sqrt(elementProduct(vector1,vector1))/Math.sqrt(elementProduct(vector2,vector2))/Math.PI);
		if(Double.isNaN(angle))
			angle = 999;
		return angle;
	}
	
	//implements %*%
	public static double elementProduct(Coordinate vector1, Coordinate vector2){
		return vector1.getX()*vector2.getX()+vector1.getY()*vector2.getY();
	}
	
	//This function is used to calculate third side of the triagnle
	public static double calculateThirdSide(double lengthA,double lengthB, double theta){
		return Math.sqrt(Math.pow(lengthA, 2)+Math.pow(lengthB, 2)-2*Math.cos(theta*Math.PI/180)*lengthA*lengthB);
	}
	
	
	public static void main(String[] args){
		//10^((1 - 10*60/5)/((24*60/5)-(10*60/5))-1))*9.9 = 0.1937768
		//(10^((t - 10*60/5)/((24*60/5)-(10*60/5))-1)) = 0.5179475
//		double t = Math.pow(10,(1 - 10*60/5)/((24*60/5)-(10*60/5))-1 );
		double t = Math.pow(10,(240 - 10*60.0/5)/((24*60/5)-(10*60/5))-1);
		System.out.println(t);
//		List<Integer> test = new ArrayList<Integer>();
//		test.add(1);
//		test.add(2);
//		test.add(3);
//		test.add(4);
//		Iterator<Integer> it = test.iterator();
//		while(it.hasNext()){
//			int temp = it.next();
//			if(temp == 2){
//				it.remove();
//			}
//			if(temp ==4){
//			}
//			System.out.println(temp);
//		}

	}
}
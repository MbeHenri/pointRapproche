package pointRapproche.main;

import java.util.LinkedList;

import pointRapproche.models.Point;
import pointRapproche.utils.Couple;

public class Main {

	public static void main(String[] args) {
		
		LinkedList<Point> nuage =new LinkedList<>();
		
		Point aux=null;
		for (int i = 0; i < 10000; i++) {
			aux=new Point(Math.random()*100, Math.random()*100);
			if(!exits(nuage,aux))
				nuage.add(aux);
		}
		
		Point[] points=new Point[nuage.size()];
		int i=0;
		for (Point point : nuage) 
			points[i++]=point;
		
		for (Point point : points) {
			System.out.println(point);
		}
		
		Couple<Point> c=Point.coupleMin(points);
		System.out.println(c);
	}
	private static boolean exits(LinkedList<Point> nuage, Point aux) {
		for (Point point : nuage) 
			if(point.equals(aux))
				return true;
		return false;
	}

}

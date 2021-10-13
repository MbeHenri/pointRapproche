package pointRapproche.models;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import pointRapproche.utils.Couple;

public class Point {
	private double x;
	private double y;
	
	public Point(double x , double y) {
		setX(x);
		setY(y);
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "("+x+", "+y+")";
	}
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Point) {
			Point aux=(Point)obj;
			if(aux==this)
				return true;
			if(aux.x==x && aux.y==y)
				return true;
		}
		return false;
	}
	
	public static Couple<Point> coupleMin(Point[] nuage) {
		//on verifie que le tableau contient plus de deux points
		if(nuage==null || nuage.length<=1)
			return null;
		
		/*
		 * on trie le tableau selon l'axe des abscisses
		 * (on tiens aussi compte de l'ordre leurs y si les x sont égaux)
		 */
		Arrays.sort(nuage, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if((o1.x - o2.x)<0)
					return -1;
				else if((o1.x - o2.x)>0)
					return 1;
				if((o1.y - o2.y)<0)					
					return -1;
				else if((o1.y - o2.y)>0)
					return 1;
				return 0;
			}
		});
		Point[] X= Arrays.copyOf(nuage, nuage.length);
		
		/*
		 * on trie le tableau selon l'axe des ordonnees
		 * (on tiens aussi compte de l'ordre leurs x si les y sont égaux)
		 */
		Arrays.sort(nuage, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if((o1.y - o2.y)<0)					
					return -1;
				else if((o1.y - o2.y)>0)
					return 1;
				if((o1.x - o2.x)<0)
					return -1;
				else if((o1.x - o2.x)>0)
					return 1;
				return 0;
			}
		});
		Point[] Y= Arrays.copyOf(nuage, nuage.length); 
		return coupleMinBis(X,Y,nuage.length);
	}
	public static Couple<Point> coupleMinBis(Point[] X, Point[] Y , int n){
		if(X.length <= 3) {
			//si on est dans le cas de deux points alors il suffit de renvoyé ces points 
			if(X.length==2) 
				return new Couple<Point>(X[0], X[1]);
			
			//dans le cas où on as 3 point on recherche le couple entre ces derniers de manière naive
			Couple<Point> cmin=new Couple<Point>(X[0], X[1]);
			double $=distance(cmin);
			double $aux=distance(X[0], X[2]);
			if($aux<$) {
				cmin=new Couple<Point>(X[0], X[2]);
				$=$aux;
			}
			$aux=distance(X[1], X[2]);
			if($aux<$) 
				cmin=new Couple<Point>(X[1],X[2]);
			return cmin;
		}
		/*
		 * on divise le probleme en sous problèmes
		 */

		Point[] Xg=new Point[n/2];
		Point[] Yg=new Point[n/2];
		Point[] Xd=new Point[n - (n/2)];
		Point[] Yd=new Point[n - (n/2)];
		
		//on calcule Xd et Xg
		for (int i = 0; i < X.length; i++)
			if( i < (n/2) )
				Xg[i]=X[i];
			else 
				Xd[i-(n/2)]=X[i];
		
		//on calcule Yd et Yg (a revoir pour le cas les points de divisions ont le meme abscisse)
		final double S0=(X[(n/2)-1].x + X[(n/2)].x)/2;
		{
		boolean ok=false;
		int ig=0,id=0;
		for (int i = 0; i < Y.length; i++)
			if(Y[i].x < S0 )
				Yg[ig++]=Y[i];
			else if(Y[i].x > S0)
				Yd[id++]=Y[i];
			else {
				if(ok)
					Yg[ig++]=Y[i];
				else
					Yd[id++]=Y[i];
				if(X[(n/2)-1].equals(Y[i]))
					ok=true;
			}
		}
		/*
		 * on resout les sous problèmes 
		 */
		//on recupere les points les plus proches de la partie gauche et droite 
		Couple<Point> cg= coupleMinBis(Xg, Yg, n/2);
		Couple<Point> cd= coupleMinBis(Xd, Yd, n - (n/2));
		
		//on determine la distance la plus pétite
		double $g=distance(cg);
		double $d=distance(cd);
		double $=Math.min($g, $d);
		/*
		 * on détermine les points se trouvant dans la bande |x-s0|< $ avec S0 etant la ligne de séparation par ordre croissant
		 * des ordonnées 
		 */
		Point[] bande= bande(Y,S0,$);
		/*
		 * on recherche les points les plus se trouvant dans la bande telque la distance les séparant est inférieur à $
 		 * ce qui implique que selon l'axe des ordonnées pour un point quelconque de la bande , seul les 7 le suivant 
 		 * sont à prendre en compte
		 * et ensuite on choisi les points les plus proches de tout le nuage 
		 */
		Couple<Point> cmin=$g==$?cg:cd;
		if(bande!=null && bande.length!=1) {
			int end=0;
			for (int i = 0; i < bande.length; i++) {
				end=Math.min(i+7,bande.length-1);
				for (int j = i+1; j <= end; j++) {
					double $aux=distance(bande[i], bande[j]);
					if ( $aux < $ ) {
						cmin=new Couple<Point>(bande[i], bande[j]);
						$=$aux;
					}
				}
			}
		}
		return cmin;
	}
	public static double distance(Couple<Point> c) {
		return c==null?null:Math.sqrt(
				Math.pow(c.getE1().x - c.getE2().x, 2) + Math.pow(c.getE1().y - c.getE2().y, 2)
		);
	}
	public static double distance(Point e1 , Point e2) {
		return Math.sqrt(
				Math.pow(e1.x - e2.x, 2) + Math.pow(e1.y - e2.y, 2)
		);
	}
	private static Point[] bande(Point[] Y, final double S0, double $) {
		LinkedList<Point> bd=new LinkedList<>();
		for (int i = 0; i < Y.length ; i++){
			if (Math.abs(Y[i].x - S0) < $) 
				bd.add(Y[i]);
		}
		if(bd.size()==0)
			return null;
		Point[] points=new Point[bd.size()];
		int i=0;
		for (Point point : bd) {
			points[i++]=point;
		}
		return points;
	}
	
}

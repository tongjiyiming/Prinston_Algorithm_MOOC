import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class FastCollinearPoints {
	private List<LineSegment> lineSegments = new ArrayList<LineSegment>();
	private int numberOfSegments;
	private List<PointPair> pointPairs = new ArrayList<PointPair>();
	
	public FastCollinearPoints(Point[] points) {
		// finds all line segments containing 4 or more points
		// check corner cases
		validatePoints(points);

		Point[] pointsCopy = Arrays.copyOf(points, points.length);
		while (pointsCopy.length > 3 && pointsCopy != null) {
			pointsCopy = findCollinearOfFirstPoint(pointsCopy);
		}
   }
   
	private class PointPair{
		public Point small;
		public Point large;
		public double slope;
		
		public Comparator<PointPair> slopeOrder(){
	        return new Comparator<PointPair>() {
	            @Override
	            public int compare(PointPair pp1, PointPair pp2) {
	                double slopeDiff = pp1.slope - pp2.slope;
	                if (slopeDiff > 0) {
	                    return 1;
	                } else if (slopeDiff < 0) {
	                    return -1;
	                } else {
	                    return 0;
	                }
	            }
	        };
		}
	}
	
   private Point[] findCollinearOfFirstPoint(Point[] pointsCopy) {
	   // use the first point as origin, and check if it has other collinear points, if not, delect it
	   // if there are, delect all the the 3 or more collinear points and return the remaining points
	   Point p = pointsCopy[0];
	   Arrays.sort(pointsCopy, p.slopeOrder());
	   int i = 1;
	   while ( i < pointsCopy.length - 2 ) {
		   int j = i+1;
		   int k = j+1;
		   double slope_i = p.slopeTo(pointsCopy[i]);
		   double slope_j = p.slopeTo(pointsCopy[j]);
		   if (slope_i != slope_j) {
			   i++;
		   }
		   else {
			   double slope_k = p.slopeTo(pointsCopy[k]);
			   if (slope_j != slope_k) {
				   i = k;
			   }
			   else { 
				   // find three collinear points
				   // find if there are more collinear points
				   while (k < pointsCopy.length-1 && slope_k == p.slopeTo(pointsCopy[k+1])) {
					   k++;
				   }
				   
				   PointPair pp = maximalLineSegment(Arrays.copyOfRange(pointsCopy, i, k+1));
				   addLineSegment( pp );
				   i = k+1;
			   }
		   }
	   }
	   
	   Point[] m = new Point[pointsCopy.length-1];
	   for ( int h=1; h < pointsCopy.length; h++) {
		   m[h-1] = pointsCopy[h];
	   }
	   return m;
   }

	private PointPair maximalLineSegment(Point[] pointsInLine) {
		Point min = pointsInLine[0];
		for ( int i=1; i < pointsInLine.length && min.compareTo(pointsInLine[i]) > 0; i++) {
			min = pointsInLine[i];
		}
		
		Point max = pointsInLine[0];
		for ( int i=1; i < pointsInLine.length && max.compareTo(pointsInLine[i]) < 0; i++) {
			max = pointsInLine[i];
		}
		
		PointPair pp = new PointPair();
		pp.small = min;
		pp.large = max;
		pp.slope = min.slopeTo(max);
		return pp;
	}
	
	private void addLineSegment(PointPair pp) {
		PointPair[] pps = pointPairs.toArray( new PointPair[pointPairs.size()]);
		int ind = Arrays.binarySearch(pps, pp, pp.slopeOrder());
		
		if ( ind >=0 ) {
			if ( pps[ind].small.compareTo(pp.small) > 0 ) pps[ind].small = pp.small;
			if ( pps[ind].large.compareTo(pp.large) < 0 ) pps[ind].large = pp.large;
		}
		else {
			lineSegments.add(new LineSegment(pp.small, pp.large));
			pointPairs.add(pp);
		}
	}
	
	/**
	 * return the number of found line segments
	 * @return the number of found line segments
	 */
   public int numberOfSegments()  {
	   // the number of line segments
	   return numberOfSegments;
   }
   
	/**
	 * return the array containing LineSegments
	 * @return the array containing LineSegments
	 */
   public LineSegment[] segments() {
	   // the line segments
	   return lineSegments.toArray(new LineSegment[lineSegments.size()]);
   }
   
	private void validatePoints(Point[] points) {
		// validate the input array not with null or duplicated point
		if (points == null) throw new java.lang.IllegalArgumentException("points inputs is null!");
		for (int p=0; p < points.length-1; p++) {
			if (points[p] == null) throw new java.lang.IllegalArgumentException("There is a null point in point array!");
			for (int q=p+1; q < points.length; q++) {
				if (points[p].compareTo(points[q]) == 0) throw new java.lang.IllegalArgumentException("There exists points with same coordinates!");
			}
		}
		if (points[points.length-1] == null) throw new java.lang.IllegalArgumentException("There is a null point in point array!");
	}
	
	public static void main(String[] args) {

	    // read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
//	    StdDraw.enableDoubleBuffering();
//	    StdDraw.setXscale(0, 32768);
//	    StdDraw.setYscale(0, 32768);
//	    for (Point p : points) {
//	        p.draw();
//	    }
//	    StdDraw.show();

	    // print and draw the line segments
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
//	        segment.draw();
	    }
//	    StdDraw.show();
	}
}
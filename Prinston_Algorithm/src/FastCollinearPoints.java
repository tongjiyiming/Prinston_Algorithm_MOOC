import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class FastCollinearPoints {
	// assume that is no 5 or more points collinear
	private List<LineSegment> lineSegments = new ArrayList<LineSegment>();
	private List<PointPair> pointPairs = new ArrayList<PointPair>();
	private int numberOfSegments;
	
	public FastCollinearPoints(Point[] points) {
		// check corner cases
		validatePoints(points);

		for ( int i=0; i < points.length; i++) {
			findCollinearOfPoint(points, i);
		}
   }
   
	//helper class
	private static class PointPair{
		public Point small;
		public Point large;
		public double slope;
		
		public static Comparator<PointPair> slopeOrder(){
	        return new Comparator<PointPair>() {
	            @Override
	            public int compare(PointPair pp1, PointPair pp2) {
	                if (pp1.slope > pp2.slope) {
	                    return 1;
	                } else if (pp1.slope < pp2.slope) {
	                    return -1;
	                } else {
	                    return 0;
	                }
	            }
	        };
		}
	}
	
   private void findCollinearOfPoint(Point[] points, int n) {
	   // use the first point as origin, and check if it has other collinear points, if not, delect it
	   // if there are, delect all the the 3 or more collinear points and return the remaining points
	   Point p = points[n];
	   Point[] pointsCopy = Arrays.copyOf(points, points.length);
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
				   // check maximal point pair and add to lineSegments
				   PointPair pp = maximalLineSegment(new Point[] {p, pointsCopy[i], pointsCopy[j], pointsCopy[k]});
				   addLineSegment( pp );
				   i = k+1;
			   }
		   }
	   }
   }
	
	private PointPair maximalLineSegment(Point[] pointsInLine) {
		Point min = pointsInLine[0];
		for ( int i=1; i < pointsInLine.length; i++) {
			if ( min.compareTo(pointsInLine[i]) >= 0 ) min = pointsInLine[i];
		}
		
		Point max = pointsInLine[0];
		for ( int i=1; i < pointsInLine.length; i++) {
			if ( max.compareTo(pointsInLine[i]) <= 0 ) max = pointsInLine[i];
		}
		
		PointPair pp = new PointPair();
		pp.small = min;
		pp.large = max;
		pp.slope = min.slopeTo(max);
		return pp;
	}
	
	private boolean isTwoPointPairCollinear(PointPair pp1, PointPair pp2) {
		// if this pair is collinear with another pair
		return pp1.small.slopeTo(pp2.large) == pp1.large.slopeTo(pp2.small) 
				&& pp1.large.slopeTo(pp2.large) == pp1.small.slopeTo(pp2.small);
	}
	
	private void addLineSegment(PointPair pp) {
		for (int i=0; i < pointPairs.size(); i++) {
			PointPair exist_pp = pointPairs.get(i);
			if ( exist_pp.slope == pp.slope && isTwoPointPairCollinear(exist_pp, pp) ) {
		        // update this list if this pair is collinear with another pair;
				PointPair new_pp = maximalLineSegment(new Point[] {exist_pp.small, exist_pp.large, pp.small, pp.large});
				pointPairs.set(i, new_pp);
				lineSegments.set(i, new LineSegment(new_pp.small, new_pp.large));
				return;
			}
		}
		
		lineSegments.add(new LineSegment(pp.small, pp.large));
		pointPairs.add(pp);
		numberOfSegments++;
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
		if (points == null) throw new java.lang.IllegalArgumentException("points inputs is null!");
		for (int p=0; p < points.length; p++) {
			if (points[p] == null) throw new java.lang.IllegalArgumentException("There is a null point in point array!");
		}
		for (int p=0; p < points.length-1; p++) {
			for (int q=p+1; q < points.length; q++) {
				if (points[p].compareTo(points[q]) == 0) throw new java.lang.IllegalArgumentException("There exists points with same coordinates!");
			}
		}
	}
}
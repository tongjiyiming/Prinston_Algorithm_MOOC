import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class BruteCollinearPoints {
	private List<LineSegment> lineSegments = new ArrayList<LineSegment>();
	private int numberOfSegments;
	private List<PointPair> pointPairs = new ArrayList<PointPair>();
	
	/**
	 * take an array of Point, and test if four points in a line segment, store the linesegments, and create an instance in a 
	 * brute force way.
	 * @param points
	 */
	public BruteCollinearPoints(Point[] points) {
		// finds all line segments containing 4 points
		// check corner cases
		validatePoints(points);
		
		// bruteforce the points to find line segments
		for (int p=0; p < points.length-3; p++) {
			for (int q=p+1; q<points.length-2; q++) {
				for (int r=q+1; r<points.length-1; r++) {
					for (int s=r+1; s<points.length; s++) {
						if ( isCollinearPoints(points[p], points[q], points[r], points[s]) ) {
							Point[] pointsInLine = new Point[] {points[p], points[q], points[r], points[s]};
							PointPair pp = maximalLineSegment(pointsInLine);
							addLineSegment( pp );
						}
					}
				}
			}
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
	
	private PointPair maximalLineSegment(Point[] pointsInLine) {
		Point min = pointsInLine[0];
		for ( int i=1; i < pointsInLine.length; i++) {
			if ( min.compareTo(pointsInLine[i]) > 0 ) min = pointsInLine[i];
		}
		
		Point max = pointsInLine[0];
		for ( int i=1; i < pointsInLine.length; i++) {
			if ( max.compareTo(pointsInLine[i]) < 0 ) max = pointsInLine[i];
		}
		
		PointPair pp = new PointPair();
		pp.small = min;
		pp.large = max;
		pp.slope = min.slopeTo(max);
		return pp;
	}

	private boolean isCollinearPoints(Point p, Point q, Point r, Point s) {
		//check if points p, q, r, s are in the same line segment
		double p_q_slope = p.slopeTo(q);
		double p_r_slope = p.slopeTo(r);
		double p_s_slope = p.slopeTo(s);
		return  p_q_slope == p_r_slope && p_q_slope == p_s_slope;
	}
	
	private void addLineSegment(PointPair pp) {
//		PointPair[] pps = pointPairs.toArray( new PointPair[pointPairs.size()]);
//		int ind = Arrays.binarySearch(pps, pp, pp.slopeOrder());
//		
//		if ( ind >=0 && pp.slope != pp.small.slopeTo(pps[ind].small)) {
//			if ( pps[ind].small.compareTo(pp.small) > 0 ) pps[ind].small = pp.small;
//			if ( pps[ind].large.compareTo(pp.large) < 0 ) pps[ind].large = pp.large;
//		}
//		else {
//			lineSegments.add(new LineSegment(pp.small, pp.large));
//			pointPairs.add(pp);
//		}
		for ( PointPair exist_pp : pointPairs) {
			if ( ( exist_pp.small.compareTo(pp.small) == 0 && exist_pp.large.compareTo(pp.large) == 0 )
					| (exist_pp.small.compareTo(pp.large) == 0 && exist_pp.large.compareTo(pp.small) == 0 ) ) {
				return;
			}
		}
		
		lineSegments.add(new LineSegment(pp.small, pp.large));
		numberOfSegments++;
	}
	
	private void validatePoints(Point[] points) {
		if (points == null) throw new java.lang.IllegalArgumentException("points inputs is null!");
		for (int p=0; p < points.length; p++) {
			if (points[p] == null) throw new java.lang.IllegalArgumentException("There is a null point in point array!");
		}
		for (int p=0; p < points.length-1; p++) {
			if (points[p].compareTo(points[p+1]) == 0) throw new java.lang.IllegalArgumentException("There exists points with same coordinates!");
		}
}
	
	/**
	 * return the number of found line segments
	 * @return the number of found line segments
	 */
	public int numberOfSegments() {
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
}
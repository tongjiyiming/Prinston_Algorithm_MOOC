package lecture1;

public class MyBinarySearch {
    private static int[] arr;
    
    public MyBinarySearch( int[] i){
	arr = i;
    }
    
    public static int BinarySearch( int a ){
	int lo = 0;
	int hi = arr.length - 1;
	
	while (lo <= hi) { //**** pay attention to this line
	    int mid = lo + (hi - lo)/2; //**** pay attention to this line
	    if (arr[mid] > a) { hi = mid - 1; }
	    else if (arr[mid] < a) { lo = mid + 1; }
	    else { return mid; }
	}
	
	return -1;
    }
}
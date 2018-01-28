package lecture1;

// run time goes to M + N log*N. log*N means the iterated times that N is logged to 1.
public class weightedQuickUnionUF {
    private int[] id;
    private int[] sz; // keep track of the total nodes of tree/size of tree
    
    public weightedQuickUnionUF(int N){
	id = new int[N];
	sz = new int[N];
	for (int i = 0; i < N; i++){
	    id[i] = i;
	    sz[i] = 1;
	}
    }
    
    public boolean connected(int a, int b){
	return (root(a) == root(b));
    }
    
    public void union(int a, int b){
	int rootA = root(a);
	int rootB = root(b);
	if (sz[a] < sz[b]){
	    id[rootA] = rootB;
	    sz[rootB] += sz[rootA];
	} else {
	    id[rootB] = rootA;
	    sz[rootA] += sz[rootB];
	}
    }

    public int root(int i){
	int root = i;
	while (id[root] != root){
	    id[root] = id[id[root]]; // only extra for ***path compression***
	    root = id[root];
	}
	return root;
    }

    public void main(String[] args){
	System.out.println("");
    }
}

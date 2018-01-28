package lecture1;

public class QuickUnionUF {
    private int[] id;
    
    public QuickUnionUF(int N){
	id = new int[N];
	for (int i = 0; i < N; i++){
	    id[i] = i;
	}
    }
    
    public boolean connected(int a, int b){
	return (root(a) == root(b));
    }
    
    public void union(int a, int b){
	int rootA = root(a);
	int rootB = root(b);
	id[rootA] = rootB;
    }

    public int root(int i){
	int root = i;
	while (id[root] != root) root = id[root];
	return root;
    }

    public void main(String[] args){
	System.out.println("");
    }
}

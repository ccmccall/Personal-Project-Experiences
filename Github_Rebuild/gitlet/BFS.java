package gitlet;

import java.util.TreeMap;

public class BFS {
    //**instance variables to make algorithm work */
    private TreeMap<Integer, String> indexMapping;
    private String[] edgeTo;
    private int[] distTo;


    public BFS() {
        this.indexMapping = new TreeMap<>();
        this.distTo = new int[100];
        this.edgeTo = new String[100];
    }

    //*the actual implementation */
    public void bfsProcess(String commitHash) {
        Queue<String> fringe = new Queue<>();
        boolean[] marked = new boolean[100];
        fringe.enqueue(commitHash);
        this.indexMapping.put(0, commitHash);
        marked[0] = true;
        int index = 1;
        while (!fringe.isEmpty()) {
            String currentCommit = fringe.dequeue();
            int currCommIndex = index;
            String[] parentHashes = getParentHashes(currentCommit);
            for (int i = 0; i < 2; i++) {
                if (!parentHashes[i].equals("null")) {
                    if (i == 2) {
                        currCommIndex += 1;
                    }
                    if (!this.indexMapping.containsValue(parentHashes[i])) {
                        fringe.enqueue(parentHashes[i]);
                        this.indexMapping.put(index, parentHashes[i]);
                        marked[index] = true;
                        this.edgeTo[index] = currentCommit;
                        this.distTo[index] = this.distTo[currCommIndex] + 1;
                        index += 1;
                    }
                }
            }
        }
    }

    public String[] getParentHashes(String commitHash) {
        Commit commitObj = Commit.commitFromFile(commitHash);
        return commitObj.getParentHashes();
    }


    public static String latestCommonAncestor(BFS activeBFS, BFS inputBFS) {
        String inputHead = inputBFS.indexMapping.get(0);
        String activeHead = activeBFS.indexMapping.get(0);
        String[] lca = new String[1];
        for (int i = 0; i < activeBFS.indexMapping.size(); i++) {
            if (inputBFS.indexMapping.containsValue(activeBFS.indexMapping.get(i))) {
                lca[0] = activeBFS.indexMapping.get(i);
                break;
            }
        }
        String lcaHash = lca[0];
        if (lcaHash.equals(inputHead)) {
            return "failure case 1";
        }
        if (lcaHash.equals(activeHead)) {
            return "failure case 2";
        }
        return lcaHash;
    }

}

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class SpectralAlignment extends SequencingAlgorithm {


    public static int align(Vector<Integer> sp1, Vector<Integer> sp2) {

        Set<Integer> unique = new HashSet<>();  // checking of similar spectra sizes after removing of duplicates
        unique.addAll(sp1);
        sp1.clear();
        for (Integer el : unique) {
            sp1.add(el);
        }
        unique.clear();
        unique.addAll(sp2);

        sp2.clear();
        for (Integer el : unique) {
            sp2.add(el);
        }
        sp1.sort(Comparator.<Integer>naturalOrder());
        sp2.sort(Comparator.<Integer>naturalOrder());
        System.out.println(sp1);
        System.out.println(sp2);

        int n = sp1.size();
        int m = sp2.size();

        if (n != m) {
            System.out.println("Different sizes of spectra");
            return 0;
        }


        int MAX_K = n;
        int[][][] D = new int[n][n][MAX_K + 1];
        int[][][] M = new int[n][n][MAX_K + 1];

        for (int k = 0; k <= MAX_K; k++) {

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {

                    if (i == 0 && j == 0) {
                        D[i][j][k] = 1;
                        M[i][j][k] = 1;
                        continue;
                    }

                    if (i == 0 || j == 0) {
                        D[i][j][k] = 0;
                        M[i][j][k] = 0;
                        continue;
                    }

                    if (k == 0) {
                        D[i][j][k] = 0;
                    } else {
                        D[i][j][k] = M[i - 1][j - 1][k - 1] + 1;
                    }

                    if (sp1.get(i) - sp1.get(i - 1) == sp2.get(j) - sp2.get(j - 1)) {
                        D[i][j][k] = Math.max(D[i][j][k], D[i - 1][j - 1][k] + 1);
                    }

                    M[i][j][k] = Math.max(D[i][j][k], Math.max(M[i - 1][j][k], M[i][j - 1][k]));

                }
            }
            /*System.out.println("K = " + k);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(M[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.print(D[i][j][k] + " ");
                }
                System.out.println();
            }
            */

            System.out.println("Result for k = " + k + ": " + D[n - 1][n - 1][k]);
            if (D[n - 1][n - 1][k] == n) {
                System.out.println("Answer is k = " + k);
                break;
            }

        }


        return 0;
    }


    public static void main(String[] args) {

        int ar[] = {1, 6, 16, 26, 36, 40};
        int ar2[] = {1, 2, 13, 23, 33, 37};

        //int ar[] = {10, 20, 30, 40, 50, 60};
        //int ar2[] = {10, 20, 35, 45, 55, 70};

        //int ar[] = {10, 20, 30, 40, 50, 60, 70};
        //int ar2[] = {10, 15, 30, 45, 50, 55, 70};

        //int ar[] = {100, 100, 100, 50, 25, 250};
        //int ar2[] = {101, 101, 51, 25, 255};

        //int ar[] = {1, 7, 10};
        //int ar2[] = {1, 8, 9};

        //int ar[] = {1, 7, 10};
        //int ar2[] = {1, 8, 11};

        Vector<Integer> sp1 = new Vector<>();
        for (int el : ar) {
            sp1.add(el);
        }
        Vector<Integer> sp2 = new Vector<>();
        for (int el : ar2) {
            sp2.add(el);
        }

        align(sp1, sp2);
        return;
    }
}



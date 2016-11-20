import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NW_ALgorithm {

    static int MAX_LENGTH = 1001;

    static int INDEL = -1;
    static int MISMATCH = -1;
    static int MATCH = 1;
    static int INF = 100000;

    public static int nw_algo(String s, String t) {

        String res_s = "";
        String res_t = "";
        int s_length = s.length();
        int t_length = t.length();
        if (s_length > MAX_LENGTH || t_length > MAX_LENGTH) {
            System.out.println();
            return -INF;
        }
        int[][] ar = new int[s_length + 1][t_length + 1];

        for (int i = 0; i <= s_length; i++) {
            ar[i][0] = -i;
        }

        for (int i = 0; i <= t_length; i++) {
            ar[0][i] = -i;
        }

        for (int i = 1; i <= s_length; i++) {

            for (int j = 1; j <= t_length; j++) {
                int penalty = MISMATCH;
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    penalty = MATCH;
                }
                ar[i][j] = Math.max(ar[i - 1][j - 1] + penalty, Math.max(ar[i - 1][j] + INDEL, ar[i][j - 1] + INDEL));

            }
        }

        //ANSWER RECONSTRUCTION BEGINNING

        int i = s_length;
        int j = t_length;

        while (true) {


            if (i == 0 || j == 0) {
                break;
            }

            int penalty = MISMATCH;
            if (s.charAt(i - 1) == t.charAt(j - 1)) {
                penalty = MATCH;
            }

            int k1 = ar[i - 1][j - 1] + penalty;
            int k2 = ar[i - 1][j] + INDEL;
            int k3 = ar[i][j - 1] + INDEL;

            if (k1 >= k2 && k1 >= k3) {

                res_s += s.charAt(i - 1);
                res_t += t.charAt(j - 1);
                i--;
                j--;
                continue;
            }

            if (k2 >= k1 && k2 >= k3) {

                res_s += s.charAt(i - 1);
                res_t += "-";
                i--;
                continue;
            }
            res_s += "-";
            res_t += t.charAt(j - 1);
            j--;

        }
        for (int k = i; k > 0; k--) {
            res_s += s.charAt(k - 1);
            res_t += "-";
        }

        for (int k = j; k > 0; k--) {
            res_s += "-";
            res_t += t.charAt(k - 1);
        }


        res_s = new StringBuilder(res_s).reverse().toString();
        res_t = new StringBuilder(res_t).reverse().toString();
        System.out.println(res_s);
        System.out.println(res_t);
        return ar[s_length][t_length];
    }

    public static void main(String[] args) throws IOException {


        System.out.println("Score: " + nw_algo("ACGTTAG", "ACCTAG"));
        System.out.println();
        System.out.println("Score: " + nw_algo("GGATCGA", "GAATTCAGTTA"));
        System.out.println();
        System.out.println("Score: " + nw_algo("AGC", "T"));
        System.out.println();
        System.out.println("Score: " + nw_algo("GAC", "AGACAA"));

        /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s1 = br.readLine();
        String s2 = br.readLine();
        System.out.println(nw_algo(s1,s2));*/

        return;
    }


}

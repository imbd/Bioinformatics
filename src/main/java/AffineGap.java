import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AffineGap {


    static int MAX_LENGTH = 1001;

    static double GAP_OPENING = -5.0;
    static double GAP_EXTENSION = -0.2;
    static double MISMATCH = -1.0;
    static double MATCH = 1.0;
    static double INF = -100000.0;

    public static double gap_algo(String s, String t) {

        int s_length = s.length();
        int t_length = t.length();
        if (s_length > MAX_LENGTH || t_length > MAX_LENGTH) {
            System.out.println();
            return -INF;
        }
        double[][] arTT = new double[s_length + 1][t_length + 1]; // T(True): gap is opened, F(False): gap is closed
        double[][] arTF = new double[s_length + 1][t_length + 1];
        double[][] arFT = new double[s_length + 1][t_length + 1];
        double[][] arFF = new double[s_length + 1][t_length + 1];

        arFF[0][0] = 0;
        arFT[0][0] = INF;
        arTF[0][0] = INF;
        arTT[0][0] = INF;
        for (int i = 1; i <= s_length; i++) {
            arFT[i][0] = GAP_OPENING + (i - 1) * GAP_EXTENSION;
            arFF[i][0] = INF;
            arTT[i][0] = INF;
            arTF[i][0] = INF;
        }

        for (int i = 1; i <= t_length; i++) {
            arTF[0][i] = GAP_OPENING + (i - 1) * GAP_EXTENSION;

            arFF[0][i] = INF;
            arTT[0][i] = INF;
            arFT[0][i] = INF;
        }


        for (int i = 1; i <= s_length; i++) {
            for (int j = 1; j <= t_length; j++) {
                double penalty = MISMATCH;
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    penalty = MATCH;
                }
                arFF[i][j] = Math.max(Math.max(arFF[i - 1][j - 1], arFT[i - 1][j - 1]), Math.max(arTF[i - 1][j - 1], arTT[i - 1][j - 1])) + penalty;
                arTF[i][j] = Math.max(arTF[i][j - 1] + GAP_EXTENSION, arFF[i][j - 1] + GAP_OPENING);
                arFT[i][j] = Math.max(arFT[i - 1][j] + GAP_EXTENSION, arFF[i - 1][j] + GAP_OPENING);
                arTT[i][j] = Math.max(arTT[i - 1][j] + GAP_EXTENSION, arTF[i - 1][j] + GAP_OPENING);
                arTT[i][j] = Math.max(arTT[i][j - 1] + GAP_EXTENSION, arFT[i][j - 1] + GAP_OPENING);

              /*  System.out.println("i & j: " + i + " " + j);
                System.out.println(arFF[i][j]);
                System.out.println(arFT[i][j]);
                System.out.println(arTF[i][j]);
                System.out.println(arTT[i][j]);*/

            }
        }

        //ANSWER RECONSTRUCTION BEGINNING

        int i = s_length;
        int j = t_length;
        int num = 0;//FF = 0, FT = 1, TF = 2, TT = 3

        double res = Math.max(arFF[s_length][t_length], Math.max(arTF[s_length][t_length], Math.max(arFT[s_length][t_length], arTT[s_length][t_length])));

        if (arFT[s_length][t_length] == res) {
            num = 1;
        }

        if (arTF[s_length][t_length] == res) {
            num = 2;
        }

        if (arTT[s_length][t_length] == res) {
            num = 3;
        }

        String res_s = "";
        String res_t = "";

        while (true) {

            if (i == 0 || j == 0) {
                break;
            }

            if (num == 1) {

                if (arFT[i - 1][j] + GAP_EXTENSION < arFF[i - 1][j] + GAP_OPENING) {
                    num = 0;
                }

                res_s += s.charAt(i - 1);
                res_t += "-";
                i--;
                continue;
            }

            if (num == 2) {

                if (arTF[i][j - 1] + GAP_EXTENSION < arFF[i][j - 1] + GAP_OPENING) {
                    num = 0;
                }

                res_s += "-";
                res_t += t.charAt(j - 1);
                j--;
                continue;
            }

            if (num == 0) {

                double max = Math.max(Math.max(arFF[i - 1][j - 1], arFT[i - 1][j - 1]), Math.max(arTF[i - 1][j - 1], arTT[i - 1][j - 1]));

                if (arFT[i - 1][j - 1] == max) {
                    num = 1;
                }
                if (arTF[i - 1][j - 1] == max) {
                    num = 2;
                }
                if (arTT[i - 1][j - 1] == max) {
                    num = 3;
                }

                res_s += s.charAt(i - 1);
                res_t += t.charAt(j - 1);
                i--;
                j--;
                continue;

            }

            if (num == 3) {

                if (arTT[i][j] == arTT[i - 1][j] + GAP_EXTENSION || arTT[i][j] == arTF[i - 1][j] + GAP_OPENING) {

                    res_s += s.charAt(i - 1);
                    res_t += "-";
                    i--;

                    if (arTF[i - 1][j] + GAP_OPENING > arTT[i - 1][j] + GAP_EXTENSION) {
                        num = 2;
                    }

                    continue;

                }

                if (arTT[i][j] == arTT[i][j - 1] + GAP_EXTENSION || arTT[i][j] == arFT[i][j - 1] + GAP_OPENING) {

                    res_s += "-";
                    res_t += t.charAt(j - 1);
                    j--;

                    if (arFT[i][j - 1] + GAP_OPENING > arTT[i][j - 1] + GAP_EXTENSION) {
                        num = 1;
                    }

                    continue;

                }
            }
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


        return Math.max(arFF[s_length][t_length], Math.max(arTF[s_length][t_length], Math.max(arFT[s_length][t_length], arTT[s_length][t_length])));
    }


    public static void main(String[] args) throws IOException {

        System.out.println(gap_algo("ACCTAG", "ACGTTAG"));
        System.out.println();
        System.out.println(gap_algo("AAACCC", "GGCCC"));
        System.out.println();
        System.out.println(gap_algo("GGATCGA", "GAATTCAGTTA"));
        System.out.println();

        System.out.println(gap_algo("AAAAAA", "CCA"));
        System.out.println();
        System.out.println(gap_algo("AAG", "AGAACA"));
        System.out.println();
        System.out.println(gap_algo("C", "ACGT"));
        System.out.println();
        System.out.println(gap_algo("CAG", "C"));
        System.out.println();

        /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s1 = br.readLine();
        String s2 = br.readLine();
        System.out.println(gap_algo(s1,s2)); */

        return;
    }

}

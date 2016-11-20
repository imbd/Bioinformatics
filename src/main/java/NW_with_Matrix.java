import java.io.IOException;
import java.util.Scanner;

public class NW_with_Matrix {

    static int MAX_LENGTH = 1000;
    static Scanner in = new Scanner(System.in);
    static int INF = 100000;

    static char[] ALPHABET = {'A', 'C', 'G', 'T', '-'};

    static int ALPHABET_SIZE = ALPHABET.length;

    static int[] NUM = new int[256];

    static int[][] MATRIX = new int[ALPHABET_SIZE][ALPHABET_SIZE];

    public static void fill_matrix() {

        makeNum();

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            for (int j = 0; j < ALPHABET_SIZE; j++) {
                MATRIX[i][j] = -1;
            }
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            MATRIX[i][i] = 1;
        }

    }

    public static void makeNum() {

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            NUM[(int) ALPHABET[i]] = i;
        }

    }

    public static void readMatrix() {

        makeNum();

        System.out.println("Reading matrix:");

        System.out.print("  ");
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            System.out.print(ALPHABET[i] + " ");
        }
        System.out.println();

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            System.out.print(ALPHABET[i] + " ");
            for (int j = 0; j < ALPHABET_SIZE; j++) {
                MATRIX[i][j] = in.nextInt();
            }
            System.out.println();
        }
    }

    public static int nw_matrix_algo(String s, String t) {

        String res_s = "";
        String res_t = "";
        int s_length = s.length();
        int t_length = t.length();
        if (s_length > MAX_LENGTH || t_length > MAX_LENGTH) {
            System.out.println("String is too long");
            return -INF;
        }
        int[][] ar = new int[s_length + 1][t_length + 1];

        ar[0][0] = 0;
        for (int i = 1; i <= s_length; i++) {
            ar[i][0] = ar[i-1][0] + MATRIX[NUM[s.charAt(i-1)]][ALPHABET_SIZE - 1];
        }

        for (int i = 1; i <= t_length; i++) {
            ar[0][i] = ar[0][i-1] + MATRIX[ALPHABET_SIZE - 1][NUM[t.charAt(i-1)]];
        }

        for (int i = 1; i <= s_length; i++) {
            for (int j = 1; j <= t_length; j++) {

                int penalty = MATRIX[NUM[(int) s.charAt(i - 1)]][NUM[(int) t.charAt(j - 1)]];
                int indel1 = MATRIX[ALPHABET_SIZE - 1][NUM[(int) t.charAt(j - 1)]];
                int indel2 = MATRIX[NUM[(int) s.charAt(i - 1)]][ALPHABET_SIZE - 1];

                ar[i][j] = Math.max(ar[i - 1][j - 1] + penalty, Math.max(ar[i - 1][j] + indel2, ar[i][j - 1] + indel1));

            }
        }

        //ANSWER RECONSTRUCTION BEGINNING

        int i = s_length;
        int j = t_length;

        while (true) {

            if (i == 0 || j == 0) {
                break;
            }
            int penalty = MATRIX[NUM[(int) s.charAt(i - 1)]][NUM[(int) t.charAt(j - 1)]];
            int indel1 = MATRIX[ALPHABET_SIZE - 1][NUM[(int) t.charAt(j - 1)]];
            int indel2 = MATRIX[NUM[(int) s.charAt(i - 1)]][ALPHABET_SIZE - 1];
            int max = Math.max(ar[i - 1][j - 1] + penalty, Math.max(ar[i - 1][j] + indel2, ar[i][j - 1] + indel1));

            if (ar[i - 1][j - 1] + penalty == max) {

                res_s += s.charAt(i - 1);
                res_t += t.charAt(j - 1);
                i--;
                j--;
                continue;
            }

            if (ar[i - 1][j] + indel2 == max) {

                res_s += s.charAt(i - 1);
                res_t += "-";
                i--;
                continue;
            }

            if (ar[i][j - 1] + indel1 == max) {

                res_s += "-";
                res_t += t.charAt(j - 1);
                j--;

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

        return ar[s_length][t_length];
    }


    public static void main(String[] args) throws IOException {

        fill_matrix();

        System.out.println("Score: " + nw_matrix_algo("ACGTTAG", "ACCTAG"));
        System.out.println();
        System.out.println("Score: " + nw_matrix_algo("GGATCGA", "GAATTCAGTTA"));
        System.out.println();
        System.out.println("Score: " + nw_matrix_algo("ATC", "G"));
        System.out.println();
        System.out.println("Score: " + nw_matrix_algo("GAC", "AGACAA"));


        /*String s1 = in.nextLine();
        String s2 = in.nextLine();
        readMatrix();
        System.out.println(nw_matrix_algo(s1, s2));*/


        return;
    }


}

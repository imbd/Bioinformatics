import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

public class SpectraSimulator {

    public static HashMap<Character, Integer> ALPHABET_MAP;

    static {
        ALPHABET_MAP = new HashMap<>();
        ALPHABET_MAP.put('G', 57);
        ALPHABET_MAP.put('A', 71);
        ALPHABET_MAP.put('S', 87);
        ALPHABET_MAP.put('P', 97);
        ALPHABET_MAP.put('V', 99);
        ALPHABET_MAP.put('T', 101);
        ALPHABET_MAP.put('C', 103);
        ALPHABET_MAP.put('I', 113);
        ALPHABET_MAP.put('L', 113);
        ALPHABET_MAP.put('N', 114);
        ALPHABET_MAP.put('D', 115);
        ALPHABET_MAP.put('K', 128);
        ALPHABET_MAP.put('Q', 128);
        ALPHABET_MAP.put('E', 129);
        ALPHABET_MAP.put('M', 131);
        ALPHABET_MAP.put('H', 137);
        ALPHABET_MAP.put('F', 147);
        ALPHABET_MAP.put('R', 156);
        ALPHABET_MAP.put('Y', 163);
        ALPHABET_MAP.put('W', 186);
    }


    public static Vector<Integer> make_spectrum(String peptide, double error_rate) {

        Vector<Integer> v = new Vector<>();
        int[] partial_sum = new int[peptide.length()];
        partial_sum[0] = ALPHABET_MAP.get(peptide.charAt(0));
        for (int i = 1; i < peptide.length(); i++) {
            partial_sum[i] = partial_sum[i - 1] + ALPHABET_MAP.get(peptide.charAt(i));
        }

        for (int len = 0; len < peptide.length() - 1; len++) {
            for (int index = 0; index < peptide.length(); index++) {
                if (index == 0) {
                    v.add(partial_sum[len]);
                } else {
                    if (index + len >= peptide.length()) {
                        v.add(partial_sum[peptide.length() - 1] - partial_sum[index - 1] + partial_sum[index + len - peptide.length()]);
                    } else {
                        v.add(partial_sum[index + len] - partial_sum[index - 1]);
                    }
                }
            }
        }
        v.add(0);
        int error_number = (int) (v.size() * error_rate);
        for (int i = 0; i < error_number; i++) {
            int index = (int) ((10000*Math.random()) % v.size());
            v.remove(index);
        }

        for (int i = 0; i < error_number; i++) {
            int index = 0;
            if (v.size() > 0) {
                index = (int) (10000 * Math.random()) % v.size();
            }
            v.add(index, (int) (10000 * Math.random()) % 500);

        }
        v.add(partial_sum[peptide.length() - 1]);
        v.sort(Comparator.<Integer>naturalOrder());
        return v;
    }


    public static void main(String[] args) {

        Vector v = make_spectrum("NQEL", 0.0);
        System.out.println(v.size());
        for (int i = 0; i < v.size(); i++) {
            System.out.print(v.get(i) + " ");
        }
        System.out.println();

        v = make_spectrum("EINK", 0.5);
        System.out.println(v.size());
        for (int i = 0; i < v.size(); i++) {
            System.out.print(v.get(i) + " ");
        }
        System.out.println();

        return;
    }
}

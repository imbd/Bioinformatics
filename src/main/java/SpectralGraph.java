import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class SpectralGraph extends SequencingAlgorithm {

    public static int[] ION_TYPES = {0, 10, 25};
    public static double[] ION_TYPES_PROBABILITY = {1.0, 0.5, 0.2};
    public static double Q_R = 0.1;
    public static Random rn = new Random();

    public static double[] Q;

    static {
        Q = new double[ION_TYPES_PROBABILITY.length];
        for (int i = 0; i < Q.length; i++) {
            Q[i] = ION_TYPES_PROBABILITY[i] + (1 - ION_TYPES_PROBABILITY[i]) * Q_R;
        }
    }

    public static double countPenalty(double[] penalty, String curPeptide) {

        double res = 1;
        int N = curPeptide.length();

        for (int c = 0; c < N; c++) {

            int curWeight = 0;
            for (int i = 0; i < N; i++) {
                int w = ALPHABET_MAP.get(curPeptide.charAt(i));
                curWeight += w;
                res *= penalty[curWeight];
            }

            curPeptide = curPeptide.charAt(N - 1) + curPeptide.substring(0, N - 1);
        }

        return res;
    }

    public static double CUR_MAX = 0;
    public static String ANSWER = "";

    public static double FindMaxPenalty(double[] penalty, String curPeptide, int curWeight, int maxWeight) {

        if (curWeight > maxWeight) {
            return 0;
        }
        if (curWeight == maxWeight) {
            double cP = countPenalty(penalty, curPeptide);
            if (CUR_MAX < cP + 1E-5) {
                CUR_MAX = cP;
                ANSWER = curPeptide;
            }
            return cP;
        }
        double res = 0;
        for (Map.Entry entry : ALPHABET_MAP.entrySet()) {
            char c = (char) entry.getKey();
            int w = (int) entry.getValue();
            res = Math.max(FindMaxPenalty(penalty, curPeptide + c, curWeight + w, maxWeight), res);
        }
        return res;
    }

    public static String MakeSpectralGraph(Vector<Integer> Spectrum) {

        int MAX_WEIGHT = ParentMass(Spectrum);
        boolean[] weights = new boolean[MAX_WEIGHT + 1];
        Arrays.fill(weights, false);

        for (int w : Spectrum) {
            weights[w] = true;
            weights[0] = true;
        }

        double[] penalty = new double[MAX_WEIGHT + 1];

        for (int curWeight = 0; curWeight <= MAX_WEIGHT; curWeight++) {
            double p = 1.0;
            for (int i = 0; i < ION_TYPES.length; i++) {
                if (curWeight - ION_TYPES[i] >= 0 && weights[curWeight - ION_TYPES[i]]) {
                    p *= (Q[i] / Q_R);
                } else {
                    p *= ((1 - Q[i]) / (1 - Q_R));
                }
            }
            penalty[curWeight] = p;
        }

        FindMaxPenalty(penalty, "", 0, MAX_WEIGHT);

        return ANSWER;
    }

    public static Vector<Integer> addIonsToSpectrum(Vector<Integer> spectrum) {
        Vector<Integer> res = new Vector<>();

        int maxW = ParentMass(spectrum);

        boolean[] weights = new boolean[maxW + 1];

        int N = spectrum.size();

        for (int i = 0; i < N; i++) {
            weights[spectrum.get(i)] = true;
            res.add(spectrum.get(i));
        }

        for (int i = 0; i < N; i++) {

            int el = spectrum.get(i);
            for (int j = 0; j < ION_TYPES.length; j++) {

                if (el - ION_TYPES[j] >= 0 && !weights[el - ION_TYPES[j]]) {

                    int num = Math.abs(rn.nextInt());
                    num %= 1000;
                    if (num < (int) (ION_TYPES_PROBABILITY[j] * 1000)) {
                        res.add(el - ION_TYPES[j]);
                    }
                }

            }
        }

        return res;
    }

    public static void main(String[] args) {

        Vector<Integer> spWithIons = addIonsToSpectrum(make_spectrum("ANHEL", 0));
        //Vector<Integer> spWithIons = addIonsToSpectrum(make_spectrum("GSAGE", 0));
        //Vector<Integer> spWithIons = addIonsToSpectrum(make_spectrum("NHDEL", 0));
        //Vector<Integer> spWithIons = addIonsToSpectrum(make_spectrum("ANGELS", 0));
        //Vector<Integer> spWithIons = addIonsToSpectrum(make_spectrum("GGGGHL", 0));
        System.out.println("Spectrum with some ions: " + spWithIons);
        System.out.println("Best peptide: " + MakeSpectralGraph(spWithIons));

        return;
    }

}

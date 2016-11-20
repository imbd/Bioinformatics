import java.util.*;

public class SequencingAlgorithm extends SpectraSimulator {

    public static int Mass(String p) {
        int res = 0;
        for (int i = 0; i < p.length(); i++) {
            res += ALPHABET_MAP.get(p.charAt(i));
        }
        return res;
    }


    public static int ParentMass(Vector<Integer> Spectrum) {
        if (Spectrum.size() == 0) {
            System.out.print("Spectrum is empty");
            return 0;
        }
        Spectrum.sort(Comparator.<Integer>naturalOrder());
        return Spectrum.get(Spectrum.size() - 1);
    }

    public static HashMap<Integer, Integer> makeSpectrumMap(Vector<Integer> Spectrum) {
        HashMap<Integer, Integer> SPECTRUM_MAP = new HashMap<>();
        for (int m : Spectrum) {
            if (SPECTRUM_MAP.get(m) != null) {
                SPECTRUM_MAP.put(m, SPECTRUM_MAP.get(m) + 1);
            } else {
                SPECTRUM_MAP.put(m, 1);
            }
        }
        return SPECTRUM_MAP;
    }

    public static int Score(String p, HashMap<Integer, Integer> Spectrum_map, int mass) {  //not cyclic

        if (p == "") {
            return 0;
        }
        int res = 0;
        if (Mass(p) > mass)
            return -1;

        Vector<Integer> p_Spectrum = new Vector<>();
        p_Spectrum.add(ALPHABET_MAP.get(p.charAt(0)));
        for (int i = 1; i < p.length(); i++) {
            p_Spectrum.add(p_Spectrum.get(p_Spectrum.size() - 1) + ALPHABET_MAP.get(p.charAt(i)));
        }

        HashMap<Integer, Integer> p_hashmap = makeSpectrumMap(p_Spectrum);

        for (int key : Spectrum_map.keySet()) {
            if (p_hashmap.get(key) != null) {
                res += Math.min(Spectrum_map.get(key), p_hashmap.get(key));
            }
        }

        return res;
    }

    public static int RealScore(String p, HashMap<Integer, Integer> Spectrum_map, int mass) {  //cyclic

        if (p.equals("")) {
            return 0;
        }
        int res = 0;
        if (Mass(p) > mass)
            return -1;
        Vector<Integer> p_Spectrum = make_spectrum(p, 0);

        HashMap<Integer, Integer> p_hashmap = new HashMap();

        for (int m : p_Spectrum) {
            if (p_hashmap.get(m) != null) {
                p_hashmap.put(m, p_hashmap.get(m) + 1);
            } else {
                p_hashmap.put(m, 1);
            }
        }

        for (int key : Spectrum_map.keySet()) {
            if (p_hashmap.get(key) != null) {
                res += Math.min(Spectrum_map.get(key), p_hashmap.get(key));
            }
        }

        return res;
    }


    public static String CyclopeptideSequencing(Vector<Integer> Spectrum, int N, int M) {

        Vector<String> Peptides = new Vector<>();
        Peptides.add("");
        String Best = "";
        int cur_size = 0;

        HashMap<Integer, Integer> SPECTRUM_MAP = makeSpectrumMap(Spectrum);

        int MAX_WEIGHT = ParentMass(Spectrum);
        int MAX_SCORE = 0;

        int[] num = new int[MAX_WEIGHT + 1];
        num[0] = 1;
        Vector<Integer> v = new Vector<>();
        for (int i = 0; i <= MAX_WEIGHT; i++) {
            v.add(i);
        }
        for (int p1 : Spectrum) {
            for (int p2 : Spectrum) {
                num[Math.abs(p1 - p2)]++;
            }
        }

        v.sort((m1, m2) -> num[m1] <= num[m2] ? -1 : 1);

        int cur_num = 0;
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i = v.size() - 1; i >= 0; i--) {
            if (ALPHABET_MAP.containsValue(v.get(i))) {
                hashSet.add(v.get(i));
                cur_num++;
                if (v.get(i) == 113 || v.get(i) == 128) {
                    cur_num++;
                }
            }
            if (cur_num >= M) {  // it can be M + 1
                break;
            }
        }

        while (!Peptides.isEmpty()) {

            cur_size++;
            int size = Peptides.size();
            for (int i = 0; i < size; i++) {

                String el = Peptides.get(i);
                for (Map.Entry entry : ALPHABET_MAP.entrySet()) {

                    if (hashSet.contains(entry.getValue()) && Mass(el + entry.getKey()) <= MAX_WEIGHT) {
                        Peptides.add(el + entry.getKey());
                    }
                }
            }

            int new_size = Peptides.size();
            for (int j = 0; j < new_size; j++) {
                String p = Peptides.elementAt(j);
                if (Mass(p) == MAX_WEIGHT) {
                    int score = RealScore(p, SPECTRUM_MAP, MAX_WEIGHT);
                    if (score > MAX_SCORE) {
                        MAX_SCORE = score;
                        System.out.println("New best score: " + score + " " + p);
                        Best = p;
                    }
                }
            }

            Collections.sort(Peptides, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (Score(o1, SPECTRUM_MAP, MAX_WEIGHT) == Score(o2, SPECTRUM_MAP, MAX_WEIGHT))
                        return 0;
                    return Score(o1, SPECTRUM_MAP, MAX_WEIGHT) > Score(o2, SPECTRUM_MAP, MAX_WEIGHT) ? -1 : 1;
                }
            });

            for (int i = Peptides.size() - 1; i >= 0; i--) {
                if (Peptides.get(i).length() < cur_size) {
                    Peptides.remove(i);
                }
            }
            while (Peptides.size() > N) {
                Peptides.remove(Peptides.size() - 1);
            }


        }

        //System.out.println(Score(Best, SPECTRUM_MAP, MAX_WEIGHT));
        //System.out.println(RealScore(Best, SPECTRUM_MAP, MAX_WEIGHT));
        return Best;
    }


    public static void main(String[] args) {

        Vector<Integer> Spectrum = new Vector<>();
        Spectrum = make_spectrum("NQEL", 0);
        //Spectrum = make_spectrum("DEL", 0);
        //Spectrum = make_spectrum("AAAA", 0);
        //Spectrum = make_spectrum("AVFRYSP", 0);
        System.out.println(CyclopeptideSequencing(Spectrum, 1000, 20));

        return;
    }
}

import java.util.ArrayList;

public class CardanoGrid {

    private static final ArrayList<Pair[]> template = createTemplate();

    private static String source;
    private static String encrypted;
    private static int counter;
    private static boolean find;

    public static void bruteForceAnalysis(String sourceMessage, String encryptedMessage) {
        source = sourceMessage;
        encrypted = encryptedMessage;
        counter = 0;
        int[][] key = new int[4][4];
        bruteForce(key, 0);
    }

    public static int getCounter() {
        return counter;
    }

    public static String encrypt(int[][] key, String sourceMessage) {
        String[][] encryptMatrix = new String[key.length][key.length];

        int blockNum = 1;
        if  (sourceMessage.length() > 16) {
            blockNum = (sourceMessage.length() / 16) + 1;
        }

        int count = 0;
        for (int k = 0; k < key.length; k++) {
            for (int i = 0; i < key.length; i++) {
                for (int j = 0; j < key.length; j++) {
                    if (key[i][j] == 1) {
                        StringBuilder sb = new StringBuilder();
                        for (int n = 0; n < blockNum; n++) {
                            if (count >= sourceMessage.length()) {
                                sb.append("_");
                            } else {
                                sb.append(sourceMessage.charAt(count++));
                            }
                        }
                        encryptMatrix[i][j] = sb.toString();
                        }
                    }
                }
            key = rotateMatrix(key);
        }

        StringBuilder encryptMessage = new StringBuilder();
        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < key.length; j++) {
                encryptMessage.append(encryptMatrix[i][j]);
            }
        }

        return encryptMessage.toString();
    }

    public static String decrypt(int[][] key, String cryptMessage) {
        String[][] encryptMatrix = new String[key.length][key.length];

        int matrixSize = key.length * key.length;

        int oldLength = 0;
        for (int i = 0; i < cryptMessage.length(); i++) {
            if (cryptMessage.charAt(i) != '_') {
                oldLength++;
            }
        }

        int blockNum = 1;
        if  (oldLength > matrixSize) {
            blockNum = (oldLength / matrixSize) + 1;
        }

        int count = 0;
        for (int i = 0; i < encryptMatrix.length; i++) {
            for (int j = 0; j < encryptMatrix.length; j++) {
                StringBuilder sb = new StringBuilder();
                for (int n = 0; n < blockNum; n++) {
                    if (!(count >= cryptMessage.length())) {
                        sb.append(cryptMessage.charAt(count++));
                    }
                }
                encryptMatrix[i][j] = sb.toString();
            }
        }

        StringBuilder decryptMessage = new StringBuilder();
        for (int k = 0; k < key.length; k++) {
            for (int i = 0; i < key.length; i++) {
                for (int j = 0; j < key.length; j++) {
                    if (key[i][j] == 1) {
                        decryptMessage.append(encryptMatrix[i][j]);
                    }
                }
            }

            key = rotateMatrix(key);
        }

        if (oldLength % matrixSize != 0) {
            decryptMessage.delete(decryptMessage.indexOf("_"), decryptMessage.length());
        }
        return decryptMessage.toString();
    }

    public static int[][] generateKey() {
        int[][] key = new int[4][4];

        for (Pair[] pairs : template) {
            Pair pair = pairs[(int) (Math.random() * 4)];
            key[pair.getFirst()][pair.getSecond()] = 1;
        }

        return key;
    }

    private static int[][] rotateMatrix(int[][] matrix) {
        int size = matrix.length;
        int[][] res = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                res[i][j] = matrix[size - j - 1][i];
            }
        }
        return res;
    }

    private static ArrayList<Pair[]> createTemplate() {
        ArrayList<Pair[]> template = new ArrayList<>();
        Pair[] one = {new Pair(0, 0), new Pair(0, 3), new Pair(3, 0), new Pair(3, 3)};
        template.add(one);
        Pair[] two = {new Pair(0, 1), new Pair(1, 3), new Pair(3, 2), new Pair(2, 0)};
        template.add(two);
        Pair[] three = {new Pair(1, 0), new Pair(0, 2), new Pair(2, 3), new Pair(3, 1)};
        template.add(three);
        Pair[] four = {new Pair(1, 1), new Pair(1, 2), new Pair(2, 1), new Pair(2, 2)};
        template.add(four);
        return template;
    }

    private static void bruteForce(int[][] key, int k) {

        if (k == 4) {
            counter++;
            if (CardanoGrid.decrypt(key, encrypted).equals(source)) {
                find = true;
            }
            return;
        }

        Pair[] pairs = template.get(k);
        for (int i = 0; i < 4; i++) {
            Pair pair = pairs[i];
            key[pair.getFirst()][pair.getSecond()] = 1;
            bruteForce(key, ++k);
            if (find) {
                return;
            }
            key[pair.getFirst()][pair.getSecond()] = 0;
            k--;
        }
    }

}

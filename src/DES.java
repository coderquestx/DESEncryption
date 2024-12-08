import java.util.Arrays;

public class DES {
    private final int ENCRYPT = 0x00;
    private final int DECRYPT = 0x01;

    private int[] pc1 = {
            56, 48, 40, 32, 24, 16, 8, 0,
            57, 49, 41, 33, 25, 17, 9, 1,
            58, 50, 42, 34, 26, 18, 10, 2,
            59, 51, 43, 35, 62, 54, 46, 38,
            30, 22, 14, 6, 61, 53, 45, 37,
            29, 21, 13, 5, 60, 52, 44, 36,
            28, 20, 12, 4, 27, 19, 11, 3
    };

    private int[] leftRotations = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    private int[] pc2 = {
            13, 16, 10, 23, 0, 4,
            2, 27, 14, 5, 20, 9,
            22, 18, 11, 3, 25, 7,
            15, 6, 26, 19, 12, 1,
            40, 51, 30, 36, 46, 54,
            29, 39, 50, 44, 32, 47,
            43, 48, 38, 55, 33, 52,
            45, 41, 49, 35, 28, 31
    };

    private int[] ip = {
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7,
            56, 48, 40, 32, 24, 16, 8, 0,
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6
    };

    private int[] expansionTable = {
            31, 0, 1, 2, 3, 4,
            3, 4, 5, 6, 7, 8,
            7, 8, 9, 10, 11, 12,
            11, 12, 13, 14, 15, 16,
            15, 16, 17, 18, 19, 20,
            19, 20, 21, 22, 23, 24,
            23, 24, 25, 26, 27, 28,
            27, 28, 29, 30, 31, 0
    };

    private int[][] sBox = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
                    0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
                    4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
                    15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},

            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
                    3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                    0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                    13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},

            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
                    13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                    13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                    1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12},

            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
                    13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                    10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                    3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14},

            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
                    14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
                    4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                    11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},

            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
                    10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                    9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                    4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},

            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
                    13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                    1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                    6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},

            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
                    1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                    7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                    2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    };

    private int[] p = {
            15, 6, 19, 20, 28, 11,
            27, 16, 0, 14, 22, 25,
            4, 17, 30, 9, 1, 7,
            23, 13, 31, 26, 2, 8,
            18, 12, 29, 5, 21, 10,
            3, 24
    };

    private int[] fp = {
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25,
            32, 0, 40, 8, 48, 16, 56, 24
    };

    private int[] L = new int[32];
    private int[] R = new int[32];
    private int[][] Kn = new int[16][48];
    private byte[] key = new byte[8];

    public DES(byte[] key) {
        this.key = key;
        setKey();
    }

    private void setKey() {
        int[] keyBits = stringToBitList(key);
        int[] permutedChoice1 = permutate(pc1, keyBits);
        L = Arrays.copyOfRange(permutedChoice1, 0, 28);
        R = Arrays.copyOfRange(permutedChoice1, 28, 56);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < leftRotations[i]; j++) {
                int tempL0 = L[0];
                int tempR0 = R[0];
                for (int k = 0; k < 27; k++) {
                    L[k] = L[k + 1];
                    R[k] = R[k + 1];
                }
                L[27] = tempL0;
                R[27] = tempR0;
            }
            int[] combinedKey = permutate(pc2, concatenate(L, R));
            Kn[i] = combinedKey;
        }
    }

    private int[] concatenate(int[] array1, int[] array2) {
        int[] result = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    private int[] stringToBitList(byte[] data) {
        int[] result = new int[data.length * 8];
        int pos = 0;
        for (byte b : data) {
            for (int i = 7; i >= 0; i--) {
                result[pos] = (b >> i) & 1;
                pos++;
            }
        }
        return result;
    }

    private byte[] bitListToString(int[] data) {
        byte[] result = new byte[data.length / 8];
        int pos = 0;
        int c = 0;
        for (int i = 0; i < data.length; i++) {
            c += data[i] << (7 - (i % 8));
            if ((i % 8) == 7) {
                result[pos] = (byte) c;
                c = 0;
                pos++;
            }
        }
        return result;
    }

    private int[] permutate(int[] table, int[] block) {
        int[] result = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            result[i] = block[table[i]];
        }
        return result;
    }

    private int[] des_crypt(int[] block, int crypt_type) {
        block = permutate(ip, block);
        L = Arrays.copyOfRange(block, 0, 32);
        R = Arrays.copyOfRange(block, 32, 64);

        int iteration = (crypt_type == ENCRYPT) ? 0 : 15;
        int iterationAdjustment = (crypt_type == ENCRYPT) ? 1 : -1;

        for (int i = 0; i < 16; i++) {
            int[] tempR = R.clone();
            R = permutate(expansionTable, R);
            R = xor(R, Kn[iteration]);
            int[][] B = new int[8][6];
            int[] Bn = new int[32];
            int pos = 0;
            for (int j = 0; j < 8; j++) {
                System.arraycopy(R, 6 * j, B[j], 0, 6);
                int m = (B[j][0] << 1) + B[j][5];
                int n = (B[j][1] << 3) + (B[j][2] << 2) + (B[j][3] << 1) + B[j][4];
                int v = sBox[j][(m << 4) + n];
                Bn[pos] = (v & 8) >> 3;
                Bn[pos + 1] = (v & 4) >> 2;
                Bn[pos + 2] = (v & 2) >> 1;
                Bn[pos + 3] = v & 1;
                pos += 4;
            }
            R = permutate(p, Bn);
            R = xor(R, L);
            L = tempR;
            iteration += iterationAdjustment;
        }

        int[] result = permutate(fp, concatenate(R, L));
        return result;
    }

    private int[] xor(int[] arr1, int[] arr2) {
        int[] result = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] ^ arr2[i];
        }
        return result;
    }

    public byte[] crypt(byte[] data, int crypt_type) {
        int pos = 0;
        byte[] result = new byte[data.length];
        while (pos < data.length) {
            int[] block = stringToBitList(Arrays.copyOfRange(data, pos, pos + 8));
            int[] processed_block = des_crypt(block, crypt_type);
            int copyLength = Math.min(processed_block.length / 8, result.length - pos);
            System.arraycopy(bitListToString(processed_block), 0, result, pos, copyLength);
            pos += 8;
        }
        return result;
    }

    public byte[] encrypt(byte[] data) {
        return crypt(data, ENCRYPT);
    }

    public byte[] decrypt(byte[] data) {
        return crypt(data, DECRYPT);
    }

    public static void main(String[] args) {
        byte[] key = "mySecret".getBytes();
        DES des = new DES(key);
        String plainText = "Message!";
        byte[] encryptedText = des.encrypt(plainText.getBytes());
        System.out.println("Encrypted: " + new String(encryptedText));
        byte[] decryptedText = des.decrypt(encryptedText);
        System.out.println("Decrypted: " + new String(decryptedText));
    }
}

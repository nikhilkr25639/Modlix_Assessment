public class StringCustomCompressor {

    // Compress string using modified RLE
    public static String compress(String input) {
        StringBuilder compressed = new StringBuilder();

        int count = 1;
        for (int i = 1; i <= input.length(); i++) {
            if (i < input.length() && input.charAt(i) == input.charAt(i - 1)) {
                count++;
            } else {
                if (count > 3) {
                    // Only compress if repeated more than 3 times
                    compressed.append("#").append(input.charAt(i - 1)).append(count);
                } else {
                    // Else write the character(s) directly
                    for (int j = 0; j < count; j++) {
                        compressed.append(input.charAt(i - 1));
                    }
                }
                count = 1;
            }
        }

        return compressed.toString();
    }

    // Decompress RLE string back to original
    public static String decompress(String compressed) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < compressed.length()) {
            if (compressed.charAt(i) == '#') {
                char ch = compressed.charAt(i + 1);
                int j = i + 2;
                StringBuilder num = new StringBuilder();
                while (j < compressed.length() && Character.isDigit(compressed.charAt(j))) {
                    num.append(compressed.charAt(j));
                    j++;
                }
                int count = Integer.parseInt(num.toString());
                for (int k = 0; k < count; k++) {
                    result.append(ch);
                }
                i = j;
            } else {
                result.append(compressed.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    // Generate random alphanumeric string
    public static String generateRandomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // For testing: create a long alphanumeric string with some repetitions
        StringBuilder inputBuilder = new StringBuilder();
        inputBuilder.append("AAAAAA"); // will be compressed
        inputBuilder.append("BCDEF");  // won't be compressed
        inputBuilder.append("11111111"); // will be compressed
        inputBuilder.append(generateRandomAlphanumeric(99980)); // random data
        inputBuilder.append("BBBB"); // will be compressed
        inputBuilder.append("BCDF");  // won't be compressed
        inputBuilder.append("222334444"); // will be compressed

        String original = inputBuilder.toString();
        System.out.println("Original length: " + original.length());

        String compressed = compress(original);
        System.out.println("Compressed length: " + compressed.length());

        String decompressed = decompress(compressed);
        System.out.println("Decompressed length: " + decompressed.length());

        System.out.println("Decompressed equals original: " + original.equals(decompressed));
    }
}

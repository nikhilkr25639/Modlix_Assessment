import java.io.*;

public class PeekingInsidePi {

    private static final int CHUNK_SIZE = 100_000; // Read in chunks for billion file
    private static final int OVERLAP_SIZE = 1000;  // Buffer overlap to avoid cutting matches

    // Load 1 million digits into memory and search
    public static int searchIn1Million(String filePath, String sequence) throws IOException {
        StringBuilder pi = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                pi.append(line.trim());
            }
        }

        // Remove leading '3' if present
        if (pi.length() > 0 && pi.charAt(0) == '3') {
            pi.deleteCharAt(0);
        }

        return pi.indexOf(sequence);
    }

    // Efficient chunked search for 1 billion digits
    public static long searchIn1Billion(String filePath, String sequence) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder buffer = new StringBuilder();
            char[] chunk = new char[CHUNK_SIZE];
            int read;
            long totalOffset = 0;
            boolean firstChunk = true;

            while ((read = reader.read(chunk)) != -1) {
                buffer.append(chunk, 0, read);

                // Remove leading '3' if it's the very first character
                if (firstChunk && buffer.length() > 0 && buffer.charAt(0) == '3') {
                    buffer.deleteCharAt(0);
                    firstChunk = false;
                }

                int index = buffer.indexOf(sequence);
                if (index != -1) return totalOffset + index;

                if (buffer.length() > OVERLAP_SIZE) {
                    totalOffset += buffer.length() - OVERLAP_SIZE;
                    buffer = new StringBuilder(buffer.substring(buffer.length() - OVERLAP_SIZE));
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java PeekingInsidePi <file_path> <search_string>");
            return;
        }

        String filePath = args[0];
        String sequence = args[1];

        try {
            File file = new File(filePath);
            long fileSize = file.length();

            if (fileSize <= 10_000_000) { // ≈10MB or less, treat as 1 million digits
                int index = searchIn1Million(filePath, sequence);
                System.out.println("Found in 1 million digits at index: " + index);
            } else {
                long index = searchIn1Billion(filePath, sequence);
                System.out.println("Found in 1 billion digits at index: " + index);
            }

        } catch (IOException e) {
            System.err.println("Error reading Pi file: " + e.getMessage());
        }
    }
}

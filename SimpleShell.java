import java.io.*;
import java.util.*;

public class SimpleShell {
    static File currentDir = new File(System.getProperty("user.dir"));

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String[] input = sc.nextLine().split(" ");
            if (input.length == 0 || input[0].isEmpty()) continue;

            String cmd = input[0];
            String[] cmdArgs = Arrays.copyOfRange(input, 1, input.length);

            switch (cmd) {
                case "help":
                    System.out.println("help, cd <dir>, pwd, ls, echo <text>, exit");
                    break;

                case "cd":
                    if (cmdArgs.length > 0) {
                        File dir = new File(currentDir, cmdArgs[0]);
                        if (dir.exists() && dir.isDirectory()) {
                            currentDir = dir;
                        } else {
                            System.out.println("cd: no such directory");
                        }
                    }
                    break;

                case "pwd":
                    System.out.println(currentDir.getAbsolutePath());
                    break;

                case "ls":
                    File[] files = currentDir.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            System.out.println(f.getName());
                        }
                    }
                    break;

                case "echo":
                    System.out.println(String.join(" ", cmdArgs));
                    break;

                case "exit":
                    System.out.println("Goodbye!");
                    return;

                default:
                    runExternal(input);
                    break;
            }
        }
    }

    static void runExternal(String[] cmd) {
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd).directory(currentDir);
            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor();
        } catch (Exception e) {
            System.out.println("Command not found: " + cmd[0]);
        }
    }
}

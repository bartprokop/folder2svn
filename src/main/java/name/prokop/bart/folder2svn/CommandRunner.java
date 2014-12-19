package name.prokop.bart.folder2svn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandRunner {

    private final int result;
    private final List<String> out = new ArrayList<>();
    private final List<String> err = new ArrayList<>();

    public CommandRunner(String... cmd) throws IOException, InterruptedException {
        result = run(cmd);
    }

    private int run(String[] cmd) throws IOException, InterruptedException {
        System.out.print(" -#- Runnig:");
        for (String c : cmd) {
            System.out.print(" " + c);
        }
        System.out.println();
        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                out.add(line);
            }
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                err.add(line);
            }
        }
        return process.exitValue();
    }

    public List<String> getOut() {
        return out;
    }

    public List<String> getErr() {
        return err;
    }

    public int getResult() {
        return result;
    }

    public void print() {
        for (String s : out) {
            System.out.println(s);
        }
        for (String s : err) {
            System.out.println(s);
        }
    }
}

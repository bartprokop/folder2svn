package name.prokop.bart.folder2svn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private static int run(String cmd, List<String> out, List<String> err) throws IOException, InterruptedException {
        System.out.println("Runnig: " + cmd);
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

    private static void print(List<String> out, List<String> err) throws IOException, InterruptedException {
        for (String s : out) {
            System.out.println(s);
        }
        for (String s : err) {
            System.out.println(s);
        }
    }

    public static void main(String... args) throws Exception {
        List<String> out = new ArrayList<>(), err = new ArrayList<>();
        List<String> toAdd = new ArrayList<>(), toDel = new ArrayList<>();

        out.clear();
        err.clear();
        run("svn status", out, err);
        print(out, err);
        if (err.size() > 0) {
            return;
        }

        for (String s : out) {
            if (s.startsWith(".git")) {
                continue;
            }
            if (s.startsWith("?")) {
                s = s.substring(1).trim();
                System.out.println("ADDING: '" + s + "'");
                toAdd.add(s);
            }
            if (s.startsWith("!")) {
                s = s.substring(1).trim();
                System.out.println("DELETING: '" + s + "'");
                toDel.add(s);
            }
        }

        for (String s : toAdd) {
            out.clear();
            err.clear();
            run("svn add " + s + "\"", out, err);
            print(out, err);
        }

        for (String s : toDel) {
            out.clear();
            err.clear();
            run("svn rm " + s + "\"", out, err);
            print(out, err);
        }

        out.clear();
        err.clear();
        run("svn update", out, err);
        print(out, err);

        out.clear();
        err.clear();
        run("svn commit -m \"master 2 svn sync - " + new Date() + "\"", out, err);
        print(out, err);
    }
}

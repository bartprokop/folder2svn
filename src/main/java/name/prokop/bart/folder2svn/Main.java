package name.prokop.bart.folder2svn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String... args) throws Exception {
        List<String> toAdd = new ArrayList<>(), toDel = new ArrayList<>();

//        CommandRunner cr = new CommandRunner("svn update");
//        cr.print();
        CommandRunner cr = new CommandRunner("svn", "status");
        if (cr.getErr().size() > 0) {
            return;
        }

        for (String s : cr.getOut()) {
            System.out.println("--> " + s);
            if (".git".equalsIgnoreCase(s.substring(1).trim())) {
                System.out.println("Skipping .git folder");
                continue;
            }
            if (s.startsWith("?")) {
                s = s.substring(1).trim();
                System.out.println("TO ADD: '" + s + "'");
                toAdd.add(s);
            }
            if (s.startsWith("!")) {
                s = s.substring(1).trim();
                System.out.println("TO DEL: '" + s + "'");
                toDel.add(s);
            }
        }

        for (String s : toAdd) {
            cr = new CommandRunner("svn", "add", s);
            cr.print();
        }
        for (String s : toDel) {
            cr = new CommandRunner("svn", "rm", s);
            cr.print();
        }
        cr = new CommandRunner("svn", "commit", "-m", "master 2 svn sync - " + new Date());
        cr.print();
    }
}

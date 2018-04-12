import java.io.*;
import java.util.List;

public class ArgsComm extends Command {//Класс для echo

    ArgsComm(String cmd) {
        this.cmd = cmd;
    }

    PipedInputStream execute(List<Argument> args, PipedInputStream pin) throws FileNotFoundException {//Метод выполняющий команду для входного потока pin, возвращает поток
        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream res = new PipedInputStream();
        try {
            pout.connect(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cmd.equals("echo")) {
            boolean start = true;
            for (Argument arg : args) {
                try {
                    if (!start) pout.write(' ');
                    start = false;
                    pout.write(arg.getValue().getBytes());
//                    pout.write(' ');
                    pout.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                pout.write('\n');
                pout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (cmd.equals("=")) {
            Interpretator.variables.put(args.get(0).arg, args.get(1));
        } else if (cmd.equals("ls")) {
            String path = null;
            if (args.isEmpty()) {
                path = Interpretator.getCurrentDirectory().toString();
            } else {
                path = Interpretator.getCurrentDirectory() + args.get(0).arg;
            }

            File dir = new File(path);
            File[] filesList = dir.listFiles();
            for (File file : filesList) {
                System.out.println(file.getName());
            }
        } else if (cmd.equals("cd")) {
            if (args.isEmpty()) {
                Interpretator.getCurrentDirectory().toHome();
            } else {
                String path = args.get(0).arg;

                File f = new File(Interpretator.getCurrentDirectory() + path);
                if (f.exists() && !f.isDirectory()) {
                    throw new FileNotFoundException("Invalid directory path");
                }

                Interpretator.getCurrentDirectory().toDirectory(path);
            }
        }
        try {
            pout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String cmd;
}

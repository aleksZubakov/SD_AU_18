package ru.spbau.mit;

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
            } else if (args.size() > 1) {
                throw new IllegalArgumentException("ls can work only with one argument");
            } else {
                path = Interpretator.getCurrentDirectory() + args.get(0).arg;
            }

            File dir = Interpretator.getCurrentDirectory().getFile(path);
            if (!dir.exists() || !dir.isDirectory()) {
                throw new FileNotFoundException("Invalid directory path");
            }
            File[] filesList = dir.listFiles();

            try {
                for (File file : filesList) {
                    pout.write(file.getName().getBytes());
                    pout.write('\n');
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (cmd.equals("cd")) {
            if (args.isEmpty()) {
                Interpretator.getCurrentDirectory().toHome();
            } else if (args.size() > 1) {
                throw new IllegalArgumentException("cd can work only with one argument");
            } else {
                String path = args.get(0).arg;

                File f = Interpretator.getCurrentDirectory().getFile(path);
                if (!f.exists() || !f.isDirectory()) {
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

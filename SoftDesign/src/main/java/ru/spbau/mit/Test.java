package ru.spbau.mit;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Test {
    public static boolean test(String text, String result) {
        PipedOutputStream pout = new PipedOutputStream();
        PipedInputStream pin = new PipedInputStream();
        String out = "";
        try {
            pout.connect(pin);
            pout.write(text.getBytes());
            pout.flush();
            out = Interpretator.Interpret(pin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.equals(result);
    }

    public static void tst() {
        boolean res;
        res = test("echo 1\n", "1");
        System.out.println("ru.spbau.mit.Test 1: " + res);
        res = test("c=5 | echo $c\n", "5");
        System.out.println("ru.spbau.mit.Test 2: " + res);
        res = test("c=3 | echo $c\n", "3");
        System.out.println("ru.spbau.mit.Test 3: " + res);
        res = test("pwd\n", System.getProperty("user.dir"));
        System.out.println("ru.spbau.mit.Test 4: " + res);
        res = test("pwd\n | cat", System.getProperty("user.dir"));
        System.out.println("ru.spbau.mit.Test 5: " + res);
        res = test("wc src/ru.spbau.mit.Arg.java\n", "8 24 192");
        System.out.println("ru.spbau.mit.Test 6: " + res);
        res = test("echo $c | cat\n", "3");
        System.out.println("ru.spbau.mit.Test 7: " + res);


        res = test("cd src/..", "");
        System.out.println("ru.spbau.mit.Test 8: " + res);
    }
}

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
        System.out.println("Test 1: " + res);
        res = test("c=5 | echo $c\n", "5");
        System.out.println("Test 2: " + res);
        res = test("c=3 | echo $c\n", "3");
        System.out.println("Test 3: " + res);
        res = test("pwd\n", System.getProperty("user.dir"));
        System.out.println("Test 4: " + res);
        res = test("pwd\n | cat", System.getProperty("user.dir"));
        System.out.println("Test 5: " + res);
        res = test("wc src/Arg.java\n", "8 24 192");
        System.out.println("Test 6: " + res);
        res = test("echo $c | cat\n", "3");
        System.out.println("Test 7: " + res);


        res = test("cd src/..", "");
        System.out.println("Test 8: " + res);

        /* my specific tests*/
//        res = test("cd ..", "/Users/alekseyzubakov/Documents/edu/AU/sd/SD_AU_18/");
//        System.out.println("Test 9: " + res);
//
//        res = test("cd ls", "SD_AU_18");
//        System.out.println("Test 10: " + res);

    }
}

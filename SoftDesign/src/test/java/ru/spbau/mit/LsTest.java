package ru.spbau.mit;

import com.google.common.base.Splitter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class LsTest {
    private static PipedOutputStream pout;
    private static PipedInputStream pin;
    final String pwdCmd = "pwd\n";

    public static String runCommand(String command) {
        String out = "";
        try {
            pout.write(command.getBytes());
            pout.flush();
            out = Interpretator.Interpret(pin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Before
    public void setUp() throws Exception {
        pout = new PipedOutputStream();
        pin = new PipedInputStream();
        pout.connect(pin);
    }

    @After
    public void tearDown() throws Exception {
        Interpretator.resetDirectory();
    }

    @Test
    public void notExistingLs() {
        String output = runCommand("ls not_existing_long_path/to/blah_lblah\n");
        assertThat(output)
                .isEqualTo("");
    }


    @Test
    public void complicatedCommands() {
        File currentDir = new File(System.getProperty("user.dir"));
        File subfolder = new File(currentDir, "new_fold");
        subfolder.mkdir();
        File subSubfolder = new File(subfolder, "second_new_fold");
        subSubfolder.mkdir();

        List<File> generatedFiles = IntStream.range(0, 10).mapToObj(
                a -> new File(subSubfolder, "test_name_" + a)
        ).peek(file -> {
            try {
                file.createNewFile();
            } catch (IOException e) {}
        }).collect(Collectors.toList());

        List<String> expectedOutput = generatedFiles.stream()
                .map(File::getName)
                .collect(Collectors.toList());

        String command = "cd new_fold/second_new_fold\n";
        runCommand(command);

        String output = runCommand("ls\n");

        Iterable<String> split = Splitter.fixedLength(11).split(output);

        assertThat(split).containsAll(
            expectedOutput
        );
        generatedFiles.forEach(File::deleteOnExit);
        subfolder.deleteOnExit();
        subSubfolder.deleteOnExit();
    }
}

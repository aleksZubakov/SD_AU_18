package ru.spbau.mit;

import org.assertj.core.internal.bytebuddy.agent.builder.AgentBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CdTest {

    private static PipedOutputStream pout;
    private static PipedInputStream pin;
    final String pwdCmd = "pwd\n";

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

    @Test
    public void testCdToParentWorks() {
        File currentDir = new File(System.getProperty("user.dir"));

        String command = "cd ..\n";

        runCommand(command);
        String output = runCommand(pwdCmd);

        assertThat(output).isEqualTo(currentDir.getParent());
    }

    @Test
    public void testCdWithManyArgs() {
        File currentDir = new File(System.getProperty("user.dir"));
        runCommand("cd .. ..\n");
        String output = runCommand(pwdCmd);
        assertThat(output).isEqualTo(currentDir.getPath());
    }

    @Test
    public void testComplicatedPath() {
        File currentDir = new File(System.getProperty("user.dir"));
        runCommand("cd ../..\n");
        String output = runCommand(pwdCmd);
        assertThat(output)
                .isEqualTo(currentDir.getParentFile().getParent());
    }

    @Test
    public void notExistingPath() {
        File currentDir = new File(System.getProperty("user.dir"));
        runCommand("cd not_existing_long_path/to/blah_lblah\n");
        String output = runCommand(pwdCmd);

        assertThat(output)
                .isEqualTo(currentDir.getPath());
    }

}

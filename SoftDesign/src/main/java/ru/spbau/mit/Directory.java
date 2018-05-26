package ru.spbau.mit;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * Directory is a wrapper for containing current directory and path
 * manipulation
 */
public final class Directory {
    public Path path;

    private static final String HOME = System.getProperty("user.home");

    Directory(String path) {
        this.path = Paths.get(path);
    }

    void toDirectory(String dir) {
        try {
            path = path.resolve(dir).toRealPath();
        } catch (IOException e) {
            throw new IllegalArgumentException("oops");
        }
    }

    void toHome() {
        path = Paths.get(HOME);
    }


    @Override
    public String toString() {
        return path.toString();
    }

    public File getFile(String pathToFile) {
        return new File(Interpretator.getCurrentDirectory().path.resolve(pathToFile).toString());
    }
}

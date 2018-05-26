package ru.spbau.mit;

import java.io.FileNotFoundException;
import java.io.PipedInputStream;
import java.util.List;

public class Command {//Базовый класс команд

    PipedInputStream execute(List<Argument> args, PipedInputStream pin) throws FileNotFoundException {
        return null;
    }//Метод выполнения команды, возвращающий поток
}

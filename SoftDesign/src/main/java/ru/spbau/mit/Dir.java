package ru.spbau.mit;

public class Dir {
    public static void main(String[] args) {
        Directory directory = new Directory("~/home");
        System.out.println(directory.toString());

        directory.toDirectory("new");
    }
}

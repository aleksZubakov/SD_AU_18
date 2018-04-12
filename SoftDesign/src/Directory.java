import java.util.LinkedList;
import java.util.ListIterator;

public final class Directory {
    private final LinkedList<String> path;

    private static final String HOME = System.getProperty("user.home");

    Directory(String path) {
        this.path = new LinkedList<>();


        for (String elem : path.split("/")) {
            this.path.push(elem);
        }
    }

    void toDirectory(String dir) {
        if (path.isEmpty()) {
            for (String elem : HOME.split("/")) {
                this.path.push(elem);
            }
        }

        for (String elem : dir.split("/")) {
            if (elem.equals("..") && !path.isEmpty()) {
                this.path.pop();
            } else {
                this.path.push(elem);
            }
        }
    }

    void toHome() {
        path.clear();
    }


    @Override
    public String toString() {
        if (path.isEmpty()) {
            return HOME;
        }


        StringBuilder builder = new StringBuilder();
        ListIterator<String> li = path.listIterator(path.size());
        while (li.hasPrevious()) {
            String curr = li.previous();
            builder.append(curr).append("/");
        }
        return builder.toString();
    }
}

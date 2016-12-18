package Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.walk;
import static java.nio.file.Paths.get;

class Reader {
    static Set<String> getPackages(String rootPackageName) throws IOException {
        final String FOLDER_REGEX = "\\w+\\\\";
        final int INDEX_PACKAGE = 1;
        return walk(get(rootPackageName + "/"))
                .filter(filePath -> isRegularFile(filePath))
                .map(files -> files.getParent().toString().split(FOLDER_REGEX)[INDEX_PACKAGE])
                .collect(Collectors.toSet());
    }

    static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        dirs.forEach(directory -> classes.addAll(findClasses(directory, packageName)));
        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class> findClasses(File directory, String packageName) {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                try {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
        return classes;
    }
}

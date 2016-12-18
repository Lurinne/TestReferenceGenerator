package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Main.Reader.getClasses;
import static Main.Reader.getPackages;
import static java.util.Arrays.stream;

public class Main {

    private static HashMap<String, String> generateReferenceForClass(String pack, String classContainsValue, String methodContainsValue)
            throws IOException, ClassNotFoundException {

        final HashMap<String, String> methodSet = new HashMap<>();
        stream(getClasses(pack))
                .filter(c -> c.getName().contains(classContainsValue))
                .forEach(c -> stream(c.getDeclaredMethods())
                        .filter(method -> method.getName().startsWith(methodContainsValue))
                        .forEach(method -> {
                                    final String methodReferenceName = c.getPackage().getName() + "." + c.getSimpleName() + "#" + method.getName();
                                    final String methodAnnotationName = method.getDeclaredAnnotation(Demo.class).stringValue();
                                    methodSet.put(methodAnnotationName, methodReferenceName);
                                    System.out.println(methodReferenceName + " " + methodAnnotationName);
                                }
                        )
                );
        return methodSet;
    }

    private static List<HashMap<String, String>> generateReferenceForPackage(String rootPackageName, String classContainsValue, String methodContainsValue) throws IOException, ClassNotFoundException {
        final List<HashMap<String, String>> list = new ArrayList<>();
        for (String packName : getPackages(rootPackageName))
            list.add(generateReferenceForClass(packName, classContainsValue, methodContainsValue));
        return list;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final String ROOT_PACKAGE_NAME = "src/Test/";
        final String CONTAINS_CLASS_NAME = "Test";
        final String CONTAINS_METHOD_NAME = "example";
        generateReferenceForPackage(ROOT_PACKAGE_NAME, CONTAINS_CLASS_NAME, CONTAINS_METHOD_NAME);
    }
}

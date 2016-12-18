package Test;

import Main.Demo;

public class TestClass1 {
    @Demo(stringValue = "Demo Annotation 1", val = 100)
    public String example1() {
        return null;
    }

    @Demo(stringValue = "Demo Annotation 2", val = 100)
    public String example2() {
        return null;
    }
}

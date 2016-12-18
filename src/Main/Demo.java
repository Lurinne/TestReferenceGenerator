package Main;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// declare a new annotation
@Retention(RetentionPolicy.RUNTIME)
public @interface Demo {

    String stringValue();

    int val();
}

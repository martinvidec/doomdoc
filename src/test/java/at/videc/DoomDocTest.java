package at.videc;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class DoomDocTest {

    @Test
    public void testDoomDoclet() {
        // Given
        String[] args = new String[] {
            "-doclet", "at.videc.DoomDoclet",
            "-sourcepath", "src/main/java",
            "src/main/java/at/videc/Main.java",
            "src/main/java/at/videc/BOMB.java"
        };

        // Execute DoomDoclet
        //com.sun.tools.javadoc.Main.execute(args);

        // Then
        // Check if output.html exists
        File file = new File("output.html");
        assertTrue(file.exists());

        // Check if output.html is not empty
        assertTrue(file.length() > 0);
    }

}

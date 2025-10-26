package at.videc;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.spi.ToolProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DoomDocTest {

    @Test
    public void testCustomDoclet() throws Exception {
        // Given
        ToolProvider javadoc = ToolProvider.findFirst("javadoc")
                .orElseThrow(() -> new IllegalStateException("javadoc tool not found"));

        String[] args = new String[] {
                "-doclet", "at.videc.DoomDoclet",
                "-sourcepath", "src/main/java:src/test/java",
                "-subpackages", "at.videc"
        };

        // When
        int result = javadoc.run(System.out, System.err, args);

        // Then
        assertEquals("Doclet execution failed", 0, result);

        // Verify output file exists
        File outputFile = new File("output.html");
        assertTrue("Output file does not exist", outputFile.exists());

        // Verify output file is not empty
        assertTrue("Output file is empty", outputFile.length() > 0);

        // Optionally, verify specific content in the output file
        String content = Files.readString(outputFile.toPath());
        assertTrue("Output file does not contain expected content", content.contains("<h1 id=\"docTitle\">Documentation</h1>"));
    }
}
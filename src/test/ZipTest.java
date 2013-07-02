import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ZipTest {
    private Zip zip;

    @Before
    public void setUp() {
        zip = new Zip();
    }

    @Test
    public void testZipOneFile() throws Exception {
        File testFile = new File("test.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(testFile));
        output.write("This is a test.");
        output.close();

        File outZip = zip.createZip("test.txt", "output.zip");
        testFile.delete();
        assertTrue(outZip.exists());
        outZip.delete();
    }

    @Test
    public void testZipMultipleFiles() throws Exception {
        List<String> fileNamesList = new ArrayList<String>();
        File testFile1 = new File("test1.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(testFile1));
        output.write("This is a test1.");
        output.close();

        File testFile2 = new File("test2.txt");
        output = new BufferedWriter(new FileWriter(testFile2));
        output.write("This is a test2.");
        output.close();

        File testFile3 = new File("test3.txt");
        output = new BufferedWriter(new FileWriter(testFile3));
        output.write("This is a test3.");
        output.close();

        fileNamesList.add("test1.txt");
        fileNamesList.add("test2.txt");
        fileNamesList.add("test3.txt");

        File outZip = zip.createZip(fileNamesList, "output.zip");
        testFile1.delete();
        testFile2.delete();
        testFile3.delete();
        assertTrue(outZip.exists());
        outZip.delete();
    }
}

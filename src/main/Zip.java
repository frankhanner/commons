import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Simple Archiver
 *
 * @author frank hanner
 */
public class Zip {
    private final Logger LOG = Logger.getLogger(Zip.class);

    /**
     * Creates zip file with single file in archive. Zip file is created in the same directory as inputfiles
     *
     * @param inputFileName  File name to be zipped
     * @param outputFileName Absolute path of created zip
     * @throws Exception
     */
    public File createZip(String inputFileName, String outputFileName) throws Exception {
        List<String> fileNameList = new ArrayList<String>();
        fileNameList.add(inputFileName);
        return createZip(fileNameList, outputFileName);
    }

    /**
     * Creates zip file with multiple files in archive. Zip file is created in the same directory as input files
     *
     * @param inputFileNames List of file names to be zipped
     * @param outputFileName Absolute path of created zip
     * @throws Exception
     */
    public File createZip(List<String> inputFileNames, String outputFileName) throws Exception {
        LOG.info("Creating " + outputFileName);

        //append directory path to zip name by looking at one of the input file names
        for (String fileName : inputFileNames) {
            outputFileName = addPathToZip(fileName, outputFileName);
            break;
        }

        //output file
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFileName));

        for (String fileName : inputFileNames) {
            //input file
            FileInputStream in = new FileInputStream(fileName);

            //name the file inside the zip file
            File absolutePath = new File(fileName);
            String relativePath = absolutePath.getName();
            out.putNextEntry(new ZipEntry(relativePath));

            //buffer size
            byte[] b = new byte[1024];
            int count;

            LOG.info("Preparing to add " + fileName + " to " + outputFileName);
            //read each line and compress
            while ((count = in.read(b)) > 0) {
                out.write(b, 0, count);
            }

            //close input stream
            in.close();
            LOG.info("Successfully added " + fileName + " to " + outputFileName);
        }
        //close zip stream
        out.close();
        LOG.info("Successfully created " + outputFileName);

        File zippedFile = new File(outputFileName);

        return zippedFile;
    }

    //adds an absolute path to the zip name, so it can be retrieved properly
    private String addPathToZip(String inputFilePath, String zipName) {
        File inFile = new File(inputFilePath);

        String path;
        if (inFile.getParentFile() == null) {
            path = "";
        } else {
            path = inFile.getParentFile().getAbsolutePath();
        }

        if (!zipName.contains(path)) {
            zipName = path + "/" + zipName;
        }

        return zipName;
    }
}

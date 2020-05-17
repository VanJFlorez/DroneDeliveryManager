package org.jcamilo.common;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {
    
    public static void deleteFiles(File folder) {
        if(folder.isDirectory()) {
            for(File f : folder.listFiles()) {
                f.delete();
            }
            return;
        }
        System.out.println("ERROR: The associated path is not folder...");
    }

    /**
     * Gets the name that should have the output file given an input file name.
     * @param inputFileName
     * @return
     */
    public static String getOutputFilename(String inputFileName) {
        return Constants.FILE_OUT_PREFIX + getFileNumber(inputFileName) + Constants.FILE_EXTENSION;
    }

    /**
     * @param fileName the name of a numbered in file
     * @return
     */
    public static String getFileNumber(String fileName) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(fileName);
        matcher.find();
        return matcher.group();
    }


    /**
     * Creates an object file pointing to this project's output folder
     * @param fileName
     * @return File the object representation af an output file located in 
     *         ./output/
     */
    public static File getOutFile(String fileName) {
        String projectPath;
        if(Pattern.matches(Constants.IN_FILENAME_REGEX, fileName)) {
            projectPath = Constants.OUT_FOLDER_NAME + "\\" + Constants.FILE_OUT_PREFIX + getFileNumber(fileName) + Constants.FILE_EXTENSION;
        } else {
            projectPath = Constants.OUT_FOLDER_NAME + "\\" + Constants.FILE_OUT_PREFIX + fileName + Constants.FILE_EXTENSION;
        }
        return new File(projectPath);
    }

    public static HashMap<File, File> getDataset() {
        File datasetsFolder = new File(Constants.DATASETS_FOLDER_NAME);
        // put in this map the in00i.txt and out00i.txt
        HashMap<File, File> datasets = new HashMap<>();
        for(File dataset : datasetsFolder.listFiles()) {
            if (Pattern.matches(Constants.IN_FILENAME_REGEX, dataset.getName())) { // get in files
                // get the associated output file
                String outFileName = FileUtils.getOutputFilename(dataset.getName());
                File outFile = new File(Constants.DATASETS_FOLDER_NAME + "\\" + outFileName);
                datasets.put(dataset, outFile);
            }
        }
        return datasets;
    }
}
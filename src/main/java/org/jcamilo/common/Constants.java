package org.jcamilo.common;

public class Constants {
    public static final int TEST_FACTOR = 1000;
    public static final char[] DIRECTIONS = {'N', 'E', 'S', 'W'}; // must match clockwise enumeration starting from north
    public static final String[] DIRECTIONS_ES = {"Norte", "Oriente", "Sur", "Occidente"}; // must match clockwise enumeration starting from north
    public static final char[] ACTIONS = {'A', 'I', 'D'};
    public static final int[] START_POINT = {0, 0, 0};
    
    public static final String REPORT_SPLIT_MESSAGE = "== Reporte de entregas ==";
    public static final String FILE_IN_PREFIX = "in";
    public static final String FILE_OUT_PREFIX = "out";
    public static final String FILE_EXTENSION = ".txt";
    public static final String NUMBERING_FORMAT = "%02d";
    public static final String IN_FILENAME_REGEX = "^" + Constants.FILE_IN_PREFIX + "\\d+" + Constants.FILE_EXTENSION;
    public static final String OUT_FILENAME_REGEX = "^" + Constants.FILE_OUT_PREFIX + "\\d+" + Constants.FILE_EXTENSION;
    public static final String OUTPUT_LOG_TEMPLATE = "(%d, %d) direccion %s";
    public static final String TEST_OUTPUT_LOG_TEMPLATE_REGEX = "^\\(-?\\d+, -?\\d+\\) direccion (Norte|Sur|Oriente|Occidente)";

    public static final String IN_FOLDER_NAME = "input";
    public static final String OUT_FOLDER_NAME = "output";
    public static final String DATASETS_FOLDER_NAME = "datasets";


}
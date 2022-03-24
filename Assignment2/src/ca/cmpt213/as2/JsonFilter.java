package ca.cmpt213.as2;

import java.io.File;
import java.io.FileFilter;

/**
 * JsonFilter class contains a filter to search specifically for json files
 */
public class JsonFilter implements FileFilter {
    @Override
    public boolean accept(File path) {
        return path.getAbsolutePath().toLowerCase().endsWith(".json");
    }
}
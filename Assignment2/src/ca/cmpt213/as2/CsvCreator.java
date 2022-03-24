package ca.cmpt213.as2;

import com.opencsv.CSVWriter;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * CsvReader is a class that inputs the json file information into a csv file.
 * It also has a sort function that sorts by the order of the String array ids
 */
public class CsvCreator {

    public void csvBuild(File file, ArrayList<String[]> tokiInfo,ArrayList<ArrayList<String>> teams,int numTeams) throws IOException {
        FileWriter outFile = new FileWriter(file);
        CSVWriter cWriter = new CSVWriter(outFile);
        String[] header = {"Team#","From Toki","To Toki","Score","Comment","","Extra"};
        cWriter.writeNext(header);

        for(int k=0;k<numTeams;k++){
            String[] team = {"Team"+(k+1)};
            cWriter.writeNext(team);
            for(int j=0;j<tokiInfo.size();j++){
                if(teams.get(k).contains(tokiInfo.get(j)[1].trim())) {
                    cWriter.writeNext(tokiInfo.get(j));
                }
            }
            cWriter.writeNext(new String[]{""});
        }
        cWriter.close();
    }

    public void sort(ArrayList<String[]> tokiInfo) {
        Collections.sort(tokiInfo, new Comparator<String[]>() {
            @Override
            public int compare(String[] strings, String[] t1) {
                if(strings[1].toLowerCase().compareTo(t1[1].toLowerCase())==0) {
                    if (strings[1].startsWith("-") && !t1[1].startsWith("-")) {
                        return strings[2].toLowerCase().compareTo(t1[2].toLowerCase()) - 100;
                    }
                    else if (t1[2].startsWith("-") && !strings[1].startsWith("-")) {
                        return strings[2].toLowerCase().compareTo(t1[2].toLowerCase()) - 100;
                    }
                    else{
                        return strings[2].toLowerCase().compareTo(t1[2].toLowerCase());
                    }
                }
                return strings[1].toLowerCase().compareTo(t1[1].toLowerCase());
            }
        });
    }
}


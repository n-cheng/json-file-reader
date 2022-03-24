package ca.cmpt213.as2;

import com.google.gson.*;
import java.io.*;
import java.util.*;

/**
 * JsonRead class searches for files using a filter and grabs information from these files and adds them to java objects (arrays and arraylists).
 * Data includes the tokimons's:
 * -Tokimon id
 * -mentions
 * -score
 * -compatibility score with other tokimon of the same team
 * -comments
 * -extra comments
 * -names
 */
public class JsonRead {
    String start,end,score,comment,extraComment;

    //Recursively searches through folders in the File
    public File searchJson(File file, ArrayList<File> tokiJsons) {
        if(file.isDirectory()) {
            File[] arrFile = file.listFiles();
            for(File k : arrFile) {
                File caught = searchJson(k,tokiJsons);
                if(new JsonFilter().accept(k)) {
                    tokiJsons.add(caught);
                    return caught;
                }
            }
        }
        else {
            if(new JsonFilter().accept(file)) {
                return file;
            }
        }
        return null;
    }

    public int jsonFileReader(File file, ArrayList<String[]> tokiInfo, ArrayList<ArrayList<String>> teams, int numTeams, ArrayList<String> tokiNames) {
        JsonParser parser = new JsonParser();
        try {
            JsonObject thing = (JsonObject) parser.parse(new FileReader(file));
            JsonArray arr = (JsonArray) thing.get("team");
            extraComment = thing.get("extra_comments").getAsString();
            int count = 0;

            for (Object tokis : arr) {
                JsonObject tokimon = (JsonObject) tokis;
                if (count == 0) {
                    start = tokimon.get("id").getAsString();
                    end = "-";
                } else {
                    end = tokimon.get("id").getAsString();
                }

                JsonObject compatibility = (JsonObject) tokimon.get("compatibility");
                score = compatibility.get("score").getAsString();

                if(compatibility.get("score").getAsInt()<0){
                    System.out.println("Compatibility score of this Tokimon is less than 0:");
                    System.out.println(file);
                    System.exit(-1);
                }

                comment = compatibility.get("comment").getAsString();

                if (end.equals("-")) {
                    tokiInfo.add(new String[]{"", start, end, score, comment, "", "" + extraComment});
                    tokiNames.add(tokimon.get("name").getAsString());
                } else {
                    tokiInfo.add(new String[]{"", start, end, score, comment, "", ""});
                    tokiNames.add(tokimon.get("name").getAsString());
                    int left = comment.indexOf("to '") + 4;
                    int right = comment.indexOf("'\n");
                    String name2 = comment.substring(left,right);
                    tokiNames.add(name2);
                }

                String temp = tokimon.get("name").getAsString();
                int index = temp.indexOf(" ");
                temp = temp.substring(0, index);
                String id = tokimon.get("id").getAsString();

                if(count == 0) {
                    boolean newTeam = true;
                    for (int k = 0; k < teams.size(); k++) {
                        if (teams.get(k).contains(temp)) {
                            newTeam = false;
                        }
                    }

                    if (newTeam) {
                        teams.add(new ArrayList<String>());
                        teams.get(numTeams).add(temp);
                        numTeams++;
                    }
                    for (int k = 0; k < numTeams; k++) {
                        if (teams.get(k).contains(temp)) {
                            teams.get(k).add(id.trim());
                        }
                    }
                }
                count++;
            }
        }
        catch(Exception e){
            System.out.println("Bad JSON file format or missing required fields in file:");
            System.out.println(file);
            System.exit(-1);
        }
        return numTeams;
    }
}
package ca.cmpt213.as2;

import java.io.*;
import java.util.*;

/**
 * Tokimon Processor is the main class of the project
 * Contains functions to check for multiple differnt errors
 * The main function runs the step by step process of collecting information from multiple json
 * files into an array manipulating this information into a single csv file.
 * The TokimonProcessor class has three arrays in which one contains all the data,another one contains the groupings of each team, and the final one has a list of tokimon names.
 */
public class TokimonProcessor {

    public static void tokiCheckError1(ArrayList<ArrayList<String>> teams, int numTeams) {
        for(int k=0;k<numTeams;k++){
            for(int j=1;j<teams.get(k).size();j++){
                for(int i=0;i<numTeams;i++){
                    if(i!=k) {
                        if(teams.get(i).contains(teams.get(k).get(j))){
                            System.out.println("A team shares the same tokimon.");
                            System.exit(-1);
                        }
                    }
                }
            }
        }
    }

    private static void tokiCheckError2(ArrayList<String[]> tokiInfo, ArrayList<ArrayList<String>> teams, int numTeams){
        boolean containsJson = false;
        for(int i = 0; i < tokiInfo.size(); i++){
            containsJson = false;

            if(!(tokiInfo.get(i)[2].equals("-"))){
                for(int j = 0; j < numTeams; j++){
                    if(teams.get(j).contains(tokiInfo.get(i)[2])){
                        containsJson = true;
                        break;
                    }
                }
            }
            else{
                containsJson = true;
            }
            if(!containsJson){
                System.out.println("Some tokimons don't have a json file.");
                System.exit(-1);
            }
        }
    }

    public static void tokiCheckError3(ArrayList<String[]> tokiInfo, ArrayList<ArrayList<String>> teams, int numTeams){
        ArrayList<String> mentions = new ArrayList<String>();
        for(int i = 0; i < numTeams; i ++) {
            for(int j = 1; j < teams.get(i).size(); j++) {
                for(int k = 0; k < tokiInfo.size(); k++) {
                    if(tokiInfo.get(k)[1].trim().equals(teams.get(i).get(j))) {
                        mentions.add(tokiInfo.get(k)[2]);
                    }
                }
                for(int k = 1; k < teams.get(i).size(); k++) {
                    if(!(teams.get(i).get(k).equals(teams.get(i).get(j)))) {
                        if(!mentions.contains(teams.get(i).get(k))) {
                            System.out.println("Some tokimon don't mention their teammates.");
                            System.exit(-1);
                        }
                    }
                }
            }
        }
    }

    public static void tokiCheckError4(ArrayList<String[]> tokiInfo, ArrayList<ArrayList<String>> teams, ArrayList<String> tokiNames){

        int count = 0,teamNum = 0,inTeam = 0;
        Collections.sort(tokiNames);
        String currToki = tokiNames.get(0);

        for(int i = 0; i < tokiNames.size(); i++) {
            if(tokiNames.get(i).equals(currToki)) {
                count++;
            }
            else{
                if(count != ((teams.get(teamNum).size()-1)*2)-1) {
                    System.out.println("Tokimon's properties don't match the ones that already exist.");
                    System.exit(-1);
                }

                count = 1;
                inTeam++;

                if(inTeam == teams.get(teamNum).size()-1){
                    inTeam = 0;
                    teamNum++;
                }
                currToki = tokiNames.get(i);
            }
        }
    }

    public static void main(String[] args) throws IOException{

        ArrayList<File> files = new ArrayList<File>();
        ArrayList<String[]> tokiInfo = new ArrayList<String[]>();
        ArrayList<String> tokiNames = new ArrayList<String>();
        CsvCreator csvFinal = new CsvCreator();

        ArrayList<ArrayList<String>> teams = new ArrayList<ArrayList<String>>();
        int numTeams = 0;

        //If too many arguements are given
        if(args.length!=2) {
            System.out.println("Only accepts 2 arguments:\n-.json directory\n-Path where .csv is created");
            System.exit(-1);
        }

        File out = new File(args[1]);
        File in  = new File(args[0]);

        if(!in.isDirectory()) {
            System.out.println("The input for the file path does not exist.");
            System.exit(-1);
        }
        if(!out.isDirectory()) {
            System.out.println("The output for the file path does not exist.");
            System.exit(-1);
        }

        File output = new File(out+"/team_info.csv");

        JsonRead reader = new JsonRead();
        reader.searchJson(in,files);

        if(files.size()==0) {
            System.out.println("No json files found.");
            System.exit(-1);
        }

        //Writing the data to the tokiInfo array
        for(File k : files) {
            numTeams = reader.jsonFileReader(k,tokiInfo,teams,numTeams,tokiNames);
        }

        //Checking for errors
        tokiCheckError1(teams,numTeams);
        tokiCheckError2(tokiInfo,teams,numTeams);
        tokiCheckError3(tokiInfo,teams,numTeams);
        tokiCheckError4(tokiInfo,teams,tokiNames);

        //Sortings
        csvFinal.sort(tokiInfo);
        for(int k = 0;k<numTeams;k++){
            Collections.sort(teams.get(k));
        }

        //Building the csv file
        csvFinal.csvBuild(output,tokiInfo,teams,numTeams);
    }
}


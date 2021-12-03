import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    
    
    public static void main(String[] args) throws MalformedURLException, 
            FileNotFoundException, 
            IOException{
        
        //read users choice of search type
        System.out.println("Enter 1 to search by ingredient");
        System.out.println("Enter 2 to search by drink.");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        
        //read search word
        System.out.println("Enter the word you want to search for.");
        input = new Scanner(System.in);
        String searchCriteria = input.nextLine();
        
        //set URL based on search choice
        URL url = null;
        if (choice == 1){
            url = new URL ("https://www.thecocktaildb.com/api/json/"
                    + "v1/1/search.php?i=" + searchCriteria);
        } else if (choice == 2){
            url = new URL ("https://www.thecocktaildb.com/api/json/v1/1/"
                    + "search.php?s=" + searchCriteria);
        } else {
            System.out.println("Not a valid choice.");
        }
        
        //retreive data
        JSONArray dataObject = retrieveData(url);
        
        //check if results are found
        if (dataObject != null){
            
            //convert JSONArray to array of Maps
            List<Map<String, String>> list = new ArrayList<>();
            
            for (int i = 0; i < dataObject.size(); i++){
                Map<String, String> searchResult 
                        = (Map<String, String>) dataObject.get(i);
                
                list.add(searchResult);
            }
            
            Map<String, String>[] searchResults  
                    = list.toArray(new HashMap[list.size()]);
            
            //output to file
            try (BufferedWriter writer = 
                    new BufferedWriter(new FileWriter("output.dat"))) {
                
                int i = 1;
                
                switch (choice) {
                    
                    case 1 -> {
                        formatResultsAndOutput(choice,
                                writer,
                                searchResults[0], 
                                searchCriteria,
                                i);
                        break;
                    }
                    
                    case 2 -> {
                            for(Map<String, String> searchResult 
                                    : searchResults){
                                
                                formatResultsAndOutput(choice,
                                        writer,
                                        searchResult, 
                                        searchCriteria,
                                        i);
                                
                                i++;
                            }
                            break;
                        }
                }
                writer.close();
            }
            //output no results found
        } else{
            System.out.println("No results found.");
        }
    }
    
    //retrieves data from URL
    static JSONArray retrieveData(URL url){
        JSONArray dataObject = null;
        try{
            //connect to url
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //check valid response code
            int responseCode = conn.getResponseCode();

            if (responseCode != 200){
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                
                //convert information to string
                StringBuilder informationString = new StringBuilder();
                
                try (Scanner scanner = new Scanner(url.openStream())) {
                    while(scanner.hasNext()){
                        informationString.append(scanner.nextLine());
                    }
                }
                
                //format string
                int x = informationString.indexOf(":");
                informationString.delete(0, x+1);
                x = informationString.lastIndexOf("}");
                informationString.delete(x, informationString.length());
                
                String information = String.valueOf(informationString);
                
                //create JSONArray
                if (information != null){
                    JSONParser parse = new JSONParser();
                    dataObject = (JSONArray) parse.parse(information);
                }
                
            }
            //catch for invalid urls
        } catch (IOException | RuntimeException | ParseException ex){
            System.out.println("    Invalid URL");
            return null;
        }
        return dataObject;
    }
    
    //formats results and outputs to output.dat file
    private static void formatResultsAndOutput(int choice, 
            BufferedWriter writer, 
            Map<String, String> searchResult, 
            String searchCriteria,
            int iteration) throws IOException{
        
        //output for ingredient or drinks
        String text = "";
        if (choice == 1){
            text = "Ingredient";
        } else {
            text = "Drinks";
        }
        
        //outputs search information
        writer.write("Search: " + text + "\n");
        writer.write("Search for: " + searchCriteria + "\n");
        writer.write("Result Number: " + iteration + "\n");

        //outputs search results row by row
        searchResult.entrySet().forEach(entry -> {
            try {
                writer.write("Key = " + entry.getKey() 
                        + ", Value = " + entry.getValue() + "\n");

            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        });
        
        writer.write("\n");
    }
}

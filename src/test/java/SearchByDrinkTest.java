import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.json.simple.JSONArray;
import org.junit.Test;

public class SearchByDrinkTest extends TestCase {
    
    //creates a data set for tests to use
    private Map<String, String>[] setup(String searchWord) 
            throws MalformedURLException{
        
        URL url = new URL(
                "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=" 
                        + searchWord);
      
        JSONArray testDataObject = Main.retrieveData(url);
        
        List<Map<String, String>> list = new ArrayList<>();
            
            for (int i = 0; i < testDataObject.size(); i++){
                Map<String, String> testDataObjects 
                        = (Map<String, String>) testDataObject.get(i);
                
                list.add(testDataObjects);
            }

        return list.toArray(new HashMap[list.size()]);
    }
    
    //Tests the api response has all the required Schema properties
    @Test
    public void test1ContainsFields() throws MalformedURLException {
        System.out.println("test1ContainsFields");
        
        Map<String, String>[] testSearchResults = setup("margarita");
       
        for(Map<String, String> testSearchResult: testSearchResults){
            assertTrue("strDrink missing",
                    testSearchResult.containsKey("strDrink"));
            assertTrue("strTags missing",
                    testSearchResult.containsKey("strTags"));
            assertTrue("strCategory missing",
                    testSearchResult.containsKey("strCategory"));
            assertTrue("strAlcoholic missing",
                    testSearchResult.containsKey("strAlcoholic"));
            assertTrue("strGlass missing",
                    testSearchResult.containsKey("strGlass"));
            assertTrue("strInstructions missing",
                    testSearchResult.containsKey("strInstructions"));
            assertTrue("strIngredient1 missing",
                    testSearchResult.containsKey("strIngredient1"));
            assertTrue("strMeasure1 missing",
                    testSearchResult.containsKey("strMeasure1"));
            assertTrue("strCreativeCommonsConfirmed missing", 
                    testSearchResult.containsKey(
                            "strCreativeCommonsConfirmed"));
            assertTrue("dateModified missing",
                    testSearchResult.containsKey("dateModified"));
       }
    }
    
    //tests the size of the drinks results array 
    //to show that an array was created
    @Test
    public void test2ArraySize() throws MalformedURLException {
        System.out.println("test2ArraySize");
        
        Map<String, String>[] testSearchResults = setup("margarita");
        int expectedSize = 6;
        
        assertEquals("Results array size incorrect" ,
                expectedSize, testSearchResults.length);
    }    
    
    //tests that results are null when drink doesn't exist
    @Test
    public void test3NullDrinksWhenNoResults() throws MalformedURLException {
        System.out.println("test3NullDrinksWhenNoResults");
        
        String nullPointerString = "";
        String expectedString = "Null pointer exception Thrown";
        
        try{
            Map<String, String>[] testSearchResults = setup("Derivco");
        }catch (NullPointerException npe){
             nullPointerString = "Null pointer exception Thrown";   
        }
        
        assertEquals("Drinks not null when no results found"
                ,expectedString, nullPointerString);
    }
    
    //tests the results don't depend on case
    @Test
    public void test4CaseSensitive() throws MalformedURLException{
        System.out.println("test4CaseSensitive");
        
        Map<String, String>[] testSearchResultsLC = setup("margarita");
        Map<String, String>[] testSearchResultsUC = setup("MaRgaRitA");
        
        for (int i = 0; i < testSearchResultsLC.length; i++)
            
            for (int j = 0; j <testSearchResultsLC[i].size(); j++){
                
               assertEquals("Different results given for different cases" ,
                       testSearchResultsLC[i].get(j), 
                       testSearchResultsUC[i].get(j)); 
            }
    }
 }
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

public class AdditionalTest extends TestCase {
    
    //creates a data set for tests to use
    private Map<String, String>[] setup(String searchWord) 
            throws MalformedURLException{
        
        URL url = new URL(
                "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=" 
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
    
    //Tests if an invalid URL is used an exception is thrown and 
    //the results are null
    @Test
    public void test1InvalidUrlResponseCode() throws MalformedURLException{
        System.out.println("test1InvalidUrlResponseCode");
        
        URL invalidUrl = new URL("http://www.thecocktaildb.com/"
                + "api/json/v1/1/search.php?i=vodka");
        
        String expected = "Success";
        String actual = "Success";
        JSONArray dummy = null;
        
        try{
            dummy = Main.retrieveData(invalidUrl);
        } catch (Exception ex) {
            actual = "Fail";
        }        
        
        assertEquals(
                "Exception should already have "
                        + "been caught and program terminated",
                actual,
                expected);
        
        assertNull("Result Should be null", dummy);
    }
    
    //Test that ingredient searches always return only 1 result
    @Test
    public void test2OneIngredientResult() throws MalformedURLException {
        System.out.println("test2OneIngredientResult");
        
        Map<String, String>[] testSearchResults = setup("vodka");
        
        int expectedSize = 1;
        int actualSize = testSearchResults.length;
        
        assertEquals("More than 1 ingreidnet result", actualSize, expectedSize);
    }
}
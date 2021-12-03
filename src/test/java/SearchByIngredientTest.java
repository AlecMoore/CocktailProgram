import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import junit.framework.TestCase;
import org.json.simple.JSONArray;
import org.junit.Test;

public class SearchByIngredientTest extends TestCase {
    
    //creates a data set for tests to use
    public static Map<String, String> setup(String searchWord) throws 
            MalformedURLException{
        
        URL url = new URL(
                "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=" 
                        + searchWord);
      
        JSONArray testDataObject = Main.retrieveData(url);
        
        return (Map<String, String>) testDataObject.get(0);
    }
    
    //tests ingredients results contain correct fields
    @Test
    public void test1ContainsFields() throws MalformedURLException {
        System.out.println("test1ContainsFields");

        Map<String, String> testSearchResult = setup("vodka");

        assertTrue("idIngredient missing",
                testSearchResult.containsKey("idIngredient"));
        assertTrue("strIngredient missing",
                testSearchResult.containsKey("strIngredient"));
        assertTrue("strDescription missing",
                testSearchResult.containsKey("strDescription"));
        assertTrue("strType missing",
                testSearchResult.containsKey("strType"));
        assertTrue("strAlcohol missing",
                testSearchResult.containsKey("strAlcohol"));
        assertTrue("strABV missing",
                testSearchResult.containsKey("strABV"));
    }
   
    //tests that alcohol fields are not null when ingredient is alcoholic
    @Test
    public void test2Alcoholic() throws MalformedURLException{
        System.out.println("test2Alcoholic");

        Map<String, String> testSearchResult = setup("vodka");
        
        String expected = "Yes";
        assertEquals("strAlcohol is null", expected,
                testSearchResult.get("strAlcohol"));
        
        assertNotNull("strABV is null", testSearchResult.get("strABV"));
    }
   
    //tests that alcohol fields are null when ingredient is non-alcoholic
    @Test
    public void test3NonAlcoholic() throws MalformedURLException{
        System.out.println("test3NonAlcoholic");
        
        Map<String, String> testSearchResult = setup("lemonade");
       
        assertNull("strAlcohol is not null",
                testSearchResult.get("strAlcohol"));
        assertNull("strABV is not null", testSearchResult.get("strABV"));
    }
    
}
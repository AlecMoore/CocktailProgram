import junit.framework.TestCase;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("-------------------------------------------------");
        System.out.println("SearchByIngredientTest\n");
        Result ingredientResult = JUnitCore.runClasses(
                SearchByIngredientTest.class);
        testResultOutput("SearchByIngredientTest", ingredientResult);

        System.out.println("-------------------------------------------------");
        System.out.println("SearchByDrinkTest\n");
        Result nameResult = JUnitCore.runClasses(SearchByDrinkTest.class);
        testResultOutput("SearchByDrinkTest", nameResult);
        
        System.out.println("-------------------------------------------------");
        System.out.println("AdditionalTest\n");
        Result additionalResult = JUnitCore.runClasses(AdditionalTest.class);
        testResultOutput("AdditionalTest", additionalResult);
    }
    
    //outputs test results
    private static void testResultOutput(String test, Result result){
        
        result.getFailures().forEach(failure -> {
            System.out.println("\n" + test + " failed");
            System.out.println(failure.toString() + "\n");
        });

        if (result.wasSuccessful()){
            System.out.println("\n" + test + " was successful\n");
        }
    }
    
}
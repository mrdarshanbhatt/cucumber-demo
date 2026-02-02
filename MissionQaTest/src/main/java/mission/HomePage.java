package mission;

public class HomePage extends BasePage {

    public static void homePage() {
        driver.get(LoadProp.getProperty("url"));
    }
}

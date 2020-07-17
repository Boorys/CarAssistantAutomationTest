package BasicFeatureTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class BasicFeatureTest {

    private final String PASSWORD = "123456";
    private final String EMAIL = "ekc28308@eoopy.com";
    private WebDriver driver;

    @BeforeEach
    void beforeEach() {
        System.setProperty("webdriver.chrome.driver", "D:\\Repozytorium\\CarAssistantSeleniumWebDriver\\src\\test\\java\\driver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        driver.get("https://calm-coast-64010.herokuapp.com");
    }

    @AfterEach
    void afterEach() throws InterruptedException {
        signOut();
        driver.quit();
    }

    private void singIn() {
        WebElement webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav/ul/li[1]/a"));
        webElement.click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[1]/input")).sendKeys(EMAIL);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[2]/input")).sendKeys(PASSWORD);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/button")).click();
    }

    private void signOut() throws InterruptedException {
        sleep(500);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav/ul/li[1]/a")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/div/button[1]")).click();
    }

    @Test
    public void singInTest() throws InterruptedException {

        singIn();
        WebElement webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav"));
        List<WebElement> list = webElement.findElements(By.tagName("li"));
        assertNotEquals(list.size(), 4);
        sleep(500);
    }

    @Test
    public void signOutTest() throws InterruptedException {

        //sing in
        singIn();
        WebElement webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav"));
        List<WebElement> webElements = webElement.findElements(By.tagName("li"));
        assertNotEquals(webElements.size(), 4);

        signOut();

        //check sing out
        webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav"));
        webElements = webElement.findElements(By.tagName("a"));
        assertEquals(webElements.size(), 4);
        singIn();
    }

    @Test
    public void addCarTest() throws InterruptedException {

        String carBrand;
        String carModel;

        singIn();
        WebElement webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav/ul/li[6]/a"));
        webElement.click();

        //choose car model
        webElement = webElement.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form"));
        List<WebElement> webElementList = webElement.findElements(By.tagName("option"));
        webElementList.get(1).click();
        String[] parts = webElementList.get(1).getText().split("  ");
        carBrand = parts[0];
        carModel = parts[1];
        //add more informations about car
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[2]/input")).sendKeys("7000");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[3]/input")).sendKeys("7000");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[4]/input")).sendKeys("7000");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[5]/input")).sendKeys("7000");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[6]/input")).sendKeys("7000");
        //confirm button
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/input")).click();

        //find car in garage
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav/ul/li[5]/a")).click();
        sleep(500);

        webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/div/table/tbody"));
        List<WebElement> garageTable = webElement.findElements(By.tagName("tr"));
        int size = garageTable.size();
        size--;
        webElement = garageTable.get(size);

        List<WebElement> list = webElement.findElements(By.tagName("th"));

        //check add car in garage
        assertEquals(list.get(0).getText(), carBrand);
        assertEquals(list.get(1).getText(), carModel);
    }

    @Test
    public void addCarModelTest() throws InterruptedException {

        String brand = "BrandTest";
        String model = "ModelTest";

        singIn();
        //select card

        WebElement webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav/ul/li[7]/a"));
        webElement.click();
        //add more informations about model
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[1]/input")).sendKeys(brand);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[2]/input")).sendKeys(model);
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[3]/input")).sendKeys("1");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[4]/input")).sendKeys("1");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/label[5]/input")).sendKeys("1");

        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form/input")).click();

        //check if model is avaliable
        webElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/aside/nav/ul/li[6]/a"));
        webElement.click();
        sleep(500);
        webElement = webElement.findElement(By.xpath("//*[@id=\"root\"]/div/main/section/form"));
        List<WebElement> webElementList = webElement.findElements(By.tagName("option"));

        assertTrue(webElementList.stream().anyMatch(item -> (brand + "  " + model).equals(item.getText())));
    }
}

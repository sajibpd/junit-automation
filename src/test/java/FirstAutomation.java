import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.jspecify.annotations.Nullable;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class FirstAutomation {
   WebDriver driver;

    @Before
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

    }

    @Test
    public void getTitle(){
        driver.get("https://demoqa.com/");
        String title_actual=driver.getTitle();
        String title_expected="DEMOQA";
        //System.out.println(title_actual);
        Assert.assertEquals(title_actual,title_expected);
    }
    @Test
    public void checkIfImageExists(){
        driver.get("https://demoqa.com/");
        boolean status=driver.findElement(By.xpath("//body/div[@id='app']/div[1]/div[1]/div[1]/a[1]/img[1]")).isDisplayed();
        //System.out.println(status);
        Assert.assertTrue(status);
    }
    @Test
    public void submitForm(){
        driver.get("https://demoqa.com/text-box");
      //  driver.findElement(By.id("userName")).sendKeys("Test User");  // using id
      //  driver.findElement(By.cssSelector("[type=text")).sendKeys("Test User");  // using type
        List<WebElement> formControl=driver.findElements(By.className("form-control")); //using class name
        formControl.get(0).sendKeys("Test User");
        formControl.get(1).sendKeys("testuser@hotmail.com");
       // driver.findElement(By.id("userEmail")).sendKeys("testuser@hotmail.com");
       // driver.findElement(By.id("submit")).click();
        WebElement txtCurrentAddress=driver.findElement(By.cssSelector("[placeholder='Current Address']"));  // using css property
        txtCurrentAddress.sendKeys("Dhaka");
        List <WebElement> btnSubmit=driver.findElements(By.tagName("button"));  //using tag name
        // scrolldown
        JavascriptExecutor js=(JavascriptExecutor) driver;
       // js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        js.executeScript("window.scrollBy(0,500)");
        btnSubmit.get(1).click();
        String name_actual=driver.findElement(By.id("name")).getText();
        String name_expected="Test User";
        Assert.assertTrue(name_actual.contains(name_expected));


    }
    @Test
    public void clickOnButtons(){
        driver.get("https://demoqa.com/buttons");
        List<WebElement> buttons=driver.findElements(By.cssSelector("[type=button]"));
        Actions actions=new Actions(driver);
        actions.doubleClick(buttons.get(1)).perform();
        actions.contextClick(buttons.get(2)).perform();
        actions.click(buttons.get(3)).perform();


        String actual_message1=driver.findElement(By.id("doubleClickMessage")).getText();
        String expected_message1="double click";
        String actual_message2=driver.findElement(By.id("rightClickMessage")).getText();
        String expected_message2="right click";

        Assert.assertTrue(actual_message1.contains(expected_message1));
        Assert.assertTrue(actual_message2.contains(expected_message2));
    }
@Test
    public void alert() throws InterruptedException {
        driver.get("https://demoqa.com/alerts");

     /*    driver.findElement(By.id("alertButton")).click();
        Thread.sleep(2000);  // to slow down the alert option. otherwise it'll disappear so fast
        driver.switchTo().alert().accept();

        driver.findElement(By.id("confirmButton")).click();
       // driver.switchTo().alert().accept();
        driver.switchTo().alert().dismiss();  */

    driver.findElement(By.id("promtButton")).click();
    driver.switchTo().alert().sendKeys("Hajarta kobita likte pari tomar jonno");
    Thread.sleep(2000);
    driver.switchTo().alert().accept();

    }
   @Test
    public void selectDate(){
        driver.get("https://demoqa.com/date-picker");
        WebElement calendar=driver.findElement(By.id("datePickerMonthYearInput"));
        calendar.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        calendar.sendKeys("01/07/1995");
        calendar.sendKeys(Keys.ENTER);

    }
   @Test
    public void selectDropdownClassic(){
        driver.get("https://demoqa.com/select-menu");
        Select select=new Select(driver.findElement(By.id("oldSelectMenu")));
        select.selectByValue("3");
       // select.selectByIndex(5);
       // select.selectByVisibleText("Green");
    }
    @Test
    public void selectDropdown(){
        driver.get("https://demoqa.com/select-menu");
        Select color=new Select(driver.findElement(By.id("oldSelectMenu")));
        color.selectByValue("1");

        Select cars=new Select(driver.findElement(By.id("cars")));
        if(cars.isMultiple()){
            cars.selectByValue("volvo");
            cars.selectByValue("audi");
        }
    }

    @Test
    public void keyboardEvents(){
        driver.get("https://www.google.com");
        WebElement searchElement=driver.findElement(By.name("q"));
      /*  Actions action=new Actions(driver);
        action.moveToElement(searchElement);
        action.keyDown(Keys.SHIFT);
        action.sendKeys("sajib prodhan");
        action.keyUp(Keys.SHIFT);
        action.perform(); */

        // we also can do this way
        Actions action=new Actions(driver);
        action.moveToElement(searchElement)
        .keyDown(Keys.SHIFT)
        .sendKeys("sajib prodhan")
        .keyUp(Keys.SHIFT)
        .perform();
    }

    @Test
    public void uploadImage(){
        driver.get("https://demoqa.com/upload-download");
        WebElement uploadElement=driver.findElement(By.id("uploadFile"));
        uploadElement.sendKeys("G:\\ab.jpg");
        String test=driver.findElement(By.id("uploadedFilePath")).getText();
        Assert.assertTrue(test.contains("ab.jpg"));
    }

    @Test
    public void downloadFile(){
        driver.get("https://demoqa.com/upload-download");
        driver.findElement(By.id("downloadButton")).click();

    }

    @Test
    public void takeScreenShot() throws IOException {
        driver.get("https://demoqa.com");
        File screenShotFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String time= new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-aa").format(new Date());
        String fileWithPath="./src/test/resources/screenshots/"+time+ ".png";
        File destFile=new File(fileWithPath);
        FileUtils.copyFile(screenShotFile,destFile);
    }

    @Test
    public void handleMultipleTabs() throws InterruptedException {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("tabButton")).click();
        Thread.sleep(3000);
        ArrayList<String> w = new ArrayList(driver.getWindowHandles());

        //switch to open tab
        driver.switchTo().window(w.get(1));
        String text=driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertEquals(text,"This is a sample page");
        driver.close();
        driver.switchTo().window(w.get(0));
        driver.findElement(By.id("messageWindowButton")).click();

    }

    @Test
    public void handleChildWindow()  {
        driver.get("https://demoqa.com/browser-windows");
        driver.findElement(By.id("windowButton")).click();

        String mainWindowHandle  = driver.getWindowHandle();
        Set<String> allWindowHandles= driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();

        while (iterator.hasNext()){
            String childWindow = iterator.next();

            if (!mainWindowHandle.equalsIgnoreCase(childWindow)){
                driver.switchTo().window(childWindow);
                String text = driver.findElement(By.id("sampleHeading")).getText();
                Assert.assertTrue(text.contains("This is a sample page"));
            }
        }

    }

    @Test
    public void scrapDataFromWebTable()  {
        driver.get("https://demoqa.com/webtables");
        WebElement table= driver.findElement(By.className("rt-tbody"));
        List<WebElement> allRows= table.findElements(By.className("rt-tr"));
        int i=0;

        for(WebElement row : allRows){
            List<WebElement> columns = row.findElements(By.className("rt-td"));

            for (WebElement cells : columns){
                System.out.print(cells.getText()+"     ");
                i++;
            }
            System.out.println();
        }
    }

    @Test
    public void handleIFrame()  {
        driver.get("https://demoqa.com/frames");
        driver.switchTo().frame("frame1");  // Here frame id=frame1
        String text= driver.findElement(By.id("sampleHeading")).getText();
        Assert.assertTrue(text.contains("This is a sample page"));
        driver.switchTo().defaultContent();
    }

    @After
    public void closeBrowser() throws InterruptedException {
         //Thread.sleep(7000);
        // driver.quit(); // to close the browser
       // driver.close(); // to close single tab
    }

}
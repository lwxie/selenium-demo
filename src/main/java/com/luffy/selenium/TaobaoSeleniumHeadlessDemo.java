package com.luffy.selenium;



import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

/**
 * <p>selenium 成功绕过淘宝登录反爬机制(headless模式)<p/>
 * <ref>https://blog.csdn.net/xlw_like/article/details/107937011#comments_13042832<ref/>
 *
 * @author 吃货路飞
 * @date 2020/8/11 16:24
 */
public class TaobaoSeleniumHeadlessDemo {
    private final static String CHROME_DRIVER_NAME = "webdriver.chrome.driver";
    private final static String CHROME_DRIVER_PATH = "C:\\drive\\chromedriver.exe";
    private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";
    private final static String LOGIN_URL = "https://login.taobao.com/member/login.jhtml?style=mini";
    private final static String LOGIN_NAME = "66666666666";
    private final static String LOGIN_PWD = "66666666666";

    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty(CHROME_DRIVER_NAME, CHROME_DRIVER_PATH);
        ChromeOptions option = new ChromeOptions();
        //去掉chrome 正受到自动测试软件的控制
        option.addArguments("disable-infobars");
        option.addArguments("--headless");
        option.addArguments("--window-size=1920,1050");
        option.addArguments("user-agent=" + USER_AGENT);
        //设置开发者模式启动
        option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        ChromeDriver driver = new ChromeDriver(option);
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("source", "Object.defineProperty(navigator, 'webdriver', {get: () => undefined }); ");
        driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", map);
        driver.get(LOGIN_URL);
        driver.manage().window().maximize();
        Thread.sleep(500);
        driver.findElement(By.xpath("//*[@id=\"fm-login-id\"]")).sendKeys(LOGIN_NAME);
        driver.findElement(By.xpath("//*[@id=\"fm-login-password\"]")).sendKeys(LOGIN_PWD);
        screenshot(driver);

        if (!Strings.isNullOrEmpty(driver.findElementById("nc_1__scale_text").getText())) {
            //headless模式 会出现滑块
            //滑块控件元素
            WebElement nc_1_n1z = driver.findElementById("nc_1_n1z");
            //整个拖拽框的控件元素
            WebElement nc_1__scale_text = driver.findElementById("nc_1__scale_text");
            //拖拽的宽度即x的距离
            int x = nc_1__scale_text.getSize().getWidth();
            System.out.println(x);
            //拖拽的高度即y的距离
            int y = nc_1__scale_text.getSize().getHeight();
            System.out.println(y);
            Actions actions = new Actions(driver);
            actions.dragAndDropBy(nc_1_n1z, x, y).perform();
            Thread.sleep(2000);
            screenshot(driver);
        }
        driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[4]/button")).click();
        Thread.sleep(6000);
        screenshot(driver);
        System.out.println(driver.getTitle());
        driver.close();
    }

    private static void screenshot(ChromeDriver driver) throws IOException {
        //调用截图方法 ,指定了OutputType.FILE做为参数传递给getScreenshotAs()方法，其含义是将截取的屏幕以文件形式返回
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String name = "selenium-" + RandomStringUtils.randomAlphanumeric(6) + ".jpg";
        FileUtils.copyFile(srcFile, new File("D:\\" + name + ""));

    }
}

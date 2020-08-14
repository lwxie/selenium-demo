package com.luffy.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;

/**
 * <p>selenium 成功绕过淘宝登录反爬机制<p/>
 * <ref>https://blog.csdn.net/xlw_like/article/details/107939072<ref/>
 *
 * @author 吃货路飞
 * @date 2020/8/11 15:21
 */
public class TaobaoSeleniumDemo {
    private final static String CHROME_DRIVER_NAME = "webdriver.chrome.driver";
    private final static String CHROME_DRIVER_PATH = "C:\\drive\\chromedriver.exe";
    private final static String LOGIN_NAME = "66666666666";
    private final static String LOGIN_PWD = "66666666666";

    public static void main(String[] args) throws InterruptedException {
        System.setProperty(CHROME_DRIVER_NAME, CHROME_DRIVER_PATH);
        ChromeOptions option = new ChromeOptions();
        //去掉chrome 正受到自动测试软件的控制
        option.addArguments("disable-infobars");
        //开启开发者模式
        option.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver driver = new ChromeDriver(option);
        driver.manage().window().maximize();
        driver.get("https://login.taobao.com/member/login.jhtml?style=mini");
        driver.findElement(By.xpath("//*[@id=\"fm-login-id\"]")).sendKeys(LOGIN_NAME);
        driver.findElement(By.xpath("//*[@id=\"fm-login-password\"]")).sendKeys(LOGIN_PWD);
        driver.findElement(By.xpath("//*[@id=\"login-form\"]/div[4]/button")).click();
        Thread.sleep(3000);
        driver.getTitle();
        driver.close();
    }
}

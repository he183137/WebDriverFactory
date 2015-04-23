package com.ruijie.AutoTest.driver;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.opera.core.systems.OperaDriver;

abstract class WebDriverFactoryInternal { 
    public abstract WebDriver getDriver(String hub, Capabilities capabilities,FirefoxProfile ifirefoxProfile);
    public abstract void dismissDriver(WebDriver driver);
    public abstract void dismissAll();
    public abstract boolean isEmpty();

    private String defaultHub = null;
    private FirefoxProfile defaultFirefoxProfile =null;

    public void setDefaultHub(String defaultHub) {
      this.defaultHub = defaultHub;
    }

    public WebDriver getDriver(Capabilities capabilities) {
      return getDriver(defaultHub, capabilities,defaultFirefoxProfile);
    }
    
    public WebDriver getDriver(Capabilities capabilities,FirefoxProfile firefoxProfile){
        return getDriver(defaultHub, capabilities,firefoxProfile);
    }

    protected static String createKey(Capabilities capabilities, String hub) {
      return capabilities.toString() + ":" + hub;
    }

    protected static WebDriver newDriver(String hub, Capabilities capabilities,FirefoxProfile firefoxProfile) {
      return (hub == null)
          ? createLocalDriver(capabilities,firefoxProfile)
          : createRemoteDriver(hub, capabilities);
    }

    private static WebDriver createRemoteDriver(String hub, Capabilities capabilities) {
      try {
        return new RemoteWebDriver(new URL(hub), capabilities);
      } catch (MalformedURLException e) {
        e.printStackTrace();
        throw new Error("Could not connect to WebDriver hub", e);
      }
    }
   
    
    private static WebDriver createLocalDriver(Capabilities capabilities,FirefoxProfile firefoxProfile) {
      String browserType = capabilities.getBrowserName();
      if (browserType.equals(BrowserType.FIREFOX)){
         if(firefoxProfile!=null){
           return new FirefoxDriver(firefoxProfile);
         }     
         return new FirefoxDriver(capabilities);
      }
      if (browserType.equals(BrowserType.IE))
        return new InternetExplorerDriver(capabilities);
      if (browserType.equals(BrowserType.CHROME))
        return new ChromeDriver(capabilities);
      if (browserType.equals(BrowserType.OPERA))
        return new OperaDriver(capabilities);
      if (browserType.equals(BrowserType.SAFARI))
        return new SafariDriver(capabilities);
      if (browserType.equals(BrowserType.PHANTOMJS))
        return new PhantomJSDriver(capabilities);
      if (browserType.equals(BrowserType.HTMLUNIT))
        return new HtmlUnitDriver(capabilities);

      try {
        Class<?> driverClass = WebDriverFactoryInternal.class.getClassLoader().loadClass(browserType);
        Constructor<?> constructor = driverClass.getConstructor(Capabilities.class);
        return (WebDriver) constructor.newInstance(capabilities);
      } catch (Exception e) {
        throw new Error("Unrecognized browser type: " + browserType);
      }
    }

}

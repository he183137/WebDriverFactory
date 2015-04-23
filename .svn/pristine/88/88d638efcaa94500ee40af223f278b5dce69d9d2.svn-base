package com.ruijie.AutoTest.driver;


import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

class SingletonStorage extends WebDriverFactoryInternal {

  private String key;
  private WebDriver driver;

  @Override
  public WebDriver getDriver(String hub, Capabilities capabilities,FirefoxProfile firefoxProfile) {
    String newKey = createKey(capabilities, hub);
    if (driver == null) {
      createNewDriver(hub, capabilities,firefoxProfile);

    } else {
      if (!newKey.equals(key)) {
        // A different flavour of WebDriver is required
        dismissDriver();
        createNewDriver(hub, capabilities,firefoxProfile);

      } else {
        // Check the browser is alive
        try {
          driver.getCurrentUrl();
        } catch (Throwable t) {
          createNewDriver(hub, capabilities,firefoxProfile);
        }

      }
    }

    return driver;
  }

  @Override
  public void dismissDriver(WebDriver driver) {
    if (driver != this.driver) {
      throw new Error("The driver is not owned by the factory: " + driver);
    }
    dismissDriver();
  }

  @Override
  public void dismissAll() {
    dismissDriver();
  }

  @Override
  public boolean isEmpty() {
    return driver == null;
  }

  private void createNewDriver(String hub, Capabilities capabilities,FirefoxProfile firefoxProfile) {
    String newKey = createKey(capabilities, hub);
    driver = newDriver(hub, capabilities,firefoxProfile);
    key = newKey;
  }

  private void dismissDriver() {
    if (driver != null) {
      driver.quit();
      driver = null;
      key = null;
    }
  }
}

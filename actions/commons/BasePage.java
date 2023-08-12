package commons;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;

public class BasePage {

    public static BasePage getBasePage() {
        return new BasePage();
    }

    public void openPageUrl(WebDriver driver, String urlPage) {
        driver.get(urlPage);
    }

    public String getPageTitle(WebDriver driver) {
        return driver.getTitle();
    }

    public String getCurrentPageUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    public String getPageResource(WebDriver driver) {
        return driver.getPageSource();
    }

    public void backToPage(WebDriver driver) {
        driver.navigate().back();
    }

    public void forwardToPage(WebDriver driver) {
        driver.navigate().forward();
    }

    public void refreshPageCurrent(WebDriver driver) {
        driver.navigate().refresh();
    }

    public Alert waitAlertPresence(WebDriver driver) {
        return new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.alertIsPresent());
    }

    public void acceptToAlert(WebDriver driver) {
        waitAlertPresence(driver).accept();
    }

    public void cancelToAlert(WebDriver driver) {
        waitAlertPresence(driver).dismiss();
    }

    public String getTextAlert(WebDriver driver) {
        return waitAlertPresence(driver).getText();
    }

    public void sendkeyToAlert(WebDriver driver, String valueToSend) {
        waitAlertPresence(driver).sendKeys(valueToSend);
    }

    public void switchToWindowByID(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindow : allWindows) {
            if (!runWindow.equals(parentID)) {
                driver.switchTo().window(runWindow);
                break;
            }
        }
    }

    public void switchToWindowByTitle(WebDriver driver, String title) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindows : allWindows) {
            driver.switchTo().window(runWindows);
            String currentWin = driver.getTitle();
            if (currentWin.equals(title)) {
                break;
            }
        }
    }

    public void closeAllWindowsWithoutParent(WebDriver driver, String parentID) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String runWindows : allWindows) {
            if (!runWindows.equals(parentID)) {
                driver.switchTo().window(runWindows);
                driver.close();
            }
        }
        driver.switchTo().window(parentID);
    }

    public void sleepInSecond(long timeInSecond) {
        try {
            Thread.sleep(timeInSecond * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public By getByLocator(String locator) {
        By by = null;
        if (locator.startsWith("xpath=") || locator.startsWith("Xpath=") || locator.startsWith("XPATH=") || locator.startsWith("xPath=")) {
            by = By.xpath(locator.substring(6));
        } else if (locator.startsWith("css=") || locator.startsWith("Css=") || locator.startsWith("CSS=")) {
            by = By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("id=") || locator.startsWith("Id=") || locator.startsWith("ID=")) {
            by = By.id(locator.substring(3));
        } else if (locator.startsWith("class=") || locator.startsWith("Class=") || locator.startsWith("CLASS=")) {
            by = By.className(locator.substring(6));
        } else if (locator.startsWith("name=") || locator.startsWith("Name=") || locator.startsWith("NAME=")) {
            by = By.className(locator.substring(5));
        } else {
            throw new RuntimeException("By Locator is invalid");
        }

        return by;
    }

    public By getElementByXpath(String locator) {
        return By.xpath(locator);
    }

    public WebElement getWebElement(WebDriver driver, String locator) {
        return driver.findElement(getByLocator(locator));
    }

    public List<WebElement> getListWebElement(WebDriver driver, String locator) {
        return driver.findElements(getByLocator(locator));
    }

    public void clickToElement(WebDriver driver, String locator) {
        getWebElement(driver, locator).click();
    }

    public void sendkeyToElement(WebDriver driver, String locator, String valueToSend) {
        getWebElement(driver, locator).clear();
        getWebElement(driver, locator).sendKeys(valueToSend);
    }

    public void selectItemInDropdown(WebDriver driver, String locator, String valueToSelect) {
        new Select(getWebElement(driver, locator)).selectByVisibleText(valueToSelect);
    }

    public void getSelectedItemInDropdown(WebDriver driver, String locator) {
        new Select(getWebElement(driver, locator)).getFirstSelectedOption();
    }

    public boolean isDropdownMultiple(WebDriver driver, String locator) {
        return new Select(getWebElement(driver, locator)).isMultiple();
    }

    public void selectItemInCustomDropdown(WebDriver driver, String parentLocator, String childItemLocator, String expectedItem) {
        getWebElement(driver, parentLocator).click();
        sleepInSecond(1);

        List<WebElement> allItems = new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(childItemLocator)));

        for (WebElement item : allItems) {
            if (item.getText().trim().equals(expectedItem)) {

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", item);
                sleepInSecond(1);

                item.click();
                sleepInSecond(1);
                break;
            }
        }
    }

    public String getAttributeValue(WebDriver driver, String locator, String attributeName) {
        return getWebElement(driver, locator).getAttribute(attributeName);
    }

    public String getTextElement(WebDriver driver, String locator) {
        return getWebElement(driver, locator).getText();
    }

    public String getCssValue(WebDriver driver, String locator, String CssValue) {
        return getWebElement(driver, locator).getCssValue(CssValue);
    }

    public String convertRGBAToHexaColor(WebDriver driver, String locator) {
        return Color.fromString(getCssValue(driver, locator, "background-color")).asHex().toUpperCase();
    }

    public int getListElementSize(WebDriver driver, String locator) {
        return getListWebElement(driver, locator).size();
    }

    public void checkTheCheckBoxOrRadio(WebDriver driver, String locator) {
        if (!getWebElement(driver, locator).isSelected()) {
            clickToElement(driver, locator);
        }
    }

    public void uncheckTheCheckBox(WebDriver driver, String locator) {
        if (getWebElement(driver, locator).isSelected()) {
            clickToElement(driver, locator);
        }
    }

    public boolean isElementDisplayed(WebDriver driver, String locator) {
        return getWebElement(driver, locator).isDisplayed();
    }

    public boolean isElementSelected(WebDriver driver, String locator) {
        return getWebElement(driver, locator).isSelected();
    }

    public boolean isElementEnabled(WebDriver driver, String locator) {
        return getWebElement(driver, locator).isEnabled();
    }

    public void swithToIframe(WebDriver driver, String locator) {
        new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public void swithToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    public void doubleClickToElement(WebDriver driver, String locator) {
        new Actions(driver).doubleClick(getWebElement(driver, locator)).perform();
    }

    public void moveToElement(WebDriver driver, String locator) {
        new Actions(driver).moveToElement(getWebElement(driver, locator)).perform();
    }

    public void rightClick(WebDriver driver, String locator) {
        new Actions(driver).contextClick(getWebElement(driver, locator)).perform();
    }

    public void drapAndDrop(WebDriver driver, String sourceLocator, String targetLocator) {
        new Actions(driver).dragAndDrop(getWebElement(driver, sourceLocator), getWebElement(driver, targetLocator)).perform();
    }

    public void sendKeyboardToElement(WebDriver driver, String locator, Keys keys) {
        new Actions(driver).sendKeys(getWebElement(driver, locator), keys).perform();
    }

    public Object executeForBrowser(WebDriver driver, String javaScript) {
        return ((JavascriptExecutor) driver).executeScript(javaScript);
    }

    public String getInnerText(WebDriver driver) {
        return (String) ((JavascriptExecutor) driver).executeScript("return document.documentElement.innerText;");
    }

    public boolean areExpectedTextInInnerText(WebDriver driver, String textExpected) {
        String textActual = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0]");
        return textActual.equals(textExpected);
    }

    public void scrollToBottomPage(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void navigateToUrlByJS(WebDriver driver, String url) {
        ((JavascriptExecutor) driver).executeScript("window.location = '" + url + "'");
    }

    public void highlightElement(WebDriver driver, String locator) {
        WebElement element = getWebElement(driver, locator);
        String originalStyle = element.getAttribute("style");
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
                "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public void clickToElementByJS(WebDriver driver, String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getWebElement(driver, locator));
    }

    public void scrollToElement(WebDriver driver, String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getWebElement(driver, locator));
    }

    public void sendkeyToElementByJS(WebDriver driver, String locator, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '" + value + "')", getWebElement(driver, locator));
    }

    public void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getWebElement(driver, locator));
    }

    public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, GlobalContants.LONG_TIME);
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    return true;
                }
            }
        };
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }

    public String getElementValidationMessage(WebDriver driver, String locator) {
        return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage;", getWebElement(driver, locator));
    }

    public boolean isImageLoaded(WebDriver driver, String locator) {
        boolean status = (boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
                getWebElement(driver, locator));
        return status;
    }

    public void waitForElementVisible(WebDriver driver, String locator) {
        new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }

    public void waitForListElementVisible(WebDriver driver, String locator) {
        new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.visibilityOfAllElements(getListWebElement(driver, locator)));
    }

    public void waitForElementClickable(WebDriver driver, String locator) {
        new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
    }

    public void waitForElementInvisible(WebDriver driver, String locator) {
        new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }

    public void waitForListElementInvisible(WebDriver driver, String locator) {
        new WebDriverWait(driver, GlobalContants.LONG_TIME).until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(driver, locator)));
    }

    public boolean isPageLoadedSuccess(WebDriver driver) {
        WebDriverWait explicitWait = new WebDriverWait(driver, GlobalContants.LONG_TIME);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) jsExecutor.executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };

        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
            }
        };
        return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
    }


}

package com.lpl.issue.rpa.invoiceplane.page;

import com.workfusion.bot.service.SecureEntryDTO;
import com.lpl.issue.rpa.RobotDriverWrapper;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;

public class LoginPage extends RobotDriverWrapper {

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement submit;

    @FindBy(xpath = "//*[self::div[@id='main-area'] or self::div[@id='login']/div[contains(@class,'alert')]]")
    private WebElement loginFailed;

    @FindBy(xpath = "//div[@id='login']/div[contains(@class,'alert')]")
    private WebElement loginFailedMessage;

    public LoginPage(Logger logger) {
        super(logger);
    }

    public MainPage login(SecureEntryDTO invoicePlaneCred) {
        email.click();
        email.clear();
        email.sendKeys(invoicePlaneCred.getKey());

        password.click();
        password.clear();
        password.sendKeys(invoicePlaneCred.getValue());

        submit.click();

        try {
            String actualId = loginFailed.getAttribute("id");
            logger.debug(actualId);
            if (!"main-area".equalsIgnoreCase(actualId)) {
                throw new RuntimeException(loginFailedMessage.getText() + "\n" + "User Id: " + invoicePlaneCred.getKey());
            }
        } catch (TimeoutException e) {
            logger.debug("Unkown error during Invoice Place authorisation process.");
            throw new RuntimeException();
        }

        return new MainPage(logger);
    }

}
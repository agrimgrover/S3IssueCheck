package com.lpl.issue.rpa.invoiceplane;

import org.slf4j.Logger;

import com.lpl.issue.rpa.RobotDriverWrapper;
import com.lpl.issue.rpa.invoiceplane.page.LoginPage;

import java.util.concurrent.TimeUnit;

import static com.workfusion.rpa.helpers.RPA.driver;

public class InvoicePlaneClient extends RobotDriverWrapper {

    public static final String HTTP_INVOICEPLANE_WORKFUSION_COM = "http://invoiceplane.workfusion.com";

    public InvoicePlaneClient(Logger logger) {
        super(logger);
        initDriver();
    }

    private void initDriver() {
        driver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS).pageLoadTimeout(90, TimeUnit.SECONDS);
        driver().manage().deleteAllCookies();
    }

    public LoginPage getLoginPage() {
        driver().navigate().to(HTTP_INVOICEPLANE_WORKFUSION_COM);
        return new LoginPage(logger);
    }

}

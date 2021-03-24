package com.lpl.issue.rpa;

import com.workfusion.rpa.helpers.RPA;

import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

public class RobotDriverWrapper {

    protected final Logger logger;

    public RobotDriverWrapper(final Logger logger) {
        PageFactory.initElements(RPA.driver(), this);
        this.logger = logger;
    }

}
package com.lpl.issue.app;

import com.workfusion.odf.core.App;
import com.workfusion.odf.core.Module;
import groovy.lang.Binding;

import java.util.List;

public class AppExample extends App {

    protected AppExample(Binding context, List<Module> additionalModules, List<Module> overrideModules, Object injectContext) {
        super(context, additionalModules, overrideModules, injectContext);
    }

    public static AppBuilderExample init(Binding binding) {
        return new AppBuilderExample(binding);
    }
}

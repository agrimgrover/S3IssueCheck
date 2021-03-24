package com.lpl.issue.module;

import com.workfusion.odf.core.Module;
import com.workfusion.rpa.core.BindingUtils;
import com.workfusion.rpa.core.plugin.PluginAdapterFactory;
import com.workfusion.rpa.core.plugin.s3.S3PluginAdapter;
import com.workfusion.rpa.core.storage.S3Manager;
import groovy.lang.Binding;
import org.bouncycastle.crypto.tls.SSL3Mac;
import org.codejargon.feather.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

public class ModuleExample implements Module {


    private final Binding context;

    public ModuleExample(Binding context) {
        this.context = context;
    }

    @Provides
    @Named("binding")
    public Binding binding() {
        return context;
    }

    @Provides
    @Named("defaultS3Bucket")
    public String s3Bucket() {
        return "lpl";
    }

    @Provides
    @Singleton
    public S3PluginAdapter s3PluginAdapter() {
        return(S3PluginAdapter) PluginAdapterFactory.getInstance().getPluginAdapter(PluginAdapterFactory.PluginsEnum.S3);
    }

}

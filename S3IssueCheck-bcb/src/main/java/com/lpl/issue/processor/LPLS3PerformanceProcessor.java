package com.lpl.issue.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.workfusion.rpa.core.BindingUtils;
import com.workfusion.rpa.core.plugin.PluginAdapterFactory;
import com.workfusion.rpa.core.plugin.s3.S3PluginAdapter;
import com.workfusion.rpa.core.plugin.s3.S3PluginWebharvestAdapter;
import com.workfusion.rpa.core.storage.S3Manager;
import groovy.lang.Binding;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.lpl.issue.utils.Cipher;
import com.workfusion.odf.api.domain.Document;
import com.workfusion.odf.api.domain.Transaction;
import com.workfusion.odf.processor.TransactionProcessor;
import org.springframework.http.MediaType;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LPLS3PerformanceProcessor implements TransactionProcessor {

    private final static String version = "v1.014";

    private S3PluginAdapter s3PluginAdapter;
    private final Logger logger;
    private final Binding binding;
    private S3Manager s3Manager;

    private S3Manager s3Manager() {
        return new S3Manager(logger, getDefaultS3Bucket(), BindingUtils.getSys(binding));
    }

    private String getDefaultS3Bucket(){
        return BindingUtils.getInputAsMap(binding,"bucketname").getOrDefault("bucketname","lpl-data");
    }

    public S3PluginAdapter s3PluginAdapter() {
        S3PluginAdapter s3PluginAdapter = PluginAdapterFactory.getInstance().getPluginAdapter(PluginAdapterFactory.PluginsEnum.S3);
        s3PluginAdapter.getCommonS3Attributes().put(S3PluginWebharvestAdapter.ATTRIBUTE_BUCKET, getDefaultS3Bucket());
        return s3PluginAdapter;
    }


    private byte[] getS3File(String url) {
        return this.s3PluginAdapter.get(url);
    }

    @Override
    public Transaction transform(Transaction transaction) {
        this.s3PluginAdapter = s3PluginAdapter();
        byte[] inputStream = getS3File(BindingUtils.getInputAsMap(binding, "s3file").getOrDefault("s3file", "https://ucs-1016ga-wfaw-10037-minio-lb1.workfusion.com/lpl-data/F75.pdf"));
        generateAndUploadDocument(inputStream);
        return transaction;
    }

    private String generateAndUploadDocument(byte[] data) {
        boolean isFolder = Boolean.valueOf(BindingUtils.getInputAsMap(binding,"isfolder").getOrDefault("isfolder","false"));
        String path = uuid()+".pdf";
        if(isFolder){
            path = uuid()+"/"+uuid()+".pdf";
        }
        return s3PluginAdapter().put(data,path,
                CannedAccessControlList.PublicRead,
                MediaType.APPLICATION_PDF_VALUE ,
                "inline",
                (long)Integer.MAX_VALUE).getDirectUrl();
    }

    private String uuid() {
        return UUID.randomUUID().toString();
    }
}

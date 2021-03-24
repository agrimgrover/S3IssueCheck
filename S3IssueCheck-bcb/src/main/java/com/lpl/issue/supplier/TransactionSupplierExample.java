package com.lpl.issue.supplier;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.workfusion.odf.api.connector.TransactionSupplier;
import com.workfusion.odf.api.domain.Document;
import com.workfusion.odf.api.domain.Transaction;
import com.workfusion.rpa.core.BindingUtils;
import com.workfusion.rpa.core.plugin.PluginAdapterFactory;
import com.workfusion.rpa.core.plugin.s3.S3PluginAdapter;
import com.workfusion.rpa.core.plugin.s3.S3PluginWebharvestAdapter;
import com.workfusion.rpa.core.storage.S3Manager;
import groovy.lang.Binding;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.MediaType;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransactionSupplierExample implements TransactionSupplier {

    private final static String version = "v1.012";

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
//        return this.s3Manager.getFile("doc-upload", url);
    }

    @Override
    public Collection<Transaction> get() {
        logger.info(" STarted the Bot task -- Demo {}", version);
        this.s3Manager = s3Manager();
        this.s3PluginAdapter = s3PluginAdapter();
        Collection<Transaction> transactions = new ArrayList<>();
        int counter = Integer.parseInt(BindingUtils.getInputAsMap(binding, "counter").getOrDefault("counter", "100").toString());
        logger.info("counter {}", counter);

        byte[] inputStream = getS3File(BindingUtils.getInputAsMap(binding, "s3file").getOrDefault("s3file", "https://ucs-1016ga-wfaw-10037-minio-lb1.workfusion.com/lpl-data/F75.pdf"));
        logger.info(" Uploading Start  -- {} with file length {}", version,inputStream.length);
        for (int i = 0; i <= counter; i++) {
            generateAndUploadDocument(inputStream);
        }
        logger.info("Upload finished {} documents, each of length {} ", counter, inputStream.length);

        Transaction transaction = new Transaction();
        transaction.setId(uuid());
        transaction.setDocs(Arrays.asList(createDocument()));
        transactions.add(transaction);
        logger.info("Transaction ended");
        return transactions;
    }

    /*private void listFilesInS3(){
        List list = this.s3PluginAdapter.listEntries(
                BindingUtils.getInputAsMap(binding,"link")
                        .getOrDefault("link",
                                "https://ucs-1016ga-wfaw-10037-minio-lb1.workfusion.com/minio/vds-models/"));
        logger.info("Number of files in lpl s3 bucket {}", list.size());
    }*/

    private Document createDocument() {
        Document document = new Document();
        document.setId(uuid());
        document.setName("Example Document");
        //document.setTextLink(generateAndUploadDocument());
        document.setTextLink(generateDocument());
        return document;
    }

    /**
     * This method generates document content without storing it to external storage
     * Document content will be available for processing on next steps
     * for next steps
     *
     * @return link to document stored on S3
     */
    private String generateDocument() {
        return "Document content or link to document should be here";
    }


    /**
     * This method will store document to S3 and return link to this document
     * for next steps
     *
     * @return link to document stored on S3
     */
    private String generateAndUploadDocument(byte[] data) {
         boolean isFolder = Boolean.valueOf(BindingUtils.getInputAsMap(binding,"isfolder").getOrDefault("isfolder","false"));
         String path = uuid()+".pdf";
         if(isFolder){
             path = uuid()+"/"+uuid()+".pdf";
         }

         //logger.info("data packet {} for version{}",data,version);
         //byte [] d = Base64.getDecoder().decode(new String(data).getBytes(Charset.defaultCharset()));

       return this.s3PluginAdapter.put(data,path,
               CannedAccessControlList.PublicRead,
               MediaType.APPLICATION_PDF_VALUE ,
               "inline",
               (long)Integer.MAX_VALUE).getDirectUrl();

        /*return this.s3Manager.putFile(
                uuid() + "/" + uuid() + ".txt",
                new ByteArrayInputStream("This is test file by Workfusion LPL test".getBytes(StandardCharsets.UTF_8)),
                MediaType.TEXT_PLAIN_VALUE);*/
    }

    private String uuid() {
        return UUID.randomUUID().toString();
    }
}

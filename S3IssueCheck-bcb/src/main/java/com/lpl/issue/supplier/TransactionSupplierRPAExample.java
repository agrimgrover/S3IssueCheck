package com.lpl.issue.supplier;

import com.workfusion.odf.api.connector.TransactionSupplier;
import com.workfusion.odf.api.domain.Document;
import com.workfusion.odf.api.domain.Transaction;
import com.lpl.issue.rpa.invoiceplane.ProductsParserRobot;
import com.workfusion.rpa.core.security.SecurityUtils;

import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class TransactionSupplierRPAExample implements TransactionSupplier {

    private final SecurityUtils securityUtils;
    private final Logger logger;

    /**
     * To enable dependency injection, you need to apply @Inject annotation to your constructor.
     *
     * @param logger        will be injected without any configuration,
     * @param securityUtils injection was configured in com.workfusion.intakequickstart.core.SecurityModule
     */
    @Inject
    public TransactionSupplierRPAExample(final SecurityUtils securityUtils, final Logger logger) {
        this.logger = logger;
        this.securityUtils = securityUtils;
    }

    @Override
    public Collection<Transaction> get() {
        Collection<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setId(uuid());
        transaction.setDocs(createDocuments());
        transactions.add(transaction);

        return transactions;
    }

    private List<Document> createDocuments() {
        final List<Document> collect = new ProductsParserRobot(securityUtils, logger).parseProductsToDocuments();
        return collect;
    }

    private String uuid() {
        return UUID.randomUUID().toString();
    }

}

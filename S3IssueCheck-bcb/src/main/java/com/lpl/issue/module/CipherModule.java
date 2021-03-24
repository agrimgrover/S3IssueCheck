package com.lpl.issue.module;

import org.codejargon.feather.Provides;

import com.lpl.issue.utils.Cipher;
import com.lpl.issue.utils.InMemoryBase64Encoder;
import com.workfusion.odf.core.Module;

public class CipherModule implements Module {

    @Provides
    public Cipher cipher(InMemoryBase64Encoder inMemoryBase64Encoder) {
        return inMemoryBase64Encoder;
    }
}

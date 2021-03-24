package com.lpl.issue.utils;

import org.apache.commons.codec.binary.Base64;

public class InMemoryBase64Encoder implements Cipher {

    @Override
    public byte[] encode(byte[] content) {
        return Base64.encodeBase64(content);
    }

    @Override
    public byte[] decode(byte[] content) {
        return Base64.decodeBase64(content);
    }
}

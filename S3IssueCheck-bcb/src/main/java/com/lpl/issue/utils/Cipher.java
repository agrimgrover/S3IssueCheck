package com.lpl.issue.utils;

public interface Cipher {

    byte[] encode(byte[] content);

    byte[] decode(byte[] content);

}

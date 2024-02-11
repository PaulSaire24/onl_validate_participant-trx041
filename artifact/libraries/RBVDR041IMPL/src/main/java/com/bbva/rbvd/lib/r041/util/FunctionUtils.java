package com.bbva.rbvd.lib.r041.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FunctionUtils {
    private FunctionUtils(){}
    public static String encodeB64(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}

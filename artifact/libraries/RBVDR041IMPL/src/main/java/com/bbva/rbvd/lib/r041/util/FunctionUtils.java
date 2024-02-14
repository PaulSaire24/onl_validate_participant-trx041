package com.bbva.rbvd.lib.r041.util;

import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class FunctionUtils {
    private FunctionUtils(){}
    public static String encodeB64(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static <T> Optional<List<T>> isNotEmptyList(List<T> input) {
        if(!CollectionUtils.isEmpty(input)){
            return Optional.of(input);
        }
        return Optional.empty();
    }
}

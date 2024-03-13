package com.bbva.rbvd.lib.r041.util;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

public class FunctionUtils {
    private FunctionUtils(){}
    public static <T> Optional<List<T>> isNotEmptyList(List<T> input) {
        if(!CollectionUtils.isEmpty(input)){
            return Optional.of(input);
        }
        return Optional.empty();
    }

}

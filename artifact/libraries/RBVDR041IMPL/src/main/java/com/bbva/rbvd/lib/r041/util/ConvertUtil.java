package com.bbva.rbvd.lib.r041.util;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class ConvertUtil {

    public static LocalDate toLocalDate(Date date) {
        if(!Objects.nonNull(date)){
            return null;
        }
        return date.toInstant().atZone(ConstantsUtil.Zone.ZONE_ID_GTM).toLocalDate();
    }

    private ConvertUtil() {
    }
}

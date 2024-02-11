package com.bbva.rbvd.lib.r041.validation;

import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {

    public static boolean isBBVAClient(String clientId){
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_LETTERS) && clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_NUMBERS) && clientId.length()>ConstantsUtil.Numero.CLIENT_BANK_LENGHT);
    }
}

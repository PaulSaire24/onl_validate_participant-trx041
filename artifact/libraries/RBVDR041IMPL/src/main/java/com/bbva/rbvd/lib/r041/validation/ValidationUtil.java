package com.bbva.rbvd.lib.r041.validation;

import com.bbva.rbvd.dto.insrncsale.bo.emision.EntidadBO;
import com.bbva.rbvd.dto.insrncsale.utils.PersonTypeEnum;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class ValidationUtil {

    private ValidationUtil() {
    }
    private static final String RUC_ID = "R";

    public static boolean isBBVAClient(String clientId){
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_LETTERS) && clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_NUMBERS) && clientId.length()>ConstantsUtil.Number.CLIENT_BANK_LENGHT);
    }

    public static PersonTypeEnum getPersonType(EntidadBO person) {
        if (RUC_ID.equalsIgnoreCase(person.getTipoDocumento())){
            if (StringUtils.startsWith(person.getNroDocumento(), "20")) return PersonTypeEnum.JURIDIC;
            else return PersonTypeEnum.NATURAL_WITH_BUSINESS;
        }
        return PersonTypeEnum.NATURAL;
    }
    public static String validateAllVia(String via){
        return !StringUtils.isEmpty(via)?via:ConstantsUtil.RegularExpression.UNSPECIFIED;
    }

    public static String validateSN(String name) {
        if(Objects.isNull(name) || "null".equals(name) || " ".equals(name)){
            return "";
        }else{
            name = name.replace("#","Ñ");
            return name;
        }
    }

    public static int getValueByName(String name){
        ConstantsUtil.Rol[] val = ConstantsUtil.Rol.values();
        for (ConstantsUtil.Rol er: val) {
            if(er.getName().equalsIgnoreCase(name)){
                return er.getValue();
            }
        }
        return 0;
    }


}

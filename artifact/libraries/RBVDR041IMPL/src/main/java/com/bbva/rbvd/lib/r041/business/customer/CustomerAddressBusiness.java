package com.bbva.rbvd.lib.r041.business.customer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.lib.r041.model.AddressBO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CustomerAddressBusiness {

    public AddressBO fillAddress(PEWUResponse persona, StringBuilder stringAddress) {

        AddressBO addressBO = fillAddressUbigeo(persona);

        String addressViaList = fillAddressViaList(persona, addressBO);
        String addressGroupList = fillAddressGroupList(persona, addressViaList, addressBO);

        if(isNull(addressGroupList) && isNull(addressViaList)) {
            addressBO.setTipoVia(ConstantsUtil.PERSONAL_ADDRESS.SIN_ESPECIFICAR);
            addressBO.setNombreVia(ConstantsUtil.PERSONAL_ADDRESS.SIN_ESPECIFICAR);
            addressBO.setNumeroVia(ConstantsUtil.PERSONAL_ADDRESS.SIN_ESPECIFICAR);
            addressBO.setDireccion(ConstantsUtil.PERSONAL_ADDRESS.SIN_ESPECIFICAR);
        }else{
            String addressNumberVia = fillAddressNumberVia(persona, addressBO);

            String fullNameOther = fillAddressOther(persona, stringAddress);

            if (ConstantsUtil.PERSONAL_ADDRESS.NO_EXIST.equals(addressNumberVia) || ConstantsUtil.PERSONAL_ADDRESS.NO_EXIST.equals(fullNameOther)){
                fillAddressAditional(persona, stringAddress);
            }

            getFullDirectionFrom(addressViaList, addressGroupList, addressNumberVia, stringAddress, addressBO);
        }

        return addressBO;
    }

    private  AddressBO fillAddressUbigeo(final PEWUResponse geographicGroups) {
        String ubigeo = StringUtils.defaultString(geographicGroups.getPemsalwu().getCodigod()) +
                StringUtils.defaultString(geographicGroups.getPemsalwu().getCodigop()) +
                StringUtils.defaultString(geographicGroups.getPemsalwu().getCodigdi());

        String department = Optional.ofNullable(geographicGroups.getPemsalw4()).map(pemsalw4 ->
                StringUtils.defaultString(pemsalw4.getDesdept())).orElse(StringUtils.EMPTY);
        String province = Optional.ofNullable(geographicGroups.getPemsalw4()).map(pemsalw4 ->
                StringUtils.defaultString(pemsalw4.getDesprov())).orElse(StringUtils.EMPTY);
        String district = Optional.ofNullable(geographicGroups.getPemsalw4()).map(pemsalw4 ->
                StringUtils.defaultString(pemsalw4.getDesdist())).orElse(StringUtils.EMPTY);

        AddressBO addressBO = AddressBO.Builder.an()
                .departamento(department)
                .provincia(province)
                .distrito(district)
                .ubigeo(ubigeo)
                .build();

        return addressBO;
    }

    private  String fillAddressViaList(PEWUResponse geographicGroupsAddress, AddressBO addressBO) {

        String nombreDir1 = null;
        String viaType = "";
        String viaName = "";

        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getIdendi1())
                && !geographicGroupsAddress.getPemsalwu().getIdendi1().equals("NA")) {
            viaType = geographicGroupsAddress.getPemsalwu().getIdendi1();
            viaName = StringUtils.defaultString(geographicGroupsAddress.getPemsalwu().getNombdi1());

            addressBO.setTipoVia(viaType);
            addressBO.setNombreVia(viaName);

            nombreDir1 = viaType.concat(" ").concat(viaName);
        }

        return nombreDir1;

    }

    private  String fillAddressGroupList(PEWUResponse geographicGroupsAddress, String addressViaList, AddressBO addressBO) {

        String nombreDir2 = null;
        String groupType = "";
        String groupName = "";

        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getIdendi2())
                && !geographicGroupsAddress.getPemsalwu().getIdendi2().equals("NA")){
            groupType = geographicGroupsAddress.getPemsalwu().getIdendi2();
            groupName = StringUtils.defaultString(geographicGroupsAddress.getPemsalwu().getNombdi2());
            nombreDir2 = groupType.concat(" ").concat(groupName);
        }

        if(isNull(addressViaList)) {
            addressBO.setTipoVia(groupType);
            addressBO.setNombreVia(groupName);
        }

        return nombreDir2;

    }

    private  String fillAddressNumberVia(PEWUResponse geographicGroupsAddress, AddressBO addressBO) {
        String numberVia = ConstantsUtil.PERSONAL_ADDRESS.NO_EXIST;
        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getNroext1())){
            addressBO.setNumeroVia(geographicGroupsAddress.getPemsalwu().getNroext1());
            numberVia = geographicGroupsAddress.getPemsalwu().getNroext1();
        }else{
            addressBO.setNumeroVia(ConstantsUtil.PERSONAL_ADDRESS.SIN_ESPECIFICAR);
        }

        return numberVia;

    }

    public  String fillAddressOther(PEWUResponse geographicGroupsAddress, StringBuilder stringAddress) {

        String addressOther;
        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getNroint1())){
            addressOther = geographicGroupsAddress.getPemsalwu().getNroint1();
            stringAddress.append(ConstantsUtil.PERSONAL_ADDRESS.INTERIOR_NUMBER_ID.concat(" ").concat(addressOther));
        }else{
            addressOther = ConstantsUtil.PERSONAL_ADDRESS.NO_EXIST;
        }

        return addressOther;
    }

    public  void fillAddressAditional(PEWUResponse geographicGroupsAddress, StringBuilder stringAddress) {

        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getManzana())){
            appendToAddress(stringAddress, geographicGroupsAddress.getPemsalwu().getManzana());
        }
        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getLote())){
            appendToAddress(stringAddress, geographicGroupsAddress.getPemsalwu().getLote());
        }

    }

    private  void appendToAddress(StringBuilder stringAddress, String toAppend) {
        if (stringAddress.length() > 0 && !stringAddress.toString().endsWith(" ")) {
            stringAddress.append(" ");
        }
        stringAddress.append(toAppend);
    }

    private  String getFullDirectionFrom(String addressViaList, String addressGroupList, String addressNumberVia, StringBuilder stringAddress, AddressBO addressBO) {

        StringBuilder directionForm = new StringBuilder();

        if (nonNull(addressViaList)) {
            directionForm.append(addressViaList);
            if (!ConstantsUtil.PERSONAL_ADDRESS.NO_EXIST.equals(addressNumberVia)) {
                directionForm.append(" ").append(addressNumberVia);
            }
            directionForm.append(", ");
        }

        if (nonNull(addressGroupList)) {
            directionForm.append(addressGroupList).append(" ");
        }

        directionForm.append(stringAddress.toString());

        String finalDirection = directionForm.toString().trim();
        if (!finalDirection.isEmpty()) {
            addressBO.setDireccion(finalDirection);
        }

        return finalDirection;

    }

}

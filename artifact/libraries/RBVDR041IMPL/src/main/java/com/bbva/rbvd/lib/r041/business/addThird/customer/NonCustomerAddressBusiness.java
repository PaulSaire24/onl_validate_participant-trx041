package com.bbva.rbvd.lib.r041.business.addThird.customer;

import com.bbva.rbvd.dto.participant.request.AddressComponentsDTO;
import com.bbva.rbvd.dto.participant.request.AddressesDTO;
import com.bbva.rbvd.lib.r041.model.AddressBO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonCustomerAddressBusiness {


    public AddressBO getAddressBO(List<AddressesDTO> addresses) {
        AddressBO addressBO = new AddressBO();
        if(addresses.size()>0) {
            List<AddressComponentsDTO> addressComponents= addresses.get(0).getLocation().getAddressComponent();
            addressBO.setDireccion(addresses.get(0).getFormattedAddress());
            addressBO.setDistrito(getAddressComponentValue(addressComponents, ConstantsUtil.ADDRESS_LABEL.DISTRICT));
            addressBO.setProvincia(getAddressComponentValue(addressComponents, ConstantsUtil.ADDRESS_LABEL.PROVINCE));
            addressBO.setDepartamento(getAddressComponentValue(addressComponents, ConstantsUtil.ADDRESS_LABEL.DEPARTMENT));
            addressBO.setUbigeo(getAddressComponentValue(addressComponents, ConstantsUtil.ADDRESS_LABEL.UBIGEO));
            addressBO.setTipoVia(filterViaTypeName(addressComponents));
            addressBO.setNumeroVia(getAddressComponentValue(addressComponents, ConstantsUtil.ADDRESS_LABEL.EXTERIOR_NUMBER));
            addressBO.setNombreVia(getAddressComponentValue(addressComponents, ConstantsUtil.ADDRESS_LABEL.STREET));
        }else{
            addressBO.setDistrito(StringUtils.EMPTY);
            addressBO.setProvincia(StringUtils.EMPTY);
            addressBO.setDepartamento(StringUtils.EMPTY);
            addressBO.setUbigeo(StringUtils.EMPTY);
            addressBO.setTipoVia(StringUtils.EMPTY);
            addressBO.setNumeroVia(StringUtils.EMPTY);
            addressBO.setNombreVia(StringUtils.EMPTY);
        }

        return addressBO;
    }
    private String getAddressComponentValue(List<AddressComponentsDTO> addressComponents, String componentType) {
        return addressComponents.stream()
                .filter(component -> componentType.equals(ConstantsUtil.ADDRESS_LABEL.STREET) ?
                        filterViaTypeCondition(component.getComponentTypes().get(0)) :
                        component.getComponentTypes().stream().findFirst().orElse("").equals(componentType))
                .map(AddressComponentsDTO::getName)
                .findFirst()
                .orElse(StringUtils.EMPTY);

    }

    private Map<String, String> tipeListDir() {

        Map<String, String> tipeListDirMap = new HashMap<>();

        tipeListDirMap.put("ALAMEDA", "ALM");
        tipeListDirMap.put("AVENUE", "AV.");
        tipeListDirMap.put("STREET", "CAL");
        tipeListDirMap.put("MALL", "CC.");
        tipeListDirMap.put("ROAD", "CRT");
        tipeListDirMap.put("SHOPPING_ARCADE", "GAL");
        tipeListDirMap.put("JIRON", "JR.");
        tipeListDirMap.put("JETTY", "MAL");
        tipeListDirMap.put("OVAL", "OVA");
        tipeListDirMap.put("PEDESTRIAN_WALK", "PAS");
        tipeListDirMap.put("SQUARE", "PLZ");
        tipeListDirMap.put("PARK", "PQE");
        tipeListDirMap.put("PROLONGATION", "PRL");
        tipeListDirMap.put("PASSAGE", "PSJ");
        tipeListDirMap.put("BRIDGE", "PTE");
        tipeListDirMap.put("DESCENT", "BAJ");
        tipeListDirMap.put("PORTAL", "POR");
        tipeListDirMap.put("GROUP", "AGR");
        tipeListDirMap.put("AAHH", "AHH");
        tipeListDirMap.put("HOUSING_COMPLEX", "CHB");
        tipeListDirMap.put("INDIGENOUS_COMMUNITY", "COM");
        tipeListDirMap.put("PEASANT_COMMUNITY", "CAM");
        tipeListDirMap.put("HOUSING_COOPERATIVE", "COV");
        tipeListDirMap.put("STAGE", "ETP");
        tipeListDirMap.put("SHANTYTOWN", "PJJ");
        tipeListDirMap.put("NEIGHBORHOOD", "SEC");
        tipeListDirMap.put("URBANIZATION", "URB");
        tipeListDirMap.put("NEIGHBORHOOD_UNIT", "UV.");
        tipeListDirMap.put("ZONE", "ZNA");
        tipeListDirMap.put("ASSOCIATION", "ASC");
        tipeListDirMap.put("FUNDO", "FUN");
        tipeListDirMap.put("MINING_CAMP", "MIN");
        tipeListDirMap.put("RESIDENTIAL", "RES");
        return tipeListDirMap;

    }

    private  boolean filterViaTypeCondition(final String geographicGroupTyeId) {
        Map<String, String> mapTypeListDir = tipeListDir();
        return mapTypeListDir.entrySet().stream().anyMatch(element -> element.getKey().equals(geographicGroupTyeId));
    }

    private  String filterViaTypeName(List<AddressComponentsDTO> addressComponents) {
        Map<String, String> mapTypeListDir = tipeListDir();
        String viaTypeName = StringUtils.EMPTY;
        for(AddressComponentsDTO address : addressComponents){
            if(mapTypeListDir.entrySet().stream().anyMatch(element -> element.getKey().equals(address.getComponentTypes().get(0)))){
                viaTypeName = mapTypeListDir.get(address.getComponentTypes().get(0));
            }
        }

        return viaTypeName;
    }
}

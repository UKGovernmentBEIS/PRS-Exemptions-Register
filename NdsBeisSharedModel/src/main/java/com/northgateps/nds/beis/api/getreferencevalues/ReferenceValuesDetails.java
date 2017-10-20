package com.northgateps.nds.beis.api.getreferencevalues;

import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;


/**
 * Class to contain Reference Values
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferenceValuesDetails", propOrder = { "domainCode", "serviceCode", "workplaceCode", "code", "name",
        "translations", "sequence" })
public class ReferenceValuesDetails extends AbstractValidatableModel {

    private String domainCode;
    private String serviceCode;
    private String workplaceCode;
    private String code;
    private String name;
    private List<Translation> translations;
    private int sequence;

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getWorkplaceCode() {
        return workplaceCode;
    }

    public void setWorkplaceCode(String workplaceCode) {
        this.workplaceCode = workplaceCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /* Comparator for sorting the list by Name */
    public static Comparator<ReferenceValuesDetails> NameComparator = new Comparator<ReferenceValuesDetails>() {

        @Override
        public int compare(ReferenceValuesDetails r1, ReferenceValuesDetails r2) {
            if (r1 != null && r2 != null) {
                String str1 = r1.getName();
                String str2 = r2.getName();
                if(str1!=null && str2!=null){
                    return str1.compareTo(str2);
                }
            }

            // ascending order
            return 0;

        }
    };

}

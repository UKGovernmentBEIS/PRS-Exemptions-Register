package com.northgateps.nds.beis.ui.controller;

import java.util.ResourceBundle;

import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import com.northgateps.nds.beis.ui.model.UiData;
import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.controller.UploadLimitations;
import com.northgateps.nds.platform.ui.model.AbstractNdsMvcModel;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Gets limitation settings for exemption uploads file from configuration.
 * 
 */
public class ExemptionFileUploadLimitations implements UploadLimitations {

    private String kindName;
    private int maxResources = 1;
    private String supportedTypes;
    private long maxResourceSize;

    public ExemptionFileUploadLimitations(String documentKind) {
        ConfigurationManager config = ConfigurationFactory.getConfiguration();

        kindName = documentKind.toLowerCase();
        supportedTypes = config.getString("supported." + kindName + ".type", "");
        maxResourceSize = config.getLong("supported." + kindName + ".maxsize", 0L);
    }

    /** no args constructor for JSON round tripping */
    public ExemptionFileUploadLimitations() {
    }

    @Override
    public int getMaxResources(AbstractNdsMvcModel model) {
        String maxDocuments = ((UiData) model.getUiData()).getSelectedExemptionTypeText().getMaxDocuments() == null
                ? "0" : ((UiData) model.getUiData()).getSelectedExemptionTypeText().getMaxDocuments();
        maxResources = Integer.parseInt(maxDocuments);
        return maxResources;
    }

    public void setMaxResources(int maxResources) {
    }

    @Override
    public String getSupportedTypes(AbstractNdsMvcModel model) {
        return supportedTypes;
    }

    public void setSupportedTypes(String supportedTypes) {
        this.supportedTypes = supportedTypes;
    }

    @Override
    public String getKindName() {
        return kindName;
    }

    public void setKindName(String documentKind) {
        ConfigurationManager config = ConfigurationFactory.getConfiguration();

        this.kindName = documentKind.toLowerCase();
        supportedTypes = config.getString("supported." + kindName + ".type", "");
    }

    @Override
    public String getMaxResourceSizeMB(AbstractNdsMvcModel model) {
        String ckBytes = String.valueOf(getMaxResourceSize(model) * 100 / 1048576);
        if (ckBytes.length() < 2) {
            return "0." + ckBytes;
        } else {
            return ckBytes.substring(0, ckBytes.length() - 2) + "." + ckBytes.substring(ckBytes.length() - 2);
        }
    }

    @Override
    public String getSupportedTypeNames(LocalizationContext localizationContext, AbstractNdsMvcModel model) {
        ResourceBundle bundle = localizationContext.getResourceBundle();
        StringBuilder names = new StringBuilder();
        for (String type : getSupportedTypes(model).split(" ")) {
            if (names.length() > 0) {
                names.append(", ");
            }
            names.append(bundle.getString("Mime_" + type));
        }
        if (names.indexOf(",") > -1) {
            names.replace(names.lastIndexOf(","), names.lastIndexOf(",") + 1, " " + bundle.getString("MimeTypes_or"));
        }
        return names.toString();
    }

    public long getMaxResourceSize() {
        return maxResourceSize;
    }

    public void setMaxResourceSize(long maxResourceSize) {
        this.maxResourceSize = maxResourceSize;
    }

    @Override
    public long getMaxResourceSize(AbstractNdsMvcModel model) {
        return maxResourceSize;
    }
}

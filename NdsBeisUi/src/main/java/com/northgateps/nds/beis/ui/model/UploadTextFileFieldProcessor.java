package com.northgateps.nds.beis.ui.model;

import java.util.Arrays;
import java.util.List;

import com.northgateps.nds.platform.ui.model.process.NullFieldProcessor;

/**
 * Provides path to Upload object to force validation on it.
 */
public class UploadTextFileFieldProcessor extends NullFieldProcessor {

    public UploadTextFileFieldProcessor() {
    }

    @Override
    public List<String> getPaths(String sourcePath) {
        return Arrays.asList(new String[] { "exemptionDetails.exemptionTextFile" });
    }
}

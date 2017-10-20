package com.northgateps.nds.beis.api.metadata;

import com.northgateps.nds.platform.application.api.format.js.AbstractFieldFormatter;
import com.northgateps.nds.platform.application.api.format.js.PrintableCharacterFieldFormatter;
import com.northgateps.nds.platform.application.api.metadata.FormatterOverride;
import com.northgateps.nds.platform.application.api.validation.ValidationContext;
import com.northgateps.nds.platform.util.scripts.ResourceLoader;
import com.northgateps.nds.platform.util.scripts.ScriptsLoader;

@FormatterOverride({ PrintableCharacterFieldFormatter.class })
public class EPCReferenceNumberFieldFormatter extends AbstractFieldFormatter {

	@Override
    public String getFormatFn(ValidationContext<?> validationContext) {
        ResourceLoader scriptsLoader = validationContext != null ? validationContext.getScriptsLoader() : new ScriptsLoader();
        String script = "var document = this;\n" + scriptsLoader.load("metadata.beis.js") +
                        "\nvar fn = document.formatFunctions['" + this.getTypeName() + "'];";
        return script;
    }
	
}

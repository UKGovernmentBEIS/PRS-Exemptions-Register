package com.northgateps.nds.beis.ui.converter;

import org.springframework.core.convert.converter.Converter;

import com.northgateps.nds.beis.ui.util.NdsDateTimeUtils;
import com.northgateps.nds.platform.api.model.datatypes.DateTimeHolder;
import com.northgateps.nds.platform.api.model.datatypes.DateTimePrecision;

/**
 * Spring converter to convert a value between a DateTime and String. 
 * Use as a pair with StringToJodaDateTimeAsDate.
 */
public final class DateTimeHolderToString implements Converter<DateTimeHolder, String> {

    @Override
    public String convert(DateTimeHolder source) {
        if (source.getDateTime() == null) {
            return "";
        }
        if (source.getPrecision() == DateTimePrecision.DATE) {
            return NdsDateTimeUtils.getDatePrintFormatter().format(source.getDateTime());
        } else {
            return NdsDateTimeUtils.getDateTimePrintFormatter().format(source.getDateTime());
        }
    }
}

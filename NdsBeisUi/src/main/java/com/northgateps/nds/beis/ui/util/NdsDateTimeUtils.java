package com.northgateps.nds.beis.ui.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.NoSuchElementException;

import com.northgateps.nds.platform.ui.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.ui.model.DateTimeField;

/**
 * Contains date and time formatter utility methods for serialization and deserialisatuon.
 * 
 * The date time serialised format is expected to be ISO 8601 (e.g. 2008-12-25T13:30:45.653+05:30)
 * which contains not just time and date information, but also information about the timezone offset.
 * For backward compatibility however, it is recognised that dates and time may need to be read in
 * other serialisations, so this provides a means of attempting to parse those alternative serialisations   
 */
public class NdsDateTimeUtils {
    
    /**
     *  Parses the date and time from a string by trying a fixed set of candidate formats until a matching one is found 
     */
    private static ZonedDateTime getDateTime(String dt) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(""
                + "[yyyy/MM/dd]"
                + "[yyyy-MM-dd]"
                + "[ddd/MM/yyyy]"
                + "[dd-MM-yyyy]"
                + "[yyyy/MM/dd HH:mm:ss.SSS]"
                + "[yyyy-MM-dd HH:mm:ss.SSS]"
                + "[dd/MM/yyyy HH:mm:ss.SS]"
                + "[dd-MM-yyyy HH:mm:ss.SSS]"
            );
        
        return getDateTime(dt,formatter);
    }
    
    /**
     * Parses the date and time from a string by first trying ISO 8601 format, then if that doesn't match,
     * trying a set of provided candidate parsers, one per format, until a matching one is found 
     */
    private static ZonedDateTime getDateTime(String dt, DateTimeFormatter formatter) {
        try {
            
            OffsetDateTime odt = OffsetDateTime.parse(dt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            
            return odt.toZonedDateTime();
            
        } catch (IllegalArgumentException illegalArgException) {// This exception occurs for non ISO 8601 dateTime
                      
            return ZonedDateTime.parse(dt, formatter).withZoneSameInstant(ZoneOffset.UTC);
        }
    }
    
    /**
     * Gets a dateTimeFormatter using format defined in properties file if available, or the Locale's default date format otherwise.
     * 
     * @return formattedDate -formatted date
     */
    public static DateTimeFormatter getDateTimePrintFormatter() {
        try {                        
            return  DateTimeFormatter.ofPattern(ConfigurationFactory.getConfiguration().getString("dateTimeFormat"));            
        } catch (NoSuchElementException e) {// This exception occurs if dateTimeFormat is not defined
            return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        }
    }
    
    /**
     * Gets a dateTimeFormatter using format defined in properties file if available, or the Locale's default date format otherwise.
     * 
     * @return formattedDate -formatted date
     */
    public static DateTimeFormatter getDatePrintFormatter() {
        try {            
            return  DateTimeFormatter.ofPattern(ConfigurationFactory.getConfiguration().getString("dateFormat"));            
        } catch (NoSuchElementException e) {// This exception occurs if dateTimeFormat is not defined
            return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        }
    }    
    
    /**
     * Formats given date in dateTimeFormat and in Locale's default date format if dateTimeFormat is not defined in properties file.
     * 
     * @return formattedDate -formatted date
     */
    public static String dateTimeFormatter(String date) {
        String formattedDate = "";
        String dateTimeFormat = "";
        if (date==null || date.equals("")){
            return formattedDate;
        }
        ZonedDateTime dateTime = getDateTime(date); 
        try {
            dateTimeFormat = ConfigurationFactory.getConfiguration().getString("dateTimeFormat");
            if (!dateTimeFormat.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat );
                formattedDate = formatter.format(dateTime);
            }
        } catch (NoSuchElementException e) {// This exception occurs if dateTimeFormat is not defined
            formattedDate = dateFormatter(date) + " " + timeFormatter(date);
        }
        return formattedDate;
    }
    
    /**
     * Formats given date in dateTimeFormat and in Locale's default date format if dateTimeFormat is not defined in properties file.
     * 
     * @return formattedDate -formatted date
     */
    public static String dateFormatter(String date) {
        String formattedDate = "";
        String dateFormat = "";
        if (date==null || date.equals("")){
            return formattedDate;
        }
        ZonedDateTime dateTime = getDateTime(date); 

        try {
            dateFormat = ConfigurationFactory.getConfiguration().getString("dateFormat");
            if (!dateFormat.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                formattedDate = formatter.format(dateTime);
            }
        } catch (NoSuchElementException e) {// This exception occurs if dateTimeFormat is not defined            
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);            
            formattedDate = formatter.format(dateTime.toLocalDate());           
        }
        return formattedDate;
    }


    /**
     * Formats given time in "HH:mm" format
     * 
     * @return formattedTime -formatted time
     */
    public static String timeFormatter(String time) {
        String formattedTime = "";
        if (time==null){
            return formattedTime;
        }
        
        DateTimeFormatter readerFormatter = DateTimeFormatter.ofPattern(""
                + "[HH:mm:ss:SSS]"
                + "[HH:mm:ss.SSS]"
                + "[HH:mm:ss]"
                + "[HH:mm]"
                + "[HH.mm]"
                + "[HH]"
            );

        ZonedDateTime dateTime = ZonedDateTime.parse(time, readerFormatter.withZone(ZoneOffset.UTC));
        
        try {
            String timeFormat = ConfigurationFactory.getConfiguration().getString("timeFormat");
            if (!timeFormat.isEmpty()) {                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
                formattedTime = formatter.format(dateTime);

            }
        } catch (NoSuchElementException e) {// This exception occurs if timeFormat is not defined
            DateTimeFormatter dtfOut = DateTimeFormatter.ofPattern("HH:mm");
            formattedTime = dtfOut.format(dateTime);

        }        
        return formattedTime;
    }
    
    
    /**
     * Convert ZonedDateTime to a DateTimeField
     * @param dateTimeField
     * @param dateTime
     * @return
     */
    public static DateTimeField convertToDateTimeField(DateTimeField dateTimeField, ZonedDateTime dateTime) {

        if (dateTime != null) {

            dateTimeField.setYear(Integer.toString(dateTime.getYear()));
            dateTimeField.setMonthOfYear(Integer.toString(dateTime.getMonthValue()));
            dateTimeField.setDayOfMonth(Integer.toString(dateTime.getDayOfMonth()));
        }

        return dateTimeField;

    }

}

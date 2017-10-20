package com.northgateps.nds.beis.esb.purgedocuments;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.platform.api.db.NdsDbResponseDetails;
import com.northgateps.nds.platform.esb.adapter.BatchJobAdapter;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.db.NdsBatchJobMetaData;
import com.northgateps.nds.platform.esb.db.NdsDbMetadata;
import com.northgateps.nds.platform.esb.db.NdsDbService;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Deletes files from mongo after a given period.
 * 'purgeRecordsPeriod' is defined in the properties file ,which is used to determine the uploaded date that in turn will
 * be used as a criteria to delete files from Mongo.
 *
 */
public class PurgeDocumentsAdapter extends BatchJobAdapter {

    ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    @Override
    protected void doProcess(NdsSoapRequestAdapterExchangeProxyImpl ndsExchange)
            throws KeyManagementException, NoSuchAlgorithmException, ClassNotFoundException {
        
        final NdsBatchJobMetaData batchJobMetadata = new NdsBatchJobMetaData();

        /* A change was done for PE-578 to allow a list of file ids to be passed in for when a file was linked to a file summary.
         * For now BEIS does not use summaries and therefore no list is passed.
         */
        NdsDbService.deleteOrphanedFiles(ndsExchange, getDateCriteria(), null);
        NdsDbResponseDetails ndsDbResponseDetails = (NdsDbResponseDetails)ndsExchange.getResponseMessageBody();
        NdsDbMetadata metadata = (NdsDbMetadata) ndsDbResponseDetails.getMetadata();
        batchJobMetadata.setStartTimestamp(metadata.getStartTimestamp());
        batchJobMetadata.setEndTimestamp(LocalDateTime.now().atZone(ZoneOffset.UTC));
        batchJobMetadata.setRecordCount(metadata.getRecordCount());
        ndsExchange.setAnExchangeProperty(BatchJobAdapter.BATCH_JOB_META_DATA, batchJobMetadata);

    }

   /**
    * @return date before given purgeRecordsPeriod.to be used as a criteria to delete the records from mongo.
    * All the records older than this date will be deleted.
    * 
    */  
    private ZonedDateTime getDateCriteria() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        dateTime = dateTime.minusDays(Integer.parseInt(configurationManager.getString("purgePeriodOrphanedFiles")));
        return dateTime;
    }
    

    @Override
    protected String processName() {
        return "Purge";
    }
    
    @Override
    protected Duration intervalPeriod(TypeConverter converter) {
        // The purgeIntervalPeriod is an integer number of hours.
        int hours =  Integer.parseInt(configurationManager.getString("purgeIntervalPeriod"));
        long ms = hours * 60L * 60L * 1000;
        return Duration.of(ms, ChronoUnit.MILLIS);
    }
   
    @Override
    protected int purgeAuditRecordsPeriod() {
        return Integer.parseInt(configurationManager.getString("purgeAuditRecordsPeriod"));
    }
}

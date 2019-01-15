package com.northgateps.nds.beis.esb.purgedocuments;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.camel.TypeConverter;

import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.adapter.persistence.BatchJobPersistenceAdapter;
import com.northgateps.nds.platform.esb.db.NdsBatchJobMetaData;
import com.northgateps.nds.platform.esb.db.NdsDbMetadata;
import com.northgateps.nds.platform.esb.exception.NdsApplicationException;
import com.northgateps.nds.platform.esb.exception.NdsDbException;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Deletes Orphaned files from mongo after "purgePeriodOrphanedFiles" period.
 */
public class PurgeDocumentsPersistenceAdapter extends BatchJobPersistenceAdapter {
    
    private static final NdsLogger logger = NdsLogger.getLogger(PurgeDocumentsPersistenceAdapter.class);

    ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    @Override
    protected void doProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange) throws NdsDbException, NdsApplicationException {       
        
        final NdsBatchJobMetaData batchJobMetadata = new NdsBatchJobMetaData();
        long start = System.currentTimeMillis();      
       
        // delete associated files
        NdsDbMetadata metadata = getPersistenceManager().deleteOrphanedFilesReturnMetaData(getGridFsCollectionName(), getDateCriteria(), null);
        
        batchJobMetadata.setStartTimestamp(metadata.getStartTimestamp());
        batchJobMetadata.setRecordCount(metadata.getRecordCount());
        batchJobMetadata.setEndTimestamp(metadata.getEndTimestamp());
        ndsExchange.setAnExchangeProperty(BatchJobPersistenceAdapter.BATCH_JOB_META_DATA, batchJobMetadata);
        
        logger.info("Purged "+metadata.getRecordCount()+" records");
        
        long end = System.currentTimeMillis();
        logger.info("Purging associated uploaded files took " + (end - start) + "ms");
    }    
   

   /**
    * @return date before given purgeRecordsPeriod.to be used as a criteria to delete the records from mongo.
    * All the records older than this date will be deleted.
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

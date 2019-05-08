package com.northgateps.nds.beis.esb.statistics;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;

import com.mongodb.QueryBuilder;
import com.northgateps.nds.beis.api.statistics.BeisEvent;
import com.northgateps.nds.beis.api.statistics.ExemptionInformation;
import com.northgateps.nds.beis.api.statistics.StatisticsNdsRequest;
import com.northgateps.nds.platform.api.db.NdsEventRecord;
import com.northgateps.nds.platform.api.model.PlatformCommonContext;
import com.northgateps.nds.platform.api.statistics.UserAccountStatisticsNdsRequest;
import com.northgateps.nds.platform.api.statistics.UserAccountStatisticsNdsResponse;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxy;
import com.northgateps.nds.platform.esb.adapter.NdsSoapRequestAdapterExchangeProxyImpl;
import com.northgateps.nds.platform.esb.adapter.persistence.BatchJobPersistenceAdapter;
import com.northgateps.nds.platform.esb.db.NdsBatchJobMetaData;
import com.northgateps.nds.platform.esb.db.NdsDbData;
import com.northgateps.nds.platform.esb.exception.NdsDbException;
import com.northgateps.nds.platform.esb.persistence.FetchResult;
import com.northgateps.nds.platform.esb.persistence.PersistenceKernel;
import com.northgateps.nds.platform.esb.persistence.query.DocumentWrapperPersistenceQuery;
import com.northgateps.nds.platform.esb.persistence.query.PersistenceQuery;
import com.northgateps.nds.platform.esb.util.configuration.ConfigurationFactory;
import com.northgateps.nds.platform.logger.NdsLogger;
import com.northgateps.nds.platform.util.configuration.ConfigurationManager;

/**
 * Adapter to collect statistics about exemptions and users from MongoDb.
 */
public class StatisticsPersistenceAdapter extends BatchJobPersistenceAdapter {

    protected final NdsLogger logger = NdsLogger.getLogger(getClass());

    ConfigurationManager configurationManager = ConfigurationFactory.getConfiguration();

    private final static String USER_STATS_RESPONSE = "UserAccountStatisticsNdsResponse";

    @Override
    protected void doProcess(NdsSoapRequestAdapterExchangeProxy ndsExchange)
            throws NdsDbException {

        NdsBatchJobMetaData batchJobMetadata = new NdsBatchJobMetaData();

        batchJobMetadata.setStartTimestamp(LocalDateTime.now().atZone(ZoneOffset.UTC));

        FetchResult fetchResult = getPersistenceManager().fetch(getEventCollectionName(), buildRecordsCriteria());

        List<NdsDbData> eventRecords = fetchResult.getData();

        StatisticsNdsRequest statisticsNdsRequest = new StatisticsNdsRequest();

        PlatformCommonContext commonContext = new PlatformCommonContext();
        commonContext.setRequestGuid(UUID.randomUUID().toString());
        commonContext.setPreferredLanguage("en");
        statisticsNdsRequest.setCommonContext(commonContext);

        if (ndsExchange.getAnExchangeProperty(DATE_TIME_LAST_RUN) != null
                && ndsExchange.getAnExchangeProperty(DATE_TIME_NOW_RAN) != null) {
            statisticsNdsRequest.setFromDateTime((ZonedDateTime) ndsExchange.getAnExchangeProperty(DATE_TIME_LAST_RUN));
            statisticsNdsRequest.setToDateTime((ZonedDateTime) ndsExchange.getAnExchangeProperty(DATE_TIME_NOW_RAN));
        }

        if (eventRecords != null && eventRecords.size() > 0) {
            setExemptionType(statisticsNdsRequest, eventRecords);
            statisticsNdsRequest.setNumberOfRegisteredExemptions(eventRecords.size());
        }

        // put the NdsBatchJobMetadata in exchange property
        ndsExchange.setAnExchangeProperty(BatchJobPersistenceAdapter.BATCH_JOB_META_DATA, batchJobMetadata);

        ndsExchange.setResponseMessageBody(statisticsNdsRequest);

    }

    /*
     * Collect references for each exemption type and count
     */
    private void setExemptionType(StatisticsNdsRequest statisticsNdsRequest, List<NdsDbData> eventRecords) {
       
        List<ExemptionInformation> domesticExemptions = new ArrayList<ExemptionInformation>();
        
        List<ExemptionInformation> nonDomesticExemptions = new ArrayList<ExemptionInformation>();

        for (NdsDbData dataRecord : eventRecords) {
            
            NdsEventRecord ndsEventRecord = (NdsEventRecord) dataRecord.getData();
            ExemptionInformation exemptionInformation = new ExemptionInformation();
            exemptionInformation.setExemptionsReferences(ndsEventRecord.getEventReferenceId());
            exemptionInformation.setExemptionType(ndsEventRecord.getEventType());      
            
            if (ndsEventRecord.getEvent().equals(BeisEvent.DOMESTIC.toString())) {
                domesticExemptions.add(exemptionInformation);
            } else {
                nonDomesticExemptions.add(exemptionInformation);
            }
        }

        statisticsNdsRequest.setDomesticExemptions(domesticExemptions);
        statisticsNdsRequest.setNonDomesticExemptions(nonDomesticExemptions);
        statisticsNdsRequest.setNumberOfDomesticExemptions(domesticExemptions.size());
        statisticsNdsRequest.setNumberOfNonDomesticExemptions(nonDomesticExemptions.size());
        
        logger.debug("Found " + String.valueOf(domesticExemptions.size()) + " domestic property");
        logger.debug("Found " + String.valueOf(nonDomesticExemptions.size()) + " non domestic property");

    }

    /* Criteria to use to retrieve incident events */
    private PersistenceQuery buildRecordsCriteria() {

        ZonedDateTime dateTime = ZonedDateTime.now();
        dateTime = dateTime.minusHours(Integer.parseInt(configurationManager.getString("statisticsPeriod")));

        String startDateOfPeriod = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        
        QueryBuilder isAfterThisSartPeriodDateTime = new QueryBuilder().put(
                PersistenceKernel.ndsRecord(PersistenceKernel.ndsData("eventDateTime"))).greaterThan(startDateOfPeriod);
        
        List<String> incidentEventStatusList = Arrays.asList(
                ConfigurationFactory.getConfiguration().getString("exemptionEvents").split("\\s*,\\s*"));

        QueryBuilder inThisEventStatusList = new QueryBuilder().put(
                PersistenceKernel.ndsRecord(PersistenceKernel.ndsData("event"))).in(incidentEventStatusList);   
        
        QueryBuilder criteria = new QueryBuilder().and(isAfterThisSartPeriodDateTime.get(), inThisEventStatusList.get());
        
        return new DocumentWrapperPersistenceQuery(criteria);

    }    

    /**
     * Create the request to get the user stats
     * 
     * @param exchange
     */
    public void createUserAccountStatisticsRequest(Exchange exchange) {
        
        NdsSoapRequestAdapterExchangeProxy ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);

        UserAccountStatisticsNdsRequest userAccountStatisticsNdsRequest = new UserAccountStatisticsNdsRequest();

        userAccountStatisticsNdsRequest.setToDateTime(
                (ZonedDateTime) ndsExchange.getAnExchangeProperty(DATE_TIME_NOW_RAN));
        userAccountStatisticsNdsRequest.setFromDateTime(
                (ZonedDateTime) ndsExchange.getAnExchangeProperty(DATE_TIME_LAST_RUN));

        ndsExchange.setResponseMessageBody(userAccountStatisticsNdsRequest);

    }

    /**
     * Extract the user stats from response
     * 
     * @param exchange
     */
    public void processUserAccountStatisticsResponse(Exchange exchange) {

        NdsSoapRequestAdapterExchangeProxy ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);

        UserAccountStatisticsNdsResponse userAccountStatisticsNdsResponse = null;

        try {
            userAccountStatisticsNdsResponse = (UserAccountStatisticsNdsResponse) ndsExchange.getRequestMessageBody(
                    UserAccountStatisticsNdsResponse.class);
        } catch (ClassNotFoundException e) {
            logger.error("processUserAccountStatisticsResponse userAccountStatisticsNdsResponse failed due to "
                    + e.getMessage());
        }

        ndsExchange.setAnExchangeProperty(USER_STATS_RESPONSE, userAccountStatisticsNdsResponse);

    }

    /**
     * Prepare the stats for the email request
     * 
     * @param exchange
     */
    public void prepareStatsForEmailRequest(Exchange exchange) {

        NdsSoapRequestAdapterExchangeProxy ndsExchange = new NdsSoapRequestAdapterExchangeProxyImpl(exchange);

        UserAccountStatisticsNdsResponse userAccountStatisticsNdsResponse = (UserAccountStatisticsNdsResponse) ndsExchange.getAnExchangeProperty(
                USER_STATS_RESPONSE);

        StatisticsNdsRequest statisticsNdsRequest = null;
        try {
            statisticsNdsRequest = (StatisticsNdsRequest) ndsExchange.getRequestMessageBody(StatisticsNdsRequest.class);
        } catch (ClassNotFoundException e) {
            logger.error("prepareStatsForEmailRequest StatisticsNdsRequest failed due to " + e.getMessage());
        }
        statisticsNdsRequest.setNumberOfSuccessfulLogins(userAccountStatisticsNdsResponse.getLoginSuccessCount());
        statisticsNdsRequest.setNumberOfFailedLogins(userAccountStatisticsNdsResponse.getLoginFailedCount());
        statisticsNdsRequest.setNumberOfChangePassword(userAccountStatisticsNdsResponse.getChangePasswordCount());
        statisticsNdsRequest.setNumberOfRegistration(userAccountStatisticsNdsResponse.getRegistrationCount());
        statisticsNdsRequest.setNumberOfResetPassword(userAccountStatisticsNdsResponse.getResetPasswordCount());

        NdsBatchJobMetaData batchJobMetadata = (NdsBatchJobMetaData) ndsExchange.getAnExchangeProperty(
                BatchJobPersistenceAdapter.BATCH_JOB_META_DATA);
        
        if (batchJobMetadata != null) {
            batchJobMetadata.setEndTimestamp(LocalDateTime.now().atZone(ZoneOffset.UTC));
            batchJobMetadata.setNotes(" Succesful logins count: " + statisticsNdsRequest.getNumberOfSuccessfulLogins()
                    + " Failed logins count: " + statisticsNdsRequest.getNumberOfFailedLogins()
                    + " Number of registrations: " + statisticsNdsRequest.getNumberOfRegistration()
                    + " Number of change password: "+ statisticsNdsRequest.getNumberOfChangePassword()
                    + " Number of reset password: "+ statisticsNdsRequest.getNumberOfResetPassword()
                    + " Successfully submitted exemptions: "+ statisticsNdsRequest.getNumberOfRegisteredExemptions()                  
                    + " Domestic Exemptions: "+ statisticsNdsRequest.getNumberOfDomesticExemptions()
                    + " Nondomestic Exemptions: "+ statisticsNdsRequest.getNumberOfNonDomesticExemptions());
            // NB: not setting the batchJobMetadata.RecordCount, as no records were actually affected and we've already set the individual counts in the notes
        }

        ndsExchange.setAnExchangeProperty(BatchJobPersistenceAdapter.BATCH_JOB_META_DATA, batchJobMetadata);
        ndsExchange.setResponseMessageBody(statisticsNdsRequest);
    }

    @Override
    protected String processName() {
        return "Statistics";
    }

    @Override
    protected Duration intervalPeriod(TypeConverter converter) {
        // Integer number of hours.
        int hours = configurationManager.getInt("statisticsPeriod");
        long ms = hours * 60L * 60L * 1000;
        return Duration.of(ms, ChronoUnit.MILLIS);
    }

    @Override
    protected int purgeAuditRecordsPeriod() {
        return configurationManager.getInt("purgeAuditRecordsPeriod");
    }

}

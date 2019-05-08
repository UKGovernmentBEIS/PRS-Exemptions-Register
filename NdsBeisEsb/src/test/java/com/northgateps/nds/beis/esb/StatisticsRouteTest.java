package com.northgateps.nds.beis.esb;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.northgateps.nds.beis.esb.statistics.StatisticsPersistenceAdapter;
import com.northgateps.nds.platform.api.db.NdsEventRecord;
import com.northgateps.nds.platform.api.db.NdsMasterProcessRecord;
import com.northgateps.nds.platform.esb.Visits;
import com.northgateps.nds.platform.esb.db.NdsDbData;
import com.northgateps.nds.platform.esb.db.NdsDbMetadata;
import com.northgateps.nds.platform.esb.persistence.FetchResult;
import com.northgateps.nds.platform.esb.persistence.PersistenceAmendment;
import com.northgateps.nds.platform.esb.persistence.PersistenceKernel;
import com.northgateps.nds.platform.esb.persistence.PersistenceResult;
import com.northgateps.nds.platform.esb.persistence.query.ByReferenceIdQuery;
import com.northgateps.nds.platform.esb.persistence.query.PersistenceQuery;
import com.northgateps.nds.platform.esb.statistics.UserAccountStatisticsPersistenceAdapter;
import com.northgateps.nds.platform.logger.NdsLogger;

/**
 * Route test for statistics route
 * 
 * @see com.northgateps.nds.platform.esb.senddormantuseremail.SendEmailToDormantUsersJobRouteTest which this class
 * is based on.
 */

@UseAdviceWith(true)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsRouteTest extends CamelSpringTestSupport {

    private static final NdsLogger logger = NdsLogger.getLogger(StatisticsRouteTest.class);

    public static String TENANT = "BEIS";

    private static final String routeNameUnderTest = "statisticsRoute";
    private static final String userAccountStatsRouteName = "userAccountStatisticsSubRoute";
    private static final String TEST_START_NAME = "direct:statisticsRoute";
    private static final String TEST_START_NAME_USER_STATS = "direct:userAccountStatistics";
    
    @EndpointInject(uri = TEST_START_NAME)
    private ProducerTemplate apiStatisticsRoute;
    
    @EndpointInject(uri = TEST_START_NAME_USER_STATS)
    private ProducerTemplate apiUserStatsRoute;

    /**
     * Creates Spring's application context.
     */

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return RouteTestUtils.createApplicationContext(routeNameUnderTest, userAccountStatsRouteName);
    }

    
    /** Test to check statistic route*/
    @Test
    public void testStats() throws Exception {
        
        List<AssertionError> assertionErrors = new ArrayList<AssertionError>(); 
        Visits visits = new Visits();
        visits.setExpected("fetch-mpr", 2);
        visits.setExpected("fetch-event", 2);
        visits.setExpected("upsert", 1);
        visits.setExpected("update", 1);
        visits.setExpected("discard", 1);
        
        final List<String> masterProcessRecordUuid = new ArrayList<String>(1);
        masterProcessRecordUuid.add(UUID.randomUUID().toString());
        ObjectId masterProcessRecordObjectId = new ObjectId(); 

        logger.info("testStats test started");
        
        context.getRouteDefinition(userAccountStatsRouteName).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                PersistenceKernel mockPersistenceKernel = new PersistenceKernel() {
                    
                    // now override any method call to the db, to return mocked data
                    
                    @Override
                    protected FetchResult fetch(String collectionName, PersistenceQuery query, String sortField, int order, int skip, int limit) {
                        if ("NDS_EVENTS_DATA".equals(collectionName)) {
                            visits.incr("fetch-event");
                            FetchResult result = new FetchResult();
                            addResultRecords(result);
                            result.setFailures(new ArrayList<NdsDbData>());
                            result.setMetadata(new NdsDbMetadata());                            
                            return result;
                        } else {
                            return null;
                        }
                    }
                    
                    private void addResultRecords(FetchResult result) {
                        result.setData(new ArrayList<NdsDbData>());
                        NdsDbData record = new NdsDbData();
                        record.setData(buildEventRecord(ZonedDateTime.now().minusHours(3),"uid=BeisTest-c1121ade-3c49-43f8-a77d-c2be543d6605,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com",
                                "LOGIN_SUCCESS","null"));
                        record.setReferenceId("BeisTest-c1121ade-3c49-43f8-a77d-c2be543d6605");
                        record.setId(new ObjectId());
                        record.setLastUpdatedDate(ZonedDateTime.now().minusHours(3));
                        result.getData().add(record);
                        record = new NdsDbData();
                        record.setData(buildEventRecord(ZonedDateTime.now().minusHours(4),"uid=BeisTest-68110b9c-e6fc-4a85-ab88-eaaf485847cz,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com",
                                "LOGIN_FAILED","null"));
                        record.setReferenceId("BeisTest-68110b9c-e6fc-4a85-ab88-eaaf485847cz");
                        record.setId(new ObjectId());
                        record.setLastUpdatedDate(ZonedDateTime.now().minusHours(4));
                        result.getData().add(record);
                       
                    }
                };                
                
                UserAccountStatisticsPersistenceAdapter mockPersistence = new UserAccountStatisticsPersistenceAdapter();
                mockPersistence.getPersistenceManager().setPersistenceKernel(mockPersistenceKernel);                    
                this.weaveById("userAccountStatisticsAdapter.processRequest").replace().bean(mockPersistence, "processRequest");
                this.weaveById("userAccountStatisticsAdapter.processResponse").replace().bean(mockPersistence, "processResponse");
               
            }
        });
        
        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {                             
                
                interceptSendToEndpoint("smtp://{{smtp.host.server}}").skipSendToOriginalEndpoint().process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        // nothing to change
                    }
                });
                
                PersistenceKernel mockPersistenceKernel = new PersistenceKernel() {
                    
                    // now override any method call to the db, to return mocked data
                    
                    @Override
                    protected FetchResult fetch(String collectionName, PersistenceQuery query, String sortField, int order, int skip, int limit) {
                        if ("NdsMasterProcessRecord:Statistics".equals(query.getDocument().get("NDSRECORD.REFERENCEID"))) {
                            visits.incr("fetch-mpr");
                            FetchResult result = new FetchResult();
                            result.setData(new ArrayList<NdsDbData>());
                            NdsMasterProcessRecord masterProcessRecord = new NdsMasterProcessRecord();
                            masterProcessRecord.setProcessName("Statistics");
                            masterProcessRecord.setTimestamp(ZonedDateTime.now().minusDays(2));
                            masterProcessRecord.setUuid(masterProcessRecordUuid.get(0));
                            NdsDbData record = new NdsDbData();
                            record.setData(masterProcessRecord);
                            record.setReferenceId("NdsMasterProcessRecord:Statistics");
                            record.setId(masterProcessRecordObjectId);
                            record.setLastUpdatedDate(ZonedDateTime.now().minusDays(2));
                            result.getData().add(record);
                            result.setFailures(new ArrayList<NdsDbData>());
                            result.setMetadata(new NdsDbMetadata());
                            return result;
                        } else if ("NDS_EVENTS_DATA".equals(collectionName)) {
                            visits.incr("fetch-event");
                            FetchResult result = new FetchResult();
                            addResultRecords(result);
                            result.setFailures(new ArrayList<NdsDbData>());
                            result.setMetadata(new NdsDbMetadata());                            
                            return result;
                        } else {
                            return null;
                        }
                    }
                    
                    private void addResultRecords(FetchResult result) {
                        result.setData(new ArrayList<NdsDbData>());
                        NdsDbData record = new NdsDbData();
                        record.setData(buildEventRecord(ZonedDateTime.now().minusHours(1),
                                "uid=BeisTest-08b88728-4cdc-4cc4-b945-f8861b0bfeba,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com",
                                "NON_DOMESTIC","You have recently become a landlord under circumstances that qualify the property for an exemption.<br>This is regulation 33(1)"));
                        record.setReferenceId("f8861b0bfeba");
                        record.setId(new ObjectId());
                        record.setLastUpdatedDate(ZonedDateTime.now().minusHours(1));
                        result.getData().add(record);
                        record = new NdsDbData();
                        record.setData(buildEventRecord(ZonedDateTime.now().minusHours(2),"uid=BeisTest-68110b9c-e6fc-4a85-ab88-eaaf485847cb,ou=Users,ou=BEIS,ou=Tenants,ou=NDS,dc=northgateps,dc=com",
                                "DOMESTIC","No suitable funding can be obtained to pay for a recommended energy efficiency improvement.<br>This is regulation 25"));
                        record.setReferenceId("BeisTest-68110b9c-e6fc-4a85-ab88-eaaf485847cb");
                        record.setId(new ObjectId());
                        record.setLastUpdatedDate(ZonedDateTime.now().minusHours(2));
                        result.getData().add(record);                            
                    }
                    
                    
                    @Override
                    protected boolean upsert(String collectionName, ByReferenceIdQuery query, Object data) {
                        visits.incr("upsert");
                        if ("NDS_AUDIT_DATA".equals(collectionName) && ((String)query.getDocument().get("NDSRECORD.REFERENCEID")).matches("[0-9a-fA-F]{24}")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    
                    
                    @SuppressWarnings("rawtypes")
                    @Override
                    protected PersistenceResult update(String collectionName, PersistenceQuery query, PersistenceAmendment amendment) {
                        visits.incr("update");
                        if ("NdsMasterProcessRecord:Statistics".equals(query.getDocument().get("NDSRECORD.REFERENCEID"))) {
                            masterProcessRecordUuid.remove(0);
                            Set coupled = amendment.getDocument().entrySet();
                            Document theActualFlippinDoc = (Document)((Entry)(coupled.toArray()[0])).getValue();
                            String newUuid = theActualFlippinDoc.getString("NDSRECORD.DATA.uuid");
                            masterProcessRecordUuid.add(newUuid);
                            
                            return new PersistenceResult().setMetadata(new NdsDbMetadata().setRecordCount(1));
                        } else {
                            return new PersistenceResult().setMetadata(new NdsDbMetadata().setRecordCount(0));
                        }
                    }
                    
                    @Override
                    protected PersistenceResult discard(String collectionName, PersistenceQuery query) {
                        visits.incr("discard");
                        try {
                            assertEquals("NDS_AUDIT_DATA", collectionName);
                            assertEquals("Statistics-Audit", query.getDocument().get("NDSRECORD.DATA.processName"));
                            Document minDateTimeDoc = (Document)query.getDocument().get("NDSRECORD.LAST_UPDATED_TIMESTAMP");
                            String minDateTime = (String)minDateTimeDoc.get("$lt");
                            ZonedDateTime zdt = ZonedDateTime.parse(minDateTime);
                            assertTrue(zdt.isBefore(ZonedDateTime.now().minusDays(90).plusSeconds(10))); // Allow for small clock differences
                            assertTrue(zdt.isAfter(ZonedDateTime.now().minusDays(90).minusMinutes(20)));
                        } catch (AssertionError e) {
                            logger.error("AssertionError", e);
                            assertionErrors.add(e);
                        }
                        return new PersistenceResult().setMetadata(new NdsDbMetadata().setRecordCount(1));
                    }
                };
                
                StatisticsPersistenceAdapter mockPersistence = new StatisticsPersistenceAdapter();
                mockPersistence.getPersistenceManager().setPersistenceKernel(mockPersistenceKernel);
                this.weaveById("statisticsAdapter.processRecords").replace().bean(mockPersistence, "processRecords");
                this.weaveById("statisticsAdapter.insertAuditRecord").replace().bean(mockPersistence, "insertAuditRecord");
                this.weaveById("statisticsAdapter.purgeAuditRecords").replace().bean(mockPersistence, "purgeAuditRecords");
            
            }
        });
        
        context.start();

        apiStatisticsRoute.sendBody("start");

        assertEquals(0, assertionErrors.size());
        visits.assertMatch();
        assertMockEndpointsSatisfied();
    }
    
    private NdsEventRecord buildEventRecord(ZonedDateTime eventDateTime, String userDn, String event, String eventType) {
        NdsEventRecord ndsEventRecord = new NdsEventRecord();
        
        ndsEventRecord.setUserDn(userDn);
        ndsEventRecord.setEvent(event);
        ndsEventRecord.setEventDateTime(eventDateTime);
        ndsEventRecord.setEventType(eventType);
        
        return ndsEventRecord;
    }

   
    /** Test to run actual route. Useful for debugging */
    @Ignore
    @Test
    public void testCounts() throws Exception {

        logger.info("statisticsRouteTest test started");

        context.getRouteDefinition(routeNameUnderTest).adviceWith(context, new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {

            }
        });

        context.start();

        apiStatisticsRoute.sendBody("start");

        assertMockEndpointsSatisfied();
    }
}

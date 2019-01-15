package com.northgateps.nds.beis.ui.model;

import java.util.HashMap;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.northgateps.nds.beis.api.ExemptionData;
import com.northgateps.nds.beis.api.ExemptionDetail;
import com.northgateps.nds.beis.api.ExemptionSearch;
import com.northgateps.nds.beis.api.GetExemptionSearchResponseDetail;
import com.northgateps.nds.beis.api.GetPenaltySearchResponseDetail;
import com.northgateps.nds.beis.api.PenaltyData;
import com.northgateps.nds.beis.api.PenaltySearch;
import com.northgateps.nds.beis.api.RegisteredExemptionDetail;
import com.northgateps.nds.beis.api.UserType;
import com.northgateps.nds.beis.api.dashboard.DashboardDetails;
import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.depend.FieldDependencyPart;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.ui.controller.ControllerState;
import com.northgateps.nds.platform.ui.model.AddressField;
import com.northgateps.nds.platform.ui.model.DateTimeField;
import com.northgateps.nds.platform.ui.model.NdsUiData;
import com.northgateps.nds.platform.ui.model.process.FieldsFacade;
import com.northgateps.nds.platform.application.api.metadata.ViolationFieldMetadata;

/**
 * Data supplied by the user which does not form part of the end submission but
 * is used to select states during the data entry process.
 * 
 * Initialising the child objects to null, indicates that the data for them has
 * not yet been supplied by the user. The validation logic used that to
 * determine whether to apply the validation rules to those objects.
 */
public class UiData extends NdsUiData {

    /**
     * Clears the data capture state fields, to reset the inputs after e.g. the data has been submitted
     */
    public void reset(ControllerState<?> controllerState) {
        this.setAddressFields(new HashMap<String, AddressField>());
        this.setEpcFileDescription(null);
        this.setMultipartFile(null);
        this.setMultipartExemptionTextFile(null);
        this.setMultipartExemptionFile(null);
        this.setOpenIncidents(null);
        this.setDashboardIncident(null);
        this.setDateTimeFields(new HashMap<String, DateTimeField>());
        this.setIsAgreed(null);
        this.setUserType(null);
    }

    public UiData() {
        this.setRefData(new RefData());
    }

    private RegistrationStatusType registrationStatus;

    /* the logged in user's open incidents */
    @JsonIgnore
    private transient DashboardDetails openIncidents = null;

    /* the currently selected incident being shown on the dashboard */
    private ExemptionDetail dashboardIncident = null;
    private ExemptionTypeText selectedExemptionTypeText;

    private String selectedExemptionTypeLovText;

    /* hold currently selected exemption type.Require to clear data if exemption type is changed */
    private String selectedExemptionType;
    private String durationUnit;

    private int duration;

    @RequiredFieldMetadata(invalidMessage = "Validation_IsAgree_Field_should_be_selected")
    private String isAgreed;

    @RequiredFieldMetadata(invalidMessage = "Validation_Checkbox_Field_should_be_selected")
    private String isAgreeRegistrationTermsConditions;

    @JsonIgnore
    private transient GetExemptionSearchResponseDetail getExemptionSearchResult;

    @JsonIgnore
    private transient GetPenaltySearchResponseDetail getPenaltySearchResult;

    private ExemptionData selectedExemptionData;
    private PenaltyData selectedPenaltyData;
    private UserType userType;
    
    private String penaltyValue;    
    private String selectedExemptionRefNo;
    private String selectedPenaltyRefNo;
    
    @JsonIgnore
    private transient RegisteredExemptionDetail registeredExemptionDetail;  

    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_not_be_empty_search",
            dependencies = {
            @FieldDependency(type = "allOf", parts = {
                    @FieldDependencyPart(path = ".exemptionPostcodeCriteria",comparator = "empty"),
                    @FieldDependencyPart(path = ".exemptionLandlordsNameCriteria",comparator = "empty"),
                    @FieldDependencyPart(path = ".exemptPropertyDetails",comparator = "empty"),
                    @FieldDependencyPart(path = ".town",comparator = "empty"),
                    @FieldDependencyPart(path = ".service",values = { "ALL" }, comparator = "contains"),
                    @FieldDependencyPart(path = ".exemptionType_PRSD",values = { "ALL" }, comparator = "contains"),
                    @FieldDependencyPart(path = ".exemptionType_PRSN",values = { "ALL" }, comparator = "contains")}),
           @FieldDependency(path = "action", values = { "FindExemptions" }, comparator = "contains")})
    private ExemptionSearch exemptionSearch;
    
    public ExemptionSearch getExemptionSearch() {
    	return exemptionSearch;
    }

    public void setExemptionSearch(ExemptionSearch exemptionSearch) {
    	this.exemptionSearch = exemptionSearch;
    }

    @ViolationFieldMetadata(invalidMessage = "Validation_Field_must_not_be_empty_search",
            dependencies = {
            @FieldDependency(type = "allOf", parts = {
                    @FieldDependencyPart(path = ".penaltyPostcodesCriteria",comparator = "empty"),
                    @FieldDependencyPart(path = ".penaltyLandlordsNameCriteria",comparator = "empty"),
                    @FieldDependencyPart(path = ".rentalPropertyDetails",comparator = "empty"),
                    @FieldDependencyPart(path = ".town",comparator = "empty"),
                    @FieldDependencyPart(path = ".propertyType",values = { "ALL" }, comparator = "contains"),
                    @FieldDependencyPart(path = ".penaltyType_PRSD",values = { "ALL" }, comparator = "contains"),
                    @FieldDependencyPart(path = ".penaltyType_PRSN",values = { "ALL" }, comparator = "contains")}),
            @FieldDependency(path = "action", values = { "FindPenalties" }, comparator = "contains") })
    private PenaltySearch penaltySearch;
    
    @JsonIgnore
    @RequiredFieldMetadata(dependencies = {            
            @FieldDependency(path = "action", values = { "SearchGdarGdip" }, comparator = "contains") })
    private transient GdipGdarSearch gdarGdipSearch;
    
    
    public GdipGdarSearch getGdarGdipSearch() {
		return gdarGdipSearch;
	}

	public void setGdarGdipSearch(GdipGdarSearch gdarGdipSearch) {
		this.gdarGdipSearch = gdarGdipSearch;
	}
	
	@JsonIgnore
	@RequiredFieldMetadata(invalidMessage="Validation_UsedServiceBefore")
	transient Boolean usedServiceBefore;
		
	/**
     * Info about a file just uploaded, if any
     * Overrides the implementation in NdsUiData to apply the fields facade
     */
    @JsonIgnore
    @FieldsFacade(processor = UploadFieldProcessor.class)
    private transient MultipartFile multipartFile = null;

    @JsonIgnore
    private transient String epcFileDescription;

    /**
     * Info about a file just uploaded, if any
     * Overrides the implementation in NdsUiData to apply the fields facade
     */
    @JsonIgnore
    @FieldsFacade(processor = UploadProofFieldProcessor.class)
    private transient MultipartFile multipartExemptionFile = null;

    /**
     * Info about a file just uploaded, if any
     * Overrides the implementation in NdsUiData to apply the fields facade
     */
    @JsonIgnore
    @FieldsFacade(processor = UploadTextFileFieldProcessor.class)
    private transient MultipartFile multipartExemptionTextFile = null;

    public MultipartFile getMultipartExemptionFile() {
        return multipartExemptionFile;
    }

    public void setMultipartExemptionFile(MultipartFile multipartExemptionFile) {
        this.multipartExemptionFile = multipartExemptionFile;
    }

    @Override
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    @Override
    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public DashboardDetails getOpenIncidents() {
        return openIncidents;
    }

    public void setOpenIncidents(DashboardDetails openIncidents) {
        this.openIncidents = openIncidents;
    }

    public ExemptionDetail getDashboardIncident() {
        return dashboardIncident;
    }

    public void setDashboardIncident(ExemptionDetail dashboardIncident) {
        this.dashboardIncident = dashboardIncident;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ExemptionTypeText getSelectedExemptionTypeText() {
        return selectedExemptionTypeText;
    }

    public void setSelectedExemptionTypeText(ExemptionTypeText selectedExemptionTypeText) {
        this.selectedExemptionTypeText = selectedExemptionTypeText;
    }

    public MultipartFile getMultipartExemptionTextFile() {
        return multipartExemptionTextFile;
    }

    public void setMultipartExemptionTextFile(MultipartFile multipartExemptionTextFile) {
        this.multipartExemptionTextFile = multipartExemptionTextFile;
    }

    public String getSelectedExemptionTypeLovText() {
        return selectedExemptionTypeLovText;
    }

    public void setSelectedExemptionTypeLovText(String selectedExemptionTypeLovText) {
        this.selectedExemptionTypeLovText = selectedExemptionTypeLovText;
    }

    public String getIsAgreeRegistrationTermsConditions() {
        return isAgreeRegistrationTermsConditions;
    }

    public void setIsAgreeRegistrationTermsConditions(String isAgreeRegistrationTermsConditions) {
        this.isAgreeRegistrationTermsConditions = isAgreeRegistrationTermsConditions;
    }

    public String getIsAgreed() {
        return isAgreed;
    }

    public GetExemptionSearchResponseDetail getGetExemptionSearchResult() {
        return getExemptionSearchResult;
    }

    public void setGetExemptionSearchResult(GetExemptionSearchResponseDetail getExemptionSearchResult) {
        this.getExemptionSearchResult = getExemptionSearchResult;
    }

    public void setIsAgreed(String isAgreed) {
        this.isAgreed = isAgreed;
    }

    public ExemptionData getSelectedExemptionData() {
        return selectedExemptionData;
    }

    public void setSelectedExemptionData(ExemptionData selectedExemptionData) {
        this.selectedExemptionData = selectedExemptionData;
    }

    public GetPenaltySearchResponseDetail getGetPenaltySearchResult() {
        return getPenaltySearchResult;
    }
    
    public PenaltySearch getPenaltySearch() {
        return penaltySearch;
    }
    
    public void setPenaltySearch(PenaltySearch penaltySearch) {
        this.penaltySearch = penaltySearch;
    }

    public void setGetPenaltySearchResult(GetPenaltySearchResponseDetail getPenaltySearchResult) {
        this.getPenaltySearchResult = getPenaltySearchResult;
    }
    
    public PenaltyData getSelectedPenaltyData() {
        return selectedPenaltyData;
    }

    public void setSelectedPenaltyData(PenaltyData selectedPenaltyData) {
        this.selectedPenaltyData = selectedPenaltyData;
    }
   
    public String getEpcFileDescription() {
        return epcFileDescription;
    }

    public void setEpcFileDescription(String epcFileDescription) {
        this.epcFileDescription = epcFileDescription;
    }

    public RegistrationStatusType getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatusType registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getSelectedExemptionType() {
        return selectedExemptionType;
    }

    public void setSelectedExemptionType(String selectedExemptionType) {
        this.selectedExemptionType = selectedExemptionType;
    }
    
    public Boolean getUsedServiceBefore() {
        return usedServiceBefore;
    }

    public void setUsedServiceBefore(Boolean usedServiceBefore) {
        this.usedServiceBefore = usedServiceBefore;
    }

    public RegisteredExemptionDetail getRegisteredExemptionDetail() {
        return registeredExemptionDetail;
    }

    public void setRegisteredExemptionDetail(RegisteredExemptionDetail registeredExemptionDetail) {
        this.registeredExemptionDetail = registeredExemptionDetail;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    public String getPenaltyValue() {
        return penaltyValue;
    }
    
    public void setPenaltyValue(String penaltyValue) {
        this.penaltyValue = penaltyValue;
    }
    
    public String getSelectedExemptionRefNo() {
        return selectedExemptionRefNo;
    }

    public void setSelectedExemptionRefNo(String selectedExemptionRefNo) {
        this.selectedExemptionRefNo = selectedExemptionRefNo;
    }
    
    public String getSelectedPenaltyRefNo() {
        return selectedPenaltyRefNo;
    }

    public void setSelectedPenaltyRefNo(String selectedPenaltyRefNo) {
        this.selectedPenaltyRefNo = selectedPenaltyRefNo;
    }
}
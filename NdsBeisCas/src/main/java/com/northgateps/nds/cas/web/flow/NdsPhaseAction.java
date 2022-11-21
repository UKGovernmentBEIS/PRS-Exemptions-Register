package com.northgateps.nds.cas.web.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.northgateps.nds.cas.config.NdsCasWebflowContextConfiguration;

/** Used on UI (casLoginView.html) page to display application phase (Live /Test) to user */
public class NdsPhaseAction {
	private static final Logger logger = LoggerFactory.getLogger(NdsPhaseAction.class);
    private String phase;
    
    private String feedbackEmail;
    
    public NdsPhaseAction(){}

    public NdsPhaseAction(String applicationPhase, String feedbackEmail) {
        phase = applicationPhase;
        this.feedbackEmail = feedbackEmail;
    }

    public String getPhase() {
        return phase;
    }

    public String getFeedBackEmail() {
        return feedbackEmail;
    }
    
    //Show Feebback message 
    public Boolean isFeedbackMessageRequired() {
        return ! ("LIVE".equalsIgnoreCase(phase) || "TEST".equalsIgnoreCase(phase));
    }
    
}

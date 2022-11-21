package com.northgateps.nds.cas.configuration;

import org.apereo.cas.configuration.model.support.mongo.BaseMongoDbProperties;
import com.northgateps.nds.platform.util.configuration.ex.ConfigurationException;

/** Properties for connecting to the Mongo database. */
public class NdsTicketRegistryMongoProperties extends BaseMongoDbProperties {

    private static final long serialVersionUID = -1714489812997819293L;
    private int poolSize = 100;
    private String replicaSet = "none";
    
    /** 
     * Seemingly duplicate the host and port settings in superclass both as strings since
     * we allow for lists of hosts (and therefore ports) in a different format to CAS
     * (theirs makes more sense but it's not worth changing it now).  
     */
    private String hosts;
    private String ports;

    /**
     * Sets the superclass variable too to avoid confusion.
     */
    public void setHosts(String hosts) {
      super.setHost(hosts);
      this.hosts = hosts;
    }

    /**
     * Sets superclass variable too as we want it set the same in the latest CAS code, though that
     * does mean we no longer support multiple port numbers. (CAS must support it though, possibly
     * through the clientUri property or port numbers with the hosts setting.)
     */
    public void setPorts(String ports) {
      if (ports.contains(",")) {
        throw new ConfigurationException("Multiple port numbers no longer supported");
      }
      super.setPort(Integer.parseInt(ports));
      this.ports = ports;
    }


    public int getPoolSize() {
        return poolSize;
    }
    
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
    
    @Override
    public String getReplicaSet() {
        return replicaSet;
    }

    @Override
    public BaseMongoDbProperties setReplicaSet(String replicaSet) {
        this.replicaSet = replicaSet;
        return this;
    }
    
    public String getHosts() {
        return hosts;
    }

    public String getPorts() {
        return ports;
    }
  }

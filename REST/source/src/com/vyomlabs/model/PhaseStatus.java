package com.vyomlabs.model;

public class PhaseStatus {
	 
	private String phaseNodeType;
	private String id;

	    private String incidentStatus;

	    private String status;

	    private String isDefault;

	    private String name;

	    private String seq;

	    public String getPhaseNodeType ()
	    {
	        return phaseNodeType;
	    }

	    public void setPhaseNodeType (String phaseNodeType)
	    {
	        this.phaseNodeType = phaseNodeType;
	    }

	    public String getId ()
	    {
	        return id;
	    }

	    public void setId (String id)
	    {
	        this.id = id;
	    }

	    public String getIncidentStatus ()
	    {
	        return incidentStatus;
	    }

	    public void setIncidentStatus (String incidentStatus)
	    {
	        this.incidentStatus = incidentStatus;
	    }

	    public String getStatus ()
	    {
	        return status;
	    }

	    public void setStatus (String status)
	    {
	        this.status = status;
	    }

	    public String getIsDefault ()
	    {
	        return isDefault;
	    }

	    public void setIsDefault (String isDefault)
	    {
	        this.isDefault = isDefault;
	    }

	    public String getName ()
	    {
	        return name;
	    }

	    public void setName (String name)
	    {
	        this.name = name;
	    }

	    public String getSeq ()
	    {
	        return seq;
	    }

	    public void setSeq (String seq)
	    {
	        this.seq = seq;
	    }

	    @Override
	    public String toString()
	    {
	        return "{\nphaseNodeType : "+phaseNodeType+",\nid : "+id+",\nincidentStatus : "+incidentStatus+",\nstatus : "+status+",\nisDefault : "+isDefault+",\nname : "+name+",\nseq : "+seq+"\n}";
	    }	
	
}

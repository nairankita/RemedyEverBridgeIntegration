package com.vyomlabs.model;

public class Response {
	 private String lastName;

	    private String time;

	    private String deliveryMethod;

	    private String pathText;

	    private String firstConfirmed;

	    private String attemptNumber;

	    private String firstName;

	    private String externalId;

	    public String getLastName ()
	    {
	        return lastName;
	    }

	    public void setLastName (String lastName)
	    {
	        this.lastName = lastName;
	    }

	    public String getTime ()
	    {
	        return time;
	    }

	    public void setTime (String time)
	    {
	        this.time = time;
	    }

	    public String getDeliveryMethod ()
	    {
	        return deliveryMethod;
	    }

	    public void setDeliveryMethod (String deliveryMethod)
	    {
	        this.deliveryMethod = deliveryMethod;
	    }

	    public String getPathText ()
	    {
	        return pathText;
	    }

	    public void setPathText (String pathText)
	    {
	        this.pathText = pathText;
	    }

	    public String getFirstConfirmed ()
	    {
	        return firstConfirmed;
	    }

	    public void setFirstConfirmed (String firstConfirmed)
	    {
	        this.firstConfirmed = firstConfirmed;
	    }

	    public String getAttemptNumber ()
	    {
	        return attemptNumber;
	    }

	    public void setAttemptNumber (String attemptNumber)
	    {
	        this.attemptNumber = attemptNumber;
	    }

	    public String getFirstName ()
	    {
	        return firstName;
	    }

	    public void setFirstName (String firstName)
	    {
	        this.firstName = firstName;
	    }

	    public String getExternalId ()
	    {
	        return externalId;
	    }

	    public void setExternalId (String externalId)
	    {
	        this.externalId = externalId;
	    }

	    @Override
	    public String toString()
	    {
	        return "{\nlastName : "+lastName+",\ntime : "+time+",\ndeliveryMethod : "+deliveryMethod+",\npathText : "+pathText+",\nfirstConfirmed : "+firstConfirmed+",\nattemptNumber : "+attemptNumber+",\nfirstName : "+firstName+",\nexternalId : "+externalId+"\n}";
	    }	
	
}

package com.calendarize.calendarize.Models;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String coyName;
    private LocalDateTime dateCreated;

    public String getId() { return id;}
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName;}
    public void setFirstName(String firstName) { this.firstName = firstName;}

    public String getLastName() { return lastName;}
    public void setLastName(String lastName) { this.lastName = lastName;}

    public String getCoyName() { return coyName;}
    public void setCoyName(String coyName) { this.coyName = coyName;}
    
    public LocalDateTime getDateCreated() { return dateCreated;}
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
}

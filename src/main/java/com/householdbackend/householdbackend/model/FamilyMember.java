package com.householdbackend.householdbackend.model;

import com.householdbackend.householdbackend.enums.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FamilyMember {
    @Id
    private Integer id;
    @Indexed
    private Integer householdId;
    private String name;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String spouseName;
    private OccupationType occupationType;
    private Integer annualIncome;
    // dateOfBirth is epoch time in seconds
    private Integer dateOfBirth;

    public FamilyMember(Integer id, Integer householdId, String name, Gender gender, MaritalStatus maritalStatus, String spouseName, OccupationType occupationType, Integer annualIncome, Integer dateOfBirth) {
        super();
        this.id = id;
        this.householdId = householdId;
        this.name = name;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.spouseName = spouseName;
        this.occupationType = occupationType;
        this.annualIncome = annualIncome;
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHouseholdId() {
        return householdId;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public OccupationType getOccupationType() {
        return occupationType;
    }

    public Integer getAnnualIncome() {
        return annualIncome;
    }

    public Integer getDateOfBirth() {
        return dateOfBirth;
    }
}
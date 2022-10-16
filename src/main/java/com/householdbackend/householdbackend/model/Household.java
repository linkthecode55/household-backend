package com.householdbackend.householdbackend.model;

import com.householdbackend.householdbackend.enums.HousingType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document
public class Household {
    @Id
    private Integer id;
    private HousingType housingType;
    @Indexed
    private Boolean qualifySEB;
    @Indexed
    private Boolean qualifyFTS;
    @Indexed
    private Boolean qualifyEB;
    @Indexed
    private Boolean qualifyBSG;
    @Indexed
    private Boolean qualifyYGG;

    public Household(Integer id, HousingType housingType) {
        super();
        this.id = id;
        this.housingType = housingType;
        this.qualifySEB = false;
        this.qualifyFTS = false;
        this.qualifyEB = false;
        this.qualifyBSG = false;
        this.qualifyYGG = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HousingType getHousingType() {
        return housingType;
    }

    public Boolean getQualifySEB() {
        return qualifySEB;
    }

    public Boolean getQualifyFTS() {
        return qualifyFTS;
    }

    public Boolean getQualifyEB() {
        return qualifyEB;
    }

    public Boolean getQualifyBSG() {
        return qualifyBSG;
    }

    public Boolean getQualifyYGG() {
        return qualifyYGG;
    }

    public void updateQualifications(Boolean qualifySEB, Boolean qualifyFTS, Boolean qualifyEB, Boolean qualifyBSG, Boolean qualifyYGG) {
        this.qualifySEB = qualifySEB;
        this.qualifyFTS = qualifyFTS;
        this.qualifyEB = qualifyEB;
        this.qualifyBSG = qualifyBSG;
        this.qualifyYGG = qualifyYGG;
    }
}

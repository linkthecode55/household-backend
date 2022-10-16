package com.householdbackend.householdbackend.aggregates;

import com.householdbackend.householdbackend.enums.HousingType;
import com.householdbackend.householdbackend.model.FamilyMember;

import java.util.List;

public class HouseholdAggregate {
    private Integer id;
    private HousingType housingType;
    private List<FamilyMember> familyMembers;

    public HouseholdAggregate(Integer id, HousingType housingType, List<FamilyMember> familyMembers) {
        this.id = id;
        this.housingType = housingType;
        this.familyMembers = familyMembers;
    }

    public Integer getId() {
        return id;
    }

    public HousingType getHousingType() {
        return housingType;
    }

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }
}
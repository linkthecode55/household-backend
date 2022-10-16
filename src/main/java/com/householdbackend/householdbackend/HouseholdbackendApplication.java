package com.householdbackend.householdbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


import com.householdbackend.householdbackend.repository.HouseholdRepository;
import com.householdbackend.householdbackend.repository.FamilyMemberRepository;

import com.householdbackend.householdbackend.model.Household;
import com.householdbackend.householdbackend.model.FamilyMember;

import com.householdbackend.householdbackend.enums.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
@EnableMongoRepositories
public class HouseholdbackendApplication {

    @Autowired
    HouseholdRepository householdRepository;

    @Autowired
    FamilyMemberRepository familyMemberRepository;

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private static final int YEAR_IN_SECONDS = 31536000;

    public static void main(String[] args) {
        SpringApplication.run(HouseholdbackendApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setup() {
        LOG.info("Setting up db");
        deleteOldData();
        createHouseholds();
        createFamilyMembers();
        updateAllHouseholdQualifications();
    }

    private void deleteOldData() {
        LOG.info("Deleting old data");
        householdRepository.deleteAll();
        familyMemberRepository.deleteAll();
    }

    private void createHouseholds() {
        LOG.info("Creating households");
        householdRepository.save(new Household(1, HousingType.Landed));
        householdRepository.save(new Household(2, HousingType.Condominium));
        householdRepository.save(new Household(3, HousingType.HDB));
    }

    private void createFamilyMembers() {
        LOG.info("Creating family members");
        familyMemberRepository.save(new FamilyMember(1, 1, "Tom", Gender.Male,
                MaritalStatus.Single, "None", OccupationType.Unemployed, 0, 31509000));
        familyMemberRepository.save(new FamilyMember(2, 1, "Jerry", Gender.Male,
                MaritalStatus.Single, "None", OccupationType.Student, 0, 1262275200));
        familyMemberRepository.save(new FamilyMember(3, 2, "Adam", Gender.Male,
                MaritalStatus.Married, "Mary", OccupationType.Employed, 30000, 631123200));
        familyMemberRepository.save(new FamilyMember(4, 2, "Mary", Gender.Female,
                MaritalStatus.Married, "Adam", OccupationType.Employed, 30000, 631123200));
        familyMemberRepository.save(new FamilyMember(5, 2, "Jane", Gender.Female,
                MaritalStatus.Single, "Jane", OccupationType.Unemployed, 0, 1577808000));
        familyMemberRepository.save(new FamilyMember(6, 3, "Harry", Gender.Male,
                MaritalStatus.Single, "None", OccupationType.Employed, 30000, 631123200));

    }

    private void updateAllHouseholdQualifications() {
        LOG.info("Updating all household qualifications");
        for (int i = 1; i <= 3; i++) {
            updateHouseholdQualifications(i);
        }
    }

    private void updateHouseholdQualifications(Integer householdId) {
        List<FamilyMember> familyMembersList = familyMemberRepository.getFamilyMembersByHouseholdId(householdId);
        int totalIncome = 0;
        Set<String> spouseNames = new HashSet<String>();
        boolean elderAbove50 = false;
        boolean childUnder16 = false;
        boolean childUnder18 = false;
        boolean childUnder5 = false;
        boolean marriedSpouses = false;

        for (int i = 0; i < familyMembersList.size(); i++) {
            FamilyMember familyMember = familyMembersList.get(i);
            totalIncome += familyMember.getAnnualIncome();
            int age = (int) (System.currentTimeMillis() / 1000 - familyMember.getDateOfBirth()) / YEAR_IN_SECONDS;
            if (age >= 50) elderAbove50 = true;
            if (age < 16) childUnder16 = true;
            if (age < 18) childUnder18 = true;
            if (age < 5) childUnder5 = true;
            if (familyMember.getMaritalStatus() == MaritalStatus.Married) {
                if (spouseNames.contains(familyMember.getName())) {
                    marriedSpouses = true;
                } else {
                    spouseNames.add(familyMember.getSpouseName());
                }
            }
        }

        boolean qualifySEB = childUnder16 && totalIncome < 150000 ? true : false;
        boolean qualifyFTS = marriedSpouses && childUnder18 ? true : false;
        boolean qualifyEB = elderAbove50;
        boolean qualifyBSG = childUnder5;
        boolean qualifyYGG = totalIncome < 100000;

        Optional<Household> household = householdRepository.getHouseholdById(householdId);
        if (household.isPresent()) {
            household.get().updateQualifications(qualifySEB, qualifyFTS, qualifyEB, qualifyBSG, qualifyYGG);
            householdRepository.save(household.get());
        }
    }

}

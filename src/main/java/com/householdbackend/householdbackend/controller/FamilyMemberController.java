package com.householdbackend.householdbackend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.householdbackend.householdbackend.enums.MaritalStatus;
import com.householdbackend.householdbackend.model.FamilyMember;
import com.householdbackend.householdbackend.model.Household;
import com.householdbackend.householdbackend.repository.FamilyMemberRepository;
import com.householdbackend.householdbackend.repository.HouseholdRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@CrossOrigin()
@RestController
@RequestMapping(value = "/family-member")
public class FamilyMemberController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final FamilyMemberRepository familyMemberRepository;
    private final HouseholdRepository householdRepository;

    private static final int YEAR_IN_SECONDS = 31536000;

    public FamilyMemberController(FamilyMemberRepository familyMemberRepository, HouseholdRepository householdRepository) {
        this.familyMemberRepository = familyMemberRepository;
        this.householdRepository = householdRepository;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public FamilyMember createFamilyMember(@RequestBody FamilyMember familyMember) {
        LOG.info("Creating family member");
        PageRequest request = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "_id"));
        Optional<FamilyMember> lastFamilyMember = familyMemberRepository.getPageableFamilyMember(request).stream().findFirst();
        int generatedId;
        if (lastFamilyMember.isPresent()) {
            generatedId = lastFamilyMember.get().getId() + 1;
        } else {
            generatedId = 1;
        }
        familyMember.setId(generatedId);
        updateHouseholdQualifications(familyMember.getHouseholdId());
        return familyMemberRepository.save(familyMember);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void deleteFamilyMember(@RequestParam(name = "id") Integer familyMemberId) {
        LOG.info("Deleting family member");
        Optional<FamilyMember> familyMember = familyMemberRepository.getFamilyMemberById(familyMemberId);
        familyMemberRepository.deleteFamilyMemberById(familyMemberId);
        if (familyMember.isPresent()) {
            updateHouseholdQualifications(familyMember.get().getHouseholdId());
        }
    }

    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    public List<FamilyMember> getAllFamilyMembers() {
        LOG.info("Getting all family members");
        return familyMemberRepository.findAll();
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

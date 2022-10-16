package com.householdbackend.householdbackend.controller;

import java.util.List;
import java.util.Optional;

import com.householdbackend.householdbackend.aggregates.HouseholdAggregate;
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
@RequestMapping(value = "/household")
public class HouseholdController {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final HouseholdRepository householdRepository;
    private final FamilyMemberRepository familyMemberRepository;

    public HouseholdController(HouseholdRepository householdRepository, FamilyMemberRepository familyMemberRepository) {
        this.householdRepository = householdRepository;
        this.familyMemberRepository = familyMemberRepository;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Household createHousehold(@RequestBody Household household) {
        LOG.info("Creating household");
        PageRequest request = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "_id"));
        Optional<Household> lastHousehold = householdRepository.getPageableHousehold(request).stream().findFirst();
        int generatedId;
        if (lastHousehold.isPresent()) {
            generatedId = lastHousehold.get().getId() + 1;
        } else {
            generatedId = 1;
        }
        household.setId(generatedId);
        return householdRepository.save(household);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public void deleteHousehold(@RequestParam(name = "id") Integer householdId) {
        LOG.info("Deleting household");
        householdRepository.deleteHouseholdById(householdId);
        familyMemberRepository.deleteFamilyMembersByHouseholdId(householdId);
    }

    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    public List<Household> getAllHouseholds() {
        LOG.info("Getting all households");
        return householdRepository.findAll();
    }

    @RequestMapping(value = "/list-all", method = RequestMethod.GET)
    public List<HouseholdAggregate> listAllHouseholds() {
        LOG.info("Listing all households");
        return householdRepository.listAllHouseholds();
    }

    @RequestMapping(value = "/list-seb-qualifying-households", method = RequestMethod.GET)
    public List<HouseholdAggregate> listSEBQualifyingHouseholds() {
        LOG.info("Listing SEB qualifying households");
        return householdRepository.listSEBQualifyingHouseholds();
    }

    @RequestMapping(value = "/list-fts-qualifying-households", method = RequestMethod.GET)
    public List<HouseholdAggregate> listFTSQualifyingHouseholds() {
        LOG.info("Listing FTS qualifying households");
        return householdRepository.listFTSQualifyingHouseholds();
    }

    @RequestMapping(value = "/list-eb-qualifying-households", method = RequestMethod.GET)
    public List<HouseholdAggregate> listEBQualifyingHouseholds() {
        LOG.info("Listing EB qualifying households");
        return householdRepository.listEBQualifyingHouseholds();
    }

    @RequestMapping(value = "/list-bsg-qualifying-households", method = RequestMethod.GET)
    public List<HouseholdAggregate> listBSGQualifyingHouseholds() {
        LOG.info("Listing BSG qualifying households");
        return householdRepository.listBSGQualifyingHouseholds();
    }

    @RequestMapping(value = "/list-ygg-qualifying-households", method = RequestMethod.GET)
    public List<HouseholdAggregate> listYGGQualifyingHouseholds() {
        LOG.info("Listing YGG qualifying households");
        return householdRepository.listYGGQualifyingHouseholds();
    }

}

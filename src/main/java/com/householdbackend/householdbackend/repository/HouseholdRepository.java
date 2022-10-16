package com.householdbackend.householdbackend.repository;

import com.householdbackend.householdbackend.aggregates.HouseholdAggregate;
import com.householdbackend.householdbackend.model.Household;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseholdRepository extends MongoRepository<Household, String> {

    @Query("{}")
    Page<Household> getPageableHousehold(Pageable pageable);

    @Query("{'_id': ?0}")
    Optional<Household> getHouseholdById(Integer householdId);


    @DeleteQuery("{'_id': ?0}")
    void deleteHouseholdById(Integer householdId);

    @Aggregation("{$lookup:{from: 'familyMember', localField: '_id', foreignField: 'householdId', as: 'familyMembers'}}")
    List<HouseholdAggregate> listAllHouseholds();


    @Aggregation(pipeline = {
            "{$match:{'qualifySEB':true}}",
            "{$lookup:{from: 'familyMember', localField: '_id', foreignField: 'householdId', as: 'familyMembers'}}"
    })
    List<HouseholdAggregate> listSEBQualifyingHouseholds();

    @Aggregation(pipeline = {
            "{$match:{'qualifyFTS':true}}",
            "{$lookup:{from: 'familyMember', localField: '_id', foreignField: 'householdId', as: 'familyMembers'}}"
    })
    List<HouseholdAggregate> listFTSQualifyingHouseholds();

    @Aggregation(pipeline = {
            "{$match:{'qualifyEB':true}}",
            "{$lookup:{from: 'familyMember', localField: '_id', foreignField: 'householdId', as: 'familyMembers'}}"
    })
    List<HouseholdAggregate> listEBQualifyingHouseholds();

    @Aggregation(pipeline = {
            "{$match:{'qualifyBSG':true}}",
            "{$lookup:{from: 'familyMember', localField: '_id', foreignField: 'householdId', as: 'familyMembers'}}"
    })
    List<HouseholdAggregate> listBSGQualifyingHouseholds();

    @Aggregation(pipeline = {
            "{$match:{'qualifyYGG':true}}",
            "{$lookup:{from: 'familyMember', localField: '_id', foreignField: 'householdId', as: 'familyMembers'}}"
    })
    List<HouseholdAggregate> listYGGQualifyingHouseholds();

}

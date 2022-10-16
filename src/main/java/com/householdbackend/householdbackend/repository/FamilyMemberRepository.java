package com.householdbackend.householdbackend.repository;

import com.householdbackend.householdbackend.model.FamilyMember;
import com.householdbackend.householdbackend.model.Household;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyMemberRepository extends MongoRepository<FamilyMember, String> {

    @Query("{}")
    Page<FamilyMember> getPageableFamilyMember(Pageable pageable);

    @Query("{'householdId': ?0}")
    List<FamilyMember> getFamilyMembersByHouseholdId(Integer householdId);

    @Query("{'_id': ?0}")
    Optional<FamilyMember> getFamilyMemberById(Integer familyMemberId);

    @DeleteQuery("{'householdId': ?0}")
    void deleteFamilyMembersByHouseholdId(Integer householdId);

    @DeleteQuery("{'_id': ?0}")
    void deleteFamilyMemberById(Integer familyMemberId);
}

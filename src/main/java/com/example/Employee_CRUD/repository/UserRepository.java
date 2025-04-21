package com.example.Employee_CRUD.repository;

import feign.Param;

import com.example.Employee_CRUD.model.Users;
import com.example.Employee_CRUD.dto.projection.UserProjection;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<Users, String>{

    @Transactional(readOnly = true)
    UserProjection findByMobileNumber(String mobileNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE instagram_link = :instagramLink)", nativeQuery = true)
    boolean existsByInstagramLink(@Param("instagramLink") String instagramLink);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE facebook_link = :facebookLink)", nativeQuery = true)
    boolean existsByFacebookLink(@Param("facebookLink") String facebookLink);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE whatsapp_number = :whatsappNumber)", nativeQuery = true)
    boolean existsByWhatsappNumber(@Param("whatsappNumber") String whatsappNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE email_id = :emailId)", nativeQuery = true)
    boolean existsByEmailId(@Param("emailId") String emailId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COUNT(1) FROM users WHERE referred_by_mobile_number = :referredByMobileNumber", nativeQuery = true)
    long countUsersByReferredMobileNumber(@Param("referredByMobileNumber") String referredByMobileNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE mobile_number = :mobileNumber)", nativeQuery = true)
    boolean existsByMobileNumber(@Param("mobileNumber") String mobileNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE voter_id = :voterId)", nativeQuery = true)
    boolean existsByVoterId(@Param("voterId") String voterId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET photo_id = :photoId WHERE mobile_number = :mobileNumber", nativeQuery = true)
    int updatePhotoId(@Param("mobileNumber") String mobileNumber, @Param("photoId") String photoId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT CASE WHEN photo_id IS NOT NULL THEN TRUE ELSE FALSE END FROM users WHERE mobile_number = :mobileNumber", nativeQuery = true)
    Boolean getPhotoStatus(@Param("mobileNumber") String mobileNumber);

    @Transactional(readOnly = true)
    @Query(value = "SELECT photo_id FROM users WHERE mobile_number = :mobileNumber", nativeQuery = true)
    String getPhotoId(@Param("mobileNumber") String mobileNumber);
}

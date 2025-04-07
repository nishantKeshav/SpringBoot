package com.example.Employee_CRUD.service;

import com.example.Employee_CRUD.dto.request.*;
import com.example.Employee_CRUD.dto.response.*;
import com.example.Employee_CRUD.exception.UserException;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    userDetailsResponse getUserDetails(userDetailsRequest userDetailsRequest) throws UserException;
    userLoginResponse userLogin(userLoginRequest userLoginRequest) throws UserException;
    userRegisterResponse userRegister(userRegisterRequest userRegisterRequest) throws UserException;
    userAddressResponse getBlocks() throws UserException;
    userAddressResponse getMunicipality(userMunicipalityRequest userMunicipalityRequest) throws UserException;
    userAddressResponse getVillages(userVillageRequest userVillageRequest) throws UserException;
    userProfilePhotoResponse uploadProfilePhoto(MultipartFile file , String mobileNumber) throws UserException;
    userProfilePhotoResponse getProfilePhoto(userLoginRequest userLoginRequest) throws UserException;
    userProfilePhotoResponse deleteProfilePhoto(userLoginRequest userLoginRequest) throws UserException;
}
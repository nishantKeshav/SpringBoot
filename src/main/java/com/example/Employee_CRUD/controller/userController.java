package com.example.Employee_CRUD.controller;

import com.example.Employee_CRUD.exception.UserException;
import com.example.Employee_CRUD.service.UserService;
import com.example.Employee_CRUD.utils.UploadS3;
import jakarta.validation.Valid;

import com.example.Employee_CRUD.dto.request.*;
import com.example.Employee_CRUD.dto.response.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;


import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/log")
public class userController {

    @Autowired
    private UserService userService;

    @Autowired
    private UploadS3 uploadS3;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public userLoginResponse login(@Valid @RequestBody userLoginRequest userLoginRequest) throws UserException {
        userLoginResponse  response = new userLoginResponse();
        try {
            response = userService.userLogin(userLoginRequest);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public userRegisterResponse register(@Valid @RequestBody userRegisterRequest userRegisterRequest) throws UserException {
        userRegisterResponse response = new userRegisterResponse();
        try {
            response = userService.userRegister(userRegisterRequest);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/getUserDetails")
    @ResponseStatus(HttpStatus.OK)
    public userDetailsResponse getUserDetails(@Valid @RequestBody userDetailsRequest userDetailRequest) throws UserException {
        userDetailsResponse response = new userDetailsResponse();
        try {
            response = userService.getUserDetails(userDetailRequest);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @GetMapping("/getBlocks")
    @ResponseStatus(HttpStatus.OK)
    public userAddressResponse getBlocks() throws UserException {
        userAddressResponse response = new userAddressResponse();
        try {
            response = userService.getBlocks();
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/getVillages")
    @ResponseStatus(HttpStatus.OK)
    public userAddressResponse getVillages(@Valid @RequestBody userVillageRequest userVillageRequest) throws UserException {
        userAddressResponse response = new userAddressResponse();
        try {
            response = userService.getVillages(userVillageRequest);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/getMunicipality")
    @ResponseStatus(HttpStatus.OK)
    public userAddressResponse getMunicipality(@Valid @RequestBody userMunicipalityRequest userMunicipalityRequest) throws UserException {
        userAddressResponse response = new userAddressResponse();
        try {
            response = userService.getMunicipality(userMunicipalityRequest);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/uploadProfilePhoto")
    @ResponseStatus(HttpStatus.OK)
    public userProfilePhotoResponse uploadProfilePhoto(@RequestParam("file") MultipartFile file , @RequestParam("mobileNumber") String mobileNumber) throws UserException {
        userProfilePhotoResponse response = new userProfilePhotoResponse();
        try {
            response = userService.uploadProfilePhoto(file , mobileNumber);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/getProfilePhoto")
    @ResponseStatus(HttpStatus.OK)
    public userProfilePhotoResponse getProfilePhoto(@RequestBody userLoginRequest userLoginRequest) throws UserException {
        userProfilePhotoResponse  response = new userProfilePhotoResponse();
        try {
            response = userService.getProfilePhoto(userLoginRequest);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @PostMapping("/deleteProfilePhoto")
    @ResponseStatus(HttpStatus.OK)
    public userProfilePhotoResponse deleteProfilePhoto(@RequestBody userLoginRequest userLoginRequest) throws UserException {
        userProfilePhotoResponse  response = new userProfilePhotoResponse();
        try {
            response = userService.deleteProfilePhoto(userLoginRequest);
        } catch (UserException e) {
            throw new UserException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new UserException("An error occurred while processing the request", HttpStatus.BAD_REQUEST);
        }
        return response;
    }
//    @PostMapping("/check")
//    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
//        System.out.println("File received: " + file.getOriginalFilename());
//        if (file.isEmpty() || !Objects.requireNonNull(file.getContentType()).equals("text/csv")) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a CSV file");
//        }
//        try {
//            saveEmployees(file);
//            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully");
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not process the file");
//        }
//    }
//    public void saveEmployees(MultipartFile file) throws IOException {
//        List<Master> masters = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("BLOCK-NAME", "GRAMPANCHAYAT-NAME", "VILLAGE-NAME").withIgnoreHeaderCase().withTrim())) {
//
//            for (CSVRecord record : csvParser) {
//                Master master = new Master();
//                master.setBlock(record.get("BLOCK-NAME"));
//                master.setGrampanchayat(record.get("GRAMPANCHAYAT-NAME"));
//                master.setVillage(record.get("VILLAGE-NAME"));
//                masters.add(master);
//            }
//        }
//        masterRepository.saveAll(masters);
//    }
}


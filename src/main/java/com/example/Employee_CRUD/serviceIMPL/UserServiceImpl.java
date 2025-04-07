package com.example.Employee_CRUD.serviceIMPL;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.time.LocalDateTime;

import com.example.Employee_CRUD.dto.projection.BlocksProjection;
import com.example.Employee_CRUD.dto.projection.MunicipalityProjection;
import com.example.Employee_CRUD.dto.projection.UserProjection;
import com.example.Employee_CRUD.dto.projection.VillageProjection;
import com.example.Employee_CRUD.dto.request.*;
import com.example.Employee_CRUD.dto.response.*;
import com.example.Employee_CRUD.exception.ExceptionHandler;
import com.example.Employee_CRUD.exception.UserException;
import com.example.Employee_CRUD.model.Users;
import com.example.Employee_CRUD.repository.MasterRepository;
import com.example.Employee_CRUD.repository.UserRepository;
import com.example.Employee_CRUD.service.UserService;
import com.example.Employee_CRUD.utils.BloodGroup;
import com.example.Employee_CRUD.utils.Gender;
import com.example.Employee_CRUD.utils.UploadS3;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UploadS3 fileUploadService;

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userLoginResponse userLogin(userLoginRequest userLoginRequest) throws UserException {
        userLoginResponse response = new userLoginResponse();
        try {
            boolean userCheck = userRepository.existsByMobileNumber(userLoginRequest.getMobileNumber());
            if (!userCheck) {
                setLoginResponse(false , "User not Registered!" , HttpStatus.BAD_REQUEST , response);
                return response;
            }
            setLoginResponse(true, "User login successfull!", HttpStatus.OK, response);
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userRegisterResponse userRegister(final userRegisterRequest request) throws UserException {
        final userRegisterResponse response = new userRegisterResponse();
        try {
            validateRequest(request);
            Users user = mapToUserEntity(request);
            userRepository.save(user);
            buildRegisterSuccessResponse(response);
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userDetailsResponse getUserDetails(userDetailsRequest userDetailsRequest) throws UserException {
        userDetailsResponse response = new userDetailsResponse();
        try {
            String mobileNumber = userDetailsRequest.getMobileNumber();
            UserProjection user = userRepository.findByMobileNumber(mobileNumber);
            if (user == null) {
                throw new UserException("User not found", HttpStatus.NOT_FOUND);
            }
            userData userData = new userData();

            String userId = user.getUserId();
            userId = generate10DigitUserId(userId);

            userData.setUserId(userId);
            userData.setWard(user.getWard());
            userData.setBlock(user.getBlock());
            userData.setEmailId(user.getEmailId());
            userData.setFullName(user.getFullName());
            userData.setDistrict(user.getDistrict());
            userData.setVillage(user.getVillage());
            userData.setMunicipality(user.getMunicipality());
            userData.setMobileNumber(user.getMobileNumber());
            userData.setTotalRegistered((int)userRepository.countUsersByReferredMobileNumber(mobileNumber));

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("User Details fetched successfully!");
            response.setData(userData);
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userAddressResponse getBlocks() throws UserException {
        userAddressResponse response = new userAddressResponse();
        try {
            List<BlocksProjection> blocks = masterRepository.findAllBlocks();
            if (blocks.isEmpty()) {
                throw new UserException("No blocks found", HttpStatus.NOT_FOUND);
            }
            List<String> data = new ArrayList<>();
            for (BlocksProjection block : blocks) {
                data.add(block.getBlock());
            }
            response.setData(data);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Blocks fetched successfully!");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userAddressResponse getMunicipality(userMunicipalityRequest userMunicipalityRequest) throws UserException {
        userAddressResponse response = new userAddressResponse();
        try {
            String block = userMunicipalityRequest.getBlock().trim();
            List<MunicipalityProjection> municipalities = masterRepository.findGrampanchayatByBlock(block);
            if (municipalities.isEmpty()) {
                throw new UserException("No Grampanchayat found for the given block", HttpStatus.NOT_FOUND);
            }
            List<String> data = new ArrayList<>();
            for (MunicipalityProjection municipality : municipalities) {
                data.add(municipality.getGrampanchayat());
            }
            response.setData(data);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Grampanchayat fetched successfully!");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userAddressResponse getVillages(userVillageRequest userVillageRequest) throws UserException {
        userAddressResponse response = new userAddressResponse();
        try {
            String block = userVillageRequest.getBlock().trim();
            String municipality = userVillageRequest.getMunicipality().trim();
            List<VillageProjection> villages = masterRepository.findVillageByBlockGrampanchayat(block , municipality);
            if (villages.isEmpty()) {
                throw new UserException("No Villages found for the given block and Grampanchayat", HttpStatus.NOT_FOUND);
            }
            List<String> data = new ArrayList<>();
            for (VillageProjection village : villages) {
                data.add(village.getVillage());
            }
            response.setData(data);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Villages fetched Successfully!");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userProfilePhotoResponse uploadProfilePhoto(MultipartFile file , String mobileNumber) throws UserException {
        userProfilePhotoResponse response = new userProfilePhotoResponse();
        try {
            if (mobileNumber == null || mobileNumber.length() != 10) {
                throw new UserException("Invalid Mobile Number", HttpStatus.BAD_REQUEST);
            }
            if (!userRepository.existsByMobileNumber(mobileNumber)) {
                throw new UserException("User not found", HttpStatus.NOT_FOUND);
            }

            String extensionName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
            String newFileName = mobileNumber + "-" + "photo" + "." + extensionName;

            Boolean photoStatus = userRepository.getPhotoStatus(mobileNumber);
            if (photoStatus) {
                fileUploadService.deleteFromS3(newFileName);
            } else {
                int updateStatus = userRepository.updatePhotoStatus(mobileNumber , true);
                if (updateStatus == 0) {
                    throw new UserException("Photo upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            fileUploadService.uploadToS3(file.getBytes() , newFileName);
            response.setMessage("Profile Photo uploaded Successfully!");
            response.setStatusCode(HttpStatus.OK.value());

        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userProfilePhotoResponse getProfilePhoto(userLoginRequest userLoginRequest) throws UserException {
        userProfilePhotoResponse response = new userProfilePhotoResponse();
        try {
            String mobileNumber = userLoginRequest.getMobileNumber();
            if (!userRepository.existsByMobileNumber(mobileNumber)) {
                throw new UserException("User not found", HttpStatus.NOT_FOUND);
            }
            String fileName = mobileNumber + "-" + "photo" + "." + "jpg";

            Boolean photoStatus = userRepository.getPhotoStatus(mobileNumber);
            if (!photoStatus) {
                throw new UserException("Profile Photo not found", HttpStatus.NOT_FOUND);
            }
            String photoBase64String = fileUploadService.getFileAsBase64(fileName);

            response.setProfilePhoto(photoBase64String);
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Profile Photo fetched Successfully!");

        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = UserException.class)
    public userProfilePhotoResponse deleteProfilePhoto(userLoginRequest userLoginRequest) throws UserException {
        userProfilePhotoResponse response = new userProfilePhotoResponse();
        try {
            String mobileNumber = userLoginRequest.getMobileNumber();
            if (!userRepository.existsByMobileNumber(mobileNumber)) {
                throw new UserException("User not found", HttpStatus.NOT_FOUND);
            }
            String fileName = mobileNumber + "-" + "photo" + "." + "jpg";
            Boolean photoStatus = userRepository.getPhotoStatus(mobileNumber);
            if (!photoStatus) {
                throw new UserException("Profile Photo not found", HttpStatus.NOT_FOUND);
            }
            int updateStatus = userRepository.updatePhotoStatus(mobileNumber, false);
            if (updateStatus == 0) {
                throw new UserException("Photo deletion failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            fileUploadService.deleteFromS3(fileName);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Profile Photo deleted Successfully!");

        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return response;
    }

    private void validateRequest(final userRegisterRequest request) throws UserException {
        validateGender(request.getGender());
        validateBloodGroup(request.getBloodGroup());
        validateDateOfBirth(request.getDateOfBirth());
        validateUniqueFields(request.getMobileNumber(), request.getVoterId());
        validateEmailId(request.getEmail());
        validateWhatsappNumber(request.getWhatsappNumber());
        validateReferralMobileNumber(request.getReferredByMobileNumber());
        validateSocialMediaFacebook(request.getFacebookLink());
        validateSocialMediaInstagram(request.getInstagramLink());
        validateSelfReferral(request.getMobileNumber(), request.getReferredByMobileNumber());
    }
    private void validateSocialMediaFacebook(final String facebookLink) throws UserException {
        if (facebookLink.isEmpty()) {
            return;
        }
        if (userRepository.existsByFacebookLink(facebookLink)) {
            throw new UserException("Facebook Link already exists", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateSocialMediaInstagram(final String instagramLink) throws UserException {
        if (instagramLink.isEmpty()) {
            return;
        }
        if (userRepository.existsByInstagramLink(instagramLink)) {
            throw new UserException("Instagram Link already exists", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateWhatsappNumber(final String whatsappNumber) throws UserException {
        if (whatsappNumber.isEmpty()) {
            return;
        }
        if (userRepository.existsByWhatsappNumber(whatsappNumber)) {
            throw new UserException("Whatsapp Number already exists", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateEmailId(final String emailId) throws UserException {
        if (emailId.isEmpty()) {
            return;
        }
        if (userRepository.existsByEmailId(emailId)) {
            throw new UserException("Email ID already exists", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateReferralMobileNumber(final String referredByMobileNumber) throws UserException {
        if (!referredByMobileNumber.isEmpty() && referredByMobileNumber.length() != 10) {
            throw new UserException("Referred By Mobile Number must be 10 digits", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateGender(final String gender) throws UserException {
        if (!Gender.isValidGender(gender)) {
            throw new UserException("Invalid Gender", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateBloodGroup(final String bloodGroup) throws UserException {
        if (bloodGroup.isEmpty()) {
            return;
        }
        if (!BloodGroup.isValidBloodGroup(bloodGroup)) {
            throw new UserException("Invalid Blood Group", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateDateOfBirth(final String dateOfBirth) throws UserException {
        LocalDateTime dob = parseDateOfBirth(dateOfBirth);
        if (dob.isAfter(LocalDateTime.now())) {
            throw new UserException("Date of birth cannot be in the future", HttpStatus.BAD_REQUEST);
        }
        if (dob.isAfter(LocalDateTime.now().minusYears(18))) {
            throw new UserException("Age must be 18 or above", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateUniqueFields(final String mobileNumber, final String voterId) throws UserException {
        if (userRepository.existsByMobileNumber(mobileNumber)) {
            throw new UserException("Mobile Number already exists", HttpStatus.BAD_REQUEST);
        }
        if (voterId.isEmpty()) {
            return;
        }
        if (userRepository.existsByVoterId(voterId)) {
            throw new UserException("Voter ID already exists", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateSelfReferral(final String mobileNumber, final String referredByMobileNumber) throws UserException {
        if (mobileNumber.equals(referredByMobileNumber)) {
            throw new UserException("Mobile Number and Referred By Mobile Number cannot be same", HttpStatus.BAD_REQUEST);
        }
    }

    private LocalDateTime parseDateOfBirth(final String dateOfBirth) throws UserException {
        try {
            String[] parts = dateOfBirth.split("-");
            return LocalDateTime.of(
                    Integer.parseInt(parts[0]), // Year
                    Integer.parseInt(parts[1]), // Month
                    Integer.parseInt(parts[2]), // Day
                    0, 0);
        } catch (Exception e) {
            throw new UserException("Invalid Date Format", HttpStatus.BAD_REQUEST);
        }
    }

    private Users mapToUserEntity(final userRegisterRequest request) throws UserException {
        return Users.builder()
                .district(request.getDistrict())
                .block(request.getBlock())
                .municipality(request.getMunicipality())
                .village(request.getVillage())
                .ward(request.getWard())
                .fullName(request.getFullName())
                .fatherName(request.getFatherName())
                .dateOfBirth(parseDateOfBirth(request.getDateOfBirth()))
                .gender(Gender.valueOf(request.getGender().toUpperCase()))
                .mobileNumber(request.getMobileNumber())
                .bloodGroup(request.getBloodGroup().isEmpty() ? null : BloodGroup.valueOf(request.getBloodGroup().toUpperCase()))
                .voterId(request.getVoterId().isEmpty() ? null : request.getVoterId())
                .emailId(request.getEmail().isEmpty() ? null : request.getEmail())
                .whatsappNumber(request.getWhatsappNumber().isEmpty() ? null : request.getWhatsappNumber())
                .facebookLink(request.getFacebookLink().isEmpty() ? null : request.getFacebookLink())
                .instagramLink(request.getInstagramLink().isEmpty()  ? null : request.getInstagramLink())
                .referredByMobileNumber(request.getReferredByMobileNumber().isEmpty() ? null : request.getReferredByMobileNumber())
                .photo(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private void buildRegisterSuccessResponse(final userRegisterResponse response) {
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("User registered successfully");
    }


    private void setLoginResponse(boolean isRegistered, String message, HttpStatus status , userLoginResponse response) {
        response.setIsRegistered(isRegistered);
        response.setMessage(message);
        response.setStatusCode(status.value());
    }

    private String generate10DigitUserId(String userId) {
        return String.format("%1$5s", userId).replace(' ', '0');
    }
}

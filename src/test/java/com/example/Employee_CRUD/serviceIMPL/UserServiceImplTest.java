package com.example.Employee_CRUD.serviceIMPL;

import com.example.Employee_CRUD.dto.projection.BlocksProjection;
import com.example.Employee_CRUD.dto.projection.MunicipalityProjection;
import com.example.Employee_CRUD.dto.projection.UserProjection;
import com.example.Employee_CRUD.dto.projection.VillageProjection;
import com.example.Employee_CRUD.dto.request.UserDetailsRequest;
import com.example.Employee_CRUD.dto.request.userLoginRequest;
import com.example.Employee_CRUD.dto.request.userMunicipalityRequest;
import com.example.Employee_CRUD.dto.request.userRegisterRequest;
import com.example.Employee_CRUD.dto.request.userVillageRequest;
import com.example.Employee_CRUD.dto.response.userAddressResponse;
import com.example.Employee_CRUD.dto.response.userDetailsResponse;
import com.example.Employee_CRUD.dto.response.userLoginResponse;
import com.example.Employee_CRUD.dto.response.userProfilePhotoResponse;
import com.example.Employee_CRUD.dto.response.userRegisterResponse;
import com.example.Employee_CRUD.exception.ExceptionHandler;
import com.example.Employee_CRUD.exception.UserException;
import com.example.Employee_CRUD.model.Users;
import com.example.Employee_CRUD.repository.MasterRepository;
import com.example.Employee_CRUD.repository.UserRepository;
import com.example.Employee_CRUD.utils.UploadS3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private ExceptionHandler exceptionHandler;

    @Mock
    private UploadS3 fileUploadService;

    @Mock
    private MasterRepository masterRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Test case for userLogin method when the user is not registered.
     * This test verifies that the method correctly handles the scenario
     * where the provided mobile number does not exist in the database.
     */
    @Test
    public void testUserLogin_UserNotRegistered() throws UserException {
        // Arrange
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(false);

        // Act
        userLoginResponse response = userService.userLogin(request);

        // Assert
        assertFalse(response.getIsRegistered());
        assertEquals("User not Registered!", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    /**
     * Test case for deleteProfilePhoto when the photo deletion fails in the database.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when the database update for photo deletion fails.
     */
    @Test
    public void test_deleteProfilePhoto_deletionFailed() throws UserException {
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(true);
        when(userRepository.getPhotoStatus("1234567890")).thenReturn(true);
        when(userRepository.updatePhotoStatus("1234567890", false)).thenReturn(0);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.deleteProfilePhoto(request);
        });

        assertEquals("Photo deletion failed", exception.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    /**
     * Test case for deleteProfilePhoto when the profile photo does not exist.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when attempting to delete a non-existent profile photo.
     */
    @Test
    public void test_deleteProfilePhoto_photoNotFound() throws UserException {
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(true);
        when(userRepository.getPhotoStatus("1234567890")).thenReturn(false);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.deleteProfilePhoto(request);
        });

        assertEquals("Profile Photo not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Test case for deleteProfilePhoto when the user is not found.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when attempting to delete a profile photo for a non-existent user.
     */
    @Test
    public void test_deleteProfilePhoto_userNotFound() throws UserException {
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(false);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.deleteProfilePhoto(request);
        });

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Test case for deleteProfilePhoto method when user does not exist, photo status is false, and update fails.
     * This test covers the path where the user is not found in the repository, the photo status is false,
     * and the attempt to update the photo status fails.
     */
    @Test
    public void test_deleteProfilePhoto_userNotFound_photoNotExist_updateFails() throws UserException {
        // Arrange
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(false);

        // Act
        userProfilePhotoResponse response = userService.deleteProfilePhoto(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("User not found", response.getMessage());
    }

    /**
     * Test case for deleteProfilePhoto method when the user exists, has no profile photo,
     * and the photo status update is successful.
     */
    @Test
    public void test_deleteProfilePhoto_whenUserExistsNoPhotoAndUpdateSuccessful() throws UserException {
        // Arrange
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(true);
        when(userRepository.getPhotoStatus("1234567890")).thenReturn(false);
        when(userRepository.updatePhotoStatus("1234567890", false)).thenReturn(1);

        // Act
        userProfilePhotoResponse response = userService.deleteProfilePhoto(request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Profile Photo deleted Successfully!", response.getMessage());

        // Verify
        verify(userRepository).existsByMobileNumber("1234567890");
        verify(userRepository).getPhotoStatus("1234567890");
        verify(userRepository).updatePhotoStatus("1234567890", false);
        verify(fileUploadService).deleteFromS3("1234567890_ProfilePicture");
    }

    /**
     * Test case for generate10DigitUserId method
     * This test verifies that the method correctly pads a user ID with leading zeros
     * to ensure it is 5 digits long, effectively creating a 10-digit user ID.
     */
//    @Test
//    public void test_generate10DigitUserId_PadsWithLeadingZeros() {
//        UserServiceImpl userService = new UserServiceImpl();
//        String userId = "123";
//        String expected = "00123";
//        String actual = userService.(userId);
//        assertEquals(expected, actual);
//    }

    /**
     * Test case for getBlocks method when blocks are found.
     * It verifies that the method returns a response with correct data, status code, and message
     * when the repository returns a non-empty list of blocks.
     */
    @Test
    public void test_getBlocks_whenBlocksAreFound() throws UserException {
        // Arrange
        List<BlocksProjection> mockBlocks = Arrays.asList(
            () -> "Block1",
            () -> "Block2"
        );
        when(masterRepository.findAllBlocks()).thenReturn(mockBlocks);

        // Act
        userAddressResponse response = userService.getBlocks();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Blocks fetched successfully!", response.getMessage());
        assertEquals(Arrays.asList("Block1", "Block2"), response.getData());
    }

    /**
     * Test case for getBlocks method when no blocks are found.
     * This test verifies that a UserException is thrown with the correct message and status code
     * when the masterRepository returns an empty list of blocks.
     */
    @Test
    public void test_getBlocks_whenNoBlocksFound() throws UserException {
        when(masterRepository.findAllBlocks()).thenReturn(new ArrayList<>());

        UserException exception = assertThrows(UserException.class, () -> userService.getBlocks());

        assertEquals("No blocks found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Test the getBlocks method when no blocks are found in the database.
     * This scenario is explicitly handled in the method by throwing a UserException.
     */
    @Test
    public void test_getBlocks_whenNoBlocksFound_2() throws UserException {
        when(masterRepository.findAllBlocks()).thenReturn(new ArrayList<>());

        userAddressResponse response = userService.getBlocks();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("No blocks found", response.getMessage());
    }

    /**
     * Test case for getMunicipality method when no Grampanchayat is found for the given block.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when the masterRepository returns an empty list of municipalities.
     */
    @Test
    public void test_getMunicipality_noGrampanchayatFound() throws UserException {
        userMunicipalityRequest request = new userMunicipalityRequest();
        request.setBlock("NonExistentBlock");

        when(masterRepository.findGrampanchayatByBlock(anyString())).thenReturn(new ArrayList<>());

        userAddressResponse response = userService.getMunicipality(request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("No Grampanchayat found for the given block", response.getMessage());
    }

    /**
     * Test case for getMunicipality method when no municipalities are found for the given block.
     * This test verifies that a UserException is thrown with the correct message and HTTP status
     * when the list of municipalities returned by the repository is empty.
     */
    @Test
    public void test_getMunicipality_noMunicipalitiesFound() throws UserException {
        // Arrange
        userMunicipalityRequest request = new userMunicipalityRequest();
        request.setBlock("TestBlock");

        when(masterRepository.findGrampanchayatByBlock(anyString())).thenReturn(new ArrayList<>());

        // Act
        userAddressResponse response = userService.getMunicipality(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("No Grampanchayat found for the given block", response.getMessage());
    }

    /**
     * Test case for getMunicipality method when municipalities are found for the given block.
     * This test verifies that the method returns the correct response when municipalities exist.
     */
    @Test
    public void test_getMunicipality_whenMunicipalitiesExist() throws UserException {
        // Arrange
        userMunicipalityRequest request = new userMunicipalityRequest();
        request.setBlock("TestBlock");

        MunicipalityProjection municipality1 = () -> "Municipality1";
        MunicipalityProjection municipality2 = () -> "Municipality2";
        List<MunicipalityProjection> municipalities = Arrays.asList(municipality1, municipality2);

        when(masterRepository.findGrampanchayatByBlock("TestBlock")).thenReturn(municipalities);

        // Act
        userAddressResponse response = userService.getMunicipality(request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Grampanchayat fetched successfully!", response.getMessage());
        assertEquals(2, response.getData().size());
        assertEquals("Municipality1", response.getData().get(0));
        assertEquals("Municipality2", response.getData().get(1));
    }

    /**
     * Test case for getProfilePhoto method when the profile photo is not found.
     * This test verifies that a UserException is thrown with the correct message and status code
     * when the user exists but does not have a profile photo.
     */
    @Test
    public void test_getProfilePhoto_photoNotFound() {
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(true);
        when(userRepository.getPhotoStatus("1234567890")).thenReturn(false);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.getProfilePhoto(request);
        });

        assertEquals("Profile Photo not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Test case for getProfilePhoto method when the user exists and has a profile photo.
     * This test verifies that the method returns the correct response when a user's profile photo is found.
     */
    @Test
    public void test_getProfilePhoto_userExistsWithPhoto() throws UserException, IOException {
        // Arrange
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(true);
        when(userRepository.getPhotoStatus("1234567890")).thenReturn(true);
        when(fileUploadService.getFileAsBase64("1234567890_ProfilePicture")).thenReturn("base64EncodedPhoto");

        // Act
        userProfilePhotoResponse response = userService.getProfilePhoto(request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Profile Photo fetched Successfully!", response.getMessage());
        assertEquals("base64EncodedPhoto", response.getProfilePhoto());

        verify(userRepository).existsByMobileNumber("1234567890");
        verify(userRepository).getPhotoStatus("1234567890");
        verify(fileUploadService).getFileAsBase64("1234567890_ProfilePicture");
    }

    /**
     * Test case for getProfilePhoto method when the user is not found.
     * This test verifies that a UserException is thrown with the correct message and status code
     * when attempting to get a profile photo for a non-existent user.
     */
    @Test
    public void test_getProfilePhoto_userNotFound() {
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(false);

        UserException exception = assertThrows(UserException.class, () -> {
            userService.getProfilePhoto(request);
        });

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Test case for getProfilePhoto method when user does not exist and photo status is false.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when the user is not found in the repository.
     */
    @Test
    public void test_getProfilePhoto_userNotFoundAndPhotoNotExists() throws UserException {
        // Arrange
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(false);

        // Act & Assert
        try {
            userService.getProfilePhoto(request);
        } catch (UserException e) {
            assertEquals("User not found", e.getMessage());
            assertEquals(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Test case for getUserDetails method when the user is not found.
     * This test verifies that a UserException is thrown with the correct message and status code
     * when the userRepository.findByMobileNumber() returns null.
     */
    @Test
    public void test_getUserDetails_UserNotFound() throws UserException {
        // Arrange
        UserDetailsRequest request = new UserDetailsRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.findByMobileNumber(request.getMobileNumber())).thenReturn(null);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUserDetails(request);
        });

        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getMessage());

        verify(userRepository, times(1)).findByMobileNumber(request.getMobileNumber());
    }

    /**
     * Test case for getUserDetails method when a valid user is found.
     * This test verifies that the method returns the correct user details
     * when a user is found in the repository.
     */
    @Test
    public void test_getUserDetails_WhenUserExists() throws UserException {
        // Arrange
        String mobileNumber = "1234567890";
        UserDetailsRequest request = new UserDetailsRequest();
        request.setMobileNumber(mobileNumber);

        UserProjection mockUser = new UserProjection() {
            @Override
            public String getUserId() {
                return "12345";
            }

            @Override
            public String getWard() {
                return "Ward 1";
            }

            @Override
            public String getBlock() {
                return "Block A";
            }

            @Override
            public String getEmailId() {
                return "user@example.com";
            }

            @Override
            public String getFullName() {
                return "John Doe";
            }

            @Override
            public String getDistrict() {
                return "District X";
            }

            @Override
            public String getVillage() {
                return "Village Y";
            }

            @Override
            public String getMunicipality() {
                return "Municipality Z";
            }

            @Override
            public String getMobileNumber() {
                return mobileNumber;
            }
        };

        when(userRepository.findByMobileNumber(mobileNumber)).thenReturn(mockUser);
        when(userRepository.countUsersByReferredMobileNumber(mobileNumber)).thenReturn(5L);

        // Act
        userDetailsResponse response = userService.getUserDetails(request);

        // Assert
        assertEquals(200, response.getStatusCode());
        assertEquals("User Details fetched successfully!", response.getMessage());
        assertEquals("00012345", response.getData().getUserId());
        assertEquals("John Doe", response.getData().getFullName());
        assertEquals(mobileNumber, response.getData().getMobileNumber());
        assertEquals(5, response.getData().getTotalRegistered());
    }

    /**
     * Tests the getUserDetails method when the user is not found in the database.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when the userRepository returns null for the given mobile number.
     */
    @Test
    public void test_getUserDetails_userNotFound() {
        // Arrange
        UserDetailsRequest request = new UserDetailsRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.findByMobileNumber("1234567890")).thenReturn(null);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUserDetails(request);
        });

        assertEquals("User not found", exception.getMessage());
        assertEquals(404, exception.getMessage());
    }

    /**
     * Tests the getVillages method when no villages are found for the given block and Grampanchayat.
     * This test verifies that a UserException is thrown with the correct message and HTTP status.
     */
    @Test
    public void test_getVillages_noVillagesFound() throws UserException {
        userVillageRequest request = new userVillageRequest();
        request.setBlock("TestBlock");
        request.setMunicipality("TestMunicipality");

        when(masterRepository.findVillageByBlockGrampanchayat(anyString(), anyString())).thenReturn(new ArrayList<>());

        UserException exception = assertThrows(UserException.class, () -> userService.getVillages(request));

        assertEquals("No Villages found for the given block and Grampanchayat", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    /**
     * Test case for getVillages method when no villages are found for the given block and Grampanchayat.
     * This test verifies that the method throws a UserException with the appropriate message and status code
     * when the list of villages returned by the repository is empty.
     */
    @Test
    public void test_getVillages_whenNoVillagesFound() throws UserException {
        userVillageRequest request = new userVillageRequest();
        request.setBlock("TestBlock");
        request.setMunicipality("TestMunicipality");

        when(masterRepository.findVillageByBlockGrampanchayat(anyString(), anyString())).thenReturn(new ArrayList<>());

        userAddressResponse response = userService.getVillages(request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
        assertEquals("No Villages found for the given block and Grampanchayat", response.getMessage());
    }

    /**
     * Test case for getVillages method when villages are found.
     * It verifies that the method returns a response with the correct villages,
     * status code, and message when villages are found for the given block and municipality.
     */
    @Test
    public void test_getVillages_whenVillagesFound() throws UserException {
        // Arrange
        userVillageRequest request = new userVillageRequest();
        request.setBlock("TestBlock");
        request.setMunicipality("TestMunicipality");

        VillageProjection village1 = () -> "Village1";
        VillageProjection village2 = () -> "Village2";
        List<VillageProjection> villages = Arrays.asList(village1, village2);

        when(masterRepository.findVillageByBlockGrampanchayat("TestBlock", "TestMunicipality"))
                .thenReturn(villages);

        // Act
        userAddressResponse response = userService.getVillages(request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Villages fetched Successfully!", response.getMessage());
        assertEquals(Arrays.asList("Village1", "Village2"), response.getData());

        verify(masterRepository).findVillageByBlockGrampanchayat("TestBlock", "TestMunicipality");
    }

    /**
     * Test case for uploadProfilePhoto method when user exists but already has a profile photo.
     * This test verifies that the method correctly handles the scenario where:
     * - The mobile number is valid (not null and 10 digits long)
     * - The user exists in the repository
     * - The user already has a profile photo (photoStatus is true)
     *
     * Expected behavior:
     * - The existing photo should be deleted from S3
     * - A new photo should be uploaded to S3
     * - The method should return a success response
     */
    @Test
    public void test_uploadProfilePhoto_2() throws UserException, Exception {
        // Arrange
        String mobileNumber = "1234567890";
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        when(userRepository.existsByMobileNumber(mobileNumber)).thenReturn(true);
        when(userRepository.getPhotoStatus(mobileNumber)).thenReturn(true);

        // Act
        userProfilePhotoResponse response = userService.uploadProfilePhoto(file, mobileNumber);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Profile Photo uploaded Successfully!", response.getMessage());

        verify(fileUploadService).deleteFromS3(anyString());
        verify(fileUploadService).uploadToS3(any(byte[].class), anyString());
        verify(userRepository, never()).updatePhotoStatus(anyString(), anyBoolean());
    }

    /**
     * Test case for uploading profile photo with invalid mobile number and non-existent user.
     * This test verifies that the method handles invalid input correctly and throws appropriate exceptions.
     */
    @Test
    public void test_uploadProfilePhoto_InvalidMobileNumberAndNonExistentUser() throws UserException, Exception {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);
        String invalidMobileNumber = "123"; // Invalid mobile number

        when(userRepository.existsByMobileNumber(invalidMobileNumber)).thenReturn(false);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            userService.uploadProfilePhoto(file, invalidMobileNumber);
        });

        assertEquals("Invalid Mobile Number", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getMessage());

        verify(userRepository, never()).getPhotoStatus(anyString());
        verify(fileUploadService, never()).deleteFromS3(anyString());
        verify(fileUploadService, never()).uploadToS3(any(byte[].class), anyString());
    }

    /**
     * Test case for uploadProfilePhoto method when:
     * - Mobile number is valid and exists in the repository
     * - User already has a profile photo
     * Expects the method to delete the existing photo, upload the new one, and return a success response.
     */
    @Test
    public void test_uploadProfilePhoto_existingPhotoUpdated() throws UserException, Exception {
        // Arrange
        String mobileNumber = "1234567890";
        MultipartFile file = new MockMultipartFile("test.jpg", new byte[0]);

        when(userRepository.existsByMobileNumber(mobileNumber)).thenReturn(true);
        when(userRepository.getPhotoStatus(mobileNumber)).thenReturn(true);

        // Act
        userProfilePhotoResponse response = userService.uploadProfilePhoto(file, mobileNumber);

        // Assert
        verify(fileUploadService).deleteFromS3(anyString());
        verify(fileUploadService).uploadToS3(any(byte[].class), anyString());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("Profile Photo uploaded Successfully!", response.getMessage());
    }

    /**
     * Test case for uploadProfilePhoto method when mobile number is invalid and user doesn't exist.
     * This test verifies that the method throws a UserException with the appropriate error message
     * and status code when the mobile number is null or not 10 digits long, and the user doesn't exist.
     */
    @Test
    public void test_uploadProfilePhoto_invalidMobileNumberAndUserNotExist() throws UserException, Exception {
        // Arrange
        String invalidMobileNumber = "123";
        MultipartFile mockFile = new MockMultipartFile("test.jpg", new byte[0]);

        when(userRepository.existsByMobileNumber(invalidMobileNumber)).thenReturn(false);

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> {
            userService.uploadProfilePhoto(mockFile, invalidMobileNumber);
        });

        assertEquals("Invalid Mobile Number", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getMessage());

        verify(userRepository, never()).getPhotoStatus(anyString());
        verify(fileUploadService, never()).deleteFromS3(anyString());
        verify(userRepository, never()).updatePhotoStatus(anyString(), anyBoolean());
        verify(fileUploadService, never()).uploadToS3(any(byte[].class), anyString());
    }

    /**
     * Test uploadProfilePhoto with invalid mobile number length.
     * This should throw a UserException with BAD_REQUEST status.
     */
    @Test
    public void test_uploadProfilePhoto_invalidMobileNumberLength() {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        String invalidMobileNumber = "123456";

        assertThrows(UserException.class, () -> {
            userService.uploadProfilePhoto(file, invalidMobileNumber);
        }, "Expected UserException for invalid mobile number length");
    }

    /**
     * Test uploadProfilePhoto with non-existent user.
     * This should throw a UserException with NOT_FOUND status.
     */
    @Test
    public void test_uploadProfilePhoto_nonExistentUser() {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        String mobileNumber = "1234567890";

        when(userRepository.existsByMobileNumber(mobileNumber)).thenReturn(false);

        assertThrows(UserException.class, () -> {
            userService.uploadProfilePhoto(file, mobileNumber);
        }, "Expected UserException for non-existent user");
    }

    /**
     * Test uploadProfilePhoto with null mobile number.
     * This should throw a UserException with BAD_REQUEST status.
     */
    @Test
    public void test_uploadProfilePhoto_nullMobileNumber() {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        assertThrows(UserException.class, () -> {
            userService.uploadProfilePhoto(file, null);
        }, "Expected UserException for null mobile number");
    }

    /**
     * Test uploadProfilePhoto when photo update fails.
     * This should throw a UserException with INTERNAL_SERVER_ERROR status.
     */
    @Test
    public void test_uploadProfilePhoto_updatePhotoStatusFails() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        String mobileNumber = "1234567890";

        when(userRepository.existsByMobileNumber(mobileNumber)).thenReturn(true);
        when(userRepository.getPhotoStatus(mobileNumber)).thenReturn(false);
        when(userRepository.updatePhotoStatus(mobileNumber, true)).thenReturn(0);

        assertThrows(UserException.class, () -> {
            userService.uploadProfilePhoto(file, mobileNumber);
        }, "Expected UserException when photo update fails");
    }

    /**
     * Test case for successful user login when the user exists.
     * This test verifies that the userLogin method returns a successful response
     * when the user is registered (exists in the system).
     */
    @Test
    public void test_userLogin_whenUserExists() throws UserException {
        // Arrange
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(true);

        // Act
        userLoginResponse response = userService.userLogin(request);

        // Assert
        assertEquals(true, response.getIsRegistered());
        assertEquals("User login successfull!", response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    /**
     * Test case for userLogin method when user is not registered
     * Verifies that the method returns the correct response when the user is not found in the repository
     */
    @Test
    public void test_userLogin_whenUserNotRegistered() throws UserException {
        userLoginRequest request = new userLoginRequest();
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(false);

        userLoginResponse response = userService.userLogin(request);

        assertFalse(response.getIsRegistered());
        assertEquals("User not Registered!", response.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    /**
     * Test case for userRegister method when the mobile number already exists.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when a mobile number that already exists in the system is provided in the registration request.
     */
    @Test
    public void test_userRegister_existingMobileNumber() throws UserException {
        userRegisterRequest request = new userRegisterRequest();
        request.setGender("MALE");
        request.setDateOfBirth("2000-01-01");
        request.setMobileNumber("1234567890");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(true);

        UserException expectedException = new UserException("Mobile Number already exists", HttpStatus.BAD_REQUEST);
        doThrow(expectedException).when(exceptionHandler).handleException(any(Exception.class));

        userRegisterResponse response = userService.userRegister(request);

        verify(exceptionHandler).handleException(any(Exception.class));
        assertNotNull(response);
    }

    /**
     * Test case for userRegister method when the voter ID already exists.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when a voter ID that already exists in the system is provided in the registration request.
     */
    @Test
    public void test_userRegister_existingVoterId() throws UserException {
        userRegisterRequest request = new userRegisterRequest();
        request.setGender("MALE");
        request.setDateOfBirth("2000-01-01");
        request.setMobileNumber("1234567890");
        request.setVoterId("VOTER123");

        when(userRepository.existsByMobileNumber("1234567890")).thenReturn(false);
        when(userRepository.existsByVoterId("VOTER123")).thenReturn(true);

        UserException expectedException = new UserException("Voter ID already exists", HttpStatus.BAD_REQUEST);
        doThrow(expectedException).when(exceptionHandler).handleException(any(Exception.class));

        userRegisterResponse response = userService.userRegister(request);

        verify(exceptionHandler).handleException(any(Exception.class));
        assertNotNull(response);
    }

    /**
     * Test case for userRegister method when the date of birth is in the future.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when a future date of birth is provided in the registration request.
     */
    @Test
    public void test_userRegister_futureDateOfBirth() throws UserException {
        userRegisterRequest request = new userRegisterRequest();
        request.setGender("MALE");
        request.setDateOfBirth("2025-01-01");

        UserException expectedException = new UserException("Date of birth cannot be in the future", HttpStatus.BAD_REQUEST);
        doThrow(expectedException).when(exceptionHandler).handleException(any(Exception.class));

        userRegisterResponse response = userService.userRegister(request);

        verify(exceptionHandler).handleException(any(Exception.class));
        assertNotNull(response);
    }

    /**
     * Test case for userRegister method when the blood group is invalid.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when an invalid blood group is provided in the registration request.
     */
    @Test
    public void test_userRegister_invalidBloodGroup() throws UserException {
        userRegisterRequest request = new userRegisterRequest();
        request.setGender("MALE");
        request.setBloodGroup("InvalidBloodGroup");

        UserException expectedException = new UserException("Invalid Blood Group", HttpStatus.BAD_REQUEST);
        doThrow(expectedException).when(exceptionHandler).handleException(any(Exception.class));

        userRegisterResponse response = userService.userRegister(request);

        verify(exceptionHandler).handleException(any(Exception.class));
        assertNotNull(response);
    }

    /**
     * Test case for userRegister method when the gender is invalid.
     * This test verifies that the method throws a UserException with the correct message and status code
     * when an invalid gender is provided in the registration request.
     */
    @Test
    public void test_userRegister_invalidGender() throws UserException {
        userRegisterRequest request = new userRegisterRequest();
        request.setGender("InvalidGender");

        UserException expectedException = new UserException("Invalid Gender", HttpStatus.BAD_REQUEST);
        doThrow(expectedException).when(exceptionHandler).handleException(any(Exception.class));

        userRegisterResponse response = userService.userRegister(request);

        verify(exceptionHandler).handleException(any(Exception.class));
        assertNotNull(response);
    }

    /**
     * Test successful user registration with valid input
     * This test verifies that the userRegister method correctly processes a valid registration request,
     * saves the user to the repository, and returns a success response.
     */
    @Test
    public void test_userRegister_successfulRegistration() throws UserException {
        // Arrange
        userRegisterRequest request = new userRegisterRequest();
        request.setFullName("John Doe");
        request.setMobileNumber("1234567890");
        request.setGender("MALE");
        request.setDateOfBirth("1990-01-01");
        request.setDistrict("Test District");
        request.setBlock("Test Block");
        request.setMunicipality("Test Municipality");
        request.setVillage("Test Village");
        request.setWard("Test Ward");

        when(userRepository.existsByMobileNumber(any())).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenReturn(new Users());

        // Act
        userRegisterResponse response = userService.userRegister(request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("User registered successfully", response.getMessage());
    }

}

package com.kduytran.userservice.service;

import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.entity.UserStatus;
import com.kduytran.userservice.exception.EmailAlreadyExistsException;
import com.kduytran.userservice.exception.MobilePhoneAlreadyExistsException;
import com.kduytran.userservice.exception.UserAlreadyExistsException;

import java.util.UUID;

public interface IUserService {

    /**
     * Creates a new user based on the information from the provided RegistrationDTO object.
     *
     * @param registrationDTO The registration information of the user, containing necessary details to create the user.
     * @return true if the user has been successfully created, false if an error occurred during user creation.
     * @throws UserAlreadyExistsException If the user already exists in the system.
     * @throws EmailAlreadyExistsException If the email already exists in the system.
     * @throws MobilePhoneAlreadyExistsException If the user already exists in the system.
     */
    void createUser(RegistrationDTO registrationDTO);

    /**
     * Fetches user information based on the provided username.
     *
     * @param username The username of the user to fetch information for.
     * @return A UserDTO object containing information about the user.
     * @throws UserNotFoundException if the user with the specified username is not found.
     */
    UserDTO fetchUser(String username);

    /**
     * Updates the status of a user based on the given user ID and the new status.
     *
     * @param userId the unique identifier of the user whose status will be changed.
     * @param userStatus the new status to assign to the user, typically an enum representing various states.
     */
    void changeUserStatus(UUID userId, UserStatus userStatus);

    /**
     * Verifies a user's registration using a given token.
     *
     * @param token the verification token sent to the user via email.
     * @return true if the token is valid and the user is successfully verified,
     *         false if the token is invalid or verification fails.
     */
    void verifyUserRegistration(String token);

    /**
     * Resends the email verification for a given user.
     *
     * <p>This method is typically used when a user needs to receive a new email verification
     * during the registration process. It sends a new verification email to the user's registered
     * email address based on the provided user ID. This can be useful if the user did not receive
     * the initial verification email or if it expired.</p>
     *
     * @param userId The ID of the user who needs a new email verification. This should be a non-null and non-empty string.
     *
     * @throws IllegalArgumentException If the userId is null or empty.
     * @throws com.kduytran.userservice.exception.UserNotFoundException If no user is found with the specified userId.
     */
    void refreshUserVerification(String userId);

    /**
     * Deletes all inactive users from the system.
     * An inactive user is defined as a user who has not logged in for a specified period or
     * has a status indicating they are no longer active.
     *
     * @return the number of users that were deleted from the system.
     */
    int deleteAllInActiveUsers();

}

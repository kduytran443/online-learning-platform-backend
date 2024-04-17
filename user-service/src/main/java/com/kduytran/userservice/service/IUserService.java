package com.kduytran.userservice.service;

import com.kduytran.userservice.dto.RegistrationDTO;
import com.kduytran.userservice.dto.UserDTO;
import com.kduytran.userservice.exception.EmailAlreadyExistsException;
import com.kduytran.userservice.exception.MobilePhoneAlreadyExistsException;
import com.kduytran.userservice.exception.UserAlreadyExistsException;

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

}

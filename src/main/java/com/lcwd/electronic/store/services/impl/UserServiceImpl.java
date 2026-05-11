package com.lcwd.electronic.store.services.impl;


import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.EmailService;
import com.lcwd.electronic.store.services.UserService;
import com.lcwd.electronic.store.util.AppConstant;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SendGridEmailService emailService;

    @Autowired
    private RoleRepository roleRepository;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        // Check if user already exists
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists with email: " + userDto.getEmail());
        }

        User user =mapper.map(userDto,User.class);
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findFirstByRoleName("ROLE_" + AppConstant.GUEST_ROLE)
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            user.getRoles().add(defaultRole);
        }



        User savedUser=userRepository.save(user);

        emailService.sendEmail(savedUser.getEmail(),savedUser.getName());

        return mapper.map(savedUser,UserDto.class);
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
        user.setName(userDto.getName());
        //email update
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());

        if (!userDto.getPassword().equalsIgnoreCase(user.getPassword()))
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setImageName(userDto.getImageName());
        //save data
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));

        // Delete user profile image - ONLY if image name exists
        if (user.getImageName() != null && !user.getImageName().trim().isEmpty()) {
            String fullPath = imagePath + user.getImageName();
            try {
                Path path = Paths.get(fullPath);
                if (Files.exists(path)) {
                    Files.delete(path);
                    logger.info("Deleted user image: {}", fullPath);
                } else {
                    logger.info("User image not found: {}", fullPath);
                }
            } catch (IOException e) {
                logger.error("Error deleting user image: {}", e.getMessage());
            }
        } else {
            logger.info("User has no profile image to delete");
        }

        // ALWAYS delete user from database
        userRepository.delete(user);
        logger.info("User deleted successfully: {}", userId);
    }



    @Override
    public Page<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ?
                (Sort.by(sortBy).descending()) :
                (Sort.by(sortBy).ascending());

        // pageNumber default starts from 0
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> page = userRepository.findAll(pageable);

        return page.map(user -> mapper.map(user, UserDto.class));
    }





@Override
public UserDto getUserById(String userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id !!"));
    return entityToDto(user);
}

@Override
public UserDto getUserByEmail(String email) {
    User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with given email id !!"));
    return entityToDto(user);
}

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.searchUser(keyword);
        return users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

@Override
public Optional<User> findUserByEmailOptional(String email) {
    return userRepository.findByEmail(email);
}

private UserDto entityToDto(User savedUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();
    return mapper.map(savedUser, UserDto.class);

}

private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
    return mapper.map(userDto, User.class);
}
}

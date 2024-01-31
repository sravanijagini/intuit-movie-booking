package com.example.intuitmoviebooking.services;

import com.example.intuitmoviebooking.model.User;
import com.example.intuitmoviebooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUserById(String userMailId){
        return userRepository.findAll(userMailId);
    }

    public boolean addNewUser(User new_user){
        // Get all Cities and add if it is not a duplicate
        List<User> userList = getUserById(new_user.getMailId());
        boolean anyMatch = userList.stream()
                .anyMatch(user -> user.getMailId().equals(new_user.getMailId()));

        if(!anyMatch){
            adduserIntoDB(new_user);
        }
        return anyMatch;
    }

    public void adduserIntoDB(User user){
        userRepository.save(user);
    }
}

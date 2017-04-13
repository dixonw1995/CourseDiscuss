package com.jedd.service;

import com.jedd.model.UserRole;
import java.util.ArrayList;
import javax.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jedd.dao.UserRepository;
import com.jedd.exception.UserNotFound;
import com.jedd.exception.UsernameExists;
import com.jedd.model.User;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Resource
    UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        com.jedd.model.User user = userRepo.findOne(username);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }

    @Transactional(rollbackFor = UsernameExists.class)
    public String addUser(String username, String password) throws UsernameExists {
        return addUser(username, password, new String[]{"ROLE_USER"});
    }

    @Transactional(rollbackFor = UsernameExists.class)
    public String addUser(String username, String password, String[] roles)
            throws UsernameExists {
        if (userRepo.exists(username)) {
            throw new UsernameExists();
        }
        password = passwordEncoder.encode(password);
        User user = new User(username, password, roles);
        com.jedd.model.User savedUser = userRepo.save(user);
        return savedUser.getUsername();
    }

    @Transactional
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Transactional
    public User getUser(String username) throws UserNotFound {
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        return userRepo.findOne(username);
    }

    @Transactional(rollbackFor = UserNotFound.class)
    public void deleteUser(String username) throws UserNotFound {
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        userRepo.delete(username);
    }

    @Transactional(rollbackFor = UserNotFound.class)
    public void addRole(String username, String role) throws UserNotFound {
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        User user = userRepo.findOne(username);
        user.addRole(role);
        userRepo.save(user);
    }

    @Transactional(rollbackFor = UserNotFound.class)
    public void removeRole(String username, String role) throws UserNotFound {
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        User user = userRepo.findOne(username);
        user.addRole(role);
        userRepo.save(user);
    }

    @Transactional(rollbackFor = UserNotFound.class)
    public String editUser(String username, String password, String[] roles)
            throws UserNotFound {
        if (!userRepo.exists(username)) {
            throw new UserNotFound();
        }
        User user = userRepo.findOne(username);
        user.setPassword(passwordEncoder.encode(password));
        user.removeRoles();
        for (String role : roles) {
            user.addRole(role);
        }
        com.jedd.model.User savedUser = userRepo.save(user);
        return savedUser.getUsername();
    }
}

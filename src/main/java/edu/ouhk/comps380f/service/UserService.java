package edu.ouhk.comps380f.service;

import edu.ouhk.comps380f.model.UserRole;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import edu.ouhk.comps380f.dao.UserRepository;
import edu.ouhk.comps380f.exception.UserNotFound;
import edu.ouhk.comps380f.exception.UsernameExists;
import edu.ouhk.comps380f.model.User;
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
        edu.ouhk.comps380f.model.User user = userRepo.findOne(username);
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
        edu.ouhk.comps380f.model.User savedUser = userRepo.save(user);
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
        password = passwordEncoder.encode(password);
        User user = new User(username, password, roles);
        edu.ouhk.comps380f.model.User savedUser = userRepo.save(user);
        return savedUser.getUsername();
    }
}

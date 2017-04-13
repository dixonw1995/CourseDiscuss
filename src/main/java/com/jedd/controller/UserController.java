package com.jedd.controller;

import java.io.IOException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import com.jedd.dao.UserRepository;
import com.jedd.exception.UserNotFound;
import com.jedd.exception.UsernameExists;
import com.jedd.model.User;
import com.jedd.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    UserRepository UserRepo;
    
    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String read(ModelMap model) {
        logger.info("browsing users");
        model.addAttribute("users", userService.getUsers());
        return "userList";
    }
    
    public static class Form {
        String username;
        String password;
        String[] roles;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
        
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        logger.info("creating new user account");
        return new ModelAndView("createUser", "user", new Form());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(Form form) throws IOException {
        try {
            userService.addUser(form.getUsername(), form.getPassword(),
                    form.getRoles());
            logger.info("User " + form.getUsername() + " created.");
            return new RedirectView("/user?create", true);
        } catch (UsernameExists ex) {
            logger.info("'" + form.getUsername() + "' has been registered.");
        }
        return new RedirectView("/user?UsernameExists", true);
    }

    @RequestMapping(value = "update/{username}", method = RequestMethod.GET)
    public ModelAndView update(@PathVariable("username") String username) {
        logger.info("editting user#" + username + " account");
        ModelAndView mav = new ModelAndView("editUser", "user", new Form());
        mav.addObject("username", username);
        return mav;
    }

    @RequestMapping(value = "update/{username}", method = RequestMethod.POST)
    public View update(Form form,
            @PathVariable("username") String username) throws IOException {
        try {
            userService.editUser(username, form.getPassword(),
                    form.getRoles());
            logger.info("User " + username + " editted.");
            return new RedirectView("/user?edit", true);
        } catch (UserNotFound ex) {
            logger.info("'" + username + "' is not found.");
        }
        return new RedirectView("/user?UserNotFound", true);
    }

    @RequestMapping(value = "delete/{username}", method = RequestMethod.GET)
    public View deleteUser(@PathVariable("username") String username) {
        try {
            userService.deleteUser(username);
            logger.info("User " + username + " deleted.");
            return new RedirectView("/user?delete", true);
        } catch (UserNotFound ex) {
            logger.info("'" + username + "' is not found.");
        }
        return new RedirectView("/user?UserNotFound", true);
    }

}

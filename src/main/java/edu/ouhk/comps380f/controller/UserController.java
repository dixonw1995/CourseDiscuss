package edu.ouhk.comps380f.controller;

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
import edu.ouhk.comps380f.dao.UserRepository;
import edu.ouhk.comps380f.exception.UserNotFound;
import edu.ouhk.comps380f.exception.UsernameExists;
import edu.ouhk.comps380f.model.User;
import edu.ouhk.comps380f.service.UserService;

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
        model.addAttribute("users", userService.getUsers());
        return "userList";
    }

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("createUser", "user", new User());
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(User form) throws IOException {
        try {
            userService.addUser(form.getUsername(), form.getPassword(),
                    form.getRoles().toArray(new String[0]));
            logger.info("User " + form.getUsername() + " created.");
            return new RedirectView("/user?create", true);
        } catch (UsernameExists ex) {
            logger.info("'" + form.getUsername() + "' has been registered.");
        }
        return new RedirectView("/user?UsernameExists", true);
    }

    @RequestMapping(value = "update/{username}", method = RequestMethod.GET)
    public ModelAndView update(@PathVariable("username") String username) {
        ModelAndView mav = new ModelAndView("editUser", "user", new User());
        mav.addObject("username", username);
        return mav;
    }

    @RequestMapping(value = "update/{username}", method = RequestMethod.POST)
    public View update(User form,
            @PathVariable("username") String username) throws IOException {
        try {
            userService.editUser(username, form.getPassword(),
                    form.getRoles().toArray(new String[0]));
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

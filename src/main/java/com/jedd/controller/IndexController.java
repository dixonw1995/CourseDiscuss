package com.jedd.controller;

import com.jedd.exception.UsernameExists;
import com.jedd.model.User;
import com.jedd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class IndexController {

    @Autowired
    UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/")
    public View index() {
        return new RedirectView("/forum", true);
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public ModelAndView register() {
        return new ModelAndView("register", "user", new User());
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public View register(User form) {
        try {
            UserDetails user = userService.loadUserByUsername(
                    userService.addUser(form.getUsername(),
                            form.getPassword()));
            logger.info("User " + form.getUsername() + " created.");
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new RedirectView("/", true);
        } catch (UsernameExists ex) {
            logger.info("'" + form.getUsername() + "' has been registered.");
        }
        return new RedirectView("/register?UsernameExists", true);
    }

}

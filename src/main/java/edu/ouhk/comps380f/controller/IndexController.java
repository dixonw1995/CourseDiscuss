package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.exception.UsernameExists;
import edu.ouhk.comps380f.model.User;
import edu.ouhk.comps380f.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
            userService.addUser(form.getUsername(),
                    form.getPassword());
            logger.info("User " + form.getUsername() + " created.");
            return new RedirectView("/login?register", true);
        } catch (UsernameExists ex) {
            logger.info("'" + form.getUsername() + "' has been registered.");
        }
        return new RedirectView("/register?UsernameExists", true);
    }

}

package edu.ouhk.comps380f.controller;

import edu.ouhk.comps380f.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DebugController {

    @Autowired
    UserService userService;

    @RequestMapping("debug")
    public String login(ModelMap map) {
        map.addAttribute("users", userService.getUsers());
        return "debug";
    }
}

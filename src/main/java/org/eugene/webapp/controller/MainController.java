package org.eugene.webapp.controller;

import org.eugene.webapp.core.parsing.Data;
import org.eugene.webapp.services.AdminService;
import org.eugene.webapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    private final AdminService adminService;
    private final UserService userService;
    private String currentCommand = "";

    @Autowired
    public MainController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
        adminService.setPathToDB("C:\\Users\\ED.Kosinov\\Documents\\MagistracyProjects\\webapp-for-magistracy\\src\\main\\resources\\db");
        adminService.setPathToScripts("C:\\Users\\ED.Kosinov\\Documents\\MagistracyProjects\\webapp-for-magistracy\\src\\main\\resources\\scripts");
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/main")
    public String mainPage() {
        return "index";
    }

    @RequestMapping(value = "/admin")
    public ModelAndView adminControl(@RequestParam Map<String, String> requestParams, ModelMap modelMap) {
        String command = requestParams.get("command_text");
        if(command != null && !command.equals(currentCommand)){
            List<String> resultCommand = adminService.executeCommand(command);
            currentCommand = command;
            modelMap.addAttribute("resultCommand",resultCommand);
        }
        return new ModelAndView("admin");
    }

    @RequestMapping(value = "/user")
    public ModelAndView userSend(@RequestParam Map<String, String> requestParams) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mqttBroker = requestParams.get("mqtt");
        String topic = requestParams.get("topic");
        String content = requestParams.get("content");
        userService.sendMessage(authentication.getName(),mqttBroker,topic,content);
        return new ModelAndView("user");
    }

    @RequestMapping(value = "/user_view_queue")
    public ModelAndView userQueueWithConverters(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LinkedList<Data> queue = userService.qetQueueData(authentication.getName());
        modelMap.put("queue",queue);
        return new ModelAndView("view-queue");
    }

    @RequestMapping(value = "/user_view_queue_two")
    public ModelAndView userQueueWithoutConverters(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LinkedList<Data> queue = userService.qetQueueData(authentication.getName());
        modelMap.put("queue",queue);
        return new ModelAndView("view-queue-two");
    }

    @RequestMapping(value = "/user_input_data")
    public ModelAndView userInputData(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<Data> inputData = userService.getInputData(authentication.getName());
        modelMap.put("inputData",inputData);
        return new ModelAndView("view-input");
    }

    @RequestMapping("/logout")
    public String logout(ModelMap model) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        authentication.setAuthenticated(false);

        model.addAttribute("logout", "true");
        return "login";
    }

    @RequestMapping("/error")
    public String error(ModelMap model) {
        model.addAttribute("error", "true");
        return "login";
    }
}
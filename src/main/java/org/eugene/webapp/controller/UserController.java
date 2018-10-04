package org.eugene.webapp.controller;

import org.eugene.webapp.core.parsing.Data;
import org.eugene.webapp.services.UserService;
import org.eugene.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@SessionAttributes(value = "userJSP")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    private boolean checkUser(String login, String password){
        if(login == null || password == null) return false;
        return userService.checkUser(login,password);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userJSP", new User());
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @ModelAttribute("userJSP")
    public User createUser() {
        return new User();
    }

    @RequestMapping(value = "/user_service", method = RequestMethod.POST)
    public ModelAndView userService(@ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();
        String userLogin = user.getLogin();
        String userPassword = user.getPassword();
        modelAndView.setViewName("error");
        if(checkUser(userLogin,userPassword)){
            modelAndView.setViewName("user");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/user_send", method = RequestMethod.POST)
    public ModelAndView userSend(@RequestParam Map<String, String> requestParams, @ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user");
        if(!checkUser(user.getLogin(),user.getPassword())){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        String mqttBroker = requestParams.get("mqtt");
        String topic = requestParams.get("topic");
        String content = requestParams.get("content");
        userService.sendMessage(user.getLogin(),mqttBroker,topic,content);
        return modelAndView;
    }

    @RequestMapping(value = "/user_queue", method = RequestMethod.GET)
    public ModelAndView userQueueWithConverters(ModelMap modelMap, @ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view-queue");
        if(!checkUser(user.getLogin(),user.getPassword())){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        LinkedList<Data> queue = userService.qetQueueData(user.getLogin());
        modelMap.put("queue",queue);
        return modelAndView;
    }

    @RequestMapping(value = "/user_queue_two", method = RequestMethod.GET)
    public ModelAndView userQueueWithoutConverters(ModelMap modelMap, @ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view-queue-two");
        if(!checkUser(user.getLogin(),user.getPassword())){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        LinkedList<Data> queue = userService.qetQueueData(user.getLogin());
        modelMap.put("queue",queue);
        return modelAndView;
    }

    @RequestMapping(value = "/user_input_data", method = RequestMethod.GET)
    public ModelAndView userInputData(ModelMap modelMap, @ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view-input");
        if(!checkUser(user.getLogin(),user.getPassword())){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        Collection<Data> inputData = userService.getInputData(user.getLogin());
        modelMap.put("inputData",inputData);
        return modelAndView;
    }

    @RequestMapping(value = "/user_exit", method = RequestMethod.POST)
    public ModelAndView userExit(SessionStatus sessionStatus) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        sessionStatus.setComplete();
        return modelAndView;
    }
}

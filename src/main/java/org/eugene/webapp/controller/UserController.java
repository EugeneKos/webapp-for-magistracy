package org.eugene.webapp.controller;

import org.eugene.server.core.parsing.Data;
import org.eugene.server.services.UserService;
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
    @Autowired
    private UserService userService;

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
        if(userLogin != null && userPassword != null){
            if(userService.checkUser(userLogin,userPassword)){
                modelAndView.setViewName("user");
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/user_send", method = RequestMethod.POST)
    public ModelAndView userSend(@RequestParam Map<String, String> requestParams, @ModelAttribute("userJSP") User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user");
        if(user.getLogin() == null){
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
        if(user.getLogin() == null){
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
        if(user.getLogin() == null){
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
        if(user.getLogin() == null){
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

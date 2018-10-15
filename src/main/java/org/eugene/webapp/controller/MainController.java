package org.eugene.webapp.controller;

import org.eugene.webapp.core.model.filter.Data;
import org.eugene.webapp.services.AdminService;
import org.eugene.webapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController {
    private final AdminService adminService;
    private final UserService userService;
    private String currentCommand = "";

    @Autowired
    public MainController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
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
        List<String> resultCommand = new ArrayList<>();
        if(command != null){
            if(!command.equals(currentCommand)){
                resultCommand.addAll(adminService.executeCommand(command));
                currentCommand = command;
            } else {
                resultCommand.add("Same command entered");
            }
        }
        modelMap.addAttribute("resultCommand",resultCommand);
        return new ModelAndView("admin");
    }

    @RequestMapping(value = "/admin_upload_file")
    public ModelAndView adminSendFileScript(@RequestParam("file") MultipartFile multipartFile, ModelMap modelMap){
        List<String> resultCommand = new ArrayList<>();
        String pathToScript = adminService.getPathToScripts()+ File.separator;
        try {
            byte[] bytes = multipartFile.getBytes();
            if(bytes.length != 0){
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathToScript+multipartFile.getOriginalFilename()));
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                resultCommand.add("Script file uploaded");
            } else {
                resultCommand.add("Script file is not chosen");
            }
        } catch (IOException e) {
            e.printStackTrace();
            resultCommand.add("Error to upload script file");
        }
        modelMap.addAttribute("resultCommand",resultCommand);
        return new ModelAndView("admin");
    }

    @RequestMapping(value = "/admin_operation")
    public ModelAndView adminOperationControl(ModelMap modelMap) {
        modelMap.addAttribute("listOperation",adminService.getOperationBuffer());
        return new ModelAndView("admin-operation");
    }

    @RequestMapping(value = "/user")
    public ModelAndView userSend(@RequestParam Map<String, String> requestParams, ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        modelMap.addAttribute("devices",userService.getDevices(authentication.getName()));
        String deviceName = requestParams.get("deviceName");
        String commandName = requestParams.get("commandName");
        if(deviceName != null && commandName != null && requestParams.get("params") != null){
            String[] params = requestParams.get("params").split("\\s+");
            userService.useTheDevice(authentication.getName(),deviceName,commandName,params);
        }
        return new ModelAndView("user");
    }

    @RequestMapping(value = "/user_view_queue")
    public ModelAndView userQueueWithConverters(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LinkedList<Data> queue = userService.qetQueueData(authentication.getName());
        modelMap.addAttribute("queue",queue);
        return new ModelAndView("view-queue");
    }

    @RequestMapping(value = "/user_view_queue_two")
    public ModelAndView userQueueWithoutConverters(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LinkedList<Data> queue = userService.qetQueueData(authentication.getName());
        modelMap.addAttribute("queue",queue);
        return new ModelAndView("view-queue-two");
    }

    @RequestMapping(value = "/user_input_data")
    public ModelAndView userInputData(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<Data> inputData = userService.getInputData(authentication.getName());
        modelMap.addAttribute("inputData",inputData);
        return new ModelAndView("view-input");
    }

    @RequestMapping(value = "/user_mqtt_status")
    public ModelAndView userViewMqttConnectsStatus(ModelMap modelMap) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> mqttConnectsStatus = userService.getStatusMqttConnects(authentication.getName());
        modelMap.addAttribute("mqttConnectsStatus",mqttConnectsStatus);
        return new ModelAndView("view-mqtt-status");
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
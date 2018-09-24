package org.eugene.webapp.controller;

import org.eugene.server.services.AdminService;
import org.eugene.webapp.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@SessionAttributes(value = "adminJSP")
public class AdminController {
    private final AdminService adminService;
    private final String password = "1";
    private String currentCommand = "";

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public ModelAndView adminInit(@ModelAttribute("adminJSP") Admin admin) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("adminJSP", new Admin());
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @ModelAttribute("adminJSP")
    public Admin createAdmin() {
        adminService.setPathToDB("C:\\Users\\ED.Kosinov\\Documents\\MagistracyProjects\\programmable-server\\src\\main\\resources\\db");
        adminService.setPathToScripts("C:\\Users\\ED.Kosinov\\Documents\\MagistracyProjects\\programmable-server\\src\\main\\resources\\scripts");
        return new Admin();
    }

    @RequestMapping(value = "/admin_service", method = RequestMethod.POST)
    public ModelAndView adminService(@ModelAttribute("adminJSP") Admin admin) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        String adminPassword = admin.getPassword();
        if(adminPassword != null && adminPassword.equals(password)){
            modelAndView.setViewName("admin-control");
            return modelAndView;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/admin_control", method = RequestMethod.POST)
    public ModelAndView adminControl(@RequestParam Map<String, String> requestParams, @ModelAttribute("adminJSP") Admin admin) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin-control");
        String command = requestParams.get("command_text");
        if(admin.getPassword() == null){
            modelAndView.setViewName("error");
            return modelAndView;
        }
        if(command != null && !command.equals(currentCommand)){
            adminService.executeCommand(command);
            currentCommand = command;
        }
        return modelAndView;
    }

    @RequestMapping(value = "/admin_exit", method = RequestMethod.POST)
    public ModelAndView adminExit(SessionStatus sessionStatus) {
        ModelAndView modelAndView = new ModelAndView();
        sessionStatus.setComplete();
        modelAndView.setViewName("admin");
        return modelAndView;
    }
}

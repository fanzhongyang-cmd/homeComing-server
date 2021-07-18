package com.jerry.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String error(){
        System.out.println("Error Page");
        return "./404.html";
    }
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

}

/*
 * This class has been borrowed from https://attacomsian.com/blog/spring-boot-custom-error-page
 */

package com.kavuna.udacity.cloudstorage.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // get error status
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // display specific error page
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "We're sorry, the page you are looking for does not exist!";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "We're sorry. We are unable to process your request. It is an issue on our side.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "We're sorry, you do not have access to this resource. ";
            }
        }

        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }
}
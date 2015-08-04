package com.fastbuild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2015/8/4.
 */
@Controller
public class FastBuildController {

    @RequestMapping(value = "example")
    public void example(HttpServletRequest request,ModelMap modelMap){

    }


    @RequestMapping(value = "main")
    public void main(HttpServletRequest request,ModelMap modelMap){

    }
}

package com.fastbuild.controller;

import com.alibaba.fastjson.JSON;
import com.fastbuild.entity.SimpleArticleEntity;
import com.fastbuild.service.impl.PageServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/4.
 */
@Controller
public class FastBuildController {

    @Resource(name = "pageService")
    PageServiceImpl pageService;


    @RequestMapping(value = "example")
    public void example(HttpServletRequest request,ModelMap modelMap){

    }


    @RequestMapping(value = "main")
    public void main(HttpServletRequest request,ModelMap modelMap){

    }

    @RequestMapping(value = "/example/layout")
    public void layout(HttpServletRequest request,ModelMap modelMap){

    }

    @RequestMapping(value = "/page/article/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String list(HttpServletRequest request,ModelMap modelMap,Map param,@PathVariable Integer id){
        try {
            param.put("pageId",id);
            List artilces = pageService.getSimpleArticleByPage(param);
            modelMap.put("data",artilces);
            modelMap.put("result",Boolean.valueOf(true));
        } catch (SQLException e) {
//            modelMap.put("errorMsg", "服务器发生错误，错误信息("+e.getMessage()+")。");
//            modelMap.put("result", Boolean.valueOf(false));
            e.printStackTrace();
        }
        return JSON.toJSONString(modelMap);
    }

    @RequestMapping(value = "/article/detail/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String dataById(HttpServletRequest request,ModelMap modelMap,Map param,@PathVariable Integer id){
        try {
            param.put("articleId",id);
            SimpleArticleEntity article =pageService.getSimpleArticleById(param);
            modelMap.put("data", article);
            modelMap.put("result",Boolean.valueOf(true));
        } catch (SQLException e) {
            modelMap.put("errorMsg", "服务器发生错误，错误信息:"+e.getMessage());
            modelMap.put("result", Boolean.valueOf(false));
            e.printStackTrace();
        }
        return JSON.toJSONString(modelMap);
    }
}

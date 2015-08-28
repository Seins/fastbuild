package com.fastbuild.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @RequestMapping(value = "/example/layout")
    public void layout(HttpServletRequest request,ModelMap modelMap){

    }

    @RequestMapping(value = "/data/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String list(HttpServletRequest request,ModelMap modelMap){
        List list = new ArrayList();
        for(int i =0;i<10 ;i ++){
            HashMap data = new HashMap();
            data.put("title","Grunt项目构建工具"+i);
            data.put("description","Grunt是基于Node.js的项目构建工具。它可以自动运行你所设定的任务。Grunt拥有数量庞大的插件，几乎任何你所要做的事情都可以用Grunt实现。");
            data.put("detail","http://localhost:8080/data/list/"+i);
            list.add(data);
        }
        modelMap.put("data",list);
        modelMap.put("result",Boolean.valueOf(true));
        return JSON.toJSONString(modelMap);
    }

    @RequestMapping(value = "/data/list/{id}",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public @ResponseBody String dataById(HttpServletRequest request,ModelMap modelMap,@PathVariable Integer id){
        HashMap data = new HashMap();
        data.put("title1","为何要用构建工具？");
        data.put("title2","为什么要使用Grunt？");
        data.put("description1","一句话：自动化。对于需要反复重复的任务，例如压缩（minification）、编译、单元测试、linting等，自动化工具可以减轻你的劳动，简化你的工作。当你在 Gruntfile 文件正确配置好了任务，任务运行器就会自动帮你或你的小组完成大部分无聊的工作。");
        data.put("description2","Grunt生态系统非常庞大，并且一直在增长。由于拥有数量庞大的插件可供选择，因此，你可以利用Grunt自动完成任何事，并且花费最少的代价。如果找不到你所需要的插件，那就自己动手创造一个Grunt插件，然后将其发布到npm上吧。先看看入门文档吧。");
        data.put("img1","http://www.gruntjs.net/img/logo-coffeescript.jpg");
        data.put("img2","http://www.gruntjs.net/img/logo-handlebars.jpg");
        data.put("img3","http://www.gruntjs.net/img/logo-jade.jpg");
        data.put("img4","http://www.gruntjs.net/img/logo-jshint.jpg");
        data.put("img5","http://www.gruntjs.net/img/logo-less.jpg");
        data.put("img6","http://www.gruntjs.net/img/logo-requirejs.jpg");
        data.put("img7","http://www.gruntjs.net/img/logo-sass.jpg");
        modelMap.put("data",data);
        modelMap.put("result",Boolean.valueOf(true));
        return JSON.toJSONString(modelMap);
    }
}

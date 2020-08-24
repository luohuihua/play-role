package com.luohh.playrole.system.controller;

import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.manager.ApiServiceManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * api文档controller
 */
@Controller
@RequestMapping("/api-info")
public class ApiDocumentController implements InitializingBean {

    @Autowired
    private ApiServiceManager apiServiceManager;

    private List exceptionDefinitionList;

    @RequestMapping("/")
    public ModelAndView index() {
        return group("user-service");
    }

    @RequestMapping("/group/{gp}")
    public ModelAndView group(@PathVariable("gp") String group) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("document");
        modelAndView.addObject("model", apiServiceManager.generateGroupModel(group));
        modelAndView.addObject("groupNames", apiServiceManager.generateDocumentModel().getGroups());
        return modelAndView;
    }


    @RequestMapping("/api/{gp}/{mt}")
    public ModelAndView api(@PathVariable("gp") String gp, @PathVariable("mt") String mt) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("method");
        modelAndView.addObject("methods", apiServiceManager.methods(gp));
        modelAndView.addObject("model", apiServiceManager.generateMethodModel(gp, mt));
        modelAndView.addObject("gp", gp);
        modelAndView.addObject("exceptionList", exceptionDefinitionList);
        return modelAndView;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionDefinitionList = new LinkedList<>();
        Field[] fields = ApiServiceException.class.getFields();
        for (Field field : fields) {
            exceptionDefinitionList.add(field.get(ApiServiceException.class));
        }
    }
}

package com.luohh.playrole.manager;


import com.luohh.playrole.annotation.RequestParam;
import com.luohh.playrole.annotation.RequestParamType;

import java.util.List;

/**
 * api描述文档model
 */
public class ApiDocumentModel {
    /**
     * 组集合
     */
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public static class Group {
        /**
         * 组名
         */
        private String name;
        /**
         * 方法列表
         */
        private List<Method> methods;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Method> getMethods() {
            return methods;
        }

        public void setMethods(List<Method> methods) {
            this.methods = methods;
        }
    }

    /**
     * 方法
     */
    public static class Method {
        /**
         * 方法名
         */
        private String name;
        /**
         * 描述
         */
        private String describe;
        /**
         * 请求类型
         */
        private String resultType;
        /**
         * 返回字段集合
         */
        private List<Field> resultFilds;
        /**
         * 参数集合
         */
        private List<Parameter> parameters;
        /**
         * 是否需要登录
         */
        private Boolean checkLogin;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getResultType() {
            return resultType;
        }

        public void setResultType(String resultType) {
            this.resultType = resultType;
        }

        public List<Field> getResultFilds() {
            return resultFilds;
        }

        public void setResultFilds(List<Field> resultFilds) {
            this.resultFilds = resultFilds;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }

        public Boolean getCheckLogin() {
            return checkLogin;
        }

        public void setCheckLogin(Boolean checkLogin) {
            this.checkLogin = checkLogin;
        }
    }

    /**
     * 参数model
     */
    public static class Parameter {
        /**
         * 参数名
         */
        private String name;
        /**
         * 描述
         */
        private String describe;
        /**
         * 参数class类型
         */
        private String paramType;
        /**
         * 参数枚举
         */
        private RequestParamType type;
        /**
         * 是否必填
         */
        private Boolean required;
        /**
         * 默认值
         */
        private Object valueDef;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getParamType() {
            return paramType;
        }

        public void setParamType(String paramType) {
            this.paramType = paramType;
        }

        public RequestParamType getType() {
            return type;
        }

        public void setType(RequestParamType type) {
            this.type = type;
        }

        public Boolean getRequired() {
            return required;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }

        public Object getValueDef() {
            return valueDef;
        }

        public void setValueDef(Object valueDef) {
            this.valueDef = valueDef;
        }
    }

    /**
     * 字段model
     */
    public static class Field {
        /**
         * 名称
         */
        private String name;
        /**
         * 类型
         */
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}

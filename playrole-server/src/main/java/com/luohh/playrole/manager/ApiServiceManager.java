package com.luohh.playrole.manager;

import com.luohh.playrole.annotation.RequestParam;
import com.luohh.playrole.annotation.RequestMethod;
import com.luohh.playrole.annotation.RequestApi;
import com.luohh.playrole.annotation.param.RequestNotNull;
import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.exception.ApiServiceExceptionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.*;
import java.util.*;

/**
 * aip服务管理类
 */
@Component
public class ApiServiceManager implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ApiServiceManager.class);
    /**
     * 组为key的方法缓存
     */
    private Map<String, Map<String, Method>> methodCacheMap = new HashMap<>();
    /**
     * 上下文
     */
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Class> classList = new LinkedList<>();
        //所有服务名称
        String[] serviceArray = applicationContext.getBeanNamesForAnnotation(Service.class);
        //循环获取所有服务
        for (String service : serviceArray) {
            Object bean = applicationContext.getBean(service);//根据服务名获取服务类
            Class<?>[] interfaces = bean.getClass().getInterfaces();//class
            if (interfaces != null && interfaces.length > 0) {
                for (Class clazz : interfaces) {
                    //是否包含请求api注解
                    if (clazz.getAnnotation(RequestApi.class) != null) {
                        classList.add(clazz);
                    }
                }
            }
        }
        //循环注册服务
        for (Class clazz : classList) {
            this.registerService(clazz);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 注册服务
     *
     * @param targetClass
     * @throws ApiServiceException
     */
    public void registerService(Class<?> targetClass) throws ApiServiceException {
        RequestApi requestApi = targetClass.getDeclaredAnnotation(RequestApi.class);
        if (requestApi != null) {
            String group = requestApi.group();//组别
            Method[] methods = targetClass.getMethods();//该service类所有公开方法
            Map<String, Method> methodMap = methodCacheMap.get(group);//根据组名字获取方法
            if (methodMap == null) {
                methodMap = new TreeMap<>();
                methodCacheMap.put(group, methodMap);
            }
            for (Method method : methods) {
                RequestMethod requestMethod = method.getAnnotation(RequestMethod.class);
                if (requestMethod != null) {
                    String key = method.getName();
                    Method methodQuery = methodMap.get(key);
                    if (methodQuery != null) {
                        //api注册失败
                        throw new ApiServiceException(ApiServiceExceptionDefinition.API_REGISTER_FAILED);
                    }
                    methodMap.put(key, method);
                    logger.info("[注册RequestApi 成功] " + group + "." + method.getName());
                } else {
                    logger.info("[注册RequestApi 失败] 没有注解." + method.getName());
                }

            }
        } else {
            //api注册失败
            throw new ApiServiceException(ApiServiceExceptionDefinition.API_REGISTER_FAILED);
        }
    }


    public Method getMethod(String group, String name) {
        Map<String, Method> tempMap = methodCacheMap.get(group);
        if (tempMap != null) {
            return tempMap.get(name);
        }
        return null;
    }

    /**
     * 获取文档模型描述的方法
     *
     * @return
     */
    private ApiDocumentModel apiDocumentModelCache = null;

    public ApiDocumentModel generateDocumentModel() {
        if (apiDocumentModelCache != null) {
            return apiDocumentModelCache;
        }
        //方法缓存
        Set<String> gpKeys = methodCacheMap.keySet();
        ApiDocumentModel apiDocumentModel = new ApiDocumentModel();
        List<ApiDocumentModel.Group> groups = new LinkedList<>();
        apiDocumentModel.setGroups(groups);
        for (String gpKey : gpKeys) {
            ApiDocumentModel.Group group = new ApiDocumentModel.Group();
            groups.add(group);
            group.setName(gpKey);
            List<ApiDocumentModel.Method> docMethods = new LinkedList<>();
            group.setMethods(docMethods);
            Map<String, Method> methodMap = methodCacheMap.get(gpKey);
            Set<String> methodNameKeys = methodMap.keySet();
            for (String methodNameKey : methodNameKeys) {
                Method method = methodMap.get(methodNameKey);
                //获取参数集合
                List<ApiDocumentModel.Parameter> docParameters = new LinkedList<>();
                Parameter[] parameters = method.getParameters();
                if (parameters != null && parameters.length > 0) {
                    for (Parameter parameter : parameters) {
                        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);//请求参数注解
                        ApiDocumentModel.Parameter docParameter = new ApiDocumentModel.Parameter();
                        if (docParameter == null) {
                            logger.info("[Api] 参数未注解:" + method.getName());
                        }
                        if (requestParam == null) {
                            logger.info("[Api] 参数未注解");
                        }
                        docParameter.setName(requestParam.name());//参数名
                        docParameter.setValueDef(requestParam.valueDef());//默认值
                        docParameter.setDescribe(requestParam.describe());//描述
                        String typeName = parameter.getType().getTypeName();//参数class类型
                        if (typeName.startsWith("[L")) {
                            typeName = typeName.substring(2) + "[]";
                        }
                        docParameter.setParamType(typeName);//参数class类型
                        docParameter.setType(requestParam.type());//参数枚举
                        docParameter.setRequired(parameter.getAnnotation(RequestNotNull.class) != null);//非空必填
                        docParameters.add(docParameter);
                    }
                }
                //api方法
                ApiDocumentModel.Method docMethod = new ApiDocumentModel.Method();
                docMethod.setParameters(docParameters);//设置参数
                RequestMethod requestMethod = method.getAnnotation(RequestMethod.class);//方法注解
                if (requestMethod != null) {
                    docMethod.setDescribe(requestMethod.describe());//描述
                    docMethod.setName(method.getName());//方法名
                    Type returnType = method.getGenericReturnType();//返回类型
                    String retType = returnType.toString();
                    if (retType.startsWith("interface")) {//接口
                        if (retType.startsWith("interface [L")) {
                            retType = retType.substring(12);
                        } else {
                            retType = retType.substring(10);
                        }
                    } else if (retType.startsWith("class")) {//普通class
                        if (retType.startsWith("class [L")) {
                            retType = retType.substring(8);
                        } else {
                            retType = retType.substring(6);
                        }
                    }
                    docMethod.setResultType(retType);//请求类型
                    if (retType.indexOf(".entity.") != -1 || retType.indexOf(".model.") != -1) {
                        //若返回值类型为复杂类型
                        List<ApiDocumentModel.Field> fieldList = new ArrayList<>();
                        Class returnClass = null;
                        if (returnType instanceof Class) {
                            returnClass = (Class) returnType;
                        } else if (returnType instanceof ParameterizedType) {
                            ParameterizedType parameterizedType = (ParameterizedType) returnType;
                            returnClass = (Class) parameterizedType.getActualTypeArguments()[0];
                        }
                        if (returnClass != null) {
                            Field[] declaredFields = returnClass.getDeclaredFields();
                            for (Field field : declaredFields) {
                                ApiDocumentModel.Field docField = new ApiDocumentModel.Field();
                                docField.setName(field.getName());
                                docField.setType(field.getType().toString());
                                fieldList.add(docField);
                            }
                            docMethod.setResultFilds(fieldList);
                        }

                    }
                    docMethods.add(docMethod);

                } else {
                    logger.info("生成文档失败:" + method.getName());
                }
            }
        }
        apiDocumentModelCache = apiDocumentModel;
        return apiDocumentModelCache;
    }

    public ApiDocumentModel.Group generateGroupModel(String group) {
        ApiDocumentModel apiDocumentModel = generateDocumentModel();
        List<ApiDocumentModel.Group> groups = apiDocumentModel.getGroups();
        for (ApiDocumentModel.Group gp : groups) {
            if (gp.getName().equals(group)) {
                return gp;
            }
        }
        return null;
    }

    public ApiDocumentModel.Method generateMethodModel(String gp, String mt) {
        ApiDocumentModel apiDocumentModel = generateDocumentModel();
        List<ApiDocumentModel.Group> groups = apiDocumentModel.getGroups();
        for (ApiDocumentModel.Group group : groups) {
            if (group.getName().equals(gp)) {
                List<ApiDocumentModel.Method> methods = group.getMethods();
                for (ApiDocumentModel.Method method : methods) {
                    if (method.getName().equals(mt)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }


    public List<ApiDocumentModel.Method> methods(String gp) {
        for (ApiDocumentModel.Group group : generateDocumentModel().getGroups()) {
            if (group.getName().equals(gp)) {
                return group.getMethods();
            }
        }
        return null;
    }

}

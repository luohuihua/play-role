package com.luohh.playrole.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.luohh.playrole.annotation.RequestParam;
import com.luohh.playrole.annotation.RequestParamType;
import com.luohh.playrole.annotation.ResultType;
import com.luohh.playrole.annotation.param.RequestNotNull;
import com.luohh.playrole.exception.ApiServiceException;
import com.luohh.playrole.exception.ApiServiceExceptionDefinition;
import com.luohh.playrole.manager.ApiServiceManager;
import com.luohh.playrole.system.model.ResultData;
import com.luohh.playrole.util.RedisUtil;
import com.luohh.playrole.util.ToolsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.*;
import java.util.Map;

/**
 * 手机的contorller
 * 统一处理手机端请求
 */
@Controller
@RequestMapping("/api-service")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private ApplicationContext applicationContext;
//    @Autowired
//    private StringRedisTemplate userRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 统一接收请求，根据传入的组，方法名，参数进行调用service返回数据
     *
     * @param req
     * @param res
     * @return
     */
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String request(HttpServletRequest req, HttpServletResponse res) {
        long invokeTime = System.currentTimeMillis();
        try {
            logger.info("请求参数 request=" + JSONObject.toJSONString(req.getParameterMap()));
            Object obj = process(req, res, invokeTime);
            if (obj == null)
                return null;
            if (ToolsUtil.RETURN_PARAM_LIST.contains(obj.getClass())) {
                return obj.toString();
            }
            String result = JSONObject.toJSONString(obj);
            long during = System.currentTimeMillis() - invokeTime;
            logger.info("请求耗时 " + during + "ms, response=" + JSONObject.toJSONString(result));
            return result;
        } catch (ApiServiceException e) {
            ResultData resultData = new ResultData();
            resultData.setTimestamp(invokeTime);
            resultData.setData(e.getCode());
            resultData.setMsg(e.getMessage());
            String result = JSONObject.toJSONString(resultData);
            long during = System.currentTimeMillis() - invokeTime;
            logger.info("请求耗时 " + during + "ms, response=" + JSONObject.toJSONString(result));
            return result;
        }
    }

    /**
     * 处理过程
     *
     * @param request
     * @param response
     * @param invokeTime 请求时间
     * @return
     * @throws ApiServiceException
     */
    private Object process(HttpServletRequest request, HttpServletResponse response, long invokeTime) throws ApiServiceException {
        try {
            //api管理类
            ApiServiceManager apiServiceManager = applicationContext.getBean(ApiServiceManager.class);
            Map<String, String[]> parameterMap = request.getParameterMap();
            String[] groups = parameterMap.get("_group");
            String[] methods = parameterMap.get("_method");
            if (groups == null || methods == null || groups.length == 0 || methods.length == 0) {
                throw new ApiServiceException(ApiServiceExceptionDefinition.API_NOT_EXISTS);
            }
            String _group = groups[0];//组名
            String _method = methods[0];//方法名
            String[] _types = parameterMap.get("_type");//请求类型
            String _type = null;
            if (_types != null && _types.length > 0) {
                _type = _types[0];
            }
            Method method = apiServiceManager.getMethod(_group, _method);//获取方法
            if (method == null) {
                throw new ApiServiceException(ApiServiceExceptionDefinition.API_NOT_EXISTS);
            }
            com.luohh.playrole.annotation.RequestMethod requestMethod = method.getAnnotation(com.luohh.playrole.annotation.RequestMethod.class);
            if (requestMethod == null) {
                //只起标记作用防止调到封闭方法了
                throw new ApiServiceException(ApiServiceExceptionDefinition.API_NOT_EXISTS);
            }
            //需要校验权限
            if (requestMethod.checkLogin()) {
                String accesstoken = request.getParameter(ToolsUtil.ACCESSTOKEN);
                if (accesstoken == null || accesstoken.length() <= 0)
                    accesstoken = request.getHeader(ToolsUtil.ACCESSTOKEN);
                if (accesstoken == null || accesstoken.length() <= 0) {
                    throw new ApiServiceException(ApiServiceExceptionDefinition.USER_NOT_LOGIN);
                }
                Object user = redisUtil.get(accesstoken);
                if (user == null) {
                    throw new ApiServiceException(ApiServiceExceptionDefinition.USER_NOT_LOGIN);
                }
            }
            Object serviceBean = applicationContext.getBean(method.getDeclaringClass());//获取调用的业务bean实体
            Parameter[] methodParameters = method.getParameters();//方法参数
            Object[] args = new Object[methodParameters.length];//入参数组
            for (int i = 0; i < methodParameters.length; i++) {
                Parameter methodParam = methodParameters[i];//参数
                RequestParam requestParam = methodParam.getAnnotation(RequestParam.class);
                if (requestParam == null) {
                    throw new ApiServiceException(ApiServiceExceptionDefinition.API_NOT_EXISTS);
                }
                //普通请求
                if (requestParam.type() == RequestParamType.COMMON || requestParam.type() == RequestParamType.IP) {
                    String[] paramArray = parameterMap.get(requestParam.name());//参数名数组
                    if (paramArray != null && paramArray.length > 0 && !StringUtils.isEmpty(paramArray[0])) {
                        Class<?> type = methodParam.getType();
                        if (String.class == type) {//字符串
                            args[i] = paramArray[0];
                        } else if (ToolsUtil.RETURN_PARAM_LIST.contains(type)) {//基本数据类型
                            Constructor<?> constructor = type.getConstructor(String.class);
                            args[i] = constructor.newInstance(paramArray[0]);
                        } else if (type.isArray()) {//数组
                            Class<?> itemType = type.getComponentType();
                            Object realType[] = (Object[]) Array.newInstance(itemType, paramArray.length);
                            if (paramArray.length > 0) {
                                for (int j = 0; j < paramArray.length; j++) {
                                    if (ToolsUtil.RETURN_PARAM_LIST.contains(itemType)) {
                                        Constructor<?> constructor = itemType.getConstructor(String.class);
                                        realType[j] = constructor.newInstance(paramArray[j]);
                                    } else {
                                        realType[j] = JSONObject.parseObject(paramArray[j], itemType);
                                    }
                                }
                            }
                            args[i] = realType;
                        } else {//Json
                            args[i] = JSONObject.parseObject(paramArray[0], type);
                        }
                    } else {
                        if (!StringUtils.isEmpty(requestParam.valueDef())) {
                            //若有默认值
                            Class<?> type = methodParam.getType();
                            Constructor<?> constructor = type.getConstructor(String.class);
                            args[i] = constructor.newInstance(requestParam.valueDef());
                        } else {
                            if (methodParam.getAnnotation(RequestNotNull.class) != null) {
                                logger.error("缺少属性 :" + requestParam.name());
                                throw new ApiServiceException(ApiServiceExceptionDefinition.PARAM_CHECK_FAILED);
                            }
                            args[i] = null;
                        }
                    }
                }
            }
            //调用方法
            Object invokeObj = method.invoke(serviceBean, args);
            ResultType resultType = requestMethod.resultType();//返回类型
            if (!StringUtils.isEmpty(_type) && "raw".equals(_type)) {
                //如果是不用包装的直接返回
                return invokeObj;
            }
            //下面是需要包装返回的
            if (resultType == ResultType.COOKIE) {
                //加入Cookie时处理
                if (StringUtils.isEmpty(requestMethod.resultName())) {
                    throw new ApiServiceException(ApiServiceExceptionDefinition.API_NOT_EXISTS);
                } else {
                    //setCookie
                    Cookie cookie = new Cookie(requestMethod.resultName(), (String) invokeObj);
                    cookie.setPath("/");
                    if (requestMethod.maxAge() != -1) {
                        cookie.setMaxAge(requestMethod.maxAge());
                    }
                    response.addCookie(cookie);
                }
            }
            ResultData resultData = new ResultData();
            resultData.setStatusCode(200);
            resultData.setMsg("成功");
            resultData.setTimestamp(invokeTime);
            resultData.setData(invokeObj);
            return resultData;
        } catch (ApiServiceException e) {
            throw e;
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException proxy = (InvocationTargetException) e;
                Throwable targetException = proxy.getTargetException();
                if (targetException instanceof ApiServiceException) {
                    throw (ApiServiceException) targetException;
                }
            }
            logger.error("[网关] 系统未知异常", e);
            throw new ApiServiceException(ApiServiceExceptionDefinition.UNKNOWN_EXCEPTION);
        }
    }

}

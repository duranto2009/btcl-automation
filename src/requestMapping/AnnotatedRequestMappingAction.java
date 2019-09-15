package requestMapping;

import annotation.ForwardedAction;
import annotation.JsonPost;
import com.google.gson.*;
import common.RequestFailureException;
import exception.ExceptionMsgCustomizer;
import exception.NoDataFoundException;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import sessionmanager.SessionConstants;
import util.FormPopulator;
import util.ServiceDAOFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static common.StringUtils.isBlank;
import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.ClassUtils.primitiveToWrapper;

@Log4j
public class AnnotatedRequestMappingAction extends Action {

    public static final int API_RESPONSE_SUCCESS = 1;
    public static final int API_RESPONSE_FAILURE = 2;
    public static final int API_RESPONSE_NOT_LOGGED_IN = 2;

    private Map<String, Map<RequestMethod, Method>> mapOfMethodToRequestMethodToRequestURI = null;

    public AnnotatedRequestMappingAction() {
        injectServices();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub


        return findAndExecute(mapping, form, request, response);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    public ActionForward findAndExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {


        ActionForward actionForward = null;
        String requestedURI = request.getRequestURI();
        try {

            Map<Class, Object> parameterMap = new HashMap<>();
            parameterMap.put(ActionMapping.class, mapping);
            parameterMap.put(ActionForm.class, form);
            parameterMap.put(HttpServletRequest.class, request);
            parameterMap.put(HttpServletResponse.class, response);
            parameterMap.put(LoginDTO.class, (LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN));

            Method method = getMethodByHttpRequest(request);
            if (method == null) {
                throw new RequestFailureException("No such method found");
            }
            Logger.getLogger(getClass()).info("Invoking : " + method.getDeclaringClass() + " :: " + method.getName());
            long startTime = System.nanoTime();

            if (method.getReturnType().equals(ActionForward.class)) {
                actionForward = (ActionForward) invokeMethod(method, parameterMap, request);
                long endTime = System.nanoTime();
            } else if (method.getDeclaredAnnotation(ForwardedAction.class) != null
                    && method.getReturnType().equals(String.class)) {
                String actionForwardString = (String) invokeMethod(method, parameterMap, request);
                ActionForward actionForwardWithAnnotation =  mapping.findForward(actionForwardString);
                long endTime = System.nanoTime();
                log.info("******(" + method.getDeclaringClass().getSimpleName() + " :: " +method.getName() +")=>:" + (endTime-startTime)/1e9 + "s******");
                return actionForwardWithAnnotation;
            } else {
                ApiResponse apiResponse = null;
                try {
                    Object returnObject = invokeMethod(method, parameterMap, request);
                    apiResponse = new ApiResponse(API_RESPONSE_SUCCESS, returnObject, "Success");
                } catch (RequestFailureException ex) {
                    Logger.getLogger(getClass()).debug("fatal", ex);
                    apiResponse = new ApiResponse(API_RESPONSE_FAILURE, null, ex.getMessage());
                } catch (InvocationTargetException ex) {
                    Throwable exThrowable = ex;
                    Logger.getLogger(getClass()).fatal("[ X ] " + ex.getTargetException().getMessage());
                    while (exThrowable.getCause() != null) {
                        exThrowable = exThrowable.getCause();
                    }


                    Logger.getLogger(getClass()).debug("fatal", exThrowable);
                    if (exThrowable instanceof RequestFailureException) {
                        RequestFailureException requestFailureException = (RequestFailureException) exThrowable;
                        apiResponse = new ApiResponse(API_RESPONSE_FAILURE, null, requestFailureException.getMessage());
                    }else if (exThrowable instanceof NoDataFoundException) {
                        NoDataFoundException noDataFoundException = (NoDataFoundException) exThrowable;
                        apiResponse = new ApiResponse(API_RESPONSE_FAILURE, null, noDataFoundException.getMessage());
                    } else {
                        StackTraceElement stackTraceElement = ex.getStackTrace()[0];
                        Logger.getLogger(getClass()).fatal("[ X ]: "
                                + stackTraceElement.getClassName()
                                + " :: " + stackTraceElement.getMethodName()
                                + " :: " + stackTraceElement.getLineNumber()
                        );
                        apiResponse = new ApiResponse(API_RESPONSE_FAILURE, null, "Something Went wrong! Please Try Again");

                    }
                } catch (Exception ex) {
                    Logger.getLogger(getClass()).debug("fatal", ex);
                    apiResponse = new ApiResponse(API_RESPONSE_FAILURE, null, ex.getMessage().trim().length() > 0 ? ex.getMessage() : "Something Went wrong! Please Try Again");
                }
                Gson gson = getGsonBuilder().create();
                gson.toJson(apiResponse, response.getWriter());
                long endTime = System.nanoTime();
                log.info("******(" + method.getDeclaringClass().getSimpleName() + " :: " +method.getName() +")=>:" + (endTime-startTime)/1e9 + "s******");
            }

        } catch (Exception ex) {
            Logger.getLogger(getClass()).debug("fatal", ex);
            ExceptionMsgCustomizer.getMessage(ex);
            throw ex;
        }

        return actionForward;
    }

    public GsonBuilder getGsonBuilder() {
        return new GsonBuilder();
    }

    @SuppressWarnings("rawtypes")
    private void injectServices() {
        Class classObject = getClass();
        Field[] fields = classObject.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Service.class) != null) {
                Object serviceObject = ServiceDAOFactory.getService(field.getType());
                try {
                    field.set(this, serviceObject);
                } catch (Exception ex) {
                    Logger.getLogger(getClass()).debug("fatal", ex);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unused"})
    private Object invokeMethod(Method method, Map<Class, Object> parameterMap, HttpServletRequest request) throws Exception {

        Class classObject = getClass();
        Parameter[] parameterTypes = method.getParameters();
        Object[] methodParameters = getMethodParameters(method, request, parameterMap);
        return method.invoke(this, methodParameters);
    }
	/*
	
	private boolean isMatchingMethod(Method method, String uri,RequestMethod requestMethod){
		boolean isMatchingMethod = false;
		
		try{
			
			RequestMapping requestMapping = (RequestMapping)method.getAnnotation(RequestMapping.class);
			if(requestMapping!=null){
				isMatchingMethod = (uri.endsWith(requestMapping.mapping()) && requestMapping.requestMethod().equals(requestMethod));
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isMatchingMethod;
	}
	*/


    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<String, Map<RequestMethod, Method>> createMapOfMethodToRequestURI(Class classObject, String contextPath) {
        Map<String, Map<RequestMethod, Method>> mapOfMethodToRequestMethodToRequestURI = new HashMap<>();

        ActionRequestMapping actionRequestMapping = (ActionRequestMapping) classObject.getAnnotation(ActionRequestMapping.class);
        if (actionRequestMapping == null) {
            return mapOfMethodToRequestMethodToRequestURI;
        }
        String baseURI = actionRequestMapping.value();
        Method[] methods = classObject.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                String requestMappingSuffix = requestMapping.mapping();
                RequestMethod requestMethod = requestMapping.requestMethod();

                String requestMappingForMethod = contextPath + baseURI + requestMappingSuffix + ".do";
                if (!mapOfMethodToRequestMethodToRequestURI.containsKey(requestMappingForMethod)) {
                    mapOfMethodToRequestMethodToRequestURI.put(requestMappingForMethod, new HashMap<>());
                }

                if (requestMethod == RequestMethod.All) {
                    mapOfMethodToRequestMethodToRequestURI.get(requestMappingForMethod).put(RequestMethod.GET, method);
                    mapOfMethodToRequestMethodToRequestURI.get(requestMappingForMethod).put(RequestMethod.PUT, method);
                    mapOfMethodToRequestMethodToRequestURI.get(requestMappingForMethod).put(RequestMethod.POST, method);
                    mapOfMethodToRequestMethodToRequestURI.get(requestMappingForMethod).put(RequestMethod.DELETE, method);
                } else {
                    mapOfMethodToRequestMethodToRequestURI.get(requestMappingForMethod).put(requestMethod, method);
                }


            }
        }

        return mapOfMethodToRequestMethodToRequestURI;
    }

    private RequestMethod getRequestMethodFromHttpServletRequest(HttpServletRequest request) {

        switch (request.getMethod().toUpperCase()) {
            case "GET":
                return RequestMethod.GET;
            case "POST":
                return RequestMethod.POST;
            case "PUT":
                return RequestMethod.PUT;
            case "DELETE":
                return RequestMethod.DELETE;
            default:
                return null;
        }

    }

    private Method getMethodByHttpRequest(HttpServletRequest request) {
        if (mapOfMethodToRequestMethodToRequestURI == null) {
            synchronized (getClass()) {
                if (mapOfMethodToRequestMethodToRequestURI == null) {
                    mapOfMethodToRequestMethodToRequestURI = createMapOfMethodToRequestURI(getClass(), request.getContextPath() + "/");
                }
            }
        }
        String requestedURI = request.getRequestURI();
        if (!mapOfMethodToRequestMethodToRequestURI.containsKey(requestedURI)) {
            return null;
        }
        return mapOfMethodToRequestMethodToRequestURI.get(requestedURI).get(getRequestMethodFromHttpServletRequest(request));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object[] getMethodParameters(Method method, HttpServletRequest request, Map<Class, Object> parameterMap) throws Exception {


        Parameter[] parameterTypes = method.getParameters();
        Object[] methodParameters = new Object[parameterTypes.length];


        boolean isJsonPost = method.getAnnotation(JsonPost.class) != null;


        JsonObject jsonObject = null;
        Gson gson = getGsonBuilder().create();
        if (isJsonPost) {

            String content = request.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);

            jsonObject = new JsonParser().parse(content).getAsJsonObject();

        }
        for (int i = 0; i < parameterTypes.length; i++) {

            Parameter parameter = parameterTypes[i];
            Class parameterClass = parameter.getType();


            if (parameterMap.containsKey(parameterClass)) {
                methodParameters[i] = parameterMap.get(parameterClass);
            } else {

                RequestParameter requestParameter = (RequestParameter) parameter.getAnnotation(RequestParameter.class);
				/*if(requestParameter == null && (parameterClass.isPrimitive() || isPrimitiveOrWrapper(parameterClass))){
					throw new Exception("Invalid API design. Please contact the API designer");
				}
					*/

                if (!isJsonPost && requestParameter != null && requestParameter.isJsonBody()) {
                    throw new Exception("This is not a json post request. So the request parameter can not have json values");
                }


                String parameterName = (requestParameter == null ? parameter.getName() : requestParameter.value());


                if (parameterClass.equals(List.class)) {

                    Class clazz = (Class) ((ParameterizedType) parameter.getParameterizedType())
                            .getActualTypeArguments()[0];
					/*
					if(!clazz.isPrimitive() && !isPrimitiveOrWrapper(clazz)){
						throw new Exception("The parameterized class can not be a user defined class");
					}*/

                    if (requestParameter.isJsonBody()) {
                        List<Object> list = new ArrayList<>();
                        methodParameters[i] = list;
                        for (JsonElement jsonElement : jsonObject.get(parameterName).getAsJsonArray()) {
                            list.add(gson.fromJson(jsonElement, clazz));
                        }
                    } else {
                        List<Object> longList = new ArrayList<>();
                        methodParameters[i] = longList;
                        String[] values = request.getParameterValues(parameterName);

                        if (values != null) {
                            for (String value : values) {
                                longList.add(createObject(clazz, value, parameterName));
                            }
                        }
                    }
                } else if (parameterClass.isPrimitive() || isPrimitiveOrWrapper(parameterClass)
                        || parameterClass.equals(String.class) || parameterClass.equals(Date.class)) {

                    if (requestParameter.isJsonBody()) {
                        JsonElement jsonElement = jsonObject.get(parameterName);
                        if (jsonElement == null || jsonElement.isJsonNull()) {
                            methodParameters[i] = null;
                            continue;
                        }
                        if (parameterClass.equals(String.class)) {
                            methodParameters[i] = jsonElement.getAsString();
                        } else if (parameterClass.equals(Long.class) || parameterClass.equals(Long.TYPE)) {
                            methodParameters[i] = jsonElement.getAsLong();
                        } else if (parameterClass.equals(Integer.class) || parameterClass.equals(Integer.TYPE)) {
                            methodParameters[i] = jsonElement.getAsInt();
                        } else if (parameterClass.equals(Boolean.class) || parameterClass.equals(Boolean.TYPE)) {
                            methodParameters[i] = jsonElement.getAsBoolean();
                        } else if (parameterClass.equals(Double.class) || parameterClass.equals(Double.TYPE)) {
                            methodParameters[i] = jsonElement.getAsDouble();
                        } else {
                            methodParameters[i] = gson.fromJson(jsonObject.get(parameterName).getAsJsonObject(), parameterClass);
                        }
                    } else {
                        String stringValue = getStringValueFromRequest(request, parameterName);
                        methodParameters[i] = createObject(parameterClass, stringValue, parameterName);
                    }
                } else {
                    if (isJsonPost && requestParameter != null && requestParameter.isJsonBody()) {
                        try {
                            methodParameters[i] = gson.fromJson(jsonObject.get(parameterName).getAsJsonObject(), parameterClass);
                        } catch (NumberFormatException ex) {
                            throw new RequestFailureException(ex.getMessage());
                        } catch (Exception e) {
                            throw new Exception(e.getMessage());
                        }
                    } else {
                        methodParameters[i] = parameterClass.newInstance();
                        FormPopulator.populate(methodParameters[i], request);
                    }
                }


            }
        }

        return methodParameters;
    }

    private String getStringValueFromRequest(HttpServletRequest request, String key) {
        String stringValue = request.getParameter(key);
        if (stringValue == null) {
            stringValue = (String) request.getAttribute(key);
        }
        return stringValue;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Object createObject(Class classObject, String value, String parameterName) {
        if (isBlank(value) && classObject.isPrimitive()) {
            throw new RequestFailureException("Invalid format for " + parameterName);
        }

        if (classObject.equals(String.class) && value != null) {
            return new String(value);
        }

        if (isBlank(value)) {
            return null;
        }
        if (classObject.isPrimitive()) {
            classObject = primitiveToWrapper(classObject);
        }

        if (Integer.TYPE.equals(classObject)) {
            return new Integer(value);
        }
        if (Long.TYPE.equals(classObject)) {
            return new Long(value);
        }
        if (Boolean.TYPE.equals(classObject) || Boolean.class.equals(classObject)) {
            return value.equals("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on");

        }
        if (Double.TYPE.equals(classObject)) {
            return new Double(value);
        }
        try {
            if (Date.class.equals(classObject)) {
                return new SimpleDateFormat("dd/M/yyyy").parse(value);
            } else {
                return classObject.getConstructor(String.class).newInstance(value);
            }
        } catch (Exception ex) {
            throw new RequestFailureException("Invalid format for " + parameterName);
        }

    }

}

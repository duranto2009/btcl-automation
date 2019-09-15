package util;

import annotation.ParseDateToMillisecond;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class FormPopulator {
    public static void populate(Object object, HttpServletRequest request) {
        Enumeration<String> nameEnumerations = request.getParameterNames();

        while (nameEnumerations.hasMoreElements()) {
            String parameterName = nameEnumerations.nextElement();
            String parameterValue = request.getParameter(parameterName);
            if (parameterValue != null) {
                populate(object, parameterName, parameterValue);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void populate(Object object, String propertyName, String propertyValue) {
        if (object == null || propertyName == null || propertyValue == null || propertyName.endsWith(".")) {
            return;
        }


        try {


            if (propertyName.contains(".")) {

                String immidiatePropertyName = propertyName.substring(0, propertyName.indexOf('.'));


                if (immidiatePropertyName.contains("[")) {


                    String collectionPropertyName = immidiatePropertyName.substring(0, immidiatePropertyName.indexOf('['));
                    String indexString = immidiatePropertyName.substring(immidiatePropertyName.indexOf('[') + 1, immidiatePropertyName.lastIndexOf(']'));
                    int index = Integer.parseInt(indexString);

                    createMinimumSizeForList(object, index, collectionPropertyName);

                    Field field = getPropertyField(object.getClass(), collectionPropertyName);
                    if (field == null) {
                        return;
                    }
                    if (field.getType().equals(List.class)) {
                        List<Object> list = (List<Object>) field.get(object);


                        // Class parameterTypeClassObject =
                        Type type = field.getGenericType();
                        Class<?> classOfCollectionObject = null;
                        if (type instanceof ParameterizedType) {
                            classOfCollectionObject = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];

                        }
                        if (classOfCollectionObject != null) {
                            Object objectOfCollection = (list.get(index) == null ? classOfCollectionObject.newInstance() : list.get(index));
                            list.set(index, objectOfCollection);
                            populate(objectOfCollection, propertyName.substring(propertyName.indexOf(".") + 1), propertyValue);
                        }

                    }

                } else {
                    Field field = getPropertyField(object.getClass(), immidiatePropertyName);
                    if (field == null) {
                        return;
                    }
                    field.setAccessible(true);
                    try {
                        Object properyObject = field.get(object);
                        if (properyObject == null) {
                            Class<?> propertyClass = field.getType();
                            properyObject = propertyClass.newInstance();
                            field.set(object, properyObject);
                        }
                        populate(properyObject, propertyName.substring(propertyName.indexOf('.') + 1), propertyValue);
                    } catch (Exception ex) {

                    }

                }

            } else {
                if (propertyName.contains("[")) {


                    String indexedValue = propertyName.substring(propertyName.indexOf('[') + 1, propertyName.lastIndexOf(']'));
                    propertyName = propertyName.substring(0, propertyName.indexOf('['));
                    Field field = getPropertyField(object.getClass(), propertyName);
                    if (field == null) {
                        return;
                    }
                    field.setAccessible(true);
                    if (field.getType().equals(List.class)) {

                        int index = Integer.parseInt(indexedValue);
                        List<Object> list = (List<Object>) field.get(object);
                        while (list.size() <= index) {
                            // list.add(classOfCollectionObject.newInstance());
                            list.add(null);
                        }

                        // Class parameterTypeClassObject =
                        Type type = field.getGenericType();
                        Class<?> classOfCollectionObject = null;
                        if (type instanceof ParameterizedType) {
                            classOfCollectionObject = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];

                        }
                        if (classOfCollectionObject != null) {

                            Object objectOfCollection = createObject(classOfCollectionObject, propertyValue);
                            list.set(index, objectOfCollection);
                        }

                    }
                } else {
                    Field field = getPropertyField(object.getClass(), propertyName);
                    if (field == null) {
                        return;
                    }
                    field.setAccessible(true);

                    Class<?> classObjectOfCurrentField = field.getType();
                    if (
                            (classObjectOfCurrentField.equals(Long.TYPE) || classObjectOfCurrentField.equals(Long.class))
                                    && field.getAnnotation(ParseDateToMillisecond.class) != null
                    ) {
                        ParseDateToMillisecond parseDateToMillisecond = (ParseDateToMillisecond) field.getAnnotation(ParseDateToMillisecond.class);
                        field.set(object, TimeConverter.getTimeInMilliSec(propertyValue, parseDateToMillisecond.dateFormat()));
                    } else {
                        Object propertyObject = createObject(classObjectOfCurrentField, propertyValue);
                        field.set(object, propertyObject);
                    }


                }


            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    static private void createMinimumSizeForList(Object object, int index, String propertyName) throws Exception {

        Field field = getPropertyField(object.getClass(), propertyName);
        if (field == null) {
            return;
        }
        field.setAccessible(true);
        List<Object> list = (List<Object>) field.get(object);
        if (list == null) {
            list = new ArrayList<>();
            field.set(object, list);
        }
        while (list.size() <= index) {
            list.add(null);
        }
    }

    static private Object createObject(Class<?> classObject, String value) throws Exception {
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


        if (String.class.equals(classObject)) {
            return classObject.getConstructor(String.class).newInstance(value);
        }
        else return null;
    }

    public static Field getPropertyField(Class<?> classObject, String propertyName) {
        Field field = null;
        try {
            if (classObject != null) {
                field = classObject.getDeclaredField(propertyName);
            }
        } catch (Exception ex) {
            field = getPropertyField(classObject.getSuperclass(), propertyName);
        }
        if (field != null) {
            field.setAccessible(true);
        }
        return field;
    }

}

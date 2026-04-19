package com.budaos.common.validator.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValidateCache {

    private static ValidateCache ourInstance = new ValidateCache();

    public static ValidateCache getInstance() {
        return ourInstance;
    }

    private Map<Class<?>, Set<Field>> classFieldSetMap;
    private Map<Field, Annotation[]> fieldAnnotationMap;

    private ValidateCache() {
        classFieldSetMap = new HashMap<>();
        fieldAnnotationMap = new HashMap<>();
    }

    public void setClassFields(Class<?> classType, Set<Field> fieldSet) {
        if (null == fieldSet) fieldSet = new HashSet<>();
        classFieldSetMap.put(classType, fieldSet);
    }

    public void setFieldAnnotations(Field field, Annotation[] annotations) {
        if (null == annotations) annotations = new Annotation[0];
        fieldAnnotationMap.put(field, annotations);
    }

    public Set<Field> getFieldsByClass(Class<?> classType) {
        return classFieldSetMap.get(classType);
    }

    public Annotation[] getAnnotationsByField(Field field) {
        return fieldAnnotationMap.get(field);
    }

}

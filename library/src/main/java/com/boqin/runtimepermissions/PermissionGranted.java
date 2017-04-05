package com.boqin.runtimepermissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * custom annotation for method
 * Created by Boqin on 2017/4/4.
 * Modified by Boqin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PermissionGranted {
    String value() default "";
}

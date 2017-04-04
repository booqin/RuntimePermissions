package com.boqin.runtimepermissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Desctiption: TODO
 * Created by Vito on 2017/4/4.
 * Email:developervito@163.com
 * ModifiedBy: Vito
 * ModifiedTime: 2017/4/4 22:11
 * ModifiedNotes: TODO
 * Version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface PermissionGranted {
    String value() default "";
}

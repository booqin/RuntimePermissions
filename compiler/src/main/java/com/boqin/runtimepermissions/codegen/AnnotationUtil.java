package com.boqin.runtimepermissions.codegen;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

import com.boqin.runtimepermissions.BQConstant;
import com.boqin.runtimepermissions.PermissionActivity;
import com.boqin.runtimepermissions.PermissionGranted;
import com.boqin.runtimepermissions.codegen.bean.PermissionElement;
import com.squareup.javapoet.TypeName;

/**
 * TODO
 * Created by Boqin on 2017/4/5.
 * Modified by Boqin
 *
 * @Version
 */
public class AnnotationUtil {

    public static PermissionElement getInfoFromAnnotation(Element element){
        PermissionElement permissionElement = new PermissionElement();
        permissionElement.setGeneratedClassName(element.getSimpleName().toString()+ BQConstant.CLASS_SUFFIX);
        permissionElement.setContent(element.getAnnotation(PermissionActivity.class).value());

        List<Element> list = new ArrayList<>();
        for (Element element1 : element.getEnclosedElements()) {
            if (element1.getAnnotation(PermissionGranted.class)!=null) {
                list.add(element1);
            }
        }

        permissionElement.setGrantedElements(list);

        if (element.getEnclosingElement() instanceof PackageElement) {
            ((PackageElement) element.getEnclosingElement()).getQualifiedName();
        }
        Element e = element;
        while (!(e.getEnclosingElement() instanceof PackageElement)) {
            e = e.getEnclosingElement();
        }
        if (e.getEnclosingElement() instanceof PackageElement) {
            permissionElement.setPackageName(((PackageElement) e.getEnclosingElement()).getQualifiedName().toString());  //获取包名
//            simpleName = e.getSimpleName().toString();    //获取类名
            permissionElement.setTypeName(TypeName.get(e.asType()));

        }
        return permissionElement;
    }

}

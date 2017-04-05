package com.boqin.runtimepermissions.codegen.bean;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

import com.squareup.javapoet.TypeName;

/**
 * TODO
 * Created by Boqin on 2017/4/5.
 * Modified by Boqin
 *
 * @Version
 */
public class PermissionElement {
    private TypeName mTypeName;
    private String mPackageName;
    private String mGeneratedClassName;
    private String[] mContent;
    private List<Element> mGrantedElements;

    public PermissionElement(){
        mGrantedElements = new ArrayList<>();
    }

    public TypeName getTypeName() {
        return mTypeName;
    }

    public void setTypeName(TypeName typeName) {
        mTypeName = typeName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public String getGeneratedClassName() {
        return mGeneratedClassName;
    }

    public void setGeneratedClassName(String generatedClassName) {
        mGeneratedClassName = generatedClassName;
    }

    public List<Element> getGrantedElements() {
        return mGrantedElements;
    }

    public void setGrantedElements(List<Element> grantedElements) {
        mGrantedElements = grantedElements;
    }

    public String[] getContent() {
        return mContent;
    }

    public void setContent(String[] content) {
        mContent = content;
    }
}

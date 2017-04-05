package com.boqin.runtimepermissions.codegen;

import com.boqin.runtimepermissions.AnnotationConstant;
import com.boqin.runtimepermissions.PermissionActivity;
import com.boqin.runtimepermissions.PermissionGranted;
import com.boqin.runtimepermissions.codegen.bean.PermissionElement;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({"com.boqin.runtimepermissions.PermissionActivity", "com.boqin.runtimepermissions.PermissionGranted"})
public class AnnotationProcessor extends AbstractProcessor {

    private static final String TARGET = "target";

    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        PermissionElement permissionElement = null;
        //get annotation to PermissionElement
        for (Element element : roundEnv.getElementsAnnotatedWith(PermissionActivity.class)) {
            permissionElement = AnnotationUtil.getInfoFromAnnotation(element);
        }

        if (permissionElement!=null) {
            JavaFile javaFile = JavaFile.builder(permissionElement.getPackageName(), createTypeSpec(permissionElement)).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private TypeSpec createTypeSpec(PermissionElement permissionElement) {
        return TypeSpec.classBuilder(permissionElement.getGeneratedClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(createFieldSpec())
                .addMethod(createConstructor(permissionElement.getContent()))
                .addMethods(createWithCheckMethods(permissionElement.getGrantedElements(), permissionElement.getTypeName()))
                .addMethods(createPermissionsMethods())
                .addMethod(createGrantedMethod(permissionElement))
                .build();
    }

    private FieldSpec createFieldSpec() {
        return FieldSpec.builder(String[].class, "mPermissions", Modifier.PRIVATE).build();
    }

    private MethodSpec createConstructor(String[] permissions) {
        StringBuilder stringBuilder = new StringBuilder();
        if (permissions.length>0) {
            stringBuilder.append("{");
            for (String permission : permissions) {
                stringBuilder.append("\""+permission+"\"").append(",");
            }
            stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(), "}");
        }
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.builder().addStatement("mPermissions = new String[] \u0024N", stringBuilder.toString()).build())
                .build();
    }

    private List<MethodSpec> createWithCheckMethods(List<Element> elements, TypeName typeName){
        List<MethodSpec> list = new ArrayList<>();

        for (Element element : elements) {
            list.add(getMethodSpec(element, typeName));
        }

        return list;
    }

    /**
     * get single methodSpec
     */
    private MethodSpec getMethodSpec(Element element, TypeName typeName) {
        MethodSpec methodSpec = MethodSpec.methodBuilder(element.getSimpleName().toString()).addModifiers(Modifier.PUBLIC).addParameter(typeName, TARGET)
                .addCode(CodeBlock.builder().add("\u0024N.\u0024N(", TARGET, element.getSimpleName().toString()).addStatement(")").build()).build();
        return methodSpec;
    }

    /**
     * create the method name of doGranted
     */
    private MethodSpec createGrantedMethod(PermissionElement permissionElement){
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(AnnotationConstant.METHOD_PERMISSION_GRANTED)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(permissionElement.getTypeName(), TARGET)
                .addParameter(String.class, "permission");
        List<Element> list = permissionElement.getGrantedElements();
        if (list.size()!=0) {
            methodBuilder.beginControlFlow("switch(\u0024N)", "permission");
            for (Element element : list) {
                methodBuilder.addCode("case \"\u0024N\":\n", element.getAnnotation(PermissionGranted.class).value())
                        .addCode("\u0024N(\u0024N", element.getSimpleName(), TARGET)
                        .addStatement(")")
                        .addStatement("break");
            }
            methodBuilder.addCode("default:\n")
                    .addStatement("break");
            methodBuilder.endControlFlow();
        }
        return methodBuilder.build();
    }

    /** 
     * create multiple methods
     */
    private List<MethodSpec> createPermissionsMethods(){
        MethodSpec methodSpec = MethodSpec.methodBuilder(AnnotationConstant.METHOD_PERMISSION).addModifiers(Modifier.PUBLIC)
                .addCode(CodeBlock.builder().addStatement("return mPermissions").build()).returns(String[].class).build();
        List<MethodSpec> list = new ArrayList<>();
        list.add(methodSpec);
        return list;
    }


}

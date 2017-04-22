# 前言
　　Google在Android6.0版本中添加了一些新特性，其中之一就是运行时请求权限，用户可以拒绝应用对敏感权限的请求。而对于android开发者，我们只需要将targetSdkVersion配置为23+就能使用该特性。网上已经存在一些流行的解决方案，比如使用注解的[PermissionsDispatcher](https://github.com/hotchemi/PermissionsDispatcher)，通过Rxjava实现的[RxPermissions](https://github.com/tbruyelle/RxPermissions)。前者使用直观，但是注解过多，而且需要自己调用注解生成的类文件，而RxPermissions使用方便（通过Fragment管理权限相关的回调方法），不过需要掌握Rxjava的相关知识，并且不易阅读。结合两者的特点，实现了[RuntimePermission](https://github.com/booqin/RuntimePermissions)。

# 使用方法
　　RuntimePermission提供了两种方法，但不管使用哪一种，使用者都需要知道哪个Activity需要使用敏感权限，以及使用哪些敏感权限，这是运行时授权的基本要求。
- 直接使用注解的方式:   
　　该方式简单，直观，但_注解只适用与Activity_，且提供的接口有限，该方式可以应付绝大多数运行时请求权限的需求。
- ~~接口的形式:~~  
　　~~该方法是弥补注解的不足，提供更多接口，适用场景也更广，缺点的需要实现更多接口，不方便阅读。~~ 

## 使用注解

### 1.添加依赖
在你app的build.gradle中添加如下依赖：

```
dependencies {
    compile 'com.xinguangnet.permission:runtime-permissions:1.0.0-SNAPSHOT'
    annotationProcessor 'com.xinguangnet.permission:compiler:1.0.0-SNAPSHOT'
}
```

### 2.添加权限
- 在你需要设置权限的app中添加相应权限到manifest中，如camera:

```xml
<uses-permission android:name="android.permission.CAMERA"/>
```

- 在需要使用对应权限的Activity中设置注解：

```java
@PermissionActivity(Manifest.permission.CAMERA)
public class MainActivity extends AppCompatActivity {
	……
}
```

注解_@PermissionActivity_支持字符串数组，所以你可以通过一下方式添加多个权限：

```java
@PermissionActivity({Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA})
```

### 3.权限回调
　　在RuntimePermission中可以通过注解@PermissionGranted完成授权结果的回调。注解在于其简洁，RuntimePermission只提供了权限许可时的注解@PermissionGranted，因为在使用上该注解可以满足绝大部分开发，同时过多的注解回导致代码的混乱，从而适得其反。  
　　对于你需要做的，只要在需要回调的方法（无参方法）上添加该备注，同时声明对应的权限即可：

```java
    @PermissionGranted(Manifest.permission.CAMERA)
    public void initCamera(){
        ……
    }
```
　　如果需要所有权限都被授权后，再做回调处理，可以通过一个无参注解来实现：

```java
    @PermissionGranted  
    public void allGranted(){
        ……
    }
```  

　　后续可能会开放更多注解回调。

### 4.开启请求
　　完成以上的代码后，可以通过RuntimePermission执行权限的动态申请，必传参数为acticity，该方法默认强制需要授权所有权限后才能运行该Activity。注解回调的请求:  

```java
    RuntimePermission.tryPermissionByAnnotation(Activity activity);
```
　　如果不需要强制授权，可以通过如下方法，设置false即可：

```java
    RuntimePermission.tryPermissionByAnnotation(MainActivity.this, false);
```

## 使用接口
　　为了尽量提高开发效率，暂时不开放接口调用，视使用情况在后续版本中决定是否提供。
　　

# 注意事项
　　由于动态权限申请的结果回调到_onRequestPermissionsResult_中，而该方法存在与Activity中，所以__@PermissionActivity只适用与Activity__，同时为了最大程度的精简代码，只提供了@PermissionGranted来实现权限被许可情况下的回调的一个无参方法，__@PermissionGranted需要在@PermissionActivity注解下的Activity中才生效__，这种依赖关系可以通过lint来提示开发者，这里暂时不做处理。
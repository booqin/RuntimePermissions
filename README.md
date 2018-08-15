# version
[![](https://jitpack.io/v/booqin/RuntimePermissions.svg)](https://jitpack.io/#booqin/RuntimePermissions)

## 前言
　　Google在Android6.0版本中添加了一些新特性，其中之一就是运行时请求权限，用户可以拒绝应用对敏感权限的请求。而对于android开发者，我们只需要将targetSdkVersion配置为23+就能使用该特性。网上已经存在一些流行的解决方案，比如使用注解的[PermissionsDispatcher](https://github.com/hotchemi/PermissionsDispatcher)，通过Rxjava实现的[RxPermissions](https://github.com/tbruyelle/RxPermissions)。前者使用直观，但是注解过多，而且对结果的处理关于麻烦，另外需要自己调用注解生成的类文件。而RxPermissions使用方便（通过Fragment管理权限相关的回调方法），但在使用上不够解耦，直观，其对权限请求结果的处理也缺少封装。根据自己对Android权限机制的理解，同时参考两者的优点，实现了[RuntimePermission](https://github.com/booqin/RuntimePermissions)。


## 简介
　　RuntimePermission提供了注解的方式来实现权限请求，虽然大大减少了的模板代码，但前提，使用者需要知道哪个Activity需要使用敏感权限，以及使用哪些敏感权限，这是运行时授权的基本要求。

　　RuntimePermission覆盖了大部分权限操作的场景，尽量减少权限对代码的耦合，apt生成的权限逻辑处理代码通过反射去调取，在最乐观的条件下，仅需要在对于Activity上加上需要使用权限的注解，然后在代码中手动执行下_RuntimePermission.tryPermissionByAnnotation(Activity.this)_即可:

```java
@PermissionActivity(Manifest.permission.ACCESS_COARSE_LOCATION)
public class MainActivity extends AppCompatActivity {

    ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...

        RuntimePermission.tryPermissionByAnnotation(MainActivity.this);

    }
}
```

在需要处理成功授权后的操作，只要在Activity中添加一个无参方法，加上对应注解：

```java
    @PermissionGranted
    public void initLocation(){
        Toast.makeText(this, "ACCESS_COARSE_LOCATION is granted", Toast.LENGTH_SHORT).show();
    }
```

## 使用

### 1.添加依赖
在project下的build.gradle添加maven地址：
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
在你app的build.gradle中添加如下依赖，version为对应的最新版本号：

```
dependencies {
    implementation 'com.github.booqin.RuntimePermissions:runtime-permission:version'
    annotationProcessor 'com.github.booqin.RuntimePermissions:compiler:version'
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
    RuntimePermission.tryPermissionByAnnotation(final Activity activity, boolean isMustGranted);
```

　　在特殊情况下，可能需要处理权限被拒绝后的事件，或者添加自定义Rationale下的提醒文本，RuntimePermission中预览了自定义接口来暴露以上事件：

```java
    RuntimePermission.tryPermissionByAnnotation(final Activity activity, boolean isMustGranted, final PermissionsDeniedResultListener permissionsDeniedResultListener)
```

　　当需要指定请求某权限时，使用如下方法：
```java
    RuntimePermission.tryPermissionByAnnotation(final Activity activity, String... permissions);
```
　　
# TODO
- 添加对Fragment的支持
- ~~对在多个权限中根据需求执行单个权限的请求~~已在v1.0.1中添加

# 注意事项
　　由于动态权限申请的结果回调到_onRequestPermissionsResult_中，而该方法存在与Activity中，所以__@PermissionActivity只适用与Activity__，同时为了最大程度的精简代码，只提供了@PermissionGranted来实现权限被许可情况下的回调的一个无参方法，__@PermissionGranted需要在@PermissionActivity注解下的Activity中才生效__，这种依赖关系可以通过lint来提示开发者，这里暂时不做处理。

　　Android SDK提供了注解功能的支持包，其中可以合理的使用@RequiresPermission注解，该注解会检查在manifest中是否声明该权限，并会显示对应的警告。
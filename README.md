> 本项目是参考的:https://github.com/renxuelong/ComponentDemo

## 演示为先
![](http://odgw9c93i.bkt.clouddn.com//FmkhuM5aV97FZBUO_w_nEyOTCrFK)

# Android 组件化最佳实践

在项目的开发过程中，随着开发人员的增多及功能的增加，如果提前没有使用合理的开发架构，那么代码会越来臃肿，功能间代码耦合也会越来越严重，这时候为了保证项目代码的质量，我们就必须进行重构。

比较简单的开发架构是按照功能模块进行拆分，也就是用 Android 开发中的 module 这个概念，每个功能都是一个 module，每个功能的代码都在自己所属的 module 中添加。这样的设计在各个功能相互直接比较独立的情况下是比较合理的，但是当多个模块中涉及到相同功能时代码的耦合又会增加。

例如首页模块和直播间模块中都可能涉及到了视频播放的功能，这时候不管将播放控制的代码放到首页还是直播间，开发过程中都会发现，我们想要解决的代码耦合情况又又又又出现了。为了进一步解决这个问题，组件化的开发模式顺势而来。

## 一、组件化和模块化的区别

上面说到了从普通的无架构到模块化，再由模块化到组件化，那么其中的界限是什么，模块化和组件化的本质区别又是什么？为了解决这些问题，我们就要先了解 “模块” 和 “组件” 的区别。

#### 模块
模块指的是独立的业务模块，比如刚才提到的 [首页模块]、[直播间模块] 等。

#### 组件
组件指的是单一的功能组件，如 [视频组件]、[支付组件] 等，每个组件都可以以一个单独的 module 开发，并且可以单独抽出来作为 SDK 对外发布使用。

由此来看，[模块] 和 [组件] 间最明显的区别就是模块相对与组件来说粒度更大，一个模块中可能包含多个组件。并且两种方式的本质思想是一样的，都是为了代码重用和业务解耦。在划分的时候，模块化是业务导向，组件化是功能导向。

 ![组件化基础架构图](http://odgw9c93i.bkt.clouddn.com//FvXPlaQgAdCgkV2LsGQHxnt4WB1Q)

上面是一个非常基础的组件化架构图，图中从上向下分别为应用层、组件层和基础层。

**基础层：** 基础层很容易理解，其中包含的是一些基础库以及对基础库的封装，比如常用的图片加载，网络请求，数据存储操作等等，其他模块或者组件都可以引用同一套基础库，这样不但只需要开发一套代码，还解耦了基础功能和业务功能的耦合，在基础库变更时更加容易操作。

**组件层：** 基础层往上是组件层，组件层就包含一些简单的功能组件，比如视频，支付等等

**应用层：** 组件层往上是应用层，这里为了简单，只添加了一个 APP ，APP 就相当于我们的模块，一个具体的业务模块会按需引用不同的组件，最终实现业务功能，这里如果又多个业务模块，就可以各自按需引用组件，最后将各个模块统筹输出 APP。

到这里我们最简单的组件化架构就已经可以使用了，但是这只是最理想的状态下的架构，实际的开发中，不同的组件不可能彻底的相互隔离，组件中肯定会有相互传递数据、调用方法、页面跳转等情况。

比如直播组件中用户需要刷礼物，刷礼物就需要支付组件的支持，而支付组件中支付操作是必须需要登录状态、用户 ID 等信息。如果当前未登录，是需要先跳转到登录组件中进行登录操作，登录成功后才能正常的进行支付流程。

而我们上面的架构图中，各个组件之间是相互隔离的，没有相互依赖，如果想直接进行组件交互，也就是组件间相互依赖，这就又违背了组件化开发的规则。所以我们必须找到方法解决这些问题才能进行组件化开发。


## 二、组件化开发需要解决的问题

在实现组件化的过程中，同一个问题可能有不同的技术路径可以解决，但是需要解决的问题主要有以下几点：

1. 每个组件都是一个完整的整体，所以组件开发过程中要满足单独运行及调试的要求，这样还可以提升开发过程中项目的编译速度。

2. 数据传递与组件间方法的相互调用，这也是上面我们提到的一个必须要解决的问题。

3. 组件间界面跳转，不同组件之间不仅会有数据的传递，也会有相互的页面跳转。在组件化开发过程中如何在不相互依赖的情况下实现互相跳转？

4. 主项目不直接访问组件中具体类的情况下，如何获取组件中 Fragment 的实例并将组件中的 Fragment 实例添加到主项目的界面中？

5. 组件开发完成后相互之间的集成调试如何实现？还有就是在集成调试阶段，依赖多个组件进行开发时，如果实现只依赖部分组件时可以编译通过？这样也会降低编译时间，提升效率。

6. 组件解耦的目标以及如何实现代码隔离？不仅组件之间相互隔离，还有第五个问题中模块依赖组件时可以动态增删组件，这样就是模块不会对组件中特定的类进行操作，所以完全的隔绝模块对组件中类的使用会使解耦更加彻底，程序也更加健壮。

以上就是实现组件化的过程中我们要解决的主要问题，下面我们会一个一个来解决，最终实现比较合理的组件化开发。


## 三、组件单独调试

### 1. 动态配置组件的工程类型？

在 AndroidStudio 开发 Android 项目时，使用的是 Gradle 来构建，具体来说使用的是 Android Gradle 插件来构建，Android Gradle 中提供了三种插件，在开发中可以通过配置不同的插件来配置不同的工程。

- App 插件，id: com.android.application
- Library 插件，id: com.android.libraay
- Test 插件，id: com.android.test

区别比较简单， App 插件来配置一个 Android App 工程，项目构建后输出一个 APK 安装包，Library 插件来配置一个 Android Library 工程，构建后输出 aar 包，Test 插件来配置一个 Android Test 工程。我们这里主要使用 App 插件和 Library 插件来实现组件的单独调试。这里就出现了第一个小问题，如何动态配置组件的工程类型？

通过工程的 build.gradle 文件中依赖的 Android Gradle 插件 id 来配置工程的类型，但是我们的组件既可以单独调试又可以被其他模块依赖，所以这里的插件 id 我们不应该写死，而是通过在 module 中添加一个 gradle.properties 配置文件，在配置文件中添加一个布尔类型的变量 isRunAlone，在 build.gradle 中通过 isRunAlone 的值来使用不同的插件从而配置不同的工程类型，在单独调试和集成调试时直接修改 isRunAlone 的值即可。例如，在 Share 分享组件中的配置：

![](http://odgw9c93i.bkt.clouddn.com//Foylv2g4NRA7MTOK5O1GTGVK4SLZ)
![](http://odgw9c93i.bkt.clouddn.com//FmVWkuY5GhwF5ntf_C0WfpODlGsk)

### 2. 如何动态配置组件的 ApplicationId 和 AndroidManifest 文件

除了通过依赖的插件来配置不同的工程，我们还要根据 isRunAlone 的值来修改其他配置，一个 APP 是只有一个 ApplicationId 的，所以在单独调试和集成调试时组件的 ApplicationId 应该是不同的；一般来说一个 APP 也应该只有一个启动页， 在组件单独调试时也是需要有一个启动页，在集成调试时如果不处理启动页的问题，主工程和组件的 AndroidManifes 文件合并后就会出现两个启动页，这个问题也是需要解决的。

ApplicationId 和 AndroidManifest 文件都是可以在 build.gradle 文件中进行配置的，所以我们同样通过动态配置组件工程类型时定义的 isRunAlone 这个变量的值来动态修改 ApplicationId 和 AndroidManifest。首先我们要新建一个 AndroidManifest.xml 文件，加上原有的 AndroidManifest 文件，在两个文件中就可以分别配置单独调试和集成调试时的不同的配置，如图：

![](http://odgw9c93i.bkt.clouddn.com//FmN2a1F52enSbYvOQcXjRquUrZ4s)

其中 AndroidManifest 文件中的内容如下：

```
// main/manifest/AndroidManifest.xml 单独调试
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.loong.share">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".ShareActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>

// main/AndroidManifest.xml 集成调试
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.loong.share">

    <application android:theme="@style/AppTheme">
        <activity android:name=".ShareActivity"/>
    </application>

</manifest>
```

然后在 build.gradle 中通过判断 isRunAlone 的值，来配置不同的 ApplicationId 和 AndroidManifest.xml 文件的路径

```
// share 组件的 build.gradle

android {
    defaultConfig {
        if (isRunAlone.toBoolean()) {
            // 单独调试时添加 applicationId ，集成调试时移除
            applicationId "com.loong.login"
        }
        ...
    }
    
    sourceSets {
        main {
            // 单独调试与集成调试时使用不同的 AndroidManifest.xml 文件
            if (isRunAlone.toBoolean()) {
                manifest.srcFile 'src/main/manifest/AndroidManifest.xml'
            } else {
                manifest.srcFile 'src/main/AndroidManifest.xml'
            }
        }
    }
}

```

到这里我们就解决了组件化开发时遇到的第一个问题，实现了组件的单独调试与集成调试，并在不同情况时使用的不同配置。当然 build.gradle 中通过 Android Gradle 插件，我们还可以根据不同工程配置不同的 Java 源代码、不同的 resource 资源文件等的，有了上面问题的解决方式，这些问题就都可以解决了。


## 四、组件间数据传递与方法的相互调用

由于主项目与组件，组件与组件之间都是不可以直接使用类的相互引用来进行数据传递的，那么在开发过程中如果有组件间的数据传递时应该如何解决呢，这里我们可以采用 [接口 + 实现] 的方式来解决。

在这里可以添加一个 ComponentBase 模块，这个模块被所有的组件依赖，在这个模块中分别添加定义了组件可以对外提供访问自身数据的抽象方法的 Service。ComponentBase 中还提供了一个 ServiceFactory，每个组件中都要提供一个类实现自己对应的 Service 中的抽象方法。在组件加载后，需要创建一个实现类的对象，然后将实现了 Service 的类的对象添加到 ServiceFactory 中。这样在不同组件交互时就可以通过 ServiceFactory 获取想要调用的组件的接口实现，然后调用其中的特定方法就可以实现组件间的数据传递与方法调用。

当然，ServiceFactory 中也会提供所有的 Service 的空实现，在组件单独调试或部分集成调试时避免出现由于实现类对象为空引起的空指针异常。

下面我们就按照这个方法来解决组件间数据传递与方法的相互调用这个问题，这里我们通过**分享组件** 中调用 **登录组件** 中的方法来获取登录状态这个场景来演示。

### 1. 创建 componentbase 模块

AndroidStudio 中创建模块比较简单，通过菜单栏中的 File -> New -> New Module 来创建我们的 componentbase 模块。需要注意的是我们在创建组件时需要使用 Phone & Tablet Module ，创建 componentbase 模块时使用 Android Library 来创建，其中的区别是通过 Phone & Tablet Module 创建的默认是 APP 工程，通过 Android Library 创建的默认是 Library 工程，区别我们上面已经说过了。当然如果选错了也不要紧，在 buidl.gradle 中也可以自己来修改配置。如下图：

![](http://odgw9c93i.bkt.clouddn.com//FkgJNpwGNAQLcUOZPNBq9cgYdTqg)

这里 Login 组件中提供获取登录状态和获取登录用户 AccountId 的两个方法，分享组件中的分享操作需要用户登录才可以进行，如果用户未登录则不进行分享操作。我们先看一下 componentbase 模块中的文件结构：

![](http://odgw9c93i.bkt.clouddn.com//FnvQqvYTqleIhONTIox-O8pZRRmZ)

其中 service 文件夹中定义接口， IAccountService 接口中定义了 Login 组件向外提供的数据传递的接口方法，empty_service 中是 service 中定义的接口的空实现，ServiceFactory 接收组件中实现的接口对象的注册以及向外提供特定组件的接口实现。

```
// IAccountService
public interface IAccountService {

    /**
     * 是否已经登录
     * @return
     */
    boolean isLogin();

    /**
     * 获取登录用户的 AccountId
     * @return
     */
    String getAccountId();
}

// EmptyAccountService
public class EmptyAccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public String getAccountId() {
        return null;
    }
}

// ServiceFacoty
public class ServiceFactory {

    private IAccountService accountService;

    /**
     * 禁止外部创建 ServiceFactory 对象
     */
    private ServiceFactory() {
    }

    /**
     * 通过静态内部类方式实现 ServiceFactory 的单例
     */
    public static ServiceFactory getInstance() {
        return Inner.serviceFactory;
    }

    private static class Inner {
        private static ServiceFactory serviceFactory = new ServiceFactory();
    }

    /**
     * 接收 Login 组件实现的 Service 实例
     */
    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 返回 Login 组件的 Service 实例
     */
    public IAccountService getAccountService() {
        if (accountService == null) {
            accountService = new EmptyAccountService();
        }
        return accountService;
    }
}
```

前面我们提到的组件化架构图中，所有的组件都依赖 Base 模块，而 componentbase 模块也是所有组件需要依赖的，所以我们可以让 Base 模块依赖 componentbase 模块，这样在组件中依赖 Base 模块后就可以访问 componentbase 模块中的类。

### 2. Login 组件在 ServiceFactory 中注册接口对象

在 componentbase 定义好 Login 组件需要提供的 Service 后，Login 组件需要依赖 componentbase 模块，然后在 Login 组件中创建类实现 IAccountService 接口并实现其中的接口方法，并在 Login 组件初始化(最好是在 Application 中) 时将 IAccountService 接口的实现类对象注册到 ServiceFactory 中。相关代码如下：

```
// Base 模块的 build.gradle
dependencies {
    api project (':componentbase')
    ...
}

// login 组件的 build.gradle
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation project (':base')
}

// login 组件中的 IAccountService 实现类
public class AccountService implements IAccountService {
    @Override
    public boolean isLogin() {
        return AccountUtils.userInfo != null;
    }

    @Override
    public String getAccountId() {
        return AccountUtils.userInfo == null ? null : AccountUtils.userInfo.getAccountId();
    }
}

// login 组件中的 Aplication 类
public class LoginApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        // 将 AccountService 类的实例注册到 ServiceFactory
        ServiceFactory.getInstance().setAccountService(new AccountService());
    }
}
```

以上代码就是 Login 组件中对外提供服务的关键代码，到这里有的小伙伴可能想到了，一个项目时只能有一个 Application 的，Login 作为组件时，主模块的 Application 类会初始化，而 Login 组件中的 Applicaiton 不会初始化。确实是存在这个问题的，我们这里先将 Service 的注册放到这里，稍后我们会解决 Login 作为组件时 Appliaciton 不会初始化的问题。

### 3. Share 组件与 Login 组件实现数据传递

Login 组件中将 IAccountService 的实现类对象注册到 ServiceFactory 中以后，其他模块就可以使用这个 Service 与 Login 组件进行数据传递，我们在 Share 组件中需要使用登录状态，接下来我们看 Share 组件中如何使用 Login 组件提供的 Service。

同样，Share 组件也是依赖了 Base 模块的，所以也可以直接访问到 componentbase 模块中的类，在 Share 组件中直接通过 ServiceFactory 对象的 getAccountService 即可获取到 Login 组件提供的 IAccountService 接口的实现类对象，然后通过调用该对象的方法即可实现与 Login 组件的数据传递。主要代码如下：

```
// Share 组件的 buidl.gradle
dependencies {
    implementation project (':base')
    ...
}

// Share 组件的 ShareActivity
public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        share();
    }

    private void share() {
        if(ServiceFactory.getInstance().getAccountService().isLogin()) {
            Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "分享失败：用户未登录", Toast.LENGTH_SHORT);
        }
    }
}
```

这样的开发模式实现了各个组件间的数据传递都是基于接口编程，接口和实现完全分离，所以就实现了组件间的解耦。在组件内部的实现类对方法的实现进行修改时，更极端的情况下，我们直接删除、替换了组件时，只要新加的组件实现了对应 Service 中的抽象方法并在初始化时将实现类对象注册到 ServiceFactory 中，其他与这个组件有数据传递的组件都不需要有任何修改。

到这里我们组件间数据传递和方法调用的问题就已经解决了，其实，组件间交互还有很多其他的方式，比如 EventBus，广播，数据持久化等方式，但是往往这些方式的交互会不那么直观，所以对通过 Service 这种形式可以实现的交互，我们最好通过这种方式进行。

### 4. 组件 Application 的动态配置

上面提到了由于 Application 的替换原则，在主模块中有 Application 等情况下，组件在集中调试时其 Applicaiton 不会初始化的问题。而我们组件的 Service 在 ServiceFactory 的注册又必须放到组件初始化的地方。

为了解决这个问题可以将组件的 Service 类强引用到主 Module 的 Application 中进行初始化，这就必须要求

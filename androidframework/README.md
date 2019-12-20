### Android开发框架简单介绍

框架总共包含5部分：数据库模块，下载模块，网络请求模块，图片加载模块，view注入模块。

* 写在开头，使用框架时，需要在Application类中初始化框架
```
 LKUtil.init(this);
```

* 数据库模块

>首先是声明数据表实体映射类
```java
@Table(name = "user")
public class User{

    @Column(name = "name",isId = true)
    private String name;

    @Column(name = "passwd")
    private String passwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
```
>接下来是数据表操作方法
```
 User user = new User();
 user.setName("test");
 user.setPasswd("test");
 //保存
 LKUtil.getDb().saveOrUpdate(user);
 //查询
 User user = LKUtil.getDb().findFirst(User.class);

```

* 下载模块

>简单的操作方法，支持断点续传。

```
 File file = new File(LKUtil.getAppConfig().getExternalDownCacheDir(),"office.exe");
 LKUtil.getLkDownFileManager().downFile("https://s1.music.126.net/download/pc/cloudmusicsetup_2_2_1[192801].exe",file,1, downHandler);

 //下载回调
 DownHandler downHandler = new DownHandler() {
         @Override
         public void onSuccess(String path) {
             LogUtil.d("下载成功。。。。"+path);
         }
 
         @Override
         public void onFailed() {
 
         }
 };
 
```

* 网络请求模块

>支持get,post方法。支持base64参数加密

>加密配置信息获取
```
//获取传输参数加密信息，加密信息可后台配置
LKUtil.getLkHttpManager().get("http://192.168.2.115:80/encrypto",httpAsycResponceHandler);

//加密信息接口回调
 HttpAsycResponceHandler<EncryptoEntity> httpAsycResponceHandler = new HttpAsycResponceHandler<EncryptoEntity>(){
        @Override
        public void onSuccess(EncryptoEntity users) {
            //生成xml文件，保存到本地
            XmlUtil.generationXml(users,LKUtil.getAppConfig().getCryptoCacheFile());
            //启用加密通道
            LKUtil.enableCrypto();
        }

        @Override
        public void onFailed(int code, String message) {
            Log.d(MainActivity.class.getName(),message);
        }

        @Override
        public void onError() {
            Log.d(MainActivity.class.getName(),"error");
        }
    };

```

>post请求

```
  //请求参数
  LKRequestParams requestParams = new LKRequestParams("test","test");
  List<String> list = new ArrayList<>();
  list.add("arrtest");
  requestParams.setArrayKey("arr");
  requestParams.setArrayValue(list);
  List<LKRequestParams> lists = new ArrayList<>();
  lists.add(new LKRequestParams("LIST","list"));
  requestParams.setListKey("list");
  requestParams.setListValue(lists);
  LKUtil.getLkHttpManager().post("http://192.168.2.115:80/test",requestParams,testHandler);

 //接口回调
 HttpAsycResponceHandler<List<User>> testHandler = new HttpAsycResponceHandler<List<User>>(true){
        @Override
        public void onSuccess(List<User> users) {
            LKUtil.getDb().saveOrUpdate(users);
            User user = LKUtil.getDb().findFirst(User.class);
            Log.d(MainActivity.class.getName(),user.getName());
        }

        @Override
        public void onFailed(int code, String message) {
            Log.d(MainActivity.class.getName(),message);
        }

        @Override
        public void onError() {
            Log.d(MainActivity.class.getName(),"error");
        }
    };
```

* 图片加载模块

>支持本地缓存，只需加载一次，下次直接从本地读取
```
 LKUtil.getLkImageLoader().loadImage("http://tse3.mm.bing.net/th?id=OIP.O-QoMwNBHQDOicRUltTgtwEsEs&pid=15.1",imageView);
```

* view注入模块

>没啥说的，看代码
```java

@LKContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @LKInjectView(R.id.test)
    TextView test;
    @LKInjectView(R.id.encrypto)
    TextView encrypto;

    @LKInjectView((R.id.imagetest))
    ImageView imageView;
}
```
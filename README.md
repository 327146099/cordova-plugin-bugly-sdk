## 项目说明	

腾讯[Buyly SDK](https://bugly.qq.com/)的Cordova插件。


## 项目使用

### 安装



```shell
cordova plugin add cordova-plugin-bugly-sdk  --variable ANDROID_APPID=your value
```



### 调用



**初始化SDK**

```javascript
var args = {
    // 通用配置
    debug:true,
    channel:"test",
    develop:true,
    version:"1.0",
    // 安卓配置
    // delay:20000,
    // package:"com.jasonz.bugly.demo",
};

Bugly.initSDK(function(success){
	console.log("初始化成功");
},function(err){
   console.log("初始化失败");
   console.log(err);
},args);

```



**开启Javascript异常捕获（仅安卓）**



```javascript

// 手动引入bugly.js
  Bugly.enableJSMonitor();

```



**设置用户ID**



```javascript
  var id = "xxxxx";
  Bugly.setUserID(id);

```



**设置TagID(id必须是数字)**



```javascript
  var id = 10086;
  Bugly.setTagID(id);

```



**设置用户自定义数据**




```javascript

  var data = {
    key:'id',
    value:1
  };

 Bugly.putUserData(id);

```







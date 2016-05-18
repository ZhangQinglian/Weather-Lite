# Weather-Lite
Weather-Lite是一款开源的天气应用APP，简约但并不简单。

## 天气预报接口
Weather-Lite采用的天气预报接口为[和风天气预报接口服务](http://www.heweather.com)的免费接口，虽免费但使用的时候仍需注册并获得相关apikey。
⚠️Weather-Lite同步到本地是无法正常使用的，需要你在上述网站注册开发者并获取apikey然后替换下面的代码：

**com.zqlite.android.weather_lite.rest.HeWeatherREST.java**
```java
    //在http://www.heweather.com处申请apikey并替换xxx
    public static final String API_KEY = "xxx";
```
## 功能
- 可自由添加国内2567个城市
- 自由排列城市顺序
- 首页查看城市列表的天气概览，点击城市可详细查看城市未来七天天气，天气指数等信息

## 相关技术
- API网络请求：retrofit
- 异步网络请求：rxjava
- 天气图标：Google now weather

## 应用截图
![](http://7xprgn.com1.z0.glb.clouddn.com/device-2016-05-17-173036.png)
![](http://7xprgn.com1.z0.glb.clouddn.com/device-2016-05-17-173115.png)
![](http://7xprgn.com1.z0.glb.clouddn.com/device-2016-05-17-173149.png)
![](http://7xprgn.com1.z0.glb.clouddn.com/device-2016-05-17-173603.png)
![](http://7xprgn.com1.z0.glb.clouddn.com/device-2016-05-17-173203.png)


#License

     Copyright 2016 zhangqinglian

  	Licensed under the Apache License, Version 2.0 (the "License");
  	you may not use this file except in compliance with the License.
  	You may obtain a copy of the License at

	     http://www.apache.org/licenses/LICENSE-2.0

  	Unless required by applicable law or agreed to in writing, software
	  distributed under the License is distributed on an "AS IS" BASIS,
	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	  See the License for the specific language governing permissions and
	  limitations under the License.

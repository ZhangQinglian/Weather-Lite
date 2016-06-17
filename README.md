# Weather-Lite
Weather-Lite is an Android weather app.[中文](https://github.com/ZhangQinglian/Weather-Lite/blob/master/README_CH.md)

## Weather API
Weather-Lite use [HeWeather API](http://www.heweather.com),to use the api you should sign up and get your APIKEY and replace the 'xxx':

**com.zqlite.android.weather_lite.rest.HeWeatherREST.java**
```java
    //get apikey from http://www.heweather.com
    public static final String API_KEY = "xxx";
```
## Function
- add 2567 cities in China.
- custom the cities' sort order.
- preview city weather,add city witch you need,preview city's weather in detail.

## Library
- retrofit
- rxjava

## Branch
- master : no architecture
- use-mvpc : use mvp＋clean architecture

## ScreenS capture
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

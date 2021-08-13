# sino-sdk说明文档

------



[TOC]

## jssdk使用步骤

------

#### **引入js**

在需要调用JS接口的页面引入JS文件 **sinojs-sdk.js**

支持使用 AMD/CMD 标准模块加载方法加载

#### **接口调用说明**
所有接口通过sino对象来调用，参数是一个对象，除了每个接口本身需要传的参数之外，还有以下通用参数：

success： 	 接口调用成功时执行的回调函数。  
fail：			  接口调用失败时执行的回调函数。  
complete：   接口调用完成时执行的回调函数，无论成功或失败都会执行。  
cancel：		用户点击取消时的回调函数，仅部分有用户取消操作的api才会用到。  

以上几个函数都带有一个参数，类型为对象，其中除了每个接口本身返回的数据之外，还有一些通用属性，如：```{
    errCode: 0,
    errMsg: ""
}```  
其中errCode为必有的integer字段，含义参照错误码,errMsg为调用失败的具体描述

#### **错误码**

| errCode | 含义                         |
| ------- | ---------------------------- |
| 0       | 正常                         |
| 1       | 取消操作                     |
| 400     | 无效的请求参数               |
| 403     | 没有该方法的调用权限         |
| 404     | 请求的方法或者事件名没有找到 |

#### **如何判断是否运行在sinojs app环境中**
```
let match = 
navigator.userAgent.match(/sinojs\/(\d+\.\d+\.\d+)/)
||
navigator.userAgent.match(/sinojs\/(\d+\.\d+)/)

let vCode = match && match[1]
```



## **基础接口**

------

#### **check容器后台前台切换**

```
window.sino.onContainerResume(ret => {
    console.log(`onContainerResume ${JSON.stringify(ret)}`);
});
window.sino.onContainerPause(ret => {
    console.log(`onContainerPause ${JSON.stringify(ret)}`);
});
```



## 工具类接口

------

#### **设置浏览器标题文字**
```
sino.setTitle({
    title: "test title" //标题
});
```

#### **退出容器**
```
sino.closeWindow({});
```

#### TODO   **键值对存储-存**
```
sino.putLocalStorageKV({
    key:""      //键
    value:""    //值
});
```

#### TODO   **键值对存储-读**
```
sino.getLocalStorageKV({
    key:"",             //键
    success(ret){
        let ret.value   //取到的值
    }
});
```

------



## 图片类接口

------

#### **选择图片**
```
sino.choosePhotos({
    enableCount: 3, //可选择数量，0<enableCount<=9
    success(ret) {
        let result = ret.result; //[{nativeResourceUrl:"",name:"",size:0}]
        result.forEach(element => {
            let nativeResourceUrl = element.nativeResourceUrl; //图片本地地址，可以直接指定为image标签src
            let name = element.name; //名称
            let size = element.size; //文件大小
        });
    }
});
```

#### TODO   **上传图片**
```
sino.uploadPhotos({
    nativeResourceUrls: [""], //choosePhotos获取到的
    success(ret) {
        let result = ret.result; //[{nativeResourceUrl:"",serverResourceUrl:""}]
        result.forEach(element => {
            let nativeResourceUrl = element.nativeResourceUrl; //图片本地地址
            let serverResourceUrl = element.serverResourceUrl; //服务器地址
        });
    }
});
```

#### TODO   **预览图片**
```
sino.previewPhotos({
    urls: ["", ""], //可以为nativeResourceUrl
    index: 0, //打开时展示的图片位置
    success() {}
});
```



## 视频类接口

------

#### TODO   **选择视频**
```
sino.chooseVideos({
    enableCount: 3, //可选择数量,0<enableCount<=3
    success(ret) {
        let result = ret.result; //[{nativeResourceUrl:"",name:"",size:0}]  
        result.forEach(element => {
            let nativeResourceUrl = element.nativeResourceUrl; //本地地址
            let name = element.name; //名称
            let size = element.size; //文件大小
            let videoTime = element.videoTime; //视频时长，毫秒
        });
    }
});
```

#### TODO   **上传视频**
```
sino.uploadVideos({
    nativeResourceUrls: [""], //chooseVideos获取到的
    success(ret) {
        let result = ret.result; //[{nativeResourceUrl:"",serverResourceUrl:""}]
        result.forEach(element => {
            let nativeResourceUrl = element.nativeResourceUrl; //本地地址
            let serverResourceUrl = element.serverResourceUrl; //服务器地址
        });
    }
});
```

#### TODO   **预览视频**
```
sino.previewVideo({
    url: "", //可以为nativeResourceUrl
    success() {}
});
```



## 文件类接口

------

#### TODO   **选择文件**
```
sino.chooseFile({
    enableCount: 3, //可选择数量，0<enableCount<=9，在多选时才处理-iOS不支持多选
    maxSize: 3, //单个文件限制大小：0:无限制; >0 限制最大值，单位为MB
    isMultiSelect: true, //（true，为多选  false，单选-iOS不支持多选）
    success(ret) {
        let result = ret.result; //[{nativeResourceUrl:"",name:"",size:0}]  
        result.forEach(element => {
            let nativeResourceUrl = element.nativeResourceUrl; //本地地址
            let name = element.name; //名称
            let size = element.size; //文件大小
        });
    }
});
```

#### TODO   **上传文件**
```
sino.uploadFile({
    nativeResourceUrls: [""], //chooseVideos获取到的
    success(ret) {
        let result = ret.result; //[{nativeResourceUrl:"",serverResourceUrl:""}]
        result.forEach(element => {
            let nativeResourceUrl = element.nativeResourceUrl; //本地地址
            let serverResourceUrl = element.serverResourceUrl; //服务器地址
        });
    }
});
```

#### TODO    **预览文件**
```
sino.previewFile({
    url: "", //可以为nativeResourceUrl
    success() {}
});
```



## 位置类接口

------

#### TODO    **通过界面选取一个位置**
```
sino.chooseLocation({
    success(ret) {
        let address = ret.address; // 详细地址
        let latitude = ret.latitude; // 纬度，浮点数，范围为90 ~ -90
        let longitude = ret.longitude; // 经度，浮点数，范围为180 ~ -180。
    }
});
```

#### TODO   **直接获取当前位置**
```
sino.getLocation({
    success(ret) {
        let address = ret.address; // 详细地址
        let latitude = ret.latitude; // 纬度，浮点数，范围为90 ~ -90
        let longitude = ret.longitude; // 经度，浮点数，范围为180 ~ -180。
    }
});
```

#### **TODO   打开地图预览**
```
sino.getLocation({
    success(ret) {}
});
```

//需要注入的js方法
function iosFun() {
    window.__sino = {_sendMessage : function(type){
        var js = {'Sino-Auth':'tokenPlaceHolder','statusBarHeight':0};
        return JSON.stringify(js)
    }}
    
}
//注入成功后先调用执行
iosFun();
/*
 //    window.localStorage.setItem('Sino-Auth','tokenPlaceHolder');
 //    var objc111 =  {
 //        name111 : "11",
 //        age222 : "30",
 //        getdata: function() {
 //            console.log("990011")
 //        }
 //    };
 //    window.objc111 = objc111;
 */


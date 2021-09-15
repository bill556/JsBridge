//
//  SNPublicWebVC.swift
//  SinoHealthy
//
//  Created by leii on 2021/1/25.
//

import UIKit
import WebKit
import Photos
class SNPublicWebVC: UIViewController  {
    let uuid = UUID.init().uuidString
    var jsEventList = [SNJSEventModel]()
    lazy var config : WKWebViewConfiguration = {
        let config = WKWebViewConfiguration.init()
        // 设置webView的配置
        config.userContentController = WKUserContentController.init()
//        do{
//
//            let filePath = Bundle.main.path(forResource: "bridge", ofType: "js") ?? ""
//            //获取代码
//            guard var jsString = try? String(contentsOfFile:filePath ) else {
//                return config
//            }
//            let script = WKUserScript.init(source: jsString, injectionTime: WKUserScriptInjectionTime.atDocumentStart, forMainFrameOnly: true)
//            config.userContentController.addUserScript(script)
//
//        }
        do{
            
            let filePath = Bundle.main.path(forResource: "sinojs", ofType: "js") ?? ""
            //获取代码
            guard var jsString = try? String(contentsOfFile:filePath ) else {
                return config
            }
           // jsString = jsString.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)!
            jsString =  jsString.replacingOccurrences(of: "${_dgtVerifyRandomStr}", with: uuid)
            jsString =  jsString.replacingOccurrences(of: "${_debugLevel}", with: "0")
            let script = WKUserScript.init(source: jsString, injectionTime: WKUserScriptInjectionTime.atDocumentStart, forMainFrameOnly: true)
            config.userContentController.addUserScript(script)
            
        }
        // __sino
    
        
        config.userContentController.add(SNScriptMessageHandler.init(messageHander: self), name: "_sendMessage")
        
        return config
    }()
    lazy var webView: WKWebView = {
        //创建webView
        let webView =  WKWebView.init(frame: CGRect(x: 0, y: 0, width: KScreenW, height: KScreenH ), configuration: config)
        //webView.scrollView.contentInset = UIEdgeInsets.init(top: 0, left: 0, bottom: 40, right: 0)
        
        //导航代理
        webView.navigationDelegate = self
        //交互代理
        webView.uiDelegate = self
        //WKScriptMessageHandler
        
        return webView
    
    }()
    
    deinit {
        SNPrint("*******\(self.classForCoder)******deinit******")
        self.webView.removeObserver(self, forKeyPath: "title")
        webView.configuration.userContentController.removeScriptMessageHandler(forName: "_sendMessage")
        URLProtocol.wk_unregisterScheme("content")
        //cleanWebCacheAndLibrary 清除缓存
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.navigationController?.setNavigationBarHidden(isHiddleNav, animated: true)
        navigationController?.interactivePopGestureRecognizer?.isEnabled = false
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.interactivePopGestureRecognizer?.isEnabled = true
        
    }
    var urlString = ""
    var isHiddleNav = false
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        URLProtocol.registerClass(HybridNSURLProtocol.classForCoder())
        URLProtocol.wk_registerScheme("content")
        //URLProtocol.wk_registerScheme("http")
        //URLProtocol.wk_registerScheme("https")
        //加载网页
        //"http://172.9.194.173:8080/mall.html#/"
        //let urlString = "http://172.9.182.176:8080/mall#/productDetail"
        //获取代码
//        do{
//            guard let pathUrl = URL(string:urlString) else {
//                print("网址不合法")
//                return
//            }
//            let request = URLRequest.init(url: pathUrl)
//            webView.allowsBackForwardNavigationGestures = true
//            webView.load(request)
//        }
        //加载网页
        let filePath = Bundle.main.path(forResource: "index", ofType: "html") ?? ""
        //获取代码
        let pathURL =  URL(fileURLWithPath: filePath)
        let request = URLRequest.init(url: pathURL)
        
        //let request = URLRequest.init(url: URL(string: "http://www.baidu.com")!)
        //webView.allowsBackForwardNavigationGestures = true
        webView.load(request)
        
        view.addSubview(webView)
        if isHiddleNav == true {
            webView.frame = CGRect(x: 0, y: kStatusBarH, width: KScreenW, height: KScreenH - kStatusBarH )
        }else{
            webView.frame =  CGRect(x: 0, y: kTopH, width: KScreenW, height: KScreenH - kTopH )
        }
        webView.addObserver(self, forKeyPath: "title", options: NSKeyValueObservingOptions.new, context: nil)
        
        if #available(iOS 11.0, *) {
            self.webView.scrollView.contentInsetAdjustmentBehavior = .never
            
        }
        self.extendedLayoutIncludesOpaqueBars = true
        self.automaticallyAdjustsScrollViewInsets = false
        
    }
    
}
extension SNPublicWebVC :WKNavigationDelegate, WKScriptMessageHandler, WKUIDelegate {
    
    override  func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        
        if let title = change![NSKeyValueChangeKey.newKey] as? String {
            self.navigationItem.title = title
        }
    }
    
    //网页加载完成
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        
//        //注入监听输入框改变的方法和点击button的方法
//        //获取js代码存放路径
//        let filePath = Bundle.main.path(forResource: "bridge", ofType: "js") ?? ""
//        //获取代码
//        guard var jsString = try? String(contentsOfFile:filePath ) else {
//            // 沒有讀取出來則不執行注入
//            return
//        }
//        //在bridge.js文件里给输入框赋值是临时的,这里可以替换
//        jsString = jsString.replacingOccurrences(of: "Placeholder_searchKey", with: "这里可以更换值")
//        //获取到的代码 注入到web
//        webView.evaluateJavaScript(jsString, completionHandler: { _, _ in
//            SNPrint("代码注入成功")
//        })
    }
    
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        //接收到js发送的消息
        //判断消息名字 name    String    ""
        let name =  message.name // _sendMessage
        guard name == "_sendMessage" , let body = message.body as? String else {
            print("无法识别的数据结构")
            return
        }
        
        guard let bodyDict =  body.toAny() as? [String:String] else {
            print("无法识别的数据结构")
            return
        }
        
        
        let  jsonMessage = bodyDict["jsonMessage"] ?? ""
        let sha1Original  = jsonMessage + uuid
        let sha1Result = sha1Original.sha1()
        
        if let shaKey = bodyDict["shaKey"] ,shaKey != sha1Result {
            print("哈希校验失败")
            print("shaKey:\(shaKey)")
            print("result:\(sha1Result)")
            return
        }
        print(self.convertToJsonData(data: bodyDict as Any))
        // 取数据,建模型
        guard let jsonMessageDict =  jsonMessage.toAny() as? [String:Any] else {
            print("无法识别的数据结构")
            return
        }
        let eventModel = SNJSEventModel.init(parmas: jsonMessageDict)
        jsEventList.append(eventModel)
        switch eventModel.apiName {
        case SNApiName.choosePhotos.rawValue:
            toPhoto()
        default:
            print("默认执行情况")
        }
        
    }
    
    //1请求之前，决定是否要跳转:用户点击网页上的链接，需要打开新页面时，将先调用这个方法
    func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        //string    String?    "https://www.kinwork.jp:1443/?keyword=&address="    some
        if let string2 = navigationAction.request.url?.absoluteString{
            if string2.contains("content://") {
                
            }
        }
        
        
        if (navigationAction.navigationType == WKNavigationType.linkActivated){
            decisionHandler(WKNavigationActionPolicy.cancel)
        }else{
            decisionHandler(WKNavigationActionPolicy.allow)
        }
        
    }
    
    //接收到相应数据后，决定是否跳转
    func webView(_ webView: WKWebView, decidePolicyFor navigationResponse: WKNavigationResponse, decisionHandler: @escaping (WKNavigationResponsePolicy) -> Void) {
        
        if (!navigationResponse.isForMainFrame){
            decisionHandler(WKNavigationResponsePolicy.cancel)
        }else{
            decisionHandler(WKNavigationResponsePolicy.allow)
        }
        
    }
    
    func toPhoto()  {
        print("currentThread=\(Thread.current)\n")
        let picker = UIImagePickerController()
        picker.sourceType = .photoLibrary
        picker.allowsEditing = false
        picker.delegate = self
        picker.modalPresentationStyle = .fullScreen
        self.present(picker, animated: true, completion: nil)
    }
    func postPhotoResultToJs(jsModel:SNJSEventModel,image:UIImage)  {
        let imageName = UUID.init().uuidString + ".png"
        
        //回调js
        var jsonMessage = [String:Any]()
        jsonMessage["msgType"] = "callback"
        jsonMessage["callbackId"] = jsModel.callbackId
        
        var params = [String:Any]()
        params["errCode"] = SNJSCallErrCode.success.rawValue
        var result = [[String:Any]]()
        if let imageData = image.pngData() as NSData?{
            let path = NSHomeDirectory().appending("/Documents/").appending(imageName)
            imageData.write(toFile: path, atomically: true)
            let urlString = imageUrlPre + "/Documents/" + imageName
            result.append(["nativeResourceUrl":urlString])
        }
        params["result"] = result
        jsonMessage["params"] = params
        
        
        guard  let  jsonData =  String.toJsonData(data: jsonMessage) else{
            print("错误")
            return
        }
        if let jsonString = String.toString(data: jsonMessage) {
            print("jsonString:\(jsonString)")
        }
        let base64Encoded = jsonData.base64EncodedString(options: Data.Base64EncodingOptions(rawValue: 0))
        let shaKey = (base64Encoded + uuid).sha1()
        
        let toBridgeRet : [String:String] = ["jsonMessage":base64Encoded,"shaKey":shaKey]
        
        if var jsonToBridgeRet = String.toString(data: toBridgeRet){
            print("jsonToBridgeRet:\(jsonToBridgeRet)")
            //jsonToBridgeRet = "123"
            //var jsString : String = "{\"param1\":\"param1\",\"param2\":\"param2\"}"
            //            var  jsString = "1369162526219788289"
            //            jsString = "appResetsAddress('\(jsString)')"
            let jsString =  "sinoJSBridge._handleMessageFromNative('\(jsonToBridgeRet)')"
            self.webView.evaluateJavaScript(jsString) { (response, error) in
                if error == nil{
                    SNPrint("调用成功")
                }else{
                    SNPrint("error=\(error!)")
                }
                
            }
            
        }
        /*
         sinoJSBridge._handleMessageFromNative('{"jsonMessage":"eyJtc2dUeXBlIjoiY2FsbGJhY2siLCJjYWxsYmFja0lkIjoiMTAxNSIsInBhcmFtcyI6eyJlcnJDb2RlIjowLCJyZXN1bHQiOlt7Im5hdGl2ZVJlc291cmNlVXJsIjoiY29udGVudDpcL1wvY29tLmdvb2dsZS5hbmRyb2lkLmFwcHMucGhvdG9zLmNvbnRlbnRwcm92aWRlclwvLTFcLzFcL2NvbnRlbnQlM0ElMkYlMkZtZWRpYSUyRmV4dGVybmFsJTJGaW1hZ2VzJTJGbWVkaWElMkYyM1wvT1JJR0lOQUxcL05PTkVcLzE5MDEwMDQ5NDQifV19fQ==","shaKey":"3cd0058f55fa1f75b334424b55e3d1f046a00247"}')
         */
    }
    
    
}

extension SNPublicWebVC:UIImagePickerControllerDelegate,UINavigationControllerDelegate {
  
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        
    }
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        picker.dismiss(animated: true, completion: nil)
        var image : UIImage?
        if picker.allowsEditing  {
            image = info[.editedImage] as? UIImage
        }else{
            image = info[.originalImage] as? UIImage
        }
        
        var jsModel : SNJSEventModel?
        for (_,item) in jsEventList.enumerated() where item.apiName == SNApiName.choosePhotos.rawValue {
            jsModel = item
            break
        }
        jsEventList = jsEventList.filter{$0.apiName != SNApiName.choosePhotos.rawValue}
        if let jsModel = jsModel,let image = image{
            postPhotoResultToJs(jsModel: jsModel, image: image)
        }else{
            print("发生错误")
        }
    }
    
    
//    override func navigationShouldPopOnBackButton() -> Bool {
//        if self.webView.canGoBack == true {
//            self.webView.goBack()
//            return false
//        }
//        return true
//    }
    func clickBack()  {
        if self.webView.canGoBack {
            self.webView.goBack()
        }else{
            self.navigationController?.popViewController(animated: true)
        }
    }
    func convertToJsonData(data:Any) -> String {
        if (data is NSDictionary) == false &&  (data is NSArray) == false{
            return "{\"error\":\"json格式错误\"}"
        }
        //var dict = ["name":"wujilei ","age":"29"]
        var jsonData = Data()
        do{
            try  jsonData = JSONSerialization.data(withJSONObject: data, options: JSONSerialization.WritingOptions.prettyPrinted)
        }catch{
            print(error)
            return "{\"error\":\"json格式错误\"}"
        }
        var jsonString =  String(data: jsonData, encoding: String.Encoding.utf8) ?? "{\"error\":\"json格式错误\"}"
        jsonString = jsonString.replacingOccurrences(of: " ", with: "", options: String.CompareOptions.literal, range: jsonString.startIndex..<jsonString.endIndex)
        jsonString = jsonString.replacingOccurrences(of: "\n", with: "", options: String.CompareOptions.literal, range: jsonString.startIndex..<jsonString.endIndex)
        return jsonString
    }
//    //MARK: - 1 设置地址
//    func selectAddress()  {
//        let myAddressVC = self.pushPerformActionWithPath(path: "sinocare://me/myAddress", url: "") as? SNMyAddresssVC
//        if myAddressVC != nil {
//
//            myAddressVC!.selAdressHandler = {
//                [weak self] objc in
//                if let myAddressModel = objc as? SNMyAddressModel{
//                    self?.appResetsAddress(myAddressModel: myAddressModel)
//
//                }
//            }
//        }
//    }
//    //MARK: - 2 app设置地址成功 回调js
//    func appResetsAddress(myAddressModel:SNMyAddressModel)  {
//
//        //appResetsAddress
//        //var jsString : String = "{\"param1\":\"param1\",\"param2\":\"param2\"}"
//        var  jsString = "1369162526219788289"
//        jsString = "appResetsAddress('\(jsString)')"
//        self.webView.evaluateJavaScript(jsString) { (response, error) in
//            if error == nil{
//                SNPrint("调用成功")
//            }else{
//                SNPrint("error=\(error!)")
//            }
//
//
//        }
//    }
//
    /*
     let alertVC = UIAlertController.init(title: "提示", message: value, preferredStyle: UIAlertController.Style.alert)
     let canclelBtn = UIAlertAction.init(title: "ok", style: UIAlertAction.Style.cancel, handler: nil)
     alertVC.addAction(canclelBtn)
     self.present(alertVC, animated: true, completion: nil)
     */
    
}

//
//  SNScriptMessageHandler.swift
//  SinoHealthy
//
//  Created by leii on 2021/3/9.
//

import UIKit
import WebKit
class SNScriptMessageHandler: NSObject,WKScriptMessageHandler {
   weak  var scriptDelegate : WKScriptMessageHandler!
//    public convenience init(messageHander:WKScriptMessageHandler){
//
//        super.init()
//
//        self.scriptDelegate = messageHander
//
//    }
    init(messageHander: WKScriptMessageHandler) {
        super.init()
        self.scriptDelegate = messageHander
    }
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if self.scriptDelegate != nil &&  self.scriptDelegate.responds(to: #selector(userContentController(_:didReceive:))){
            
            self.scriptDelegate.userContentController(userContentController, didReceive: message)
            
        }
        
    }
}

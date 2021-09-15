//
//  SNJSEventModel.swift
//  jsDemo
//
//  Created by apple on 2021/8/18.
//

import UIKit

struct SNJSEventModel {
    var  apiName : String
    var  params : [String:Any]
    var  msgType : String
    var  callbackId : String
    init(parmas:[String:Any]) {
        if let apiNameTemp = parmas["apiName"] as? String{
            self.apiName = apiNameTemp
        }else{
            self.apiName = ""
        }
        if let paramsTemp = parmas["params"] as? [String:Any]{
            self.params = paramsTemp
        }else{
            self.params = [String:Any]()
        }
        if let msgTypeTemp = parmas["msgType"] as? String{
            self.msgType = msgTypeTemp
        }else{
            self.msgType = ""
        }
        if let callbackIdTemp = parmas["callbackId"] as? String{
            self.callbackId = callbackIdTemp
        }else{
            self.callbackId = ""
        }
    }
}

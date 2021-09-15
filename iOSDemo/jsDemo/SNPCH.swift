//
//  SNPCH.swift
//  SinoHealthy
//
//  Created by leii on 2020/12/1.
//

import UIKit




public  let KScreenW =  min(UIScreen.main.bounds.size.width, UIScreen.main.bounds.size.height)
public  let KScreenH = max(UIScreen.main.bounds.size.width, UIScreen.main.bounds.size.height)
public  let KScreenH_safe = iPhoneX == true  ? KScreenH - 34 : KScreenH


let iPhoneX : Bool = getStatusBarHight() > 20  ? true : false  //x  xsM  xr  的尺寸
let kStatusBarH : CGFloat  = getStatusBarHight()
let kTopH : CGFloat = 88
///后期可能会变,轻易不用
let  kBottomH : CGFloat = (kStatusBarH > 20 ? 34 : 0 )

let ApplicationDelegate : AppDelegate = UIApplication.shared.delegate as! AppDelegate

let LoginSessionPath  = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).last! + "/LoginSession.account"//
let MenuModelPath  = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).last! + "/MenuModel.account"//

//全局闭包
typealias SNIndexHandler = ((_ index:Int) -> Void)
typealias SNDoubleHandler = ((_ index:Double) -> Void)
typealias SNBoolHandler = ((_ isTrue:Bool) -> Void)
typealias SNCGFloatHandler = ((_ index:CGFloat) -> Void)
typealias SNStringHandler = ((_ string:String) -> Void)
typealias SNObjectHandler = ((_ object:Any) -> Void)
typealias SNOptionObjectHandler = ((_ object:Any?) -> Void)
typealias SNDefaultHandler = (() -> Void)
typealias SNIndexObjectHandler = ((_ index:Int,_ object:Any) -> Void)
typealias SNOptionIndexObjectHandler = ((_ index:Int,_ object:Any?) -> Void)

enum SNApiName : String {
    case choosePhotos = "choosePhotos"
}
enum SNJSCallErrCode : Int {
    case success = 0//正常
    case cancel = 1//取消操作
    case invalidParameter = 400//无效的请求参数
    case noPermission = 403//没有该方法的调用权限
    case noFind = 404//请求的方法或者事件名没有找到
}
public let imageUrlPre = "content://sinocare.temp.apps.photos.contentprovider"

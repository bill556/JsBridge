//
//  SNFunction.swift
//  SinoHealthy
//
//  Created by leii on 2020/12/1.
//
import Foundation
import UIKit

import CommonCrypto
extension String {
    
    ///MD5 加密
    func md5() -> String {
        let cStr = self.cString(using: String.Encoding.utf8);
        let buffer = UnsafeMutablePointer<UInt8>.allocate(capacity: 16)
        CC_MD5(cStr!,(CC_LONG)(strlen(cStr!)), buffer)
        let md5String = NSMutableString();
        for i in 0 ..< 16{
            md5String.appendFormat("%02x", buffer[i])
        }
        free(buffer)
        return md5String as String
    }
    
    
    ///sha1 加密
    func sha1() -> String {
        //UnsafeRawPointer
        let data = self.data(using: String.Encoding.utf8)!
        var digest = [UInt8](repeating: 0, count:Int(CC_SHA1_DIGEST_LENGTH))
        let newData = NSData.init(data: data)
        CC_SHA1(newData.bytes, CC_LONG(data.count), &digest)
        let output = NSMutableString(capacity: Int(CC_SHA1_DIGEST_LENGTH))
        for byte in digest {
            output.appendFormat("%02x", byte)
        }
        return output as String
    }
    // string -> 数组或者字典
    func toAny() -> Any? {
        guard  let data = self.data(using: .utf8) else{
            return nil
        }
        do {
            let objc =  try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments)
            return objc
        } catch  {
            print(error)
           
            return nil
        }
    }
    static func toString(data:Any) -> String? {
        if let jsonData = String.toJsonData(data: data),var jsonString =  String(data: jsonData, encoding: String.Encoding.utf8){
            jsonString = jsonString.replacingOccurrences(of: " ", with: "", options: String.CompareOptions.literal, range: jsonString.startIndex..<jsonString.endIndex)
            jsonString = jsonString.replacingOccurrences(of: "\n", with: "", options: String.CompareOptions.literal, range: jsonString.startIndex..<jsonString.endIndex)
            return jsonString
        }else{
            return nil
        }
    }
    static func toJsonData(data:Any) -> Data? {
        var jsonData = Data()
        do{
            try  jsonData = JSONSerialization.data(withJSONObject: data, options: JSONSerialization.WritingOptions.prettyPrinted)
            return jsonData
        }catch{
            print(error)
            return nil
        }
    }
}


func FITSCALEX(_ args : CGFloat) -> CGFloat {
    return (KScreenW/375.0 * args * 1000)/1000.0
}
func FITSCALEY(_ args : CGFloat) -> CGFloat {
    return (KScreenH/812 * args * 1000)/1000.0
}
//比例缩放,同时限制最大值
func FITSCALEXLimitMax(_ args : CGFloat) -> CGFloat{
    let result = FITSCALEX(args)
    return result > args ? args : result
}
func getStatusBarHight() -> CGFloat {
    var statusBarH : CGFloat = 44
    if #available(iOS 13.0, *) {
        let statusBarManager = UIApplication.shared.windows.first?.windowScene?.statusBarManager
        
        
        statusBarH = statusBarManager?.statusBarFrame.height ?? 44
    }else{
        statusBarH = UIApplication.shared.statusBarFrame.height
    }
    SNPrint("状态栏高度\(statusBarH)")
    return statusBarH
    
}

// 服务器时间字符串 转 想要的格式的显示时间
func getServiceTime(date: String, format: String) -> String {
    let date = getDateWithTimeString(date)
    return getTimeStringWithDate(date: date, format: format)
}

//date -> String
func getTimeStringWithDate(date:Date) -> String{
    return getTimeStringWithDate(date: date, format: "yyyy-MM-dd HH:mm:ss")
    
}
func getTimeStringWithDate(date:Date, format:String) -> String{
    let formatter = DateFormatter()
    formatter.dateFormat = format
    formatter.locale = Locale(identifier: "zh_GB")
    
    //formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
    guard let dateString =  formatter.string(for: date ) else {
        return "2020-12-20 20:20:20"
    }
    return dateString
    
}
//String  ->  date
func getDateWithTimeString(_ timeString:String) -> Date {
    return getDateWithTimeString(timeString, format: "yyyy-MM-dd HH:mm:ss")
    
}
func getDateWithTimeString(_ timeString:String, format:String) -> Date {
    let formatter = DateFormatter()
    //format.locale = Locale.current
    formatter.dateFormat = format
    formatter.locale = Locale(identifier: "zh_GB")
    guard let date = formatter.date(from: timeString) else {
        return Date()
    }
    return date
    
}

func getImageWithColor(color:UIColor)->UIImage{
    let rect = CGRect(x:0,y:0,width:1,height:1)
    UIGraphicsBeginImageContext(rect.size)
    let context = UIGraphicsGetCurrentContext()
    context?.setFillColor(color.cgColor)
    context!.fill(rect)
    let image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return image!
}
func getImageWithColor(color:UIColor,size:CGSize)->UIImage{
    let rect = CGRect(x:0,y:0,width:size.width,height:size.height)
    UIGraphicsBeginImageContext(rect.size)
    let context = UIGraphicsGetCurrentContext()
    context?.setFillColor(color.cgColor)
    context!.fill(rect)
    let image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return image!
}

func userBMI(height: Double, weight: Double) -> String {
    var bmiTemp = ""
    if height > 0 &&  weight > 0  {
        bmiTemp  = String(format: "%.2f", weight/(height*height) * 10000)
    }
    return bmiTemp
}




///全局函数
func SNPrint<T>(_ message:T,file:String = #file,funcName:String = #function,lineNum:Int = #line){
    
    #if DEBUG
    
    let file = (file as NSString).lastPathComponent;
    //let funcName =  #function;
    
    print("打印数据*******\(file):(\(lineNum)):\n\(message)");
    
    #endif
    
    
}
/*
 00:00:00-04:29:59 86400     凌晨 timeCode 1
 04:30:00-08:59:59 16200 空腹 timeCode 2
 09:00:00-10:59:59 32400 早餐后 timeCode 3
 11:00:00-12:59:59 39600 午餐前 timeCode 4
 13:00:00-15:59:59 46800 午餐后 timeCode 5
 16:00:00-18:59:59 57600 晚餐前timeCodeName timeCode 6
 19:00:00-21:29:59 68400 晚餐后 timeCode 7
 21:30:00-23:59:59 82800 睡前 timeCode 8
 --:--:-----:--:-- 随机 timeCode 9
 */
//根据时间戳获取时间段
//根据时间的没随机
func getTimeslotWithDate(date:Date) -> (timeCodeName:String,timeCode:Int) {
    
    let timeStamp : NSInteger = NSInteger(date.timeIntervalSince1970)
    //1970-01-01 08:00:00的时间戳是0
    let dayStamp = (timeStamp + 3600*8)%86400 
    //1614146797 - 1614096000 = 50797
    if dayStamp < 16200 {
        return ("凌晨",1)
    }
    if dayStamp < 32400 {
        return ("空腹",2)
    }
    if dayStamp < 39600 {
        return ("早餐后",3)
    }
    if dayStamp < 46800 {
        return ("午餐前",4)
    }
    if dayStamp < 57600 {
        return ("午餐后",5)
    }
    if dayStamp < 68400 {
        return ("晚餐前",6)
    }
    if dayStamp < 82800 {
        return ("晚餐后",7)
    }
    if dayStamp < 86400 {
        return ("睡前",8)
    }
    return ("睡前",8)
}
//根据时间strin个获取当前天的时间戳 截止到分
func getDayStampWithDateString(dateString:String) -> Int {
    let date = getDateWithTimeString(dateString)
    let timeStamp : NSInteger = NSInteger(date.timeIntervalSince1970)
    //1970-01-01 08:00:00的时间戳是0
    let dayStamp = ((timeStamp + 3600*8)/60)%1440
    //1614146797 - 1614096000 = 50797
    return dayStamp
}





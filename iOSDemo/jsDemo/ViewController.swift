//
//  ViewController.swift
//  jsDemo
//
//  Created by apple on 2021/8/17.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


    @IBAction func didClickBtnAction(_ sender: Any) {
        
        
        let vc  = SNPublicWebVC()
        //vc.urlString = MeHealthAssessmentUrl
        vc.isHiddleNav = true
        vc.hidesBottomBarWhenPushed = true
        self.navigationController?.pushViewController(vc, animated: true)
        
        
        
        
        
    }
}


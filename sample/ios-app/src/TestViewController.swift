/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import MultiPlatformLibrary

class TestViewController: UIViewController {
    
    @IBOutlet private var firstValueTextField: UITextField!
    @IBOutlet private var secondValueTextField: UITextField!
    @IBOutlet private var resultLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction private func run() {
        let result = Calculator().run(a: firstValueTextField.text ?? "", b: secondValueTextField.text ?? "")
        if let value = (result as? JsType.Str)?.value {
            resultLabel.text = value
        }
        
        if let value = (result as? JsType.DoubleNum)?.value {
            resultLabel.text = "\(value)"
        }
    }
}

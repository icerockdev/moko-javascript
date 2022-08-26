![moko-javascript](https://user-images.githubusercontent.com/5010169/128704305-df9c8e9e-200c-4d34-801e-a5b01c80f0cb.png)  
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Download](https://img.shields.io/maven-central/v/dev.icerock.moko/javascript) ](https://repo1.maven.org/maven2/dev/icerock/moko/javascript) ![kotlin-version](https://kotlin-version.aws.icerock.dev/kotlin-version?group=dev.icerock.moko&name=javascript)

# Mobile Kotlin javascript
This is a Kotlin MultiPlatform library that allows you to run JavaScript code from common Kotlin code

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Samples](#samples)
- [Set Up Locally](#set-up-locally)
- [Contributing](#contributing)
- [License](#license)

## Features
- Evaluate JavaScript code from Kotlin common code
- Pass objects to JavaScript as global vars

## Requirements
- Gradle version 6.0+
- Android API 16+
- iOS version 9.0+

## Installation
root build.gradle  
```groovy
allprojects {
    repositories {
        mavenCentral()
    }
}
```

project build.gradle
```groovy
dependencies {
    commonMainApi("dev.icerock.moko:javascript:0.3.0")
}
```

## Usage
```kotlin
val javaScriptEngine = JavaScriptEngine()
val result: JsType = try {
  javaScriptEngine.evaluate(
    context = emptyMap(),
    script = """ "Hello" + "World" """.trimIndent()
  )
} catch (ex: JavaScriptEvaluationException) {
  // Handle script evaluation error
  JsType.Null
}
if (result is JsType.Str) {
    println(result.value)
}
```

## Samples
More examples can be found in the [sample directory](sample).

## Set Up Locally 
- In [javascript directory](javascript) contains `javascript` library;
- In [sample directory](sample) contains samples on android, ios & mpp-library connected to apps.

## Contributing
All development (both new features and bug fixes) is performed in `develop` branch. This way `master` sources always contain sources of the most recently released version. Please send PRs with bug fixes to `develop` branch. Fixes to documentation in markdown files are an exception to this rule. They are updated directly in `master`.

The `develop` branch is pushed to `master` during release.

More detailed guide for contributers see in [contributing guide](CONTRIBUTING.md).

## License
        
    Copyright 2021 IceRock MAG Inc
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

# cash-sdk-android
SDK to convert Crypto to Cash direct at thousands of ATMs

[![CircleCI](https://circleci.com/gh/atmcoin/cash-sdk-android.svg?style=svg)](https://circleci.com/gh/atmcoin/cash-sdk-android)
[![](https://jitpack.io/v/atmcoin/cash-sdk-android.svg)](https://jitpack.io/#atmcoin/cash-sdk-android)
![LINE](https://img.shields.io/badge/line--coverage-57%25-orange.svg)
![BRANCH](https://img.shields.io/badge/branch--coverage-52%25-orange.svg)
![COMPLEXITY](https://img.shields.io/badge/complexity-3.34-brightgreen.svg)

Are you looking for the ios version of the CashSDK? 

https://github.com/atmcoin/cash-sdk-ios/

To setup the gradle dependency add to the master pom file the jitpack maven server.

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Then in the module that you want to use that dependency add the following implementation, change Tag to one of the releases for example
`com.github.atmcoin:cash-sdk-android:1.3`

```
dependencies {
  implementation 'com.github.atmcoin:cash-sdk-android:Tag'
}
```

In the module `app` you can find a demo app that shows how to use the CashSDK.


<p align="center">
  <img src="demoapp.gif" alt="demoApp" width="300px"/>
</p>


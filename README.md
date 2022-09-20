# Verifier Company



## Framework7 CLI Options



## Documentation Basis
```
https://prod-guardian-cv.oss-ap-southeast-5.aliyuncs.com/docs/liveness-detection-android/en/document-cordova.html
```

Cordova plugin installed

```
  ai.advance.liveness 1.0.0 "LivenessDetectionPlugin"
  cordova-plugin-file 7.0.0 "File"
  cordova-plugin-keyboard 1.2.0 "Keyboard"
  cordova-plugin-statusbar 3.0.0 "StatusBar"
```

## Install Dependencies and Prerequisite

If you dont have installed framework7 please install it first, run in terminal
```
npm i framework7-cli cordova -g
```

First of all we need to install dependencies, run in terminal
```
npm install
```


## build.gradle added script
Please see on cordova/platform/android/app/build.gradle for correction integration on gradle
```
                  repositories {
                      maven {
                          url 'http://public-n3.advai.net/repository/maven-releases/'
                          // If your gradle version is greater than 7.0, you need to add the following configuration to allow pulling aar via http
                          allowInsecureProtocol = true
                      }
                  }
                  dependencies {
                      implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar']) 
                      implementation "androidx.appcompat:appcompat:${cordovaConfig.ANDROIDX_APP_COMPAT_VERSION}"

                      if (cordovaConfig.IS_GRADLE_PLUGIN_KOTLIN_ENABLED) {
                          implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${cordovaConfig.KOTLIN_VERSION}"
                      }

                      // SUB-PROJECT DEPENDENCIES START
                      implementation(project(path: ":CordovaLib"))
                      implementation "androidx.webkit:webkit:1.4.0"
                      // SUB-PROJECT DEPENDENCIES END

                      compile "com.android.support:support-annotations:27.+"
                      // Add the following line of dependencies
                      // Version history see:
                      // http://public-n3.advai.net/repository/maven-releases/ai/advance/mobile-sdk/android/liveness-detection-cordova/maven-metadata.xml
                      implementation 'ai.advance.mobile-sdk.android:liveness-detection-cordova:1.3.9.1'
                  }
```

## NPM Scripts

* 🔥 `npm start` - run development server
* 📱 `npm run cordova-android` - run dev cordova Android app on android phone


## SOURCE CODE PROJECT

Please check the source code on `scr/page/home.f7` and find function `take_selfie`
Note : I add some comment on home.f7

 

## Vite

There is a [Vite](https://vitejs.dev) bundler setup. It compiles and bundles all "front-end" resources. You should work only with files located in `/src` folder. Vite config located in `vite.config.js`.
## Cordova

Cordova project located in `cordova` folder. You shouldn't modify content of `cordova/www` folder. Its content will be correctly generated when you call `npm run cordova-build-prod`.





## Assets

Assets (icons, splash screens) source images located in `assets-src` folder. To generate your own icons and splash screen images, you will need to replace all assets in this directory with your own images (pay attention to image size and format), and run the following command in the project directory:

```
framework7 assets
```

Or launch UI where you will be able to change icons and splash screens:

```
framework7 assets --ui
```



## Documentation & Resources

* [Framework7 Core Documentation](https://framework7.io/docs/)



* [Framework7 Icons Reference](https://framework7.io/icons/)
* [Community Forum](https://forum.framework7.io)

## Support Framework7

Love Framework7? Support project by donating or pledging on:
- Patreon: https://patreon.com/framework7
- OpenCollective: https://opencollective.com/framework7
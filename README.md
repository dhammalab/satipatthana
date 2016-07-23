Satipatthana
============
[![Get Satipatthana on Google Play Store](https://developer.android.com/images/brand/en_generic_rgb_wo_60.png)](https://play.google.com/store/apps/details?id=com.dhammalab.satipatthna)

[![Build status](https://travis-ci.org/dhammalab/satipatthana.svg?branch=master)](https://travis-ci.org/dhammalab/satipatthana)

Satipatthana is an Android app for doing different noting practices.
It is mainly based on Shinzen Young's framework called "5 ways to know yourself".
At the end of each session you can check some nice statistics and graphics.
Additionally you can publish your sessions anonymously to the [DhammaLab server](http://dhammalab.com/)
where it will be made available under a creative commons (CC) license.

Development
-----------
The best way to start development on Satipatthana is to use [SBT](http://www.scala-sbt.org/) and the [protify](https://github.com/pfn/protify) plugin.
To do so,

1. [download and install SBT](http://www.scala-sbt.org/download.html)
2. `git clone https://github.com/dhammalab/satipatthana.git` (checkout the code)
3. `cd satipatthana`
4. Start an emulator of your choice or connect your Android device via USB and enable USB debugging
5. `sbt` (start a sbt console in which you enter the following commands)
    1. `app/android:install` (install the app to the emulator/device)
    2. `app/protify:install` (install protify on the emulator/device)
    3. `~app/protify:run` (watch for file changes and compile and install the app on the emulator/device on each change)


Credits
-------
Satipatthana is developed by [DhammaLab](http://dhammalab.com/)

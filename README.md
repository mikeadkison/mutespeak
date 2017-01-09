# mutespeak
A way for people who can't talk to have custom voice binds for mumble, teamspeak, discord, skype, etc

Works on Windows only (most people play games on Windows unfortunately). Generates and runs a vbs to say the saying. Cross-platform hotkeys and text to speech will take much more work.

To route audio to the voice chat program, set Windows Stereo Mix to the default recording device as seen [here](http://www.howtogeek.com/howto/39532/how-to-enable-stereo-mix-in-windows-7-to-record-audio/). Select Stereo Mix as your microphone in the voice chat program. There are probably ways to use [jack](http://www.jackaudio.org/) or [CheVolume](http://www.chevolume.com/) or [audiorouter](https://github.com/audiorouterdev/audio-router) in conjunction with [virtual audio cable](http://software.muzychenko.net/eng/vac.htm) to route audio better.

#### Compiling
javac -cp jintellitype-1.3.9.jar mutespeak/*.java

#### Running
java -cp .;jintellitype-1.3.9.jar mutespeak.TTS
Run it with 64 bit java. If you want to use 32 bit java, will have to recompile with the 32 bit dll.

#### Jarring
* jar -cvfm TTS.jar MANIFEST.MF mutespeak/*.class
* zip it up with JIntellitype64.dll and jintellitype-1.3.9.jar

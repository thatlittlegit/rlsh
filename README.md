# rlsh
#### Redone Light SHell
`rlsh` is a shell meant for me on Windows and maybe on Linux. It is supposed to be very compatible with Bash, and also be extensible.
## Building
Use the Gradle wrapper.

```shell
# To build rlsh (no meaningful output)
./gradlew assemble
# To build a ZIP file for rlsh
./gradlew distZip
# To build and run
./gradlew run --console=plain
# To build a JAR file (you'll have to hook up the classpath!)
./gradlew jar
```

## Roadmap
* Add piping (to `stdin` and files) 

## Framework
In the future, there'll be two main components:

* `rlsh` main components (the command prompt) 
* The `rlsh` framework 

The framework is a general shell protocol; it makes an easy way to build a shell
in Java. Right now, it's intertwined too much with rlsh, but eventually it'll be
split into it's own part. It'll be like this stack:

```
+------+
| rlsh |
+------+
| rlsh |
|      |
|frame-|
| -work|
+------+
```

## Documentation

Right now, JavaDocs are not done.(cough, cough - contribute!) Right now, most other
docs are not done either. If you wanna help - go for it!

# rlsh
#### Redone Light SHell
`rlsh` is a shell meant for me on Windows and maybe on Linux. It is supposed to be very compatible with Bash, and also be extensible.
## Building
Use `make`.

```shell
# To just build rlsh
make
# To build and run
make run
# To run but not build
make COMPILE=no run
```

If you must, use gradle:
```shell
./gradlew assemble
```

## Roadmap
* Use Gradle for everything and just have Make a polyfill 
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

# rlsh
#### Redone Light SHell
`rlsh` is a shell meant for me on Windows and maybe on Linux. It is supposed to be very compatible with Bash, and also be extensible.
## Building
`rlsh` will use a version of Kaffe at some point as the JVM to make a exe or ELF file.
To build, run:
```shell
make clean all
```
This ensures forward compatability.

**If you don't have access to `make` (like sometimes me), SSH into a *nix box!**
## Framework
`rlsh` will eventually have two components:
* The *framework*, which can be used for non-`rlsh` shells 
* The *rlsh components*, which, although can be adapted, are specific to `rlsh` 
The framework will give you a basic shell, and let you build on it.
##### TODO

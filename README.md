# rlsh
#### Redone Light SHell
`rlsh` is a shell meant for me on Windows and maybe on Linux. It is supposed to be very compatible with Bash, and also be extensible.
## Building
`rlsh` uses the discontinued GCJ because there doesn't seem to be an alternative. Use the following to install GCJ into the directory:
```shell
make gcj
```
Then, when that's done, run:
```shell
make clean all
```
**If you don't have access to `make` (like sometimes me), SSH into a *nix box!**

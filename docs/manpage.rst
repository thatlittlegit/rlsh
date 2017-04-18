:orphan:

rlsh manual page
================


Synopsis
------
rlsh

Description
------
rlsh is a shell meant for ease of use and development. rlsh is written in Java
to provide easier development, and has classic features from :manpage:`bash(1)`
and :manpage:`sh(1)` compatable shells to make usage easier. Plugins will be
able to be written in JavaScript for ease and features.

Currently, rlsh is in beta-alpha-testing-dev stage, which means that it's very
broken. Luckily, it is being built on a strong development model for object-orientedness

Note
------
This manpage will describe basic syntax of rlsh; if you want a manual, see the
:manpage:`info(1)` page.

Arguments
------
Currently, rlsh supports no arguments.

Shell Usage
------

Calling Commands
    To call a command, just type the name of the command into the prompt.

    tar
Arguments
    To call arguments, just append them to the end of the line:

    tar -xf archive.tar
Variables
    *note: currently unimplemented.* Use them in your lines, like you would in a
    :manpage:`make(1)` file. You can use the following syntax to define the
    variable, and then the next to run it:

    VARNAME=VALUE
    tar -xf ${VARNAME}
Built-ins
    Built-ins are commands that are completely built into the shell. They are
    guaranteed to be in rlsh, and if they are not there then that's a problem
    with your installation or a plugin.

    cd
         Changes the current directory, so whenever you execute a command any paths
         you pass will be relative to the current directory.

         cd <directory>
    pwd
         Informs you of what directory you are currently in, just in case your pre-shell
         variable (PS1) does not contain your working directory.

         pwd
    alias
         Makes it so when you pass one command it creates another. For example, aliasing
         'll' to 'ls -lag' would make it so whenever ll is typed you will get the output
         of 'ls -lag'.

         alias command=command1
    exit
         Since you asked so nicely, exits the shell.

         exit
    help
         Shows some basic commands and copyright information. When given a command, will
         run <command> --help.

         help [command]

Low Built-ins
    Low built-ins are meant to be backups for users of Windows systems so they can use
    classic UNIX commands on a Windows box. **If you have these commands, then it will
    default to using yours unless you use a plugin (see the** :manpage:`info(1)` **page.)**

    For more information, it may be worth seeing one of the :manpage:`info(1)` page for
    GNU coreutils or the appropiate manpage for the command.

    :manpage:`cat(1)`
        Reads all the arguments (as files) to :manpage:`stdout(3)`. If the file does not
        exist, an error is printed to :manpage:`stderr(3)`.

        cat <file> [files...]

    :manpage:`cp(1)`
        Copies a file to another. If there are three or more arguments then it will copy
        the files to the last argument, assumed a folder.

        cp <file> <file>

        cp <files...> <folder>
    :manpage:`ls(1)`
        Lists files in the current directory (or, if an argument is provided, that directory).

        ls

        ls <directory>

    :manpage:`mkdir(1)`
        Makes the directories in the arguments. If *-p* is passed, then all parent directories
        will be made as well.

        mkdir [-p] <directory> <directory...>

    :manpage:`mv(1)`
        Moves a file to another. Currently no infitite-argument functionality.

        mv <file> <file>

        mv <file> <folder>

    :manpage:`rm(1)`
        Deletes a file.

        rm <file>

    :manpage:`rmdir(1)`
        Same as :manpage:`rm(1)`, but for directories.

        rmdir <folder> [folders...]

    :manpage:`touch(1)`
        Creates a file with no contents.

        touch <file> [files...]

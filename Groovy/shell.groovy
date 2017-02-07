def sout = new StringBuilder(), serr = new StringBuilder()
def proc = 'ls /badDir'.execute()
proc.consumeProcessOutput(sout, serr)
proc.waitForOrKill(1000)
println "out> $sout err> $serr"


def runCmd = {cmd ->
        def process = cmd.execute()
        process.in.eachLine {line->
            println line
        }
        if (proc.exitValue()) {
            println "gave the following error: "
            println "[ERROR] ${process.getErrorStream()}"
        }

}
runCmd("ls /badDir")


# http://stackoverflow.com/questions/159148/groovy-executing-shell-commands

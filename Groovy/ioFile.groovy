import java.io.File

def file = new File("./fileTest")

def newIOAction = {cpTo ->
    println "cpTo: $cpTo"
    def cpFile = new File(cpTo)

    if (cpFile.exists()) {
        cpFile.delete()
    }
    cpFile.createNewFile();

    def is = file.newInputStream()
    def os = cpFile.newOutputStream()

    os.write(is.bytes)

    is.close()
    os.close()
}
//newIOAction("cpFile")

def withIOAction = {cpTo ->
    def cpFile = new File(cpTo)

    if (cpFile.exists()) {
        cpFile.delete()
    }
    cpFile.createNewFile();

    file.withInputStream {is ->
       cpFile << is
//        << is more easy, below is also ok
//        cpFile.withOutputStream {os ->
//            os.write(is.bytes)
//        }
    }
}
withIOAction("withIOFile")

println "done"


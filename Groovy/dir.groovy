import java.io.File
import java.util.regex.Pattern
import java.nio.file.FileVisitResult

def dir = new File("./")

dir.eachFile {file ->
    println file.name
}

println "--------recurse-------------"

dir.eachFileRecurse {file ->
    if (file.isDirectory()) {
        println file.name + " is dir"
    } else {
        println file.name + " is not dir"
    }
}

println "-------match---------------"

def p = ~/.*groovy/
dir.eachFileMatch(p) {file ->
    println file.name
}
println "t:" + (p instanceof Pattern)

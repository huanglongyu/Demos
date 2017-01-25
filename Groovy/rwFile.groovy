//import java.io.File
////def file = new File("/home/hly/work/Demos/Groovy/fileTest")
//def dir = '/home/hly/work/Demos/Groovy'
//def name = 'fileTest'
//def file = new File(dir, name)
//println file.getPath()
//file.eachLine { line->
//    println line
//}
//import java.io.File
class Example {
    static void main(String[] args) {
        def file = new File("./fileTest")

        file.eachLine {line, bb->
            println "$bb: $line"}

        def lines = file.readLines()
        lines.each {line->
            println "each line: $line"
        }

        def collects = file.collect();
        collects.each{line->
            println "collect: $line"
        }

        file << """append 1\nappend 2\nappend 3\n"""

        //cover the old content
//        file.withWriter('utf-8') { writer ->
//            writer.writeLine 'writerLine 1'
//            writer.writeLine 'writerLine 2'
//            writer.writeLine 'writerLine 3'
//        }
        file.withReader('UTF-8') {
            s = it.readLine()
            println "readLine : $s"
        }

        file.withWriterAppend('utf-8') { writer ->
            writer.writeLine 'writerLine 1'
            writer.writeLine 'writerLine 2'
            writer.writeLine 'writerLine 3'
        }
        println "done"
    }
}

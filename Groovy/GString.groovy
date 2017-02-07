def a = "a b c d e f"
def split = a.split(/[\s\n\r]+/) // same as: [a, b, c, d, e, f]
split.each {line->
    println line
}

println split.join('/')

println "========================"

def split2 = a.tokenize()
split2.each {line->
    println line
}
def b = 'i,am-john'.tokenize(',-') // same as: ['i', 'am', 'john']
def b2 = 'i,am john'.tokenize(',') // same as: ['i', 'am john']
println b
println b2

println "========================"

println 'ABCDEF'.length()       // output: 6
println 'ABCDEF'.substring(1)       // output: BCDEF
println 'ABCDEF'.indexOf('C')       // output: 2
println 'ABCDEF'.replace('CD', 'XX')    // output: ABXXEF
println 'ABCDEF'.toLowerCase()      // output: abcdef
println 'AABCDEF'.count('A')      // output: 2

println "========================"

def str = 'ABC'.padLeft(5)
println str  // same as:  str
println str.length() //output: 5
def str2 = 'ABC'.center(5, '+')
println str2  // same as: +ABC+
println str2.length() // output: 5

println "========================"

println 'ABC' - 'B'   // output: AC
println 'ABBC' - 'B'  // output: ABC
println 'ABBC' - 'BB' // output: AC
println 'ABC' * 2     // output: ABCABC
println 'ABC' * 3     // output: ABCABCABC

println "========================"

println 'grails'.find{ it > 'i' }     // output: r
println 'grails'.findAll{ it > 'i' }  // output: [r, l, s]
println 'grails'.findAll{ it >= 's' } // output: [s]

println "========================"

def c = 'abc'.every{ it > 'b' }
def c2 = 'abc'[0] > 'b' && 'abc'[1] > 'b' && 'abc'[2] > 'b'
println c  //false
println c2 //false

println "========================"

def d = 'abc'.any{ it > 'b' }
def d1 = 'abc'[0] > 'b' || 'abc'[1] > 'b' || 'abc'[2] > 'b'
println d //true
println d1 // true

println "========================"

println'abc'.collect{ it }            // ['a', 'b', 'c']
println'abc'.collect{ it + '.' }      // ['a.', 'b.', 'c.']
println'abc'.collect{ 'test-' + it  } // ['test-a', 'test-b', 'test-c']
println'abc'.collect{ it * 3 }        // ['aaa', 'bbb', 'ccc']

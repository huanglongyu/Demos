a = {parameter++}
println "a: "+ (a instanceof Closure)

//-----------
b = { -> parameter++}
println "b: " + (b instanceof Closure)

//-----------
c = {it ->
        println "c: + ${it}"
}
c(1)

//-----------
d = { ->
    123
}
println "d: " +  d()

//-----------
e = { ->
    println "e: abc"
}
e()

//-----------
f = {a, b ->
    Math.pow(a,b)
}
println "f: " +  f(2,3)

//-----------
g = {println "g: " + it}
g 123
g(123)

//-----------
h = {str, cook -> cook(str)}
println h(123, {it + 4})


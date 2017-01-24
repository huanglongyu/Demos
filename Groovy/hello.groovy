print "hello world \n"
def getSomething = {
    s->
       println "getSomeThing ${s}"
}
getSomething("hh")

class User {
    public final String name
    User(String name) { this.name = name}
    String getAname() { "Name: $name" }
}
def user = new User('Bob')

println user.name
println user.@name


num = 1
def printNum(){
    println num
}
printNum()

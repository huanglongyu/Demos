def outerClosure = {
    println this.class.name    // outputs own-delegate-this
    println owner.class.name    // outputs own-delegate-this
    println delegate.class.name  //outputs own-delegate-this
    def nestedClosure = {
        println this.class.name    // outputs own-delegate-this
        println owner.class.name    // outputs own-delegate-this$_closure1
        println delegate.class.name  // outputs own-delegate-this$_closure1
    }
    nestedClosure()
}

outerClosure()


class Dog {
    def play = {
      "wang wang!"
    }

    def childmind = {
       println  delegate.play();
    }
}

class Cat {
    def play = {"mi mi !"}
}

def dog = new Dog()
def cat = new Cat()

dog.childmind()

dog.childmind.delegate  = cat;
dog.childmind()

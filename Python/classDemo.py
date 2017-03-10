#!/usr/bin/python3.2
# -*- coding: UTF-8 -*-
class MyClass1():
    """class doc"""
    i = 10
    def func1(self):
        return "func1"

print(MyClass1.i)
print(MyClass1.func1)
print(MyClass1.__doc__)
MyClass1.i = 20
print(MyClass1.i)

print("===========")

class CustomClass():
    def __init__(self, arg1, arg2):
        print("__init__")
        self.custom1 = arg1
        self.custom2 = arg2
    def func1(self):
        print("custom func1")

x = CustomClass(10, 20);
print(str(x.custom1) + " " + str(x.custom2))

x.newVariable = 1
while (x.newVariable < 10):
    x.newVariable += 3
    print(x.newVariable)

print("===========")
f = x.func1
f()

#!/usr/bin/python3.2
# -*- coding: UTF-8 -*-
name = "lzl"
def f1():
    print(name)

f1()
def f2():
    name = "eric"
    return f1

ret = f2()
ret()

i = 0
def f3():
    i = 10
    print(i)
def f4():
    global i
    i += 1
    print(i)
f3()
f4()
f4()
f4()

#!/usr/bin/python3.2
# -*- coding: UTF-8 -*-

def printTest(n):
    for i in range(n):
        print(i)
def printTest2(a):
    for i in list(a):
        print(i)

if __name__ == "__main__":
   import sys
   print(sys.argv[0] + " " + sys.argv[1])
   for i in list(sys.path):
       print(i)


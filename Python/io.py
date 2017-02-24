#!/usr/bin/python3.2
# -*- coding: UTF-8 -*-

f=open("/home/hly/work/Demos/Python/io.txt", "r+")
line = f.read(2)
print(line)
line = f.read(2)
print(line)


line2 = f.readline()
print(line2)
line2 = f.readline()
print(line2)

lines = f.readlines()
for i in lines:
    print(i)

l = f.write("write\n")
print(l)
print(f.closed)

index = f.tell()
print(index)

f.seek(2)
index = f.tell()
print(index)
print(f.closed)
f.close()
print(f.closed)

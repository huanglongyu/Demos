#!/usr/bin/python3.2
# -*- coding: UTF-8 -*-

from collections import deque

for i in range(2):
    print(i)

print("==================")

a = ['  Mary', '  had', 'a', 'little', 'lamb']
for i in range(len(a)):
    print(a[i])

print("==================")

list1 = list(a)
while len(list1):
    print(list1.pop())

print("==================")

def forTest(n):
    for i in range(n):
        print(i)
forTest(2)

print("==================")

queue = deque(a)
print(queue.popleft())
print(queue.pop())
print(queue)

print("==================")

b = [1, 2, 3]
c = [2*x for x in b]
d = [2*x for x in b if x > 2]
print(c)
print(d)

c = [ x.strip() for x in a ]
print(c)

print("==================")

set1 = {1, 2, 1, 2, "a", "a", "c"}
print(set1)
print(1 in set1)

print("==================")

d = {1:'a', 'b':2, 'c':3}
print(d.keys())
del d[1]
print(d)
for i,j in d.items():
    print("item:" + str(i) + ":" + str(j))
for i,j in enumerate(d):
    print("item:" + str(i) + ":" + str(j))










#!/bin/bash

javac HillCipher.java
python hillencode.py --coding=alpha plain-alpha.txt $4
java HillCipher $1 $2 $3 $4 $5

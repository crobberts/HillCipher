#!/bin/bash

javac HillDecipher.java
python hillencode.py --coding=alpha crypt.txt $4
java HillDecipher $1 $2 $3 $4 $5

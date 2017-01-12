#!/bin/sh

hadoop fs -rmr input3
hadoop fs -mkdir input3 2>&1
hadoop fs -put ./input.txt input3/ 

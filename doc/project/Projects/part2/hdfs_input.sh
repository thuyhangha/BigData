#!/bin/sh

hadoop fs -rmr input2
hadoop fs -mkdir input2 2>&1
hadoop fs -put ./input.txt input2/ 

#!/bin/sh

hadoop fs -rmr input4
hadoop fs -mkdir input4 2>&1
hadoop fs -put ./input.txt input4/ 

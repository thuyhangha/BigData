#!/bin/sh

hadoop fs -rmr output3
hadoop jar Stripes.jar Stripes input3/input.txt output3

echo "DONE!"


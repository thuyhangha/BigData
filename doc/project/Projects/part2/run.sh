#!/bin/sh

hadoop fs -rmr output2
hadoop jar pairs.jar Pairs input2/input.txt output2

echo "DONE!"


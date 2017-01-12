#!/bin/sh

hadoop fs -rmr output4
hadoop jar Hybrid.jar Hybrid input4/input.txt output4

echo "DONE!"


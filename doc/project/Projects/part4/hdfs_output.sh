#!/bin/sh
rm -f output.txt
hadoop fs -get output4/part-r-00000 output.txt

#!/bin/sh

hadoop fs -rmr input6
hadoop fs -mkdir input6 2>&1
hadoop fs -put ./apache_access.log input6/ 

#!/bin/sh

#hadoop fs -rmr output6
rm -fr output.txt

STARTTIME=`date +%s.%N`

spark-submit --class "spark.project.LogProcessor" --master local[4] sparkproject.jar input6/apache_access.log >> output.txt

ENDTIME=`date +%s.%N`

TIMEDIFF=`echo "$ENDTIME - $STARTTIME" | bc | awk -F"." '{print $1"."substr($2,1,3)}'`

echo "Execution Time : $TIMEDIFF"

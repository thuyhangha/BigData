package spark.project

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._
import scala.math.Ordering

case class ApacheAccessLog(ipAddress: String, hyphen1: String,
                           hyphen2: String, dateTime: String, method: String,
                           endpoint: String, protocol: String,
                           responseCode: Int, contentSize: Long) {
}

object ApacheAccessLog {
  val PATTERN = """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] "(\S+) (\S+) (\S+)" (\d{3}) (\d+)""".r

  def parseLogLine(log: String): ApacheAccessLog = {
    val res = PATTERN.findFirstMatchIn(log)
    if (res.isEmpty) {
      throw new RuntimeException("Cannot parse log line: " + log)
    }
    val m = res.get
    ApacheAccessLog(m.group(1), m.group(2), m.group(3), m.group(4),
      m.group(5), m.group(6), m.group(7), m.group(8).toInt, m.group(9).toLong)
  }
}


// ordering Pair(key,value) by value
object OrderingUtils {
  object ValueOrdering extends Ordering[(String, Int)] {
    def compare(a: (String, Int), b: (String, Int)) = {
      a._2 compare b._2
    }
  }
}

/**
 * command to run:
 * % spark-submit --class "spark.project.LogProcessor" --master local[4] sparkproject.jar input6/accesslog
 */
object LogProcessor {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("Log Analyzer in Scala")
    val sc = new SparkContext(sparkConf)

    val logFile = args(0)

    val accessLogs = sc.textFile(logFile).map(ApacheAccessLog.parseLogLine).cache()

    println("**********************************************************")
    println("*               APACHE LOG ANALYZING                     *")
    println("**********************************************************")
    println("\n")

    // Compute Response Code to Count.
    val responseErrorCode = accessLogs
      .map(log => (log.responseCode, 1))
      .reduceByKey(_ + _)
      .filter(_._1 >= 400)
      .take(100)

    val responseOkCode = accessLogs
      .map(log => (log.responseCode, 1))
      .reduceByKey(_ + _)
      .filter(_._1 < 400)
      .take(100)
    
    println("========== Response Code Statistics =====")    
    println(s"""Error Codes: ${responseErrorCode.mkString("[", ",", "]")}""")
    println(s"""Normal Codes: ${responseOkCode.mkString("[", ",", "]")}""")
    println();

    // Client access statistics
    val ipAccess = accessLogs
      .map(log => (log.ipAddress, 1))
      .reduceByKey(_ + _)
      .filter(_._2 > 10)
      .map(_._1)
      .take(100)

    println("========== IP Access More Than 10 times ==========");
    println(s"""${ipAccess.mkString("\n")}""")
    println()

    val topIpAccess = accessLogs
      .map(log => (log.ipAddress, 1))
      .reduceByKey(_ + _)
      .top(3)(OrderingUtils.ValueOrdering)
    println("========== Top 3 IP Access ==========");
    println(s"""${topIpAccess.mkString("\n")}""")
    println()

    // Top End points or web content
    val topEndpoints = accessLogs
      .map(log => (log.endpoint, 1))
      .reduceByKey(_ + _)
      .top(10)(OrderingUtils.ValueOrdering)

    println("========== Top 10 Content Access Statistics =====")    
    println(s"""${topEndpoints.mkString("\n")}""")
    println()

    // Content size.
    val contentSizes = accessLogs.map(log => log.contentSize)
    println("========== Content Size Statistics =====")   
    println("Content Size Avg:\t%s", contentSizes.reduce(_ + _) / contentSizes.count)

    sc.stop()
  }
}

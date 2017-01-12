

package apache

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.rdd._
import scala.collection.immutable.ListMap
import java.lang.invoke.LambdaForm
import org.apache.hadoop.io.IntWritable.Comparator
import java.util.Comparator

@SerialVersionUID(100L)
class LogAnalytics (sc: SparkContext, filepath: String) extends Serializable {

  // Initialize parser
  val parser = new AccessLogParser
  
  // load text file
  val log = sc.textFile(filepath)
  
  /* Computing the frequency of each response code */
  def freq() {
    val pairs = log.flatMap(line => parser.parseRecord(line)) // get records
                .map(r => (r.httpStatusCode, 1)) // create pairs of (StatusCode, 1)
                .reduceByKey(_+_) // reduce by Status Code
                .sortByKey() // sort by Status Code
                
    // print to console
    pairs.foreach(p => println(p._1 + " : " + p._2))
  }
  
  /* All IP Addresses that have accessed this server more than N times */
  def accessMoreThan(n : Int) { 
    val pairs = log.flatMap(line => parser.parseRecord(line))
            .map(r => (r.clientIpAddress, 1)) // create pairs of (IP, 1)
            .reduceByKey(_+_) // reduce by IP
            .filter{ case (k, v) => v > n } // filter access more than n
    
    // print to console
    pairs.foreach(p => println(p._1 + " (" + p._2 + ")"))
  }
  
  /* The top endpoints requested by count */
  def topEndPoints(n : Int) {
    val pairs = log.flatMap(line => parser.parseRecord(line)) // get log records
            .map(r => AccessLogParser.parseRequestField(r.request)) // get requests
            .filter(_.isDefined) // filter to get only valid requests
            .map(req => (req.get._2, 1)) // create pairs of (url, 1)
            .reduceByKey(_+_) // reduce by url
            .filter{ case (k, v) => v > n } // filter count more than n
    
    // print to console
    pairs.foreach(p => println(p._1 + " (" + p._2 + ")"))
  }
  
  /* Generating a list of URLs, sorted by hit count */
  def listUrl(n: Int) {
    // get to a series of "(URI, COUNT)" pairs; (MapReduce like)
    val uriCount = log.map(parser.parseRecordReturningNullObjectOnFailure(_).request)
      .map(r => AccessLogParser.parseRequestField(r)) // get the uri field
      .filter(_.isDefined) // filter out records that wouldn't parse properly
      .map(uri => (uri.get._2, 1)) // create a pair for each record
      .reduceByKey((a, b) => a + b) // reduce to sum 
      .collect // convert to Array[(String, Int)], which is Array[(URI, numOccurrences)]

    val uriHitCount = ListMap(uriCount.toSeq.sortWith(_._2 > _._2): _*)

    val formatter = java.text.NumberFormat.getIntegerInstance
    uriHitCount.take(n).foreach { pair =>
      val uri = pair._1
      val count = pair._2
      println(s"${formatter.format(count)} => $uri")
    }
  }

  /* Calculate the average, max, min size of the content size */
  def calSize(){
    val accessLogs  = log.map(parser.parseRecordReturningNullObjectOnFailure(_)).cache()
    
    //create a RDD to contain only the 'contentSize' field from access_log and cache it
    val contentSizes  = accessLogs.map(line => line.bytesSent)
      .filter(size => size != "-")
      .map(size => size.toInt)
      .cache()
    
    val avdContentSize = contentSizes.reduce((x,y) => x+y)/ contentSizes.count()
    
    println("The average content size: "+avdContentSize)    
    println("Max content size: " + contentSizes.max())    
    println("Min content size: " + contentSizes.min())
  }
}
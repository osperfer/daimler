package com.daimler.position

import scala.annotation.tailrec
import scala.collection.immutable
import scala.io.{Source, StdIn}
import scala.sys.exit
import scala.util.parsing.json.JSON

object Main extends App {

  println("Type the sku code to search and an optional number of elements to retrieve(default 10) i.e: sku-1 20")
  if (args.length == 0) {
    println("Missing required arguments: json_path")
    exit
  }

  val jsonFile = args(0)
  val jsonString = Source.fromFile(jsonFile).getLines().mkString
  val jsonMap = JSON.parseFull(jsonString).get.asInstanceOf[Map[String, immutable.HashMap[String, String]]]

  val recommender = Recommender(jsonMap)
  search(recommender)


  /**
   * Reads sku code to search from stdin and
   * search the similar sku, the results will
   * be printed in the stdout console
   *
   * @param recommender the recommendation engine
   */
  @tailrec def search(recommender: Recommender): Unit = {
    val stdInput = StdIn.readLine("Type your search text and press ENTER: ")
    val searchArgs = stdInput.split(" ")
    val skuCode = searchArgs(0)

    require(skuCode.matches("sku-[0-9]+"), "sku code must match the following pattern: sku-[0-9]+")

    if (jsonMap.contains(skuCode)) {
      val results = searchArgs.length match {
        case 1 => recommender.getSimilarSku(skuCode, 10)
        case _ => recommender.getSimilarSku(skuCode, searchArgs(1).toInt)
      }

      if (results.nonEmpty) {
        results.foreach(r => println(s"${r._1}: ${r._2}"))
      } else {
        println("Not found any similar sku")
      }
    } else {
      println(s"The sku [${skuCode}] is not present in the given collection")
    }

    search(recommender)
  }

}

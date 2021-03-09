package com.daimler.position

import scala.collection.immutable

/**
 * Calculates the similar sku's contained in the given jsonData map
 * compared to the data of the given skuCode. The similarity is based
 * on the comparison of the attributes that are present in the given
 * skuCode and a priority assigned to each attribute.
 *
 * @param jsonData the full collection of sku's
 */
case class Recommender(jsonData: Map[String, immutable.HashMap[String, String]]) {
  //TODO process can be enhanced by configuring some attributes as mandatory match in order to be considered as a
  // valid similar sku. Also the distance or similarity between attribute values can be considered in case the match
  // is not 100%
  /*
  This map defines the priority of the sku attributes. The lower the most priority
   */
  val attrPriority: Map[String, Int] = Map[String, Int](("att-a", 0), ("att-b", 1), ("att-c", 2),
    ("att-d", 3), ("att-e", 4), ("att-f", 5),
    ("att-g", 6), ("att-h", 7), ("att-i", 8),
    ("att-j", 9))

  /**
   * Performs the comparison of the data
   *
   * @param skuCode               the code to compare with, must exist in the given jsonData
   * @param maxElementsToRetrieve the top n similar sku's to return
   * @return the top n similar skus
   */
  def getSimilarSku(skuCode: String, maxElementsToRetrieve: Int = 10): Seq[(String, Seq[(String, String)])] = {

    val sourceSku = jsonData(skuCode)
    // Removing the element to compare from the source collection
    val elemsToCompare = jsonData.filter(e => !e._1.equals(skuCode))

    // Map of sku element and the calculated weight of the common (intersect) attributes
    val intersectResults = elemsToCompare.map(e => (e._1, e._2.toSeq))
      .map(e => (e, calculateWeight(e._2.intersect(sourceSku.toSeq))))

    // After the weights are calculated, the result wil be sorted to take the top n
    intersectResults.toList.sortWith(_._2 > _._2).map(_._1).take(maxElementsToRetrieve)

  }

  def calculateWeight(attrsA: Seq[(String, String)]): Double = {
    attrsA.map(a => attrPriority(a._1))
      .foldLeft(0.0)((a, b) => weightFormula(a, b))
  }

  /**
   * Formula used to calculate the weigth/priority of each attribute
   *
   * @param accum  accumulator of already calculated weight
   * @param weight the assigned weight
   * @return the priority value
   */
  def weightFormula(accum: Double, weight: Double): Double = {
    accum + 1.0 + Math.pow(0.5, weight + 1)
  }

}

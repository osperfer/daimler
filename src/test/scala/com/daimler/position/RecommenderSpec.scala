package com.daimler.position

import org.scalatest.FlatSpec
import org.scalatest.Matchers.convertToAnyShouldWrapper

import scala.collection.immutable.HashMap

class RecommenderSpec extends FlatSpec{

  "weightFormula" should "return 1.015625 for input 0, 5" in {
    val recommender = Recommender(HashMap.empty)
    recommender.weightFormula(0, 5) shouldBe 1.015625
  }

  "calculateWeight" should "return 2.5625 attributes att-a and att-d" in {
    val recommender = Recommender(HashMap.empty)
    recommender.calculateWeight(Seq (("att-a",""), ("att-d","")) ) shouldBe 2.5625
  }

}

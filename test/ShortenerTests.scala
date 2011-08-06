import play._
import play.test._

import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

import models._    
import play.db.anorm._

class ShortenerTests extends UnitFlatSpec with ShouldMatchers with BeforeAndAfterEach {
      
  override def beforeEach() {
      Fixtures.deleteDatabase()
  } 

  it should "create and retrieve a Shorten Url" in {
      val url = "http://google.com"
      val url2 = "http://playgramework.org"
      Shortener.create(Shortener(url))
      
      val shortenURL = Shortener.find( "fullUrl={fullUrl}")
                                .on("fullUrl" -> url )
                                .first()


      shortenURL should not be (None)      
      shortenURL.get.fullUrl should be (url)
      shortenURL.get.fullUrlHash should be ("c7b920f57e553df2bb68272f61570210")
      shortenURL.get.shortUrl should fullyMatch regex ("""[a-zA-z0-9]+""")
      Shortener.count().single() should be (1)
      
      // add url and increase count
      Shortener.create(Shortener(url2))
      Shortener.count().single() should be (2)
      
      // try to create an already existent url
      Shortener.create(Shortener(url2))
      
      //the count should be 2 since we are not saving the url just returning the existent one
      Shortener.count().single() should be (2)
      
      val shortUrl2 = Shortener.find( "fullUrl={fullUrl}")
                               .on("fullUrl" -> url2 )
                               .first()
                                                                    
      // the url2 properties      
      shortUrl2 should not be (None)      
      shortUrl2.get.fullUrl should be (url2)
      shortUrl2.get.fullUrlHash should be ("7faf91bbbde823d7c27ad7aaa17e196e")
      shortUrl2.get.shortUrl should fullyMatch regex ("""[a-zA-z0-9]+""")
      
  }

}
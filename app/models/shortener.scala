package models

import java.security._
import java.lang.Integer
import scala.util.Random

import play.db.anorm._ 
import play.db.anorm.defaults._

case class Shortener(
    id: Pk[Long],
    fullUrl: String,
    fullUrlHash:String,
    shortUrl:String = { Shortener.convertToBase36 }
)

object Shortener extends Magic[Shortener] {
    
  def apply(fullUrl:String) = {
    val urlHash = Shortener.urlHash(fullUrl);
    Shortener.find("fullUrlHash = {urlHash}")
             .on("urlHash" -> urlHash)
             .first()
             .getOrElse{
                new Shortener(NotAssigned, 
                  fullUrl, 
                  Shortener.urlHash(fullUrl)
                )
              }
  }
  
  def convertToBase36():String = { 
    Integer.toString(new Random().nextInt(100000),36) 
  }
  
  def findFirstByFullUrl(url:String) = {        
    Shortener.find( "fullUrl={fullUrl}")
             .on("fullUrl" -> url )
             .first()             
  }
  
  def findFirstByShortlUrl(url:String) = {        
    Shortener.find( "shortUrl={shortUrl}")
             .on("shortUrl" -> url )
             .first()             
  }
  
  def urlHash(url:String) = {
    val md = MessageDigest.getInstance("MD5");
    md.update( url.getBytes() );
    md.digest.map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _} 
  }    

  // val BASE36DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEVGHIJKLMNOPKRSTUVWXYZ";
  // def convertToBase36(seed:Int):String = {
  //     var response = if(seed == 0) "0" else "" //(longDecimal == 0) ? "0" | ""
  //     var mod = 0
  //     var intSeed = seed
  //     
  //     // while (longDecimal != 0) {
  //     //   mod = (longDecimal % 36);
  //     //   response = BASE36DIGITS.substring(mod, mod + 1) + response;
  //     //   longDecimal = longDecimal / 36;
  //     // }
  //     
  //     do {
  //       mod = (intSeed % 36);
  //       response = BASE36DIGITS.substring(mod, mod + 1) + response;
  //       intSeed = intSeed / 36;
  //     } while (intSeed != 0)
  // 
  //     return response.toString();
  //   }
}

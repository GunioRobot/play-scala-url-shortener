package controllers

import play._
import play.mvc._

import models.Shortener
import views.Application._
  
object Application extends Controller {
        
  def index = {
    html.index()
  }
  
  // Get the POSTed longUrl field from the submited form
  def shorten(longUrl:String) = {        
    Shortener.create(Shortener(longUrl))
    val shortenURL = Shortener.findFirstByFullUrl(longUrl)
    html.index(shortenURL)
  }
  
  // Get the dispatchUrl from the Routes Configuration
  def dispatch(dispatchUrl:String) = {
    val shortenURL = Shortener.findFirstByShortlUrl(dispatchUrl)
    shortenURL match {
      case None => NotFound
      case shortenURL:Option[Shortener] => Redirect(shortenURL.get.fullUrl)
    }
  }
}
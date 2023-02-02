package com.artistPlanner.service

import com.artistPlanner.model.Artist
import com.artistPlanner.model.Concert
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

@Service
class ArtistService() {

    private var artistDB = mutableListOf<Artist>()
    private var concertDB = mutableListOf<Concert>()

    fun scrape(artists: List<String>): MutableList<Artist> {
        artistDB = mutableListOf<Artist>()
        concertDB = mutableListOf<Concert>()

        retrieveArtists@ for (artist in artists) {
            val localConcertDB = mutableListOf<Concert>()

            var onTour: Boolean = true

            val artistDoc: Elements = getArtist(artist)
            println("\n\n=== $artist ===")
            if (artistDoc.toString() == "") {
                onTour = false
                println("$artist is not on tour")
                continue@retrieveArtists
            }

            for (concert in artistDoc) {
                val concert: Concert = getConcerts(concert, artist)

                println("\n${concert.getDate()}")
                println("${concert.getCategory()} is held in ${concert.getCity()}, ${concert.getCountry()}")
                println(concert.getEvent())
                concertDB.add(concert)
                localConcertDB.add(concert)
            }

            artistDB.add(Artist(UUID.randomUUID(), artist, onTour, localConcertDB))
        }
        return artistDB

    }

    private fun getArtist(artist: String): Elements {
        val cleanedArtistName = artist.split(" ").joinToString("+").lowercase()
        var artistCode: String = ""

        // Cleans and queries the artist on songkick.com
        val queryDoc =
            Jsoup.connect("https://www.songkick.com/search?utf8=%E2%9C%93&type=initial&query=$cleanedArtistName&commit=")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                .header("Accept-Language", "*")
                .get()

        // Get actual artist site
        try {
            artistCode = queryDoc.select("li.artist").select("a[href]").attr("href").split("/").last()
        } catch (e: Exception) {
            println("Could not find artist $artist")
        }

        // Connect to actual site
        val concertsDoc = Jsoup.connect("https://www.songkick.com/artists/$artistCode/calendar")
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
            .header("Accept-Language", "*")
            .get()

        return concertsDoc.select("ol.event-listings.tour-calendar-summary.dynamic-ad-container").select("li")

    }

    private fun getConcerts(concert: Element, artist: String): Concert {
        // Get concert date
        val dateString: String = concert.attr("title")
        var date: Date? = null

        // Get concert type and location
        val category = concert.select("div.event-details").attr("class").split(" ")[1]


        dateString.split("-").forEach {
            println(it)
            val (day, month, year) = it.split(" ").drop(1)
            val monthInt = Month.valueOf(month.uppercase()).value;
            val time = "12:00" //Ignore time
            date = SimpleDateFormat("dd MM yyyy HH:mm").parse(listOf(day, monthInt, year, time).joinToString(" "))
        }

        val locationList =
            concert.select("div.event-details").select("strong.primary-detail").text().split(",")
        val city: String = locationList.first().trim()
        val country = locationList.last().trim()
        val event = concert.select("div.event-details").select("p.secondary-detail").text()

        return Concert(UUID.randomUUID(), event, date!!, category, city, country, artist)
    }

    fun getConcertsByCity(city: String): List<Concert> {
        return concertDB.filter {
            it.getCity() == city
        }
    }

    fun getConcertsGlobal(): List<Concert> {
        return concertDB
    }

}
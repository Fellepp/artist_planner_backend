package com.artistPlanner.service

import com.artistPlanner.model.Concert
import com.google.gson.Gson
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

val COUNTRY_TO_CONTINENT = mapOf<String, String>(
    "Algeria" to "Africa",
    "Angola" to "Africa",
    "Benin" to "Africa",
    "Botswana" to "Africa",
    "Burkina" to "Africa",
    "Burundi" to "Africa",
    "Cameroon" to "Africa",
    "Cape Verde" to "Africa",
    "Central African Republic" to "Africa",
    "Chad" to "Africa",
    "Comoros" to "Africa",
    "Congo" to "Africa",
    "Djibouti" to "Africa",
    "Egypt" to "Africa",
    "Equatorial Guinea" to "Africa",
    "Eritrea" to "Africa",
    "Ethiopia" to "Africa",
    "Gabon" to "Africa",
    "Gambia" to "Africa",
    "Ghana" to "Africa",
    "Guinea" to "Africa",
    "Guinea-Bissau" to "Africa",
    "Ivory Coast" to "Africa",
    "Kenya" to "Africa",
    "Lesotho" to "Africa",
    "Liberia" to "Africa",
    "Libya" to "Africa",
    "Madagascar" to "Africa",
    "Malawi" to "Africa",
    "Mali" to "Africa",
    "Mauritania" to "Africa",
    "Mauritius" to "Africa",
    "Morocco" to "Africa",
    "Mozambique" to "Africa",
    "Namibia" to "Africa",
    "Niger" to "Africa",
    "Nigeria" to "Africa",
    "Rwanda" to "Africa",
    "Sao Tome and Principe" to "Africa",
    "Senegal" to "Africa",
    "Seychelles" to "Africa",
    "Sierra Leone" to "Africa",
    "Somalia" to "Africa",
    "South Africa" to "Africa",
    "South Sudan" to "Africa",
    "Sudan" to "Africa",
    "Swaziland" to "Africa",
    "Tanzania" to "Africa",
    "Togo" to "Africa",
    "Tunisia" to "Africa",
    "Uganda" to "Africa",
    "Zambia" to "Africa",
    "Zimbabwe" to "Africa",
    "Afghanistan" to "Asia",
    "Bahrain" to "Asia",
    "Bangladesh" to "Asia",
    "Bhutan" to "Asia",
    "Brunei" to "Asia",
    "Burma (Myanmar)" to "Asia",
    "Cambodia" to "Asia",
    "China" to "Asia",
    "East Timor" to "Asia",
    "India" to "Asia",
    "Indonesia" to "Asia",
    "Iran" to "Asia",
    "Iraq" to "Asia",
    "Israel" to "Asia",
    "Japan" to "Asia",
    "Jordan" to "Asia",
    "Kazakhstan" to "Asia",
    "Korea" to "Asia",
    "Kuwait" to "Asia",
    "Kyrgyzstan" to "Asia",
    "Laos" to "Asia",
    "Lebanon" to "Asia",
    "Malaysia" to "Asia",
    "Maldives" to "Asia",
    "Mongolia" to "Asia",
    "Nepal" to "Asia",
    "Oman" to "Asia",
    "Pakistan" to "Asia",
    "Philippines" to "Asia",
    "Qatar" to "Asia",
    "Russian Federation" to "Asia",
    "Saudi Arabia" to "Asia",
    "Singapore" to "Asia",
    "Sri Lanka" to "Asia",
    "Syria" to "Asia",
    "Tajikistan" to "Asia",
    "Thailand" to "Asia",
    "Turkey" to "Asia",
    "Turkmenistan" to "Asia",
    "United Arab Emirates" to "Asia",
    "Uzbekistan" to "Asia",
    "Vietnam" to "Asia",
    "Yemen" to "Asia",
    "Albania" to "Europe",
    "Andorra" to "Europe",
    "Armenia" to "Europe",
    "Austria" to "Europe",
    "Azerbaijan" to "Europe",
    "Belarus" to "Europe",
    "Belgium" to "Europe",
    "Bosnia and Herzegovina" to "Europe",
    "Bulgaria" to "Europe",
    "Croatia" to "Europe",
    "Cyprus" to "Europe",
    "CZ" to "Europe",
    "Denmark" to "Europe",
    "Estonia" to "Europe",
    "Finland" to "Europe",
    "France" to "Europe",
    "Georgia" to "Europe",
    "Germany" to "Europe",
    "Greece" to "Europe",
    "Hungary" to "Europe",
    "Iceland" to "Europe",
    "Ireland" to "Europe",
    "Italy" to "Europe",
    "Latvia" to "Europe",
    "Liechtenstein" to "Europe",
    "Lithuania" to "Europe",
    "Luxembourg" to "Europe",
    "Macedonia" to "Europe",
    "Malta" to "Europe",
    "Moldova" to "Europe",
    "Monaco" to "Europe",
    "Montenegro" to "Europe",
    "Netherlands" to "Europe",
    "Norway" to "Europe",
    "Poland" to "Europe",
    "Portugal" to "Europe",
    "Romania" to "Europe",
    "San Marino" to "Europe",
    "Serbia" to "Europe",
    "Slovakia" to "Europe",
    "Slovenia" to "Europe",
    "Spain" to "Europe",
    "Sweden" to "Europe",
    "Switzerland" to "Europe",
    "Ukraine" to "Europe",
    "UK" to "Europe",
    "Vatican City" to "Europe",
    "Antigua and Barbuda" to "North America",
    "Bahamas" to "North America",
    "Barbados" to "North America",
    "Belize" to "North America",
    "Canada" to "North America",
    "Costa Rica" to "North America",
    "Cuba" to "North America",
    "Dominica" to "North America",
    "Dominican Republic" to "North America",
    "El Salvador" to "North America",
    "Grenada" to "North America",
    "Guatemala" to "North America",
    "Haiti" to "North America",
    "Honduras" to "North America",
    "Jamaica" to "North America",
    "Mexico" to "North America",
    "Nicaragua" to "North America",
    "Panama" to "North America",
    "Saint Kitts and Nevis" to "North America",
    "Saint Lucia" to "North America",
    "Saint Vincent and the Grenadines" to "North America",
    "Trinidad and Tobago" to "North America",
    "US" to "North America",
    "Australia" to "Oceania",
    "Fiji" to "Oceania",
    "Kiribati" to "Oceania",
    "Marshall Islands" to "Oceania",
    "Micronesia" to "Oceania",
    "Nauru" to "Oceania",
    "New Zealand" to "Oceania",
    "Palau" to "Oceania",
    "Papua New Guinea" to "Oceania",
    "Samoa" to "Oceania",
    "Solomon Islands" to "Oceania",
    "Tonga" to "Oceania",
    "Tuvalu" to "Oceania",
    "Vanuatu" to "Oceania",
    "Argentina" to "South America",
    "Bolivia" to "South America",
    "Brazil" to "South America",
    "Chile" to "South America",
    "Colombia" to "South America",
    "Ecuador" to "South America",
    "Guyana" to "South America",
    "Paraguay" to "South America",
    "Peru" to "South America",
    "Suriname" to "South America",
    "Uruguay" to "South America",
    "Venezuela" to "South America",
    "Czech Republic" to "Europe"
)

@Service
class ArtistService() {
    var artistCodes = mutableMapOf<String, String>()
    private var concertDB = mutableListOf<Concert>()

    private fun initArtistCodes() = File("artist_codes.json")
        .readText()
        .filterNot { (it in listOf('"', '{', '}')) }
        .split(",")
        .map { it.split(":") }
        .map { it.first() to it.last() }
        .toMap(mutableMapOf())

    private fun saveToArtistCodes() {
        val gson = Gson()
        val json = gson.toJson(artistCodes)

        println("Saving artist codes to file")
        val file = File("artist_codes.json")
        file.writeText(json)
    }

    fun scrape(artists: List<String>): MutableList<Concert> {

        artistCodes = initArtistCodes()
        concertDB = mutableListOf()

        retrieveArtists@ for (artist in artists) {
            val localConcertDB = mutableListOf<Concert>()

            var onTour: Boolean = true

            // Loops through all artists in list
            val artistDoc: Elements = getArtist(artist) ?: continue@retrieveArtists
            if (artistDoc.toString() == "") {
                onTour = false
                println("$artist is not on tour")
                continue@retrieveArtists
            }

            // Loops through all concerts for artist
            for (concert in artistDoc) {
                val concert: MutableList<Concert> = getConcerts(concert, artist)
                for (concerts in concert) {
                    concertDB.add(concerts)
                    localConcertDB.add(concerts)
                }
            }
        }
        saveToArtistCodes()
        return concertDB
    }

    private fun getArtist(artist: String): Elements? {
        val cleanedArtistName = artist.split(" ").joinToString("+").lowercase()
        var artistCode: String?

        artistCode = artistCodes[cleanedArtistName]
        if (artistCode == null) {
            println("The code for $artist is not registered. Adding it to the artistcode.json file")
            // Cleans and queries the artist on songkick.com
            val queryDoc =
                Jsoup.connect("https://www.songkick.com/search?utf8=%E2%9C%93&type=initial&query=$cleanedArtistName&commit=")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                    .header("Accept-Language", "*")
                    .get()

            // Get actual artist site
            artistCode = queryDoc.select("li.artist").select("a[href]").attr("href").split("/").last()
            if (artistCode.split("-").last() == "") {
                println("Could not find the artist $artist")
                return null
            }
            artistCodes[cleanedArtistName] = artistCode
        }

        // Connect to actual site
        val concertsDoc = Jsoup.connect("https://www.songkick.com/artists/$artistCode/calendar")
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
            .header("Accept-Language", "*")
            .get()

        return concertsDoc.select("ol.event-listings.tour-calendar-summary.dynamic-ad-container").select("li")

    }

    private fun getConcerts(concert: Element, artist: String): MutableList<Concert> {
        // Get concert date
        val dateString: String = concert.attr("title")
        var date: MutableList<Date> = mutableListOf()

        // Get concert type and location
        val category = concert.select("div.event-details").attr("class").split(" ")[1]


        val cal: Calendar = Calendar.getInstance()
        val time = "12:00" //Ignore time

        val (lastDay, lastMonth, lastYear) = dateString.split(" – ").last().split(" ").drop(1)
        var monthInt = Month.valueOf(lastMonth.uppercase()).value;
        val toDate = SimpleDateFormat("dd MM yyyy HH:mm").parse(listOf(lastDay, monthInt, lastYear, time).joinToString(" "))


        var (firstDay, firstMonth, firstYear) = dateString.split(" – ").first().split(" ").drop(1)
        monthInt = Month.valueOf(firstMonth.uppercase()).value;
        val fromDate = SimpleDateFormat("dd MM yyyy HH:mm").parse(listOf(firstDay, monthInt, firstYear, time).joinToString(" "))


        var lastDate : Calendar = Calendar.getInstance()
        lastDate.time = toDate
        cal.time = fromDate

        while( cal.before(lastDate) ) {
            date.add(cal.time)
            cal.add(Calendar.DATE, 1)
        }
        date.add(lastDate.time)

        val locationList =
            concert.select("div.event-details").select("strong.primary-detail").text().split(",")
        val city: String = locationList.first().trim()
        val country = locationList.last().trim()
        val event = concert.select("div.event-details").select("p.secondary-detail").text()

        val concertRes: MutableList<Concert> = mutableListOf()

        for (dates in date) {
            concertRes.add(
                Concert(
                    UUID.randomUUID(),
                    event,
                    dates,
                    category,
                    city,
                    country,
                    getContinentFromCountry(country)!!,
                    artist
                )
            )
        }
        return concertRes
    }

    private fun getContinentFromCountry(country: String) = COUNTRY_TO_CONTINENT[country]

    fun getConcertsGlobal(): List<Concert> = concertDB
    fun getConcertsByContinent(continent: String): List<Concert> = concertDB.filter { it.getContinent() == continent }
    fun getConcertsByCountry(country: String): List<Concert> = concertDB.filter { it.getCountry() == country }
    fun getConcertsByCity(city: String): List<Concert> = concertDB.filter { it.getCity() == city }
    fun getConcertsByArtist(artist: String): List<Concert> = concertDB.filter { it.getArtist() == artist }
    fun getConcertsByContinentAndArtist(continent: String, artist: String): List<Concert> =
        concertDB.filter { it.getContinent() == continent && it.getArtist() == artist }

}
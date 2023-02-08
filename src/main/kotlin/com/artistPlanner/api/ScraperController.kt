package com.artistPlanner.api

import com.artistPlanner.model.Concert
import com.artistPlanner.service.ArtistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("api/v1")
@RestController
@CrossOrigin
class ScraperController(@Autowired val artistService: ArtistService) {

    @GetMapping(path = ["/scrape"])
    fun scrape(): MutableList<Concert> {
        // List of artists to search on
        val artists =
            listOf<String>(
                "Slipknot",
                "Bad Omens",
                "Lorna Shore",
                "Bring Me The Horizon",
                "Sleep Token",
                "Kamelot",
                "asd",
                "Metallica",
                "Five finger death punch",
                "Ice Nine Kills",
                "Nightwish",
                "Skillet",
                "Iron Maiden",
                "+=!¤!??",
                "I Prevail",
                "",
                "Sabaton"
            )
        return artistService.scrape(artists)
    }

    @GetMapping(path = ["/global"])
    fun getConcertsGlobal() = artistService.getConcertsGlobal()
    @GetMapping(path = ["/continent/{continent}"])
    fun getConcertsByContinent(@PathVariable("continent") continent: String) = artistService.getConcertsByContinent(continent)
    @GetMapping(path = ["/country/{country}"])
    fun getConcertsByCountry(@PathVariable("country") country: String) = artistService.getConcertsByCountry(country)
    @GetMapping(path = ["/city/{city}"])
    fun getConcertsByCity(@PathVariable("city") city: String) = artistService.getConcertsByCity(city)
    @GetMapping(path = ["/artist/{artist}"])
    fun getConcertsByArtist(@PathVariable("artist") artist: String) = artistService.getConcertsByArtist(artist)
    @GetMapping(path = ["/artist/{continent}/{artist}"])
    fun getConcertsByArtist(@PathVariable("continent") continent: String, @PathVariable("artist") artist: String) = artistService.getConcertsByContinentAndArtist(continent, artist)
}
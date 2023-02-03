package com.artistPlanner.api

import com.artistPlanner.model.Artist
import com.artistPlanner.service.ArtistService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("api/v1")
@RestController
@CrossOrigin
class ScraperController(@Autowired val artistService: ArtistService) {

    @GetMapping(path = ["/scrape"])
    fun scrape(): MutableList<Artist> {
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

    @GetMapping(path = ["/city/{city}"])
    fun getConcertsByCity(@PathVariable("city") city: String) = artistService.getConcertsByCity(city)

    @GetMapping(path = ["/global"])
    fun getConcertsGlobal() = artistService.getConcertsGlobal()

}
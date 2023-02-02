package com.artistPlanner.model

import java.util.*

class Concert(
    private val id: UUID,
    private val event: String,
    private val date: Date,
    private val category: String,
    private val city: String,
    private val country: String,
    private val artist: String
) {

    fun getId() : UUID = id

    fun getEvent() : String = event

    fun getDate() : Date = date

    fun getCategory() : String = category

    fun getCity() : String = city

    fun getCountry() : String = country

    fun getArtist() : String = artist
}
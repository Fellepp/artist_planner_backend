package com.artistPlanner.model

import jakarta.validation.constraints.NotBlank
import java.util.*

class Artist(
    private val id: UUID,
    @NotBlank private val name: String,
    @NotBlank private val onTour: Boolean,
    private val concerts: MutableList<Concert>
) {
    fun getId(): UUID = id

    fun getName(): String = name

    fun getOnTour(): Boolean = onTour

    fun getConcerts(): MutableList<Concert> = concerts
}

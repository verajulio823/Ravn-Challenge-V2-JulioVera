package com.ravn.codechallenge.models

import java.io.Serializable

data class PeopleDetail(
    val eyeColor: String,
    val hairColor: String,
    val skinColor: String,
    val birthYear: String,
    val vehicles: List<String>?
        ):Serializable
package ru.mkrasikoff.springmvcapp.models

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class Person(
        var id: Int = 0,

        @field:NotEmpty(message = "Name shouldn't be empty")
        @field:Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
        var name: String? = null,

        @field:NotEmpty(message = "Surname shouldn't be empty")
        @field:Size(min = 2, max = 30, message = "Surname should be between 2 and 30 characters")
        var surname: String? = null,

        @field:NotEmpty(message = "Email shouldn't be empty")
        @field:Email(message = "Email should be valid")
        var email: String? = null
) {
        constructor(name: String, surname: String, email: String) : this(0, name, surname, email)
}

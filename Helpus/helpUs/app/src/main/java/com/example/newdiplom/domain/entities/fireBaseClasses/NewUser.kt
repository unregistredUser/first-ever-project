package com.example.newdiplom.domain.entities.fireBaseClasses

open class NewUser {
    var id: String? = null
    var name: String? = null
    var secname: String? = null
    var phone: String? = null
    var age: String? = null
    var gender: String? = null
    var rating = 0.0
    var review = 0

    constructor() {}
    constructor(
        id: String?,
        name: String?,
        secname: String?,
        phone: String?,
        age: String?,
        rating: Double,
        review: Int,
        gender: String?
    ) {
        this.review = review
        this.rating = rating
        this.age = age
        this.id = id
        this.name = name
        this.secname = secname
        this.phone = phone
        this.gender = gender
    }
}

class CustomNewUser:NewUser() {

}


package com.example.newdiplom.domain.entities.fireBaseClasses

open class NewVolunteer {
    var iduser: String? = null
    var named: String? = null
    var famil: String? = null
    var phonenum: String? = null
    var age: String? = null
    var gender: String? = null
    var rating = 0.0
    var review = 0

    constructor() {}
    constructor(
        iduser: String?,
        named: String?,
        famil: String?,
        phonenum: String?,
        age: String?,
        rating: Double,
        review: Int,
        gender: String?
    ) {
        this.review = review
        this.rating = rating
        this.age = age
        this.iduser = iduser
        this.named = named
        this.famil = famil
        this.phonenum = phonenum
        this.gender = gender
    }
}
class CustomNewVolunteer:NewVolunteer(){

}
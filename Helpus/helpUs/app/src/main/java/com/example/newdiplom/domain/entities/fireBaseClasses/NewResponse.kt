package com.example.newdiplom.domain.entities.fireBaseClasses

open class NewResponse {
    var idres: String? = null
    var idtask: String? = null
    var idus: String? = null
    var Date: String? = null
    var categ: String? = null
    var taskname: String? = null

    constructor() {}
    constructor(
        idres: String?,
        idtask: String?,
        idus: String?,
        Date: String?,
        categ: String?,
        taskname: String?
    ) {
        this.idres = idres
        this.idtask = idtask
        this.idus = idus
        this.Date = Date
        this.categ = categ
        this.taskname = taskname
    }
}

class CustomNewResponse :NewResponse(){

}
package com.example.newdiplom.domain.entities.fireBaseClasses

open class NewTask {
    var idtask: String? = null
    var iduser: String? = null
    var taskname: String? = null
    var difficulty: String? = null
    var date1: String? = null
    var date2: String? = null

    constructor() {}
    constructor(
        idtask: String?,
        iduser: String?,
        taskname: String?,
        difficulty: String?,
        date1: String?,
        date2: String?
    ) {
        this.idtask = idtask
        this.iduser = iduser
        this.taskname = taskname
        this.difficulty = difficulty
        this.date1 = date1
        this.date2 = date2
    }
}

class CustomNewTask:NewTask(){

}
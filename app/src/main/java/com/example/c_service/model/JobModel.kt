package com.example.c_service.model

class JobModel {
    private var id_job: Long? = null
    private var judul: String? = null
    private var id_user: String? = null
    private var id_receive: String? = null
    private var deskripsi: String? = null
    private var department: String? = null
    private var image: String? = null
    private var key: String? = null


    constructor()
    constructor(
        id_job: Long, judul: String, id_user: String, id_receive: String, deskripsi: String,
        department: String, image: String
    ) {
        this.id_job = id_job
        this.judul = judul
        this.id_user = id_user
        this.id_receive = id_receive
        this.deskripsi = deskripsi
        this.department = department
        this.image = image
    }

    fun getId_job(): Long? {
        return id_job
    }

    fun getJudul(): String? {
        return judul
    }

    fun getId_user(): String? {
        return id_user
    }

    fun getId_receive(): String? {
        return id_receive
    }

    fun getDeskripsi(): String? {
        return deskripsi
    }

    fun getDepartment(): String? {
        return department
    }

    fun getImage(): String? {
        return image
    }

    fun getKey(): String {
        return key!!
    }

    fun setKey(key: String) {
        this.key = key
    }

    fun setId_job(id_job: Long) {
        this.id_job = id_job
    }

    fun setJudul(judul: String) {
        this.judul = judul
    }

    fun setId_user(id_user: String) {
        this.id_user = id_user
    }

    fun setId_receive(id_receive: String) {
        this.id_receive = id_receive
    }

    fun setDeskripsi(deskripsi: String) {
        this.deskripsi = deskripsi
    }

    fun setDepartment(department: String) {
        this.department = department
    }

    fun setImage(image: String) {
        this.image = image
    }

}
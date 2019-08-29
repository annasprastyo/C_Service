package com.example.c_service.model

class DetailJobModel {
    private var id_job: Long? = null
    private var id_detail_job: Long? = null
    private var id_user: String? = null
    private var deskripsi: String? = null
    private var isdone: Long? = null
    private var key: String? = null

    constructor()
    constructor(id_job: Long, id_detail_job: Long, id_user: String, deskripsi: String, isdone: Long) {
        this.id_job = id_job
        this.id_detail_job = id_detail_job
        this.id_user = id_user
        this.deskripsi = deskripsi
        this.isdone = isdone
        this.key = key
    }

    fun getId_job(): Long? {
        return id_job
    }

    fun getId_detail_job(): Long? {
        return id_detail_job
    }

    fun getId_user(): String? {
        return id_user
    }

    fun getDeskripsi(): String? {
        return deskripsi
    }

    fun getIsdone(): Long? {
        return isdone
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

    fun setId_detail_job(id_detail_job: Long) {
        this.id_detail_job = id_detail_job
    }

    fun setId_user(id_user: String) {
        this.id_user = id_user
    }

    fun setDeskripsi(deskripsi: String) {
        this.deskripsi = deskripsi
    }

    fun setIsdone(isdone: Long) {
        this.isdone = isdone
    }

}
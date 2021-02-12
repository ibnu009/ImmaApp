package com.pjb.immaapp.utils.rq

import androidx.sqlite.db.SimpleSQLiteQuery
import java.lang.StringBuilder

object SearchQuery {

    fun getSearchQueryResult(keyword: String): SimpleSQLiteQuery {
        val simpleSQLiteQuery = StringBuilder().append("SELECT * FROM purchaseorderentity ")
        simpleSQLiteQuery.append("WHERE job_title LIKE $keyword")

        return SimpleSQLiteQuery(simpleSQLiteQuery.toString())
    }
}
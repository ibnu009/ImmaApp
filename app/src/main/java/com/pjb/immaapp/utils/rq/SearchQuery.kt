package com.pjb.immaapp.utils.rq

import androidx.sqlite.db.SimpleSQLiteQuery
import java.lang.StringBuilder

object SearchQuery {

    fun getSearchQueryResult(keyword: String): SimpleSQLiteQuery {
        val simpleSQLiteQuery = StringBuilder().append("SELECT * FROM purchaseorderentity ")
        simpleSQLiteQuery.append("JOIN data_po_fts ON purchaseorderentity.job_title = data_po_fts.job_title")
        simpleSQLiteQuery.append(" WHERE data_po_fts MATCH $keyword")

        return SimpleSQLiteQuery(simpleSQLiteQuery.toString())
    }

    fun getSearchQuerySupplier(keyword: String): SimpleSQLiteQuery {
        val simpleSQLiteQuery = StringBuilder().append("SELECT * FROM supplierentity ")
        simpleSQLiteQuery.append("JOIN supplier_fts ON supplierentity.name = supplier_fts.name")
        simpleSQLiteQuery.append(" WHERE supplier_fts MATCH $keyword")

        return SimpleSQLiteQuery(simpleSQLiteQuery.toString())
    }
}
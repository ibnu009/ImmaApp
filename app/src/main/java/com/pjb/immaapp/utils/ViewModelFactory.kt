package com.pjb.immaapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(): ViewModelProvider.NewInstanceFactory() {

    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory()
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return super.create(modelClass)
//        TODO memberikan fungsi pada custom View Model Factory
    }

}
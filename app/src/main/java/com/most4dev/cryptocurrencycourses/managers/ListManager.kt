package com.most4dev.cryptocurrencycourses.managers

import com.most4dev.cryptocurrencycourses.models.CryptoModel

class ListManager {

    companion object{

        fun convertListToArrayNameCrypto(list: List<CryptoModel>): Array<String>{
            val arrayNameCrypto = arrayListOf<String>()
            for (item in list){
                arrayNameCrypto.add(item.name)
            }
            return arrayNameCrypto.toTypedArray()
        }

    }

}
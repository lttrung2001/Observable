package com.ltbth.reactiveextention

class User (var id: Int, var name: String) {
    override fun toString(): String {
        return "${id}_${name}"
    }
}
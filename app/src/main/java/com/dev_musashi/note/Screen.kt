package com.dev_musashi.note

const val ARG_ID = "id"
sealed class Screen(val route : String) {
    object Main : Screen(route = "MAIN")
    object Note: Screen(route = "NOTE?id={$ARG_ID}")

    fun passId(
        id: Int
    ) : String {
        return "NOTE?id=$id"
    }
}
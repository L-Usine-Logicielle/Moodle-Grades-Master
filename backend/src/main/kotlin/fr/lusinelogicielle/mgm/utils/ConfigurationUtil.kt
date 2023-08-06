package fr.lusinelogicielle.mgm.utils

fun splitBeforeAndAfterCharacter(input: String, character: Char): Pair<String, String> {
    val before = input.substringBefore(character, input)
    val after = input.substringAfter(character, "")
    return Pair(before, after)
}

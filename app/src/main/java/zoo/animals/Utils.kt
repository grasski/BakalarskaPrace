package zoo.animals


class Utils {

    fun stringMapToIndex(map: Map<String, Any>): Map<Int, String> {
        val keys: Set<String> = (listOf(map.keys)[0])

        return keys.mapIndexed { index: Int, value: String -> index to value }.toMap()
    }
}
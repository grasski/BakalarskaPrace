package zoo.animals.feature_category.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Animal(
    val name: String,
    val category: String,
    val info: Map<String, String>,
    val description: String,
    val previewImage: Int,
    val mainImage: Int,
    var seen: Boolean = false,
    val appearance: List<List<Float>>,
    var canDetect: Boolean = false,
    var seenInfo: MutableList<AnimalSeenInfo>? = null
): Parcelable


enum class AnimalAppearance(){
    EUROPE {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1350f, 340f, 2.5f)
            )
        }
    },
    EUROPE_NORTH {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1345f, 230f, 1f)
            )
        }
    },
    EUROPE_SOUTH {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1150f, 470f, 0.7f),
                listOf(1330f, 450f, 0.7f)
            )
        }
    },
    EUROPE_EAST {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1560f, 300f, 1f)
            )
        }
    },

    ASIA{
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(2040f, 700f, 3.5f)
            )
        }
    },
    ASIA_HIMALAYAS {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1860f, 560f, 0.5f),
                listOf(1960f, 610f, 0.5f)
            )
        }
    },
    ASIA_TIBET_NEPAL {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1925f, 565f, 0.8f)
            ) + ASIA_HIMALAYAS.getCoords()
        }
    },
    ASIA_MONGOLIA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(2060f, 450f, 1f)
            )
        }
    },
    ASIA_NORTH {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1770f, 350f, 2.3f),
                listOf(2155f, 360f, 2.3f)
            )
        }
    },
    ASIA_NORTH2 {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1770f, 250f, 2f),
                listOf(2155f, 260f, 2f)
            )
        }
    },
    ASIA_SOUTH{
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(2080f, 720f, 0.7f)
                ) + ARABIA.getCoords() + INDIA.getCoords()
        }
     },
    ASIA_WEST{
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1700f, 575f, 1f),
                listOf(1840f, 525f, 1f)
            )
        }
    },
    ASIA_CENTER{
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(2100f, 580f, 1f),
            ) + ASIA_WEST.getCoords()
        }
    },
    ASIA_INDONESIA{
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(2200f, 960f, 1f)
            )
        }
    },
    INDIA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1880f, 680f, 1f)
            )
        }
    },
    ARABIA{
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1595f, 630f, 1f)
            )
        }
    },

    AFRICA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1335f, 870f, 3.5f)
            )
        }
    },
    AFRICA_SOUTH {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1395f, 1130f, 1f)
            )
        }
    },
    AFRICA_CENTER {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1330f, 850f, 1f),
                listOf(1500f, 850f, 1f)
            )
        }
    },

    NORTH_AMERICA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(350f, 410f, 3f)
            )
        }
    },

    CENTRAL_AMERICA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(410f, 730f, 1.5f)
            )
        }
    },

    SOUTH_AMERICA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(660f, 1120f, 3.4f)
            )
        }
    },
    SOUTH_AMERICA_PERU {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(515f, 1040f, 0.7f)
            )
        }
    },
    SOUTH_AMERICA_BRAZIL {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(740f, 1035f, 1.25f)
            )
        }
    },
    SOUTH_AMERICA_CHILE {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(575f, 1175f, 0.5f),
                listOf(580f, 1300f, 0.5f),
                listOf(603f, 1405f, 0.4f),
                listOf(635f, 1490f, 0.35f)
            )
        }
    },
    SOUTH_AMERICA_BOLIVIA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(605f, 1100f, 0.65f)
            )
        }
    },
    SOUTH_AMERICA_ARGENTINA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(690f, 1190f, 0.6f),
                listOf(675f, 1285f, 0.6f),
                listOf(660f, 1423f, 0.32f)
            )
        }
    },
    SOUTH_AMERICA_PATAGONIA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(630f, 1415f, 0.8f)
            )
        }
    },
    SOUTH_AMERICA_SEASIDE {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(703f, 1500f, 0.55f),
                listOf(505f, 1040f, 0.55f),
                listOf(670f, 1390f, 0.55f),
            ) + SOUTH_AMERICA_CHILE.getCoords()
        }
    },

    MADAGASCAR {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1593f, 1135f, 0.65f)
            )
        }
    },
    AUSTRALIA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(2350f, 1190f, 1.8f)
            )
        }
    },
    CANARY {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1019f, 600f, 0.68f)
            )
        }
    },
    CANADA {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(250f, 270f, 2f),
                listOf(590f, 260f, 2f)
            )
        }
    },
    GREENLAND_NORTH {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(880f, 95f, 0.5f),
                listOf(970f, 80f, 0.5f),
                listOf(1075f, 90f, 0.5f)
            )
        }
    },
    WORLD {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(1960f, 420f, 4f),
                listOf(2240f, 900f, 1.6f)
            ) + CENTRAL_AMERICA.getCoords() + SOUTH_AMERICA.getCoords() + NORTH_AMERICA.getCoords() +
                    EUROPE.getCoords() + AFRICA.getCoords() + MADAGASCAR.getCoords() +
                    AUSTRALIA.getCoords()
        }
    },
    PENGUIN {
        override fun getCoords(): List<List<Float>> {
            return listOf(
                listOf(880f, 1500f, 2.2f),
                listOf(1200f, 1500f, 2.3f),
                listOf(1520f, 1500f, 2.3f),
                listOf(1840f, 1500f, 2f),
                listOf(2160f, 1500f, 2f)
            ) + SOUTH_AMERICA_SEASIDE.getCoords()
        }
    };

    abstract fun getCoords(): List<List<Float>>
}

package tmg.passage.home

import tmg.passage.data.models.Passage

sealed class HomeItemType {
    object Header: HomeItemType()
    class Item(val passage: Passage): HomeItemType()
}
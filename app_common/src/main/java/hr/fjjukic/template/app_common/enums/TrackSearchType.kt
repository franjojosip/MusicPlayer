package hr.fjjukic.template.app_common.enums

sealed class TrackSearchType {
    object All : TrackSearchType()
    data class Album(val albumID: Long) : TrackSearchType()
    data class Genre(val genreID: Long) : TrackSearchType()
    data class Artist(val artistID: Long) : TrackSearchType()
}
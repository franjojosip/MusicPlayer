package hr.fjjukic.template.app_home.home.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import hr.fjjukic.template.app_common.interfaces.SortClickListener
import hr.fjjukic.template.app_common.model.Track
import hr.fjjukic.template.app_common.noResult
import hr.fjjukic.template.app_common.songSingle
import hr.fjjukic.template.app_common.sortBy
import hr.fjjukic.template.app_home.home.enum.CellType

class HomeEpoxyController :
    TypedEpoxyController<List<Any>>() {
    override fun buildModels(data: List<Any>) {
        data.forEach {
            when (it) {
                is Track -> createTrackCell(it)
                is CellType.NoResult -> createNoResultCell()
            }
        }
    }

    private fun createTrackCell(data: Track) {
        songSingle {
            id("song-single-${data.title}")
            track(data)
        }
    }

    private fun createNoResultCell() {
        noResult {
            id("no-result")
        }
    }
}
package hr.fjjukic.template.app_home.home.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import hr.fjjukic.template.app_common.enums.TrackSearchType
import hr.fjjukic.template.app_common.manager.media.MediaManager
import hr.fjjukic.template.app_common.router.AppRouter
import hr.fjjukic.template.app_common.view_model.AppVM
import hr.fjjukic.template.app_home.home.enum.CellType
import kotlinx.coroutines.launch

class HomeVM(
    override val screenAdapter: ScreenAdapterImpl,
    override val router: AppRouter,
    private val mediaManager: MediaManager
) : AppVM() {
    private val _tracks: MutableLiveData<List<Any>> by lazy { MutableLiveData() }
    val tracks: LiveData<List<Any>> = _tracks

    fun init() {
        viewModelScope.launch {
            val tracks = mediaManager.getTracks(TrackSearchType.All)
            when {
                tracks.isNullOrEmpty() -> {
                    _tracks.postValue(mutableListOf(CellType.NoResult))
                }
                else -> {
                    _tracks.postValue(tracks)
                }
            }
        }
    }
}

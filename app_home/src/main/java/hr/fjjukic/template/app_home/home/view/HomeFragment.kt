package hr.fjjukic.template.app_home.home.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import hr.fjjukic.template.app_common.fragment.AppFragment
import hr.fjjukic.template.app_common.interfaces.SortClickListener
import hr.fjjukic.template.app_home.R
import hr.fjjukic.template.app_home.databinding.FragmentHomeBinding
import hr.fjjukic.template.app_home.home.epoxy.HomeEpoxyController
import hr.fjjukic.template.app_home.home.view_model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : AppFragment<HomeVM, FragmentHomeBinding>() {
    override val layoutId: Int = R.layout.fragment_home
    override val appVM: HomeVM by viewModel()
    private val controller by lazy { HomeEpoxyController()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appVM.init()
        setupUI()
        observeData()
    }

    private fun setupUI() {
        binding.epoxyRecyclerView.setController(controller)
    }

    private fun observeData() {
        appVM.tracks.observe(viewLifecycleOwner) {
            controller.setData(it)
        }
    }

}

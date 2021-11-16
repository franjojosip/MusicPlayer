package hr.fjjukic.template.app_home.home.view

import hr.fjjukic.template.app_common.fragment.AppFragment
import hr.fjjukic.template.app_home.R
import hr.fjjukic.template.app_home.databinding.FragmentHomeBinding
import hr.fjjukic.template.app_home.home.view_model.HomeVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : AppFragment<HomeVM, FragmentHomeBinding>() {
    override val layoutId: Int = R.layout.fragment_home
    override val appVM: HomeVM by viewModel()
}

package hr.fjjukic.template.app_common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import hr.fjjukic.template.app_common.delegate.EventDelegate
import hr.fjjukic.template.app_common.router.NavigationController
import hr.fjjukic.template.app_common.view_model.AppVM
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

abstract class AppFragment<VM : AppVM, Binding : ViewDataBinding> : Fragment(), EventDelegate {
    abstract val layoutId: Int
    abstract val appVM: VM
    protected lateinit var binding: Binding
        private set

    open val navigationController: NavigationController by inject { parametersOf(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appVM.router.observe(viewLifecycleOwner, navigationController)
        appVM.screenAdapter.observe(viewLifecycleOwner, this)
    }

    override fun navigate(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}

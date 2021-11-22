package hr.fjjukic.template.app_common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import hr.fjjukic.template.app_common.R
import hr.fjjukic.template.app_common.databinding.LayoutSortByBinding
import hr.fjjukic.template.app_common.interfaces.SortClickListener

class SortMenuView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {
    var listener: SortClickListener? = null
    private lateinit var popupMenu: PopupMenu
    private val binding = LayoutSortByBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setUpPopupMenu()
        setUpClickEvents()
    }

    private fun setUpPopupMenu() {
        popupMenu = PopupMenu(context, this)
        popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            true
        }
    }

    private fun setUpClickEvents() {
        binding.ivSort.setOnClickListener {
            popupMenu.show()
        }
    }

}
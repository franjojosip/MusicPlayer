package hr.fjjukic.template.app_home.container.view_model

import hr.fjjukic.template.app_common.adapter.ScreenAdapterImpl
import hr.fjjukic.template.app_common.router.AppRouter
import hr.fjjukic.template.app_common.view_model.AppVM

class MainContainerVM(
    override val screenAdapter: ScreenAdapterImpl,
    override val router: AppRouter
) : AppVM()

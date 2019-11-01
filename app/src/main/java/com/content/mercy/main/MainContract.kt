package com.content.mercy.main

import com.content.mercy.BasePresenter
import com.content.mercy.BaseView

/**
 * Created by rapsealk on 2019-11-01..
 */
interface MainContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {

    }
}
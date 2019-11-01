package com.content.mercy.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.content.mercy.R

/**
 * Created by rapsealk on 2019-11-01..
 */
class MainFragment : Fragment(), MainContract.View {

    private var mPresenter: MainContract.Presenter = MainPresenter()

    override var presenter: MainContract.Presenter
        get() = mPresenter
        set(value) { mPresenter = value }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
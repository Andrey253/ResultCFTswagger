package com.boyko.resultcftswagger.api.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import timber.log.Timber

abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onStart")
        super.onStart()
    }

    override fun onResume() {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onResume")
        super.onResume()
    }

    override fun onPause() {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onPause")
        super.onPause()
    }

    override fun onStop() {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Timber.d("Fragment lifecycle: ${this::class.simpleName} onDetach")
        super.onDetach()
    }
}
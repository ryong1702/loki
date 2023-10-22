package com.application.lokisystem

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlin.system.exitProcess


class Dialog : DialogFragment() {
    private var type: String? = null

    companion object {
        fun newInstance(type: String): DialogFragment {
            val fragment = Dialog()
            val args = Bundle()
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 스타일 적용

        type = arguments?.getString("type")
    }

    override fun onStart() {
        super.onStart()

        // 레이아웃 크기 및 위치 조정
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog?.window?.setLayout(width, height)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false            //팝업창 빠져나가기 방지
        return AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
            .setMessage("화면을 불러오지 못했습니다.")
            .setPositiveButton("앱 종료") { _, _ ->
                exitProcess(0)
            }
            .setNegativeButton("앱 재시작") { _, _ ->
                this.context?.let { restart(it) }
            }
            .create()
    }

    private fun restart(context: Context) {
        val packageManager: PackageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent!!.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}
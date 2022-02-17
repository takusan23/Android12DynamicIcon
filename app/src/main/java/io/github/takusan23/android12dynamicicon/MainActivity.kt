package io.github.takusan23.android12dynamicicon

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    /** 切り替えスイッチ */
    private val themeIconSwitch by lazy { findViewById<SwitchMaterial>(R.id.enable_theme_icon) }

    /** デフォ ComponentName */
    private val defaultComponentName by lazy { ComponentName(packageName, "${packageName}.MainActivity") }

    /** テーマアイコンにした ComponentName */
    private val themeIconComponentName by lazy { ComponentName(packageName, "${packageName}.MainActivity_dynamic_icon") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 今の状態をスイッチに入れる
        themeIconSwitch.isChecked = !isDefaultIcon()
        // 切り替えたらアイコンも切り替える
        themeIconSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            setThemeIcon(isChecked)
        }

    }

    /** デフォアイコンの場合はtrue */
    private fun isDefaultIcon(): Boolean {
        return packageManager.getComponentEnabledSetting(defaultComponentName).let {
            it == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || it == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        }
    }

    /**
     * テーマアイコンを適用するか
     *
     * @param isEnable テーマアイコンならtrue、デフォアイコンならfase
     * */
    private fun setThemeIcon(isEnable: Boolean) {
        // Android 12 以降のみ
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            packageManager.setComponentEnabledSetting(
                if (isEnable) themeIconComponentName else defaultComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
            packageManager.setComponentEnabledSetting(
                if (!isEnable) themeIconComponentName else defaultComponentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
        }
    }
}
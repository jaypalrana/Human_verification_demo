

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import android.widget.TextView
import android.widget.Toolbar
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "BaseViewModel"

    var isLoading = ObservableBoolean(false)
    var isLoadingnew = ObservableBoolean(false)
    var loadingMsg = ObservableField("")
    var isNoDataFound = ObservableBoolean(false)

    var isSelectedLang = ObservableInt(0)

    val toastLiveData = MutableLiveData<String>()
    val snackBarLiveData = MutableLiveData<String>()
    val showNoInternet = MutableLiveData<Boolean>()
    var sentNotificationData = MutableLiveData<String>()



    fun showLoading(msg: String = "") {
        loadingMsg.set(msg)
        isLoading.set(true)
    }

    fun showLoadingPage() {
        isLoadingnew.set(true)
    }

    fun dismissLoadingPage() {
        isLoadingnew.set(false)
    }

    fun dismissLoading() {
        isLoading.set(false)
    }

    fun showNoDataFound() {
        this.isNoDataFound.set(true)
    }

    fun hideNoDataFound() {
        this.isNoDataFound.set(false)
    }

    fun showToast(msg: String) {
        toastLiveData.value = msg
    }

    fun showSnackBar(msg: String) {
        snackBarLiveData.value = msg
    }

    fun showNoInternet() {
        showNoInternet.value = true
    }


}
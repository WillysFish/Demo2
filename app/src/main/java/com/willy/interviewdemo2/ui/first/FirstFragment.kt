package com.willy.interviewdemo2.ui.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lufu.koudailove.utils.SettingPrefs
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.base.BaseApp
import com.willy.interviewdemo2.base.BaseFooterAdapter
import com.willy.interviewdemo2.base.BaseFragment
import com.willy.interviewdemo2.data.api.ErrorCode
import com.willy.interviewdemo2.databinding.FirstFragmentBinding
import com.willy.interviewdemo2.extension.hideKeyboard
import com.willy.interviewdemo2.extension.textChangesFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
class FirstFragment : BaseFragment() {

    private val viewModel: FirstViewModel by viewModel()
    private var binding: FirstFragmentBinding? = null
    private val usersAdapter by lazy { UsersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FirstFragmentBinding.bind(view)

        // 訂閱
        observe()
        // View 初始設定
        initView()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun initView() {
        binding?.apply {
            var isNewSearch = false

            with(recyclerView) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter =
                    usersAdapter.withLoadStateFooter(BaseFooterAdapter { usersAdapter.retry() })

                // Paging3 Error Handle
                usersAdapter.addLoadStateListener {
                    if (it.refresh is LoadState.Error) {
                        val e =
                            ErrorCode.findErrorByThrowable((it.refresh as LoadState.Error).error)
                        showSnackbar(e.msg)
                    }
                }
                // 載完資料調整位置到頂點
                usersAdapter.registerAdapterDataObserver(object :
                    RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        super.onItemRangeInserted(positionStart, itemCount)
                        if (isNewSearch) {
                            this@with.scrollToPosition(0)
                            isNewSearch = false
                        }
                    }
                })
            }

            // 填入記錄 keyword
            editText.setText(BaseApp.settingPrefs.getString(SettingPrefs.SET_KEY_KEYWORD))
            // 失焦收鍵盤
            editText.setOnFocusChangeListener { view, b ->
                if (!b) context?.hideKeyboard(view)
            }
            // 打字即時搜尋 (0.3 秒一次)
            editText.textChangesFlow()
                .debounce(300)
                .onEach {
                    if (!it.isNullOrEmpty()) {
                        isNewSearch = true
                        BaseApp.settingPrefs.setString(SettingPrefs.SET_KEY_KEYWORD, it.toString())
                        viewModel.searchUser(it.toString())
                    } else lifecycleScope.launch {
                        // 清空 Edit，清空 Data
                        usersAdapter.submitData(PagingData.empty())
                    }
                }
                .launchIn(lifecycleScope)
        }
    }


    private fun observe() {
        // 搜尋回傳
        viewModel.usersLiveData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                usersAdapter.submitData(it)
            }
        }
    }
}

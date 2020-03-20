package dev.vkgo.pagination.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.vkgo.pagination.R
import dev.vkgo.pagination.api.GithubService
import dev.vkgo.pagination.data.GithubRepository
import dev.vkgo.pagination.roomdb.GithubLocalCache
import dev.vkgo.pagination.roomdb.RepoDatabase
import dev.vkgo.pagination.ui.adapters.RepoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

private const val TAG = "MainActivity"
private const val LAST_SEARCH_QUERY = "dev.vkgo.pagination.LAST_SEARCH_QUERY"
private const val DEFAULT_QUERY = "Android"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: SearchViewModelFactory
    private lateinit var viewModel: SearchViewModel
    private val repoAdapter = RepoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = GithubService.create()
        val repository = GithubRepository(service, githubLocalCache(this))
        viewModelFactory = SearchViewModelFactory(repository)
        viewModel = viewModelFactory.create(SearchViewModel::class.java)

        setRecyclerView()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchRepo(query)
        initSearch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.laseQuery())
    }

    private fun setRecyclerView() {
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view.adapter = repoAdapter
//        setScrollListener()
        viewModel.repos.observe(this, Observer {
            showEmptyList(it.isEmpty())
            repoAdapter.submitList(it)
        })
        viewModel.networkError.observe(this, Observer {
            Toast.makeText(this, "\uD83D\uDE28 Error $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun showEmptyList(flag: Boolean) {
        if (flag) {
            txt_no_data.visibility = View.VISIBLE
            recycler_view.visibility = View.GONE
        } else {
            txt_no_data.visibility = View.GONE
            recycler_view.visibility = View.VISIBLE
        }
    }

    private fun initSearch(query: String) {
        search_repo.setText(query)
        search_repo.setSelection(query.length)

        search_repo.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                initSearch()
                hideKeyBoard(v)
            }
            false
        }

        search_repo.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                initSearch()
                hideKeyBoard(v)
            }
            false
        }
    }

    private fun initSearch() {
        search_repo.text?.trim().let {
            if (it!!.isNotEmpty()) {
                recycler_view.scrollToPosition(0)
                viewModel.searchRepo(it.toString())
                repoAdapter.submitList(null)
            }
        }
    }

//    private fun setScrollListener() {
//        val layoutManager = recycler_view.layoutManager as LinearLayoutManager
//        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val totalItemCount = layoutManager.itemCount
//                val visibleItemCount = layoutManager.childCount
//                val lastItem = layoutManager.findLastVisibleItemPosition()
//
//                viewModel.onListScroll(lastItem, visibleItemCount, totalItemCount)
//            }
//        })
//    }

    private fun githubLocalCache(context: Context): GithubLocalCache {
        val database = RepoDatabase.getInstance(context)
        return GithubLocalCache(database.repoDao(), Executors.newSingleThreadExecutor())
    }

    private fun hideKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

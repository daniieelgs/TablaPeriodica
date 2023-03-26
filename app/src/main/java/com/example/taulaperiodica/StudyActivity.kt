package com.example.taulaperiodica

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StudyActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var elements: List<Element>
    private lateinit var categoryList: List<String>
    private lateinit var phaseList: List<String>
    private lateinit var filterCategoryBy: ArrayList<String>
    private lateinit var filterPhaseBy: ArrayList<String>
    private lateinit var adapter: elementAdapter
    private lateinit var data: Data
    private var miFavorite: MenuItem? = null
    private var miOrderType: MenuItem? = null
    private var isFavorite: Boolean = false
        set(value){

            field = value

            miFavorite?.icon = getDrawable((if(isFavorite) R.drawable.ic_baseline_star_iluminated_24 else R.drawable.ic_baseline_star_outline_24))

            updateFilter()
        }
    private var orderBy: OrderFields = OrderFields.NUMBER
        set(value){
            field = value

            adapter.update(orderList())
        }

    private var orderAsc: Boolean = true
        set(value){
            field = value

            miOrderType?.icon = getDrawable((if(orderAsc) R.drawable.ic_baseline_order_asc_24 else R.drawable.ic_baseline_order_desc_24))

            adapter.update(adapter.getElementsList().reversed())

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        elements = intent.extras!!.getSerializable("elements") as List<Element>

        elements!!.forEach(::println)

        recyclerView = findViewById(R.id.rvElements)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = elementAdapter(elements, this)

        recyclerView.adapter = adapter


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0 && dy > 30) supportActionBar?.hide()
                else if(dy < 50 && dy < 0)supportActionBar?.show()


            }

        } )

        data = Data(this)
        categoryList = elements.map { (data.category_data[it.category] ?: data.category_data["unknown"])!!.text }.distinct()
        phaseList = elements.map { (data.phase_data[it.phase] ?: data.phase_data["unknown"])!!.text }.distinct()

        filterCategoryBy = ArrayList()
        filterPhaseBy = ArrayList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_actionbar_study, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.miSearch)

        val searchView: SearchView = MenuItemCompat.getActionView(searchItem) as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                search(p0)

                val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
//                searchView.setQuery(p0, false);
//                searchView.setIconified(true)
                return true;
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                search(p0)
                return true
            }

        })

        miFavorite = menu?.findItem(R.id.miFavorite)
        miOrderType = menu?.findItem(R.id.miOrderType)

        var menuCategory: SubMenu? = menu?.findItem(R.id.miCategory)?.subMenu
        var menuPhase: SubMenu? = menu?.findItem(R.id.miPhase)?.subMenu

        menuCategory?.setGroupCheckable(1, true, true)
        menuPhase?.setGroupCheckable(1, true, true)

        categoryList.forEachIndexed{i, n ->
            val item = menuCategory?.add(Menu.NONE, i, Menu.NONE, n)
            item?.isCheckable = true
        }

        phaseList.forEachIndexed{i, n ->
            val item = menuPhase?.add(Menu.NONE, categoryList.size + i, Menu.NONE, n)
            item?.isCheckable = true
        }

        when(orderBy){
            OrderFields.NUMBER -> menu?.findItem(R.id.miOrderNumber)
            OrderFields.SYMBOL -> menu?.findItem(R.id.miOrderNumber)
        }!!.isChecked = true

        return super.onCreateOptionsMenu(menu)
    }

    fun search(q: String?){

        val query = q?.trim()?.lowercase()

        if(query == null || query!!.trim().isEmpty()){
            updateFilter()
            return
        }

        updateFilter(elements.filter { it.name.lowercase().contains(query) || it.symbol.lowercase().contains(query) })

    }

    private fun filterByFavorite(elements: List<Element>) = elements.filter { it.favorite }
    private fun filterByCategory(elements: List<Element>) = elements.filter { filterCategoryBy.contains((data.category_data[it.category] ?: data.category_data["unknown"])!!.text)}
    private fun filterByPhase(elements: List<Element>) = elements.filter { filterPhaseBy.contains((data.phase_data[it.phase] ?: data.phase_data["unknown"])!!.text) }

    fun updateFilter(elements: List<Element> = this.elements){

        var elementsFilter = if(isFavorite) filterByFavorite(elements) else elements

        elementsFilter = if(filterCategoryBy.isNotEmpty()) filterByCategory(elementsFilter) else elementsFilter

        elementsFilter = if(filterPhaseBy.isNotEmpty()) filterByPhase(elementsFilter) else elementsFilter

        adapter.update(orderList(elementsFilter))
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId < categoryList.size){
            item.isChecked = !item.isChecked

            if(item.isChecked) filterCategoryBy.add(categoryList[item.itemId])
            else filterCategoryBy.remove(categoryList[item.itemId])

            println(filterCategoryBy)

            updateFilter()

        }else if(item.itemId >= categoryList.size && item.itemId < (categoryList.size + phaseList.size)){

            item.isChecked = !item.isChecked

            if(item.isChecked) filterPhaseBy.add(phaseList[item.itemId - categoryList.size])
            else filterPhaseBy.remove(phaseList[item.itemId - categoryList.size])

            println(filterPhaseBy)

            updateFilter()

        }else{

            when(item.itemId){

                R.id.miFavorite -> isFavorite = !isFavorite
                R.id.miOrderSymbol -> {
                    orderBy = OrderFields.SYMBOL
                    item.isChecked = true
                }
                R.id.miOrderNumber -> {
                    orderBy = OrderFields.NUMBER
                    item.isChecked = true
                }
                R.id.miOrderType -> orderAsc = !orderAsc
                R.id.miInfo -> createInfoDialog().show()
                android.R.id.home -> finish()

            }

        }

        return super.onOptionsItemSelected(item)
    }


    private fun createInfoDialog(): androidx.appcompat.app.AlertDialog {

        var builder = MaterialAlertDialogBuilder(this)

        var inflater: LayoutInflater = layoutInflater

        var v: View = inflater.inflate(R.layout.info_dialog, null)

        builder.setView(v)

        builder.setPositiveButton(getString(R.string.close_button)){_, _ ->}

        val dialog = builder.create()

        return dialog
    }

    fun orderList(list: List<Element> = elements): List<Element>{

        var listShow = when(orderBy){
            OrderFields.NUMBER -> list.sortedBy { it.number }
            OrderFields.SYMBOL -> list.sortedBy { it.symbol }
            else -> list
        }

        return if(orderAsc) listShow else listShow.reversed()
    }

    override fun finish() {
        val intent = Intent()
        intent.putExtra("elements", elements as java.io.Serializable)
        setResult(RESULT_OK, intent)
        super.finish()
    }

    private enum class OrderFields {
        SYMBOL, NUMBER
    }
}
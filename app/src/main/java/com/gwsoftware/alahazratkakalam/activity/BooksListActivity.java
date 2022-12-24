package com.gwsoftware.alahazratkakalam.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.ads.AdView;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.adapter.BooksCategoriesAdapter;
import com.gwsoftware.alahazratkakalam.adapter.BooksRecyclerVierAdapter;
import com.gwsoftware.alahazratkakalam.interfaces.recyclerViewCallback;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.GridSpacingItemDecoration;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BooksListActivity extends AppCompatActivity implements recyclerViewCallback, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    RecyclerView booksListRecyclerView;
    RecyclerView booksCategoriesRecyclerView;
    BooksRecyclerVierAdapter booksRecyclerVierAdapter;
    BooksCategoriesAdapter booksCategoriesAdapter;
    AhApplication ahApplication;
    private ArrayList<DataObjectModel.Pdf> filesList;
    ArrayList<String> categories = new ArrayList<>();
    private HashMap<String, ArrayList<DataObjectModel.Pdf>> categoriesList;
    AdView adView;
    String[] spinnerItems = new String[]{
            "Urdu",
            "English"
    };
    Spinner spinner = null;
    String selectedPdfCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);
        ahApplication = AhApplication.getInstance();
        filesList = new ArrayList<>();
        booksListRecyclerView = findViewById(R.id.books_list_recycler_view);
        booksCategoriesRecyclerView = findViewById(R.id.books_categories_recycler_view);
        adView = findViewById(R.id.adView);
        //Utils.loadAdView(this, adView);

        selectedPdfCategory = getIntent().getStringExtra("category");
        if (selectedCategory == null) {
            selectedCategory = "books";
        }

        initializeBooksRecyclerView();
        initializeCategoriesRecyclerView();
    }

    private void initializeBooksRecyclerView() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(BooksListActivity.this);
        booksListRecyclerView.setLayoutManager(gridLayoutManager);
        booksRecyclerVierAdapter = new BooksRecyclerVierAdapter(BooksListActivity.this, new ArrayList<DataObjectModel.Pdf>());
        booksListRecyclerView.setAdapter(booksRecyclerVierAdapter);
    }

    private void initializeCategoriesRecyclerView() {
        //LinearLayoutManager gridLayoutManager = new LinearLayoutManager(BooksListActivity.this);
        // booksCategoriesRecyclerView.setLayoutManager(gridLayoutManager);
        //AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(BooksListActivity.this, 500);
        //booksCategoriesRecyclerView.setLayoutManager(layoutManager);
        int spacing = 10; // 50px
        boolean includeEdge = true;
        booksCategoriesRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        booksCategoriesRecyclerView.setLayoutManager(gridLayoutManager);

        booksCategoriesAdapter = new BooksCategoriesAdapter(BooksListActivity.this, getCategories());
        booksCategoriesRecyclerView.setAdapter(booksCategoriesAdapter);
    }

    /*private ArrayList<DataObjectModel.Pdf> getFilesList() {
        if (ahApplication.getDataObjectModel() != null && ahApplication.getDataObjectModel().getPdfs() != null) {
            filesList = ahApplication.getDataObjectModel().getPdfs();
        }
        return filesList;
    }*/

    private ArrayList<String> getCategories() {
        if (ahApplication.getDataObjectModel() != null && ahApplication.getDataObjectModel().getPdf() != null) {
            categoriesList = ahApplication.getDataObjectModel().getPdf();
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        for (String str : categoriesList.keySet()) {
            if (selectedPdfCategory.equalsIgnoreCase("more")) {
                arrayList.addAll(getMoreArray());
                break;
            } else if (selectedPdfCategory.equalsIgnoreCase("books") && !getMoreArray().toString().toLowerCase().contains(str.toLowerCase())) {
                arrayList.add(str);
            }
        }
        Collections.sort(arrayList);
        categories = arrayList;
        return arrayList;
    }

    private ArrayList<String> getMoreArray() {
        ArrayList<String> moreArray = new ArrayList<>();
        moreArray.add("Calendar");
        moreArray.add("Surah Yaseen");
        return moreArray;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem spinnerItem = menu.findItem(R.id.spinner);
        SearchManager searchManager = (SearchManager) BooksListActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(BooksListActivity.this.getComponentName()));
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    if (booksRecyclerVierAdapter != null && booksListRecyclerView.getVisibility() == View.VISIBLE) {
                        booksRecyclerVierAdapter.updateSearchResult(searchFiles(s));
                    } else if (booksRecyclerVierAdapter != null && booksCategoriesRecyclerView.getVisibility() == View.VISIBLE) {
                        booksCategoriesAdapter.updateSearchResult(searchCategories(s));
                    }

                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
            if (booksListRecyclerView.getVisibility() == View.VISIBLE) {
                spinnerItem.setVisible(true);
            } else {
                spinnerItem.setVisible(false);
            }
        }


        spinner = (Spinner) spinnerItem.getActionView();

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(0);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        return super.onCreateOptionsMenu(menu);
    }

    private ArrayList<DataObjectModel.Pdf> searchFiles(String searchText) {
        if (searchText != null && searchText.trim().length() > 0) {
            ArrayList<DataObjectModel.Pdf> searchResult = new ArrayList();
            for (DataObjectModel.Pdf pdf : filesList) {
                if (pdf.getPdf_name().toLowerCase().contains(searchText.toLowerCase())) {
                    searchResult.add(pdf);
                }
            }
            return searchResult;

        } else {
            return filesList;
        }

    }

    private ArrayList<String> searchCategories(String searchText) {
        if (searchText != null && searchText.trim().length() > 0 && categories != null) {
            ArrayList<String> searchResult = new ArrayList();
            for (String pdf : categories) {
                if (pdf.toLowerCase().contains(searchText.toLowerCase())) {
                    searchResult.add(pdf);
                }
            }
            return searchResult;

        } else {
            return categories;
        }

    }

    String selectedCategory;


    @Override
    public void recyclerViewItemClicked(int position, View view) {
        invalidateOptionsMenu();
        if (spinner != null) {
            spinner.setSelection(0);
        }
        spinnerSelectedItem = spinnerItems[0];
        selectedCategory = (String) view.getTag();
        HashMap<String, ArrayList<DataObjectModel.Pdf>> map = ahApplication.getDataObjectModel().getPdf();
        if (map != null && map.size() > 0) {
            booksCategoriesRecyclerView.setVisibility(View.GONE);
            booksListRecyclerView.setVisibility(View.VISIBLE);
            ArrayList<DataObjectModel.Pdf> booksList;
            booksList = map.get(view.getTag());
            if (booksList == null) {
                booksList = map.get(selectedCategory.toString().substring(0, 1).toLowerCase() + selectedCategory.toString().substring(1));
                if (booksList == null) {
                    booksList = map.get(selectedCategory.toString().substring(0, 1).toUpperCase() + selectedCategory.toString().substring(1));
                }
            }
            ArrayList<DataObjectModel.Pdf> sortedBooksByLanguage = sortItemsByLanguage(booksList);
            filesList = sortedBooksByLanguage;
            if (sortedBooksByLanguage != null && sortedBooksByLanguage.size() > 0) {
                if (booksRecyclerVierAdapter != null) {
                    booksRecyclerVierAdapter.updateSearchResult(sortedBooksByLanguage);
                }

            } else {
                Utils.showAlert(this, "No Books Found", null, false, "Ok");
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            invalidateOptionsMenu();
            if (booksListRecyclerView.getVisibility() == View.VISIBLE) {
                booksListRecyclerView.setVisibility(View.GONE);
                booksCategoriesRecyclerView.setVisibility(View.VISIBLE);
            } else {
                finish();
            }

        }
        return true;

    }


    String spinnerSelectedItem;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSelectedItem = spinnerItems[position];
        HashMap<String, ArrayList<DataObjectModel.Pdf>> map = ahApplication.getDataObjectModel().getPdf();
        if (map != null && map.size() > 0) {
            ArrayList<DataObjectModel.Pdf> booksList = map.get(selectedCategory);
            if (booksList == null) {
                booksList = map.get(selectedCategory.toString().substring(0, 1).toLowerCase() + selectedCategory.toString().substring(1));
                if (booksList == null) {
                    booksList = map.get(selectedCategory.toString().substring(0, 1).toUpperCase() + selectedCategory.toString().substring(1));
                }
            }
            ArrayList<DataObjectModel.Pdf> sortedBooksByLanguage = sortItemsByLanguage(booksList);
            filesList = sortedBooksByLanguage;
            if (booksRecyclerVierAdapter != null) {
                booksRecyclerVierAdapter.updateSearchResult(sortedBooksByLanguage);
            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private ArrayList<DataObjectModel.Pdf> sortItemsByLanguage(ArrayList<DataObjectModel.Pdf> booksList) {
        ArrayList<DataObjectModel.Pdf> sortedBooksList = new ArrayList<>();
        for (DataObjectModel.Pdf book : booksList) {
            if (book.getLanguage().toLowerCase().equalsIgnoreCase(spinnerSelectedItem) || book.getLanguage().toLowerCase().contains(spinnerSelectedItem.toLowerCase())) {
                sortedBooksList.add(book);
            }
        }
        return sortedBooksList;

    }
}

package com.example.todoapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.util.logMe
import com.example.todoapp.view.home.ListFragmentDirections
import com.example.todoapp.viewmodel.ToDoViewModel

class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<ToDoViewModel>()
    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }
    private var menu: Menu? = null
    var showFab: Boolean = true
        set(value) {
            binding.fabAdd.isVisible = value
            field = value
        }
    var showProgress: Boolean = true
        set(value) {
            binding.progress.isVisible = value
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        // Splash Screen Set Up Before setting Content View
        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen().setKeepVisibleCondition {
            // This will keep the splash screen up until the starting point is calculated
            // based on users logged in status provided by datastore in view model
            viewModel.savedToken.value == null
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.main_menu, menu)
        hideNonAuthViews(viewModel.userLoggedIn)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean = with(navHostFragment.navController) {
        navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(binding.toolbar)
        showProgress = false

        fabAdd.setOnClickListener {
            navHostFragment.navController.navigate(ListFragmentDirections.goToCompose())
        }

        navHostFragment.navController.addOnDestinationChangedListener {
                _, destination, _ ->
            hideNonAuthViews(viewModel.userLoggedIn)
            showFab = destination.id == R.id.home_destination
        }
    }

    private fun initObservers() = with(viewModel) {
        lifecycleScope.launchWhenCreated {
            savedToken.observe(this@MainActivity) {
                if (it != null) navHostFragment.navController.apply {
                    graph = navInflater.inflate(R.navigation.auth_graph).apply {
                        startDestination =
                            if (it.token != "") R.id.nav_graph
                            else R.id.login_destination
                    }
                    binding.navView.setupWithNavController(this)
                    appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.login_destination, R.id.register_destination,
                            R.id.home_destination, R.id.profile_destination
                        )
                    )
                    setupActionBarWithNavController(this, appBarConfiguration)
                }
            }
        }
    }

    private fun hideNonAuthViews(hide: Boolean) = with(binding) {
        "$hide".logMe()
        menu?.findItem(R.id.action_logout)?.isVisible = hide
        fabAdd.isVisible = hide
        navView.isVisible = hide
    }

}
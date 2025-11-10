package com.theapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class AdminActivity : AppCompatActivity() {
    
    private lateinit var loginLayout: LinearLayout
    private lateinit var dashboardLayout: LinearLayout
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var loginError: TextView
    
    private lateinit var statsLayout: LinearLayout
    private lateinit var totalUsersText: TextView
    private lateinit var paidUsersText: TextView
    private lateinit var completedTestsText: TextView
    private lateinit var pendingPaymentsText: TextView
    private lateinit var totalRevenueText: TextView
    
    private lateinit var tabLayout: LinearLayout
    private lateinit var paymentsTab: Button
    private lateinit var usersTab: Button
    private lateinit var resultsTab: Button
    
    private lateinit var contentLayout: FrameLayout
    private lateinit var paymentsRecyclerView: RecyclerView
    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var resultsRecyclerView: RecyclerView
    
    private lateinit var logoutButton: Button
    
    private var adminToken: String? = null
    private val API_URL = "http://192.168.1.4:3001/api" // Change to your server IP
    
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        
        initViews()
        setupListeners()
        
        // Check if already logged in
        adminToken = getSharedPreferences("admin_prefs", MODE_PRIVATE)
            .getString("admin_token", null)
        
        if (adminToken != null) {
            showDashboard()
        }
    }
    
    private fun initViews() {
        // Login views
        loginLayout = findViewById(R.id.loginLayout)
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        loginError = findViewById(R.id.loginError)
        
        // Dashboard views
        dashboardLayout = findViewById(R.id.dashboardLayout)
        
        // Stats
        totalUsersText = findViewById(R.id.totalUsersText)
        paidUsersText = findViewById(R.id.paidUsersText)
        completedTestsText = findViewById(R.id.completedTestsText)
        pendingPaymentsText = findViewById(R.id.pendingPaymentsText)
        totalRevenueText = findViewById(R.id.totalRevenueText)
        
        // Tabs
        paymentsTab = findViewById(R.id.paymentsTab)
        usersTab = findViewById(R.id.usersTab)
        resultsTab = findViewById(R.id.resultsTab)
        
        // Content
        contentLayout = findViewById(R.id.contentLayout)
        paymentsRecyclerView = findViewById(R.id.paymentsRecyclerView)
        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView)
        
        logoutButton = findViewById(R.id.logoutButton)
        
        // Setup RecyclerViews
        paymentsRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    
    private fun setupListeners() {
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            
            if (username.isEmpty() || password.isEmpty()) {
                loginError.text = "Please enter username and password"
                loginError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            
            login(username, password)
        }
        
        logoutButton.setOnClickListener {
            logout()
        }
        
        paymentsTab.setOnClickListener {
            switchTab("payments")
        }
        
        usersTab.setOnClickListener {
            switchTab("users")
        }
        
        resultsTab.setOnClickListener {
            switchTab("results")
        }
    }
    
    private fun login(username: String, password: String) {
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val json = JSONObject()
                    json.put("username", username)
                    json.put("password", password)
                    
                    makeRequest("POST", "$API_URL/admin/login", json.toString())
                }
                
                val response = JSONObject(result)
                
                if (response.getBoolean("success")) {
                    adminToken = response.getString("token")
                    
                    // Save token
                    getSharedPreferences("admin_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("admin_token", adminToken)
                        .putString("admin_username", username)
                        .apply()
                    
                    showDashboard()
                } else {
                    loginError.text = response.optString("error", "Login failed")
                    loginError.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                loginError.text = "Server error: ${e.message}"
                loginError.visibility = View.VISIBLE
            }
        }
    }
    
    private fun showDashboard() {
        loginLayout.visibility = View.GONE
        dashboardLayout.visibility = View.VISIBLE
        
        loadStats()
        switchTab("payments")
    }
    
    private fun logout() {
        getSharedPreferences("admin_prefs", MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
        
        adminToken = null
        loginLayout.visibility = View.VISIBLE
        dashboardLayout.visibility = View.GONE
        usernameInput.text.clear()
        passwordInput.text.clear()
    }
    
    private fun loadStats() {
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    makeAuthRequest("GET", "$API_URL/admin/stats")
                }
                
                val response = JSONObject(result)
                
                if (response.getBoolean("success")) {
                    val stats = response.getJSONObject("stats")
                    
                    totalUsersText.text = stats.getInt("totalUsers").toString()
                    paidUsersText.text = stats.getInt("paidUsers").toString()
                    completedTestsText.text = stats.getInt("completedTests").toString()
                    pendingPaymentsText.text = stats.getInt("pendingPayments").toString()
                    totalRevenueText.text = "₹${stats.getInt("totalRevenue")}"
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Error loading stats", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun switchTab(tab: String) {
        // Reset tab colors
        paymentsTab.setBackgroundColor(getColor(android.R.color.transparent))
        usersTab.setBackgroundColor(getColor(android.R.color.transparent))
        resultsTab.setBackgroundColor(getColor(android.R.color.transparent))
        
        // Hide all RecyclerViews
        paymentsRecyclerView.visibility = View.GONE
        usersRecyclerView.visibility = View.GONE
        resultsRecyclerView.visibility = View.GONE
        
        when (tab) {
            "payments" -> {
                paymentsTab.setBackgroundColor(getColor(R.color.purple_500))
                paymentsRecyclerView.visibility = View.VISIBLE
                loadPendingPayments()
            }
            "users" -> {
                usersTab.setBackgroundColor(getColor(R.color.purple_500))
                usersRecyclerView.visibility = View.VISIBLE
                loadUsers()
            }
            "results" -> {
                resultsTab.setBackgroundColor(getColor(R.color.purple_500))
                resultsRecyclerView.visibility = View.VISIBLE
                loadResults()
            }
        }
    }
    
    private fun loadPendingPayments() {
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    makeAuthRequest("GET", "$API_URL/admin/payments/pending")
                }
                
                val response = JSONObject(result)
                
                if (response.getBoolean("success")) {
                    val payments = response.getJSONArray("payments")
                    val paymentsList = mutableListOf<Payment>()
                    
                    for (i in 0 until payments.length()) {
                        val p = payments.getJSONObject(i)
                        paymentsList.add(Payment(
                            id = p.getString("id"),
                            email = p.getString("email"),
                            plan = p.getString("plan"),
                            amount = p.getInt("amount"),
                            submittedAt = p.getString("submittedAt")
                        ))
                    }
                    
                    paymentsRecyclerView.adapter = PaymentsAdapter(paymentsList) { payment, action ->
                        if (action == "approve") {
                            approvePayment(payment.id)
                        } else {
                            rejectPayment(payment.id)
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Error loading payments", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun approvePayment(paymentId: String) {
        scope.launch {
            try {
                val json = JSONObject()
                json.put("paymentId", paymentId)
                
                val result = withContext(Dispatchers.IO) {
                    makeAuthRequest("POST", "$API_URL/admin/payments/approve", json.toString())
                }
                
                val response = JSONObject(result)
                
                if (response.getBoolean("success")) {
                    Toast.makeText(this@AdminActivity, "Payment approved!", Toast.LENGTH_SHORT).show()
                    loadStats()
                    loadPendingPayments()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Error approving payment", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun rejectPayment(paymentId: String) {
        scope.launch {
            try {
                val json = JSONObject()
                json.put("paymentId", paymentId)
                json.put("reason", "Rejected by admin")
                
                val result = withContext(Dispatchers.IO) {
                    makeAuthRequest("POST", "$API_URL/admin/payments/reject", json.toString())
                }
                
                val response = JSONObject(result)
                
                if (response.getBoolean("success")) {
                    Toast.makeText(this@AdminActivity, "Payment rejected", Toast.LENGTH_SHORT).show()
                    loadStats()
                    loadPendingPayments()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Error rejecting payment", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadUsers() {
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    makeAuthRequest("GET", "$API_URL/admin/users")
                }
                
                val response = JSONObject(result)
                
                if (response.getBoolean("success")) {
                    val users = response.getJSONArray("users")
                    val usersList = mutableListOf<User>()
                    
                    for (i in 0 until users.length()) {
                        val u = users.getJSONObject(i)
                        usersList.add(User(
                            email = u.getString("email"),
                            name = u.optString("name", "N/A"),
                            isPaid = u.getBoolean("isPaid"),
                            testCompleted = u.getBoolean("testCompleted"),
                            createdAt = u.getString("createdAt")
                        ))
                    }
                    
                    usersRecyclerView.adapter = UsersAdapter(usersList)
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Error loading users", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadResults() {
        scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    makeAuthRequest("GET", "$API_URL/admin/results")
                }
                
                val response = JSONObject(result)
                
                if (response.getBoolean("success")) {
                    val results = response.getJSONArray("results")
                    val resultsList = mutableListOf<TestResult>()
                    
                    for (i in 0 until results.length()) {
                        val r = results.getJSONObject(i)
                        resultsList.add(TestResult(
                            email = r.getString("email"),
                            personalityType = r.optString("personalityType", "N/A"),
                            completedAt = r.getString("completedAt")
                        ))
                    }
                    
                    resultsRecyclerView.adapter = ResultsAdapter(resultsList)
                }
            } catch (e: Exception) {
                Toast.makeText(this@AdminActivity, "Error loading results", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun makeRequest(method: String, urlString: String, body: String? = null): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.requestMethod = method
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            if (body != null && method == "POST") {
                connection.doOutput = true
                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(body)
                writer.flush()
                writer.close()
            }
            
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            
            reader.close()
            return response.toString()
        } finally {
            connection.disconnect()
        }
    }
    
    private fun makeAuthRequest(method: String, urlString: String, body: String? = null): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        
        try {
            connection.requestMethod = method
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $adminToken")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            if (body != null && method == "POST") {
                connection.doOutput = true
                val writer = OutputStreamWriter(connection.outputStream)
                writer.write(body)
                writer.flush()
                writer.close()
            }
            
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            
            reader.close()
            return response.toString()
        } finally {
            connection.disconnect()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
    
    // Data classes
    data class Payment(
        val id: String,
        val email: String,
        val plan: String,
        val amount: Int,
        val submittedAt: String
    )
    
    data class User(
        val email: String,
        val name: String,
        val isPaid: Boolean,
        val testCompleted: Boolean,
        val createdAt: String
    )
    
    data class TestResult(
        val email: String,
        val personalityType: String,
        val completedAt: String
    )
    
    // Adapters
    inner class PaymentsAdapter(
        private val payments: List<Payment>,
        private val onAction: (Payment, String) -> Unit
    ) : RecyclerView.Adapter<PaymentsAdapter.ViewHolder>() {
        
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val emailText: TextView = view.findViewById(R.id.emailText)
            val planText: TextView = view.findViewById(R.id.planText)
            val amountText: TextView = view.findViewById(R.id.amountText)
            val approveButton: Button = view.findViewById(R.id.approveButton)
            val rejectButton: Button = view.findViewById(R.id.rejectButton)
        }
        
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_payment, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val payment = payments[position]
            holder.emailText.text = payment.email
            holder.planText.text = payment.plan
            holder.amountText.text = "₹${payment.amount}"
            
            holder.approveButton.setOnClickListener {
                onAction(payment, "approve")
            }
            
            holder.rejectButton.setOnClickListener {
                onAction(payment, "reject")
            }
        }
        
        override fun getItemCount() = payments.size
    }
    
    inner class UsersAdapter(private val users: List<User>) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
        
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val emailText: TextView = view.findViewById(R.id.emailText)
            val nameText: TextView = view.findViewById(R.id.nameText)
            val statusText: TextView = view.findViewById(R.id.statusText)
        }
        
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user = users[position]
            holder.emailText.text = user.email
            holder.nameText.text = user.name
            holder.statusText.text = if (user.isPaid) "Paid" else "Free"
        }
        
        override fun getItemCount() = users.size
    }
    
    inner class ResultsAdapter(private val results: List<TestResult>) : RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {
        
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val emailText: TextView = view.findViewById(R.id.emailText)
            val personalityText: TextView = view.findViewById(R.id.personalityText)
        }
        
        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_result, parent, false)
            return ViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val result = results[position]
            holder.emailText.text = result.email
            holder.personalityText.text = result.personalityType
        }
        
        override fun getItemCount() = results.size
    }
}

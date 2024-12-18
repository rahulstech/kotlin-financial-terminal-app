import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Scanner

class FinBookApp {
    private val FORMATTER = DateTimeFormatter.ISO_DATE
    private val input: Scanner = Scanner(System.`in`)
    private val inexDao = IncomeExpenseDao()

    fun start() {
        var choice: Int?
        do {
            println("========= [ FinBook ] =========")
            println("1. Add Income/Expense")
            println("2. Show Income/Expense")
            println("3. Edit Income/Expense")
            println("4. Delete Income/Expense")
            println("5. Create Report")
            println("0. Exit");

            print(">>> ")
            choice = input.nextInt()
            input.nextLine()
            when (choice) {
                0 -> {
                    println("Good Bye")
                    break
                }
                1 -> addIncomeExpense()
                2 -> showIncomeExpense()
                3 -> editIncomeExpense()
                4 -> deleteIncomeExpense()
                5 -> makeReport()
            }
        }
        while (true)
    }

    private fun  printHeader(title: String) {
        println("--------- ( $title ) ---------")
    }

    private fun addIncomeExpense() {
        printHeader("Add Income Expense")
        print("I - Income | E - Expense : ")
        val typeChoice = input.nextLine()
        print("Amount: ")
        val amount = input.nextFloat()
        input.nextLine()
        print("Date (yyyy-mm-dd): ")
        val dateString = input.nextLine()
        print("Note: ")
        val note = input.nextLine();
        val date = LocalDate.parse(dateString, FORMATTER)
        val type = if ("I" == typeChoice ) Type.INCOME else Type.EXPENSE
        val inex = IncomeExpense(type, amount, date, note)
        println()
        if (inexDao.save(inex)) {
            println("new record saved")
        }
        else {
            println("new record not saved")
        }
    }

    private fun showIncomeExpense() {
        printHeader("Show income expense")
        println("1. Show income between dates")
        println("2. Show all income")
        println("3. Show expense between dates")
        println("4. Show all expense")
        println("5. Show income and expense between dates")
        println("6. Show all income and expense")
        println("0. Exit Menu")
        print(">>> ")
        val choice = input.nextInt()
        input.nextLine()
        when (choice) {
            1 -> showIncomeExpenseBetweenDates(Type.INCOME)
            2 -> showAllIncomeExpense(Type.INCOME)
            3 -> showIncomeExpenseBetweenDates(Type.EXPENSE)
            4 -> showAllIncomeExpense(Type.EXPENSE)
            5 -> showIncomeExpenseBetweenDates()
            6 -> showAllIncomeExpense()
        }
    }

    private fun showIncomeExpenseBetweenDates(type: Type? = null) {
        print("Start Date (yyyy-mm-dd): ")
        val startDateString = input.nextLine()
        print("End Date (yyyy-mm-dd): ")
        val endDateString = input.nextLine()

        val start = LocalDate.parse(startDateString, FORMATTER)
        val end = LocalDate.parse(endDateString, FORMATTER)
        val entries = inexDao.getAllBetween(start,end,type)
        printIncomeExpenseList(entries, 0, 50)
        println()
    }

    private fun showAllIncomeExpense(type: Type? = null) {
        val entries = inexDao.getAll(type)
        printIncomeExpenseList(entries, 0, 50)
        println()
    }

    private fun printIncomeExpenseList(list: List<IncomeExpense>, start: Int, len: Int) {
        val size = list.size
        if (size == 0) {
            return
        }
        val end = start + len - 1
        var x = start

        println("\nShowing results from ${start+1} to ${end+1}\n")
        while (x <= end && x < size ) {
            val inex = list.get(x)
            println("[${x+1}] $inex ")
            x++
        }
        if (x >= size) {
            return
        }
        println("p - previous | n - next | q - quit")
        print(">>> ")
        val choice = input.nextLine()
        when (choice) {
            "p" -> printIncomeExpenseList(list, start-len, len)
            "n" -> printIncomeExpenseList(list, end+1, len)
        }
    }

    private fun editIncomeExpense() {
        printHeader("Edit Income Expense")
        print("Enter Id: ")
        val id = input.nextLine()
        val inex = inexDao.getById(id)
        if (inex == null) {
            println()
            println("no record found")
        }
        else {
            println()
            print("Press enter key if no change")
            println()
            println("Amount: ${inex.amount}")
            print("Amount: ")
            if (input.hasNextFloat()) {
                val amount = input.nextFloat()
                input.nextLine()
                inex.amount = amount
            }
            println("Date: ${inex.date.format(FORMATTER)}")
            print("Date (yyyy-mm-dd) : ")
            val dateString = input.nextLine()
            if (dateString != "") {
                val date = LocalDate.parse(dateString, FORMATTER)
                inex.date = date
            }
            println("Node: ${inex.note}")
            print("Note: ")
            inex.note = input.nextLine()

            println()
            println("changes saved")
            println()
        }
    }

    private fun deleteIncomeExpense() {
        printHeader("Delete Income Expense")
        print("Enter Id: ")
        val id = input.nextLine()
        val inex = inexDao.getById(id)
        if (inex == null) {
            println()
            println("no record found")
        }
        else {
            inexDao.remove(inex)
            println()
            println("record deleted")
            println()
        }
    }

    private fun makeReport() {
        println("showing report")
    }
}

fun main() {
    FinBookApp().start()
}
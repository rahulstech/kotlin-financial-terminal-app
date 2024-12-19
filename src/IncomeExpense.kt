import java.time.LocalDate
import java.time.Month
import java.util.UUID

enum class IncomeExpenseType {
    INCOME, EXPENSE
}

//sealed class IncomeExpense(
//    open var id: String,
//    var type: IncomeExpenseType,
//    open var amount: Float,
//    open var date: LocalDate,
//    open var note: String?
//) {
//
//    data class Income(
//        override var amount: Float,override var date: LocalDate,override var note: String? = null,
//        override var id: String = UUID.randomUUID().toString()
//    ) : IncomeExpense(id, IncomeExpenseType.INCOME, amount, date, note)
//
//    data class Expense(
//        override var amount: Float, override var date: LocalDate,override var note: String? = null,
//        override var id: String = UUID.randomUUID().toString()
//    ) : IncomeExpense(id, IncomeExpenseType.EXPENSE, amount, date, note)
//
//    override fun toString(): String {
//        return "$type id: $id amount: $amount date: $date note: $note"
//    }
//}

data class IncomeExpense(
    var type: IncomeExpenseType, var amount: Float,  var date: LocalDate,  var note: String? = null,
    val id: String = UUID.randomUUID().toString()) {

    override fun toString(): String {
        return "$type id: $id date: $date amount: ${amount} note: ${note} "
    }
}

class IncomeExpenseDao {
    private val entries: MutableMap<String, IncomeExpense> = mutableMapOf()

    fun save(data: IncomeExpense): Boolean {
        entries[data.id] = data
        return true;
    }

    fun getAll(type: IncomeExpenseType?): List<IncomeExpense> {
        if (type == null) {
            return entries.values.toList()
        }
        return entries.filter {
            type == it.value.type
        }.values.toList()
    }

    fun getAllBetween(start: LocalDate, end: LocalDate, type: IncomeExpenseType?): List<IncomeExpense> {
        return entries.filterValues {
            isDateInRange(it.date, start, end) && (null == type || type == it.type)
        }.values.toList()
    }

    fun getById(id: String): IncomeExpense? = entries[id]

    fun remove(data: IncomeExpense) = entries.remove(data.id)

    fun createMonthlyReportByType(start: LocalDate, end: LocalDate, type: IncomeExpenseType): Map<LocalDate, Float> {
        val map = entries.filterValues {
            type == it.type && isDateInRange(it.date, start, end)
        }
        val report = mutableMapOf<LocalDate, Float>()
        for (entry in map) {
            val date = entry.value.date.withDayOfMonth(1)
            val amount = entry.value.amount
            val sum = report.computeIfAbsent(date) { 0f }
            report[date] = sum + amount
        }
        return report
    }

    private fun isDateInRange(date: LocalDate, start: LocalDate, end: LocalDate): Boolean =
        date.isEqual(start) || date.isEqual(end) || (date.isAfter(start) && date.isBefore(end))
}

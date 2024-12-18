import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

enum class Type {
    INCOME, EXPENSE
}

data class IncomeExpense(
                          var type: Type, var amount: Float,  var date: LocalDate,  var note: String? = null,
                          val id: String = UUID.randomUUID().toString()) {

    override fun toString(): String {
        return "$type id: $id date: ${date.format(DateTimeFormatter.ISO_DATE)} amount: ${amount} note: ${note} "
    }
}

class IncomeExpenseDao() {
    private val entries: MutableList<IncomeExpense> = mutableListOf()

    fun save(data: IncomeExpense): Boolean {
        return entries.add(data)
    }

    fun getAll(type: Type?) : List<IncomeExpense> {
        if (type == null) {
            return entries
        }
        return entries.filter {
            type == it.type
        }
    }

    fun getAllBetween(start: LocalDate, end: LocalDate, type: Type?): List<IncomeExpense> {
        return entries.filter {
            val date = it.date
            date.isEqual(start) || date.isEqual(end) || (date.isAfter(start) && date.isBefore(end))
        }.filter {
            null == type || type == it.type
        }
    }

    fun getById(id: String): IncomeExpense? {
        return entries.find {
            it.id == id
        }
    }

    fun remove(data: IncomeExpense) {
        entries.remove(data)
    }

}

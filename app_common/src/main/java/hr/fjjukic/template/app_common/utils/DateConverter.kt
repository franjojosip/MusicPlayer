package hr.fjjukic.template.app_common.utils

import android.annotation.SuppressLint
import hr.fjjukic.template.app_common.R
import hr.fjjukic.template.app_common.repository.ResourceRepository
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


object DateConverter {
    @SuppressLint("SimpleDateFormat")
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.M.yyyy.")
    private val serverTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz")

    fun getLastUpdatedDate(date: String, resourceRepository: ResourceRepository): String {
        val lastUpdated = Instant.parse(date).atZone(ZoneOffset.UTC)
        val currentDate = Instant.now().atZone(ZoneOffset.UTC)

        val elapsedTime = Duration.between(lastUpdated, currentDate)

        return when {
            lastUpdated.isAfter(currentDate.minusMinutes(1)) -> {
                resourceRepository.getString(R.string.just_now)
            }
            lastUpdated.isAfter(currentDate.minusMinutes(60)) -> {
                getLastUpdatedText(
                    resourceRepository,
                    R.plurals.quantity_minutes,
                    elapsedTime.toMinutes().toInt()
                )
            }
            lastUpdated.isAfter(currentDate.minusHours(24)) -> {
                getLastUpdatedText(
                    resourceRepository,
                    R.plurals.quantity_hours,
                    elapsedTime.toHours().toInt()
                )
            }
            else -> {
                getLastUpdatedText(
                    resourceRepository,
                    R.plurals.quantity_days,
                    elapsedTime.toDays().toInt()
                )
            }
        }
    }

    private fun getLastUpdatedText(
        resourceRepository: ResourceRepository,
        plural: Int,
        value: Int
    ): String {
        return resourceRepository.getFormattedString(
            R.string.last_updated,
            resourceRepository.getResources()
                .getQuantityString(plural, value, value)
        )
    }

    fun convertStringToDate(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    fun formatString(date: String): String {
        return LocalDate.parse(date, serverTimeFormatter).format(dateTimeFormatter).toString()
    }
}

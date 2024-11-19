package com.example.overtimecontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.overtimecontrol.ui.theme.OvertimeControlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OvertimeControlTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OvertimeControlApp()
                }
            }
        }
    }
}


@Composable
fun OvertimeControlApp() {

    /**
     * Variables
     */
    var earningsPerHourInput by remember { mutableStateOf("") }
    var numberOfDaysWorkedPerMonthInput by remember { mutableStateOf("") }
    var numberOfHoursWorkedPerDayInput by remember { mutableStateOf("") }
    var numberOfOvertimeWorkedInTheMonthInput by remember { mutableStateOf("") }
    var normalOvertimePercentageInput by remember { mutableStateOf("") }

    /**
     *  Convert the value of the variable to Double or null
     *  The Elvis operator "?:" return 0.0 if the value is null
     */
    val earningsPerHour = earningsPerHourInput.toDoubleOrNull() ?: 0.0
    val numberOfDaysWorkedPerMonth = numberOfDaysWorkedPerMonthInput.toDoubleOrNull() ?: 0.0
    val numberOfHoursWorkedPerDay = numberOfHoursWorkedPerDayInput.toDoubleOrNull() ?: 0.0
    val numberOfOvertimeWorkedInTheMonth = numberOfOvertimeWorkedInTheMonthInput.toDoubleOrNull() ?: 0.0
    val normalOvertimePercent = normalOvertimePercentageInput.toDoubleOrNull() ?: 0.0


    /**
     * Store earnings of the month value returned
     * by the calculateEarningsOfTheMonth function
     */
    val earningsOfTheMonth = calculateEarningsOfTheMonth(
        earningsPerHour = earningsPerHour,
        hoursPerDay = numberOfHoursWorkedPerDay,
        workedDaysInTheMonth = numberOfDaysWorkedPerMonth)


    /**
     * Store the earnings per hour with overtime value returned by
     *  the calculateTotalNormalOvertimeAdditional function
     */
    val earningsPerHourWithOvertimeAdditional = calculateNormalOvertimeAdditional(
        earningsPerHour = earningsPerHour,
        additionalPercentage = normalOvertimePercent
    )

    /**
     * Store the overtime earnings of the month
     */
    val overtimeEarningsOfTheMonth = earningsPerHourWithOvertimeAdditional * numberOfOvertimeWorkedInTheMonth

    /**
     * Total earnings of the month normal + overtime
     */
    val totalEarnings = earningsOfTheMonth + overtimeEarningsOfTheMonth


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        /**
         * App Title
         */
        Title()

        Spacer(modifier = Modifier.padding(8.dp))

        /**
         * The earnings per hour
         */
        EditNumberField(
            label = R.string.earnings_per_hour_input,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = earningsPerHourInput,
            onValueChange = { earningsPerHourInput = it }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        /**
         * The number of worked hours per day
         */
        EditNumberField(
            label = R.string.number_of_worked_hour_per_day_input,
            leadingIcon = R.drawable.hour,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = numberOfHoursWorkedPerDayInput,
            onValueChange = { numberOfHoursWorkedPerDayInput = it }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        /**
         * The number of worked days in a month
         */
        EditNumberField(
            label = R.string.number_of_worked_days_input,
            leadingIcon = R.drawable.day,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = numberOfDaysWorkedPerMonthInput,
            onValueChange = { numberOfDaysWorkedPerMonthInput = it }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        /**
         * The number of overtime in the current month
         */
        EditNumberField(
            label = R.string.overtime,
            leadingIcon = R.drawable.hour,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = numberOfOvertimeWorkedInTheMonthInput,
            onValueChange = { numberOfOvertimeWorkedInTheMonthInput = it }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        /**
         * The normal overtime percentage TextField
         */
        EditNumberField(
            label = R.string.normal_overtime_percentage_input,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = normalOvertimePercentageInput,
            onValueChange = { normalOvertimePercentageInput = it }
        )

        Spacer(modifier = Modifier.padding(8.dp))


    /**
     * The results
     */
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            // Worked days
            Text(
                text = stringResource(R.string.worked_days,
                    numberOfDaysWorkedPerMonth)
            )
            //Number of worked hours per day
            Text(
                text = stringResource(R.string.worked_hours_per_day,
                    numberOfHoursWorkedPerDay)
            )
            // Earnings per hour (normal)
            Text(
                text = stringResource(R.string.earnings_per_hour_normal,
                    earningsPerHour)
            )
            //Earnings por hour (overtime)
            Text(
                text = stringResource(R.string.earnings_per_hour_overtime,
                    earningsPerHourWithOvertimeAdditional)
            )
            //Total earnings of the month (normal)
            Text(
                text = stringResource(R.string.total_earnings_normal,
                    earningsOfTheMonth)
            )
            // Total earnings of the month (overtime)
            Text(
                text = stringResource(R.string.total_earnings_overtime,
                    overtimeEarningsOfTheMonth)
            )
            // Total earnings of the month normal + overtime
            Text(
                text = stringResource(R.string.total_earnings_in_the_month_normal_overtime,
                    totalEarnings)
            )
        }

    }
}
/**
 * A function to calculate normal earnings of the month without overtime
 *
 * @param earningsPerHour Earnings per hour in a day
 * @param hoursPerDay Number of hours worked in a day
 * @param workedDaysInTheMonth Number of days worked in the current month
 *
 * @return The earning per month without overtime value
 */
fun calculateEarningsOfTheMonth(
    earningsPerHour: Double,
    hoursPerDay: Double,
    workedDaysInTheMonth: Double
): Double {
    val earningsPerDay = earningsPerHour * hoursPerDay
    val earningsPerMonth = earningsPerDay * workedDaysInTheMonth

    return earningsPerMonth
}

/**
 * A function to calculate the earnings of the normal overtime
 *
 * @param earningsPerHour Earnings per hour
 * @param additionalPercentage The percentage of the normal overtime value
 *
 * @return The overtime value per hour
 */
fun calculateNormalOvertimeAdditional(
    earningsPerHour: Double,
    additionalPercentage: Double): Double
{
    val additionalAmount = earningsPerHour * (additionalPercentage / 100)
    
    val result = earningsPerHour + additionalAmount
    
    return result
}

/**
    fun calculateNightAdditional(baseEarnings: Double, additionalPercentage: Double): String {
    val result = additionalPercentage / 100 * baseEarnings

    return NumberFormat.getCurrencyInstance().format(result)
    }
**/

/**
 * The title of the app
 */
@Composable
fun Title(){
    Text(text = stringResource(id = R.string.overtime_control),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

/**
 * Text field for the user to enter values
 */
@Composable
fun EditNumberField(
   @StringRes label: Int,
   @DrawableRes leadingIcon: Int,
   keyboardOptions: KeyboardOptions,
   value: String,
   onValueChange: (String) -> Unit,
   modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon), 
                contentDescription = null
            )
        },
        onValueChange = onValueChange,
        label = { Text(stringResource(id = label)) },
        keyboardOptions = keyboardOptions,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    OvertimeControlTheme {
        OvertimeControlApp()
    }
}
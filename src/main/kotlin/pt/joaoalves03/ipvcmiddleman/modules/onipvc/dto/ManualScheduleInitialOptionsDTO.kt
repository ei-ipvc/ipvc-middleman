package pt.joaoalves03.ipvcmiddleman.modules.onipvc.dto

data class ManualScheduleInitialOptionsDTO(
    val years: List<OptionPair<String, String>>,
    val semesters: List<OptionPair<String, String>>,
    val schools: List<OptionPair<String, String>>,
    val degrees: List<OptionPair<String, String>>
)

data class OptionPair<out A, out B>(
    val name: A,
    val value: B
)
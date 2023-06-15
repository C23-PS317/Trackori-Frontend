import java.util.regex.Pattern
import java.util.regex.Matcher

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
    val pattern: Pattern = Pattern.compile(emailRegex)
    val matcher: Matcher = pattern.matcher(email)
    return matcher.matches()
}

fun isValidUsername(username: String): Boolean {
    // Valid username should be of length at least 3 and doesn't contain any spaces
    return username.length >= 3 && !username.contains(" ")
}

fun isValidPassword(password: String): Boolean {
    // Password needs to be at least 8 characters long and contains at least one digit
    return password.length >= 8 && password.any { it.isDigit() }
}
package com.example.stripe.utils


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

fun TextView.setSpannableTextWithFont(fullText: String, subText: String, fontFamily: Int) {
    val spannableText = SpannableString(fullText)

    // Find the start index of the subText
    val startIndex = fullText.indexOf(subText)

    if (startIndex != -1) {
        // Apply a custom font to the subText
        val typeface = ResourcesCompat.getFont(context, fontFamily)
        spannableText.setSpan(
            typeface?.let { CustomTypefaceSpan(it) },
            startIndex,
            startIndex + subText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Optionally, you can apply other styles to the subText
        spannableText.setSpan(
            StyleSpan(Typeface.BOLD), // Example: Make it bold
            startIndex,
            startIndex + subText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        text = spannableText
    } else {
        text = fullText
    }
}

class CustomTypefaceSpan(private val typeface: Typeface) : TypefaceSpan("") {
    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeface(ds)
    }

    override fun updateMeasureState(p: TextPaint) {
        applyCustomTypeface(p)
    }

    private fun applyCustomTypeface(paint: TextPaint) {
        paint.typeface = typeface
    }
}


@SuppressLint("QueryPermissionsNeeded")
fun Activity.takePhotoWithCamera(photoFile: File, requestCode: Int) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    intent.resolveActivity(packageManager)?.let {
        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.go2life.fileprovider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(intent, requestCode)
    }
}

fun Activity.openGalleryPicker(requestCode: Int) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent, requestCode)
}

fun Fragment.openKeyboardOnClick(view: View) {
    view.setOnClickListener {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.openKeyboard(view: View) {
    val inputMethodManager =
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view.requestFocus()
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}


fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun setImageToImageView(context: Context, imageView: ImageView, drawableResourceId: Int) {
    imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableResourceId))
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun setTextColorToPink(textView: TextView) {
    textView.setTextColor(Color.parseColor("#E20DA1"))
}

fun Long.divToMonthsAndDays(): Pair<Long, Long> {
    val daysInMonth = 30L // Assuming an average month has 30 days
    val months = this / (daysInMonth * DateUtils.DAY_IN_MILLIS)
    val days = (this - months * daysInMonth * DateUtils.DAY_IN_MILLIS) / DateUtils.DAY_IN_MILLIS
    return Pair(months, days)
}

fun setTextColorToDefault(
    textView: TextView
) {
    textView.setTextColor(Color.parseColor("#474747"))
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun Context.getColors(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Bundle?.getStringValue(key: String): String {
    return this?.getString(key, "null").toString()
}

fun getCurrentDate(): Calendar {
    return Calendar.getInstance()
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateString(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun getDateDifferenceInDays(startDate: String, endDate: String): Long {
    val dateFormat =
        SimpleDateFormat("yyyy-MM-dd") // Adjust the date format based on your input date string

    val startDateObj: Date? = dateFormat.parse(startDate)
    val endDateObj: Date? = dateFormat.parse(endDate)

    val startDateCal = Calendar.getInstance()
    startDateCal.time = startDateObj!!

    val endDateCal = Calendar.getInstance()
    endDateCal.time = endDateObj!!

    // Calculate the difference between the two dates in milliseconds
    val timeDifferenceInMillis =
        kotlin.math.abs(endDateCal.timeInMillis - startDateCal.timeInMillis)

    // Convert milliseconds to days
    return timeDifferenceInMillis / (1000 * 60 * 60 * 24)


}

fun getCurrentTimeInAMPM(): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.US)
    val currentTime = Date()
    return dateFormat.format(currentTime)
}


fun TextView.setClickableAndUnderlinedText(
    fullText: String,
    clickableText: String,
    onClickAction: () -> Unit
) {
    val spannableText = SpannableString(fullText)
    val startIndex = fullText.indexOf(clickableText)

    if (startIndex != -1) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickAction()
            }
        }

        spannableText.setSpan(
            clickableSpan,
            startIndex,
            startIndex + clickableText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableText.setSpan(
            UnderlineSpan(),
            startIndex,
            startIndex + clickableText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        text = spannableText
        movementMethod = LinkMovementMethod.getInstance()
    } else {
        text = fullText
    }


    fun ImageView.loadImageWithPlaceholderAndError(
        context: Context,
        url: String?,
        placeholderResId: Int,
        errorResId: Int
    ) {
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderResId)
                    .error(errorResId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // You can customize the cache strategy here
            )
            .into(this)
    }

    fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        context?.let {
            Toast.makeText(it, message, duration).show()
        }
    }

    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    fun TextInputEditText.isEmailValid(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val email = this.text?.toString().orEmpty()
        return Pattern.matches(emailPattern, email)
    }

    fun View.showSnackbarWithAction(
        message: String,
        actionText: String,
        actionCallback: () -> Unit
    ) {
        val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)

        snackbar.setAction(actionText) {
            actionCallback.invoke()
        }

        snackbar.show()
    }


    fun ViewGroup.setMargins(margin: Int) {
        val layoutParams = ViewGroup.MarginLayoutParams(this.layoutParams)
        layoutParams.setMargins(margin, margin, margin, margin)
        this.layoutParams = layoutParams
    }

    fun StringBuilder.appendLine(text: String): StringBuilder {
        this.append(text).append("\n")
        return this
    }

    fun View.setBackgroundColorRes(@ColorRes colorRes: Int) {
        setBackgroundColor(ContextCompat.getColor(context, colorRes))
    }

    fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        val layoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(left, top, right, bottom)
        this.layoutParams = layoutParams
    }

    fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return Pattern.matches(emailPattern, email)
    }

    fun ImageView.setImageWithResource(@DrawableRes resId: Int, options: RequestOptions? = null) {
        val requestBuilder = Glide.with(this)
            .load(resId)

        options?.let {
            requestBuilder.apply(it)
        }

        requestBuilder.into(this)
    }

    fun StringBuilder.changeText(newText: String) {
        this.clear() // Clear the existing content
        this.append(newText) // Append the new text
    }

    fun String.formatMobileNumber(): String {
        // Assuming the mobile number is in the format "1234567890"
        return if (length == 10) {
            "${substring(0, 3)}-${substring(3, 6)}-${substring(6)}"
        } else {
            this // Return as is if the length is not 10
        }
    }

    fun Fragment.isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun Fragment.isPermissionGrantedBelowSdk(permission: String): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Permissions are granted at installation time on API levels below 23
        }
    }


}



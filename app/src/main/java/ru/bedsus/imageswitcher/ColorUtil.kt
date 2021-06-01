package ru.bedsus.imageswitcher

import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import androidx.annotation.ColorInt

fun extractTextByTag(text: String, startTag: String, endTag: String, @ColorInt extractColor: Int): SpannableString {
    // 1. Находим все индексы по тегам с учетом отступов самих тегов
    val listIndex = findPairIndexByTags(text, startTag, endTag)
    // 2. Убираем теги
    val textWithoutTag = text.replace(startTag, "")
        .replace(endTag, "")
    // 3. Выделяем текст по индексам
    val textSpan = SpannableString(textWithoutTag)
    listIndex.forEach {
        textSpan.setSpan(
            BackgroundColorSpan(extractColor),
            it.first,
            it.second,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return textSpan
}

fun findPairIndexByTags(text: String, startTag: String, endTag: String): List<Pair<Int, Int>> {
    if (validateTextByTag(text, startTag, endTag).not())
        return emptyList()
    var position = 0
    var offset = 0
    val listIndex = mutableListOf<Pair<Int, Int>>()
    while (position < text.length) {
        val startIndex = text.indexOf(startTag, position)
        val endIndex = text.indexOf(endTag, position + startTag.length)
        if (startIndex < 0 || endIndex < 0) break
        position = endIndex + endTag.length
        listIndex.add(Pair(
                startIndex - offset,
                endIndex - offset - startTag.length
        ))
        offset += startTag.length + endTag.length
    }
    return listIndex
}

fun validateTextByTag(text: String, startTag: String, endTag: String): Boolean {
    val startTagIndexList = findAllIndexByTag(text, startTag)
    val endTagIndexList = findAllIndexByTag(text, endTag)
    if (startTagIndexList.size != endTagIndexList.size)
        return false
    for (index in startTagIndexList.indices) {
        val startIndex = startTagIndexList[index]
        val endIndex = endTagIndexList[index]
        if (startIndex >= endIndex)
            return false
        startTagIndexList.getOrNull(index + 1)?.let { nextStartIndex ->
            if (nextStartIndex <= endIndex)
                return false
        }
    }
    return true
}

fun findAllIndexByTag(text: String, tag: String): List<Int> {
    var position = 0
    val list = mutableListOf<Int>()
    while (position < text.length) {
        val index = text.indexOf(tag, position)
        if (index < 0) break
        list.add(index)
        position = index + tag.length
    }
    return list
}
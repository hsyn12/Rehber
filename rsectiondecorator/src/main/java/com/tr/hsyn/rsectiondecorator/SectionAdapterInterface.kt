package com.tr.hsyn.rsectiondecorator

interface SectionsAdapterInterface {
    fun getSectionsCount() : Int
    fun getSectionTitleAt(sectionIndex: Int): String
    fun getItemCountForSection(sectionIndex: Int): Int
}

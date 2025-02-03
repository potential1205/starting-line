package com.example.gogoma.data.model

sealed class FilterOption(val title: String)

class All : FilterOption("")
class Region : FilterOption("지역")
class Status : FilterOption("접수 상태")
class Category : FilterOption("종목")
class Year : FilterOption("년도")
class Month : FilterOption("월")

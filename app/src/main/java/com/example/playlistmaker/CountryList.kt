package com.example.playlistmaker


fun getCountry(country: String?): String {
    return when (country) {
        "AND" -> "Андорра"
        "ARE" -> "Объединенные Арабские Эмираты"
        "AFG" -> "Афганистан"
        "ATG" -> "Антигуа и Барбуда"
        "AIA" -> "Ангилья"
        "ALB" -> "Албания"
        "ARM" -> "Армения"
        "AGO" -> "Ангола"
        "ARG" -> "Аргентина"
        "AUT" -> "Австрия"
        "AUS" -> "Австралия"
        "AZE" -> "Азербайджан"
        "BIH" -> "Босния и Герцеговина"
        "BRB" -> "Барбадос"
        "BGD" -> "Бангладеш"
        "BEL" -> "Бельгия"
        "BFA" -> "Буркина-Фасо"
        "BGR" -> "Болгария"
        "BHR" -> "Бахрейн"
        "BEN" -> "Бенин"
        "BMU" -> "Бермуды"
        "BRN" -> "Бруней Даруссалам"
        "BOL" -> "Боливия"
        "BRA" -> "Бразилия"
        "BHS" -> "Багамы"
        "BTN" -> "Бутан"
        "BWA" -> "Ботсвана"
        "BLR" -> "Беларусь"
        "BLZ" -> "Белиз"
        "CAN" -> "Канада"
        "COD" -> "Демократическая Республика Конго"
        "CAF" -> "Центрально-Африканская Республика"
        "COG" -> "Конго"
        "CHE" -> "Швейцария"
        "CIV" -> "Кот-д'Ивуар"
        "CHL" -> "Чили"
        "CMR" -> "Камерун"
        "CHN" -> "Китай"
        "COL" -> "Колумбия"
        "CRI" -> "Коста-Рика"
        "CPV" -> "Кабо-Верде"
        "CYP" -> "Кипр"
        "CZE" -> "Чехия"
        "DEU" -> "Германия"
        "DNK" -> "Дания"
        "DMA" -> "Доминика"
        "DOM" -> "Доминиканская Республика"
        "DZA" -> "Алжир"
        "ECU" -> "Эквадор"
        "EST" -> "Эстония"
        "EGY" -> "Египет"
        "ESP" -> "Испания"
        "ETH" -> "Эфиопия"
        "FIN" -> "Финляндия"
        "FJI" -> "Фиджи"
        "FSM" -> "Микронезия"
        "FRA" -> "Франция"
        "GAB" -> "Габон"
        "GBR" -> "Великобритания"
        "GRD" -> "Гренада"
        "GEO" -> "Грузия"
        "GHA" -> "Гана"
        "GMB" -> "Гамбия"
        "GIN" -> "Гвинея"
        "GRC" -> "Греция"
        "GTM" -> "Гватемала"
        "GNB" -> "Гвинея-Бисау"
        "GUY" -> "Гайана"
        "HKG" -> "Гонконг"
        "HND" -> "Гондурас"
        "HRV" -> "Хорватия"
        "HUN" -> "Венгрия"
        "IDN" -> "Индонезия"
        "IRL" -> "Ирландия"
        "ISR" -> "Израиль"
        "IND" -> "Индия"
        "IRQ" -> "Ирак"
        "ISL" -> "Исландия"
        "ITA" -> "Италия"
        "JAM" -> "Ямайка"
        "JOR" -> "Иордания"
        "JPN" -> "Япония"
        "KEN" -> "Кения"
        "KGZ" -> "Киргизия"
        "KHM" -> "Камбоджа"
        "KNA" -> "Сент-Китс и Невис"
        "KOR" -> "Республика Корея"
        "KWT" -> "Кувейт"
        "CYM" -> "Острова Кайман"
        "KAZ" -> "Казахстан"
        "LAO" -> "Лаосская Народно-Демократическая Республика"
        "LBN" -> "Ливан"
        "LCA" -> "Сент-Люсия"
        "LIE" -> "Лихтенштейн"
        "LKA" -> "Шри-Ланка"
        "LBR" -> "Либерия"
        "LTU" -> "Литва"
        "LUX" -> "Люксембург"
        "LVA" -> "Латвия"
        "LBY" -> "Ливия"
        "MAR" -> "Марокко"
        "MCO" -> "Монако"
        "MDA" -> "Молдова"
        "MNE" -> "Черногория"
        "MDG" -> "Мадагаскар"
        "MKD" -> "Северная Македония"
        "MLI" -> "Мали"
        "MMR" -> "Мьянма"
        "MNG" -> "Монголия"
        "MAC" -> "Макао"
        "MRT" -> "Мавритания"
        "MSR" -> "Монтсеррат"
        "MLT" -> "Мальта"
        "MUS" -> "Маврикий"
        "MDV" -> "Мальдивы"
        "MWI" -> "Малави"
        "MEX" -> "Мексика"
        "MYS" -> "Малайзия"
        "MOZ" -> "Мозамбик"
        "NAM" -> "Намибия"
        "NER" -> "Нигер"
        "NGA" -> "Нигерия"
        "NIC" -> "Никарагуа"
        "NLD" -> "Нидерланды"
        "NOR" -> "Норвегия"
        "NPL" -> "Непал"
        "NRU" -> "Науру"
        "NZL" -> "Новая Зеландия"
        "OMN" -> "Оман"
        "PAN" -> "Панама"
        "PER" -> "Перу"
        "PNG" -> "Папуа – Новая Гвинея"
        "PHL" -> "Филиппины"
        "PAK" -> "Пакистан"
        "POL" -> "Польша"
        "PSE" -> "Палестина"
        "PRT" -> "Португалия"
        "PLW" -> "Палау"
        "PRY" -> "Парагвай"
        "QAT" -> "Катар"
        "ROU" -> "Румыния"
        "SRB" -> "Сербия"
        "RUS" -> "Россия"
        "RWA" -> "Руанда"
        "SAU" -> "Саудовская Аравия"
        "SLB" -> "Соломоновы Острова"
        "SYC" -> "Сейшельские Острова"
        "SWE" -> "Швеция"
        "SGP" -> "Сингапур"
        "SVN" -> "Словения"
        "SVK" -> "Словакия"
        "SLE" -> "Сьерра-Леоне"
        "SEN" -> "Сенегал"
        "SUR" -> "Суринам"
        "STP" -> "Сан-Томе и Принсипи"
        "SLV" -> "Сальвадор"
        "SWZ" -> "Эсватини"
        "TCA" -> "Теркс и Кайкос"
        "TCD" -> "Чад"
        "THA" -> "Таиланд"
        "TJK" -> "Таджикистан"
        "TKM" -> "Туркменистан"
        "TUN" -> "Тунис"
        "TON" -> "Тонга"
        "TUR" -> "Турция"
        "TTO" -> "Тринидад и Тобаго"
        "TWN" -> "Тайвань"
        "TZA" -> "Танзания"
        "UKR" -> "Украина"
        "UGA" -> "Уганда"
        "USA" -> "Соединенные Штаты Америки"
        "URY" -> "Уругвай"
        "UZB" -> "Узбекистан"
        "VCT" -> "Сент-Винсент и Гренадины"
        "VEN" -> "Венесуэла"
        "VGB" -> "Британские Виргинские Острова"
        "VNM" -> "Вьетнам"
        "VUT" -> "Вануату"
        "WSM" -> "Самоа"
        "XXK" -> "Косово"
        "YEM" -> "Йемен"
        "ZAF" -> "Южно-Африканская Республика"
        "ZMB" -> "Замбия"
        "ZWE" -> "Зимбабве"
        else -> country ?: ""
    }
}






# Запуск
## Тесты
``
./gradlew test
``
## Benchmark

``
./gradlew run
``

# Результаты benchmark

Измерения проводились на массиве равномерно распределенных чисел, 
размера 100_000_000. Изначально проводилось несколько итераций для прогрева JVM,
после по 5 итераций для каждой из реализаций quicksort. 
Результаты замеров представлены в миллисекундах, ускорение представлено относительно последовательной реализации.

## Последовательная реализация

| №1    | №2    | №3     | №4      | №5     | Среднее | Ускорение |
|-------|-------|--------|---------|--------|---------|-----------|
| 10094 | 10018 | 9959   | 9942    | 10165  | 10165   | 1         |

## Реализация с паралельным фильтром

| Размер блока | №1    | №2    | №3    | №4     | №5    | Среднее | Ускорение |
|--------------|-------|-------|-------|--------|-------|---------|-----------|
| 1000         | 42016 | 41728 | 43186 | 43953  | 46219 | 43420   | 0.234     |
| 10_000       | 38297 | 37838 | 38511 | 38197  | 38233 | 38215   | 0.265     |
| 100_000      | 30890 | 29899 | 30066 | 29619  | 29897 | 30074   | 0.337     |
| 1_000_000    | 21681 | 21470 | 21767 | 21972  | 23306 | 22039   | 0.461     |
| 10_000_000   | 14094 | 12098 | 12990 | 12004  | 12972 | 12831   | 0.792     |

В реализации с параллельным фильтром ускорения достичь не удалось. 
Связано это с тем, что параллельный фильтр имеет большую константу в work, 
хотя ассимптотически work эквивалентен последовательной версии. 
В силу того, что количество процессов мало (4), даже константа 4 в work уже не позволяет 
достичь ускорения. Также параллельный фильтр множественно аллоцирует память, 
что замедляет алгоритм.

## Реализация c inplace partition и разбиением на 2 подзадачи

| Размер блока | №1   | №2   | №3   | №4   | №5   | Среднее | Ускорение |
|--------------|------|------|------|------|------|---------|-----------|
| 1000         | 2778 | 3141 | 3411 | 2918 | 2916 | 3032    | 3.35      |
| 10_000       | 2761 | 2987 | 2882 | 2880 | 2893 | 2880    | 3.52      |
| 100_000      | 3461 | 2856 | 2875 | 3469 | 2994 | 3131    | 3.24      |
| 1_000_000    | 2782 | 3322 | 2775 | 3313 | 3602 | 3158    | 3.21      |
| 10_000_000   | 3852 | 3966 | 3795 | 3827 | 3969 | 3881    | 2.61      |

Данная реализация эквивалентна последовательной реализации за исключением того, что
рекурсивные вызовы quicksort запускаются параллельно. За счёт того, что дополнительная память
не выделяется, а work совпадает с последовательной версией удается добиться ускорения.



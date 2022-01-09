fun <T> Collection<T>.pairs(): List<Pair<T, T>> {
    return this.flatMapIndexed { index, a ->
        this.drop(index).map { b -> a to b }
    }
}

fun List<Int>.median(): Int {
    return if (this.size % 2 == 0) {
        ((this[this.size / 2] + this[this.size / 2 - 1]) / 2)
    } else {
        (this[this.size / 2])
    }
}

fun List<Long>.median(): Long {
    return if (this.size % 2 == 0) {
        ((this[this.size / 2] + this[this.size / 2 - 1]) / 2)
    } else {
        (this[this.size / 2])
    }
}
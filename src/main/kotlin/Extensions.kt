fun <T> Collection<T>.pairs(): List<Pair<T, T>> {
    return this.flatMapIndexed { index, a ->
        this.drop(index).map { b -> a to b }
    }
}
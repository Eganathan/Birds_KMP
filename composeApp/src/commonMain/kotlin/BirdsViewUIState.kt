data class BirdsViewUIState(
    val content: List<BirdImage>? = null,
    val selectedCategory: String? = null
){
    val categories = content?.map{it.category}?.toSet().orEmpty()
    val selectedContent = content?.filter { it.category == selectedCategory }
}
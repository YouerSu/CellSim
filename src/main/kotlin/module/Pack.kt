package module

data class Pack(val material: Code, var amount: Int) {
    fun deepCopy(): Pack {
        // 在这里手动创建一个新的Pack对象，复制属性并进行深拷贝
        return Pack(this.material, this.amount)
    }


}
fun MutableList<Pack>.getPackIndex(material: Code): Int? {
    return indices.find { index -> this[index].material == material }
}

fun MutableList<Pack>.addPack(code: Code, amount: Int){
    val cellMaterialIndex = this.getPackIndex(code)
    if (cellMaterialIndex != null) {
        this[cellMaterialIndex].amount+=amount
    }else {
        this.add(Pack(code,amount))
    }
}


fun MutableList<Pack>.addPack(pack: Pack){
    addPack(pack.material, pack.amount)
}

fun MutableList<Pack>.minusPack(code: Code, amount: Int): Boolean{
    val cellMaterialIndex = this.getPackIndex(code)
    return if (cellMaterialIndex != null) {
        this[cellMaterialIndex].amount-=amount
        if (this[cellMaterialIndex].amount<0){
            this.removeAt(cellMaterialIndex)
            false
        } else if (this[cellMaterialIndex].amount==0){
            this.removeAt(cellMaterialIndex)
            true
        } else {
            true
        }
    }else {
        false
    }
}

fun MutableList<Pack>.minusPack(pack: Pack): Boolean{
    return minusPack(pack.material, pack.amount)
}



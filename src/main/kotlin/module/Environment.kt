package module

class Environment(val width: Int, val height: Int) {
    val env = Array(width) { x-> Array(height) { y-> Block(x,y) } }
    var cellNumber: Int = 0

    fun refresh(block: Block) {
        block.refresh()
    }

    fun refreshAll() {
        for (blocks in env){
            for (block in blocks){
                block.refresh()
            }
        }
    }

    fun refreshAt(x: Int, y: Int){
        val block = env[x][y]
        refresh(block)
    }

    fun randomRefresh(){
        refresh(env.random().random())
    }

    fun isInEnv(x: Int, y: Int): Boolean{
        return x in env.indices && y in env[0].indices
    }

    fun addCellTo(x: Int,y: Int,cell: Cell): Boolean{
        return if (isInEnv(x, y)){
            env[x][y].addCell(cell)
            true
        } else{
            false
        }

    }

    fun addInit(){
        for (blocks in env){
            for (block in blocks){
                block.addRandomCell()
                block.addRandomMaterial()
            }
        }
    }

    fun initRandomCells(){
        for (blocks in env){
            for (block in blocks){
                block.removeCells()
                block.addRandomCell()
            }
        }
    }

    fun getBlock(x: Int, y: Int): Block?{
        if (isInEnv(x, y)){
            return env[x][y]
        }else{
            return null
        }
    }


    fun randomBlock(): Block{
        return env.random().random()
    }

    fun addCellToRandomBlock(cell: Cell){
        randomBlock().addCell(cell)
    }

    fun addRandomMaterial(times: Int){
        for (i in 0..times){
            env.random().random().addRandomMaterial()
        }
    }

    fun initMaterials(packs: List<Pack>){
        for (blocks in env){
            for (block in blocks){
                block.removeMaterials()
                block.addMaterials(packs.map { it.deepCopy() })
            }
        }

    }

    fun initMaterial(code: Code, amount: Int){
        initMaterials(listOf(Pack(code, amount)))
    }


    fun randomCell(): Cell? {
        for (blocks in env){
            for (block in blocks){
                block.getCells().randomOrNull()?.let {
                    return it
                }
            }
        }
        return null
    }

    fun killCell() {
        for (blocks in env){
            for (block in blocks){
                block.removeCells()
                }
            }
        }

    fun getRemainsCell(until: Int): MutableList<Cell>? {
        if (cellNumber < until) {
            val cells = mutableListOf<Cell>()
            for (blocks in env) {
                for (block in blocks) {
                    for (cell in block.getCells()) {
                        cells.add(cell)
                    }
                }
            }
            return cells
        }
        return null
    }

    companion object {
        var environment: Environment? = null
    }


}
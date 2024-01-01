package module

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

class Block(val x: Int, val y: Int) {


    private var materials: MutableList<Pack> = mutableListOf()
    private var cells: MutableList<Cell> = mutableListOf()


    fun addCell(cell: Cell): Boolean{
        cells.add(cell)
        Environment.environment?.let { it.cellNumber++ }
        return true
    }

    fun getCells(): MutableList<Cell> {
        return cells
    }

    fun removeCell(cell: Cell){
        if (cell in cells){
            Environment.environment?.let { it.cellNumber-- }
            cells.remove(cell)
        }
    }

    fun removeCells(){
        Environment.environment?.let { it.cellNumber-=cells.size }
        cells = mutableListOf()
    }

    fun consume(cell: Cell, material: Code): Boolean{
        val isAbsorb = materials.minusPack(material,1)
        if (isAbsorb){
            cell.materials.addPack(material,10)
        }
        return isAbsorb
    }

    private fun getDistance(directionCode: Code): Pair<Int, Int> {
        val dX =
            if (directionCode == Code.East || directionCode == Code.Northeast || directionCode == Code.Southeast) 1 else if (directionCode == Code.West || directionCode == Code.Northwest || directionCode == Code.Southwest) -1 else 0
        val dY =
            if (directionCode == Code.South || directionCode == Code.Southeast || directionCode == Code.Southwest) 1 else if (directionCode == Code.North || directionCode == Code.Northeast || directionCode == Code.Northwest) -1 else 0
        return Pair(dX, dY)
    }

    fun detectMaterial(code: Code): Boolean{
        val count = materials.getPackIndex(code)
        return count!=null&&materials[count].amount>0
    }

    fun detectList(detectCodes: MutableList<Code>): Boolean {
        var (dx, dy) = Pair(0, 0)
        var isDetect = true
        for (code in detectCodes){
            if (isDetect.not()){
                return false
            }
            if (CODETYPE.Direction in code.codeType){
                val (toX, toY) = getDistance(code)
                dx+=toX
                dy+=toY
                isDetect = isDetect && Environment.environment?.isInEnv(x+dx,y+dy) ?: false
            }
            if (CODETYPE.Material in code.codeType){

                isDetect = isDetect && Environment.environment?.getBlock(x+dx, y+dy)?.detectMaterial(code) ?: false
            }

        }
        return isDetect
    }


    fun move(cell: Cell, directionCode: Code): Boolean {

        val (dX, dY) = getDistance(directionCode)
        if (dX == 0 && dY == 0){
            if (cell in cells){
                return true
            }else {
                addCell(cell)
                return true
            }

        }
        Environment.environment?.let {
            if (it.addCellTo(x+dX, y+dY, cell)){
                removeCell(cell)
                return true
            }
        }
        return false
    }


    fun attack(cell: Cell): Boolean {
        val toBeAttack = cells.randomOrNull()
        if (toBeAttack !=  cell && toBeAttack != null){
            return lostLife(toBeAttack)
        }
        return false
    }

    fun lostLife(toBeAttack: Cell): Boolean {
        val isLost = toBeAttack.materials.minusPack(Code.Life, 1)
        checkDeath()
        return isLost
    }

    private fun checkDeath() {
        val toDeath = cells.filter { cell ->
            val index = cell.materials.getPackIndex(Code.Life)
            index == null || cell.materials[index].amount<=0
        }
        for (cell in toDeath){
            removeCell(cell)
        }
    }

    fun addRandomCell(){
        addCell(Cell.randomCell())
    }

    fun addMaterial(material: Code, amount: Int){
        materials.addPack(material, amount)
    }

    fun initMaterials(packs: List<Pack>){
        materials = packs.toMutableList()
    }

    fun addRandomMaterial(){
        addMaterial(Ribosome.randomCode(CODETYPE.Material), 2)
    }

    fun refresh() {
        cells.randomOrNull()?.action(this)
    }

    fun blockColor(): Color{
        return if (cells.isEmpty().not()){
            cells.random().color
        } else {
            Color(0,0,0,0)
        }
    }

    fun removeMaterials() {
        materials = mutableListOf()
    }

    fun addMaterials(packs: List<Pack>) {
        materials.addAll(packs)
    }


}


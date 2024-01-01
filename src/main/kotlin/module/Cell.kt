package module

import androidx.compose.ui.graphics.Color

class Cell(val proteins: List<Code>) {

    var color: Color = Color(0,0,0)
    var generation = 0
    val materials: MutableList<Pack> = mutableListOf()


    fun action(block: Block) {
        Ribosome.decodeForProtein(nowProteinCode).action(this, block)
        livingConsume(block)

    }

    fun livingConsume(block: Block){
        if (materials.minusPack(Code.Glucose, 1).not()){
            block.lostLife(this)
        }
    }

    var proteinPoint: Int = 0

    val nowProteinCode: Code
        get() {
            return if (proteinPoint in proteins.indices){
                proteins[proteinPoint]
            } else {
                proteinPoint = 0
                Code.End
            }

        }


    companion object{

        var myCell = Cell(
            listOf(
                Code.Absorb,
                Code.Glucose,
                Code.Move,
                Code.North,
            )
        )

        var powerCell = Cell(
            listOf(
                Code.Switch,
                Code.Detect,
                Code.Glucose,
                Code.Absorb,
                Code.Glucose,
                Code.Split,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Detect,
                Code.North,
                Code.Glucose,
                Code.Move,
                Code.North,
                Code.Absorb,
                Code.Glucose,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Detect,
                Code.Northwest,
                Code.Glucose,
                Code.Move,
                Code.Northwest,
                Code.Absorb,
                Code.Glucose,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Detect,
                Code.Northeast,
                Code.Glucose,
                Code.Move,
                Code.Northeast,
                Code.Absorb,
                Code.Glucose,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Detect,
                Code.South,
                Code.Glucose,
                Code.Move,
                Code.South,
                Code.Absorb,
                Code.Glucose,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Detect,
                Code.Southeast,
                Code.Glucose,
                Code.Move,
                Code.Southeast,
                Code.Absorb,
                Code.Glucose,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Detect,
                Code.Southwest,
                Code.Glucose,
                Code.Move,
                Code.Southwest,
                Code.Absorb,
                Code.Glucose,
                Code.End,

                //---

                Code.Switch,
                Code.Detect,
                Code.North,
                Code.Move,
                Code.North,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Northwest,
                Code.Move,
                Code.Northwest,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Northeast,
                Code.Move,
                Code.Northeast,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.South,
                Code.Move,
                Code.South,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Southeast,
                Code.Move,
                Code.Southeast,
                Code.End,

                Code.Switch,
                Code.Detect,
                Code.Southwest,
                Code.Move,
                Code.Southwest,
                Code.End,



            )
        )


        fun randomCell(): Cell{
            val cell = Cell(Ribosome.randomDNA())
//            val cell = myCell
            cell.materials.add(Pack(Code.Glucose,100))
            cell.materials.add(Pack(Code.Life, 1))
            return cell
        }

        fun absorb(cell: Cell, block: Block): Int{
            cell.proteinPoint++
            return if (block.consume(cell, cell.nowProteinCode)){
                1
            } else {
                0
            }
        }

        fun detectAnd(cell: Cell, block: Block): Int{
            var count: Int = 0
            do {
                count++
                cell.proteinPoint++
            }while (cell.nowProteinCode == Code.Detect)

            val detectCodes = mutableListOf<Code>()
            while (count>0){
                detectCodes.add(cell.nowProteinCode)
                cell.proteinPoint++
                count--
            }

            return if (block.detectList(detectCodes)) detectCodes.size else 0
        }

        fun move(cell: Cell, block: Block): Int{
            cell.proteinPoint++
            val moved = block.move(cell,cell.nowProteinCode)

            return if (moved) 1 else 0
        }

        fun attack(cell: Cell, block: Block): Int{
            val isAttack = block.attack(cell)
            return if (isAttack) 1 else 0
        }

        fun split(cell: Cell, block: Block): Int{
            val newCell = mutate(cell)
            newCell.materials.addAll(cell.materials.map { pack: Pack -> Pack(pack.material, pack.amount/2) })
//            cell.materials.forEach { pack -> pack.amount/=2 }
            newCell.color = cell.color
            cell.proteinPoint++
            val isSplit = block.move(newCell, cell.nowProteinCode)
            return if (isSplit) 1 else 0
        }

        fun mutate(cell: Cell) = Cell(Ribosome.mutation(cell.proteins))

        fun skip(cell: Cell, block: Block): Int{
            cell.proteinPoint++
            val signal = Ribosome.decodeForProtein(cell.nowProteinCode).action(cell, block)
            cell.proteinPoint+=signal
            return signal
        }

        fun switch(cell: Cell, block: Block): Int{
            cell.proteinPoint++
            val signal = Ribosome.decodeForProtein(cell.nowProteinCode).action(cell, block)
            return if (signal<=0) {
                while (cell.nowProteinCode != Code.End && cell.nowProteinCode != Code.Stop){
                    cell.proteinPoint++
                }
                cell.proteinPoint++
                0
            }else{
                1
            }

        }

        fun end(cell: Cell, block: Block): Int{
            cell.proteinPoint = 0
            return 0
        }

    }

}


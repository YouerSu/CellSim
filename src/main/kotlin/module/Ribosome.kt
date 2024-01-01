package module

import kotlin.random.Random
import kotlin.random.nextInt

enum class CODETYPE{
    Functional, Material, Direction, Flag, Bio
}

enum class Code(vararg val codeType: CODETYPE){
    Absorb(CODETYPE.Functional), Detect(CODETYPE.Functional), Move(CODETYPE.Functional), Split(CODETYPE.Functional), Attack(CODETYPE.Functional), Skip(CODETYPE.Functional), Switch(CODETYPE.Functional),                  //Functional
    Glucose(CODETYPE.Material),                    //Material
    East(CODETYPE.Direction), West(CODETYPE.Direction), South(CODETYPE.Direction), North(CODETYPE.Direction), Northeast(CODETYPE.Direction), Southeast(CODETYPE.Direction), Northwest(CODETYPE.Direction), Southwest(CODETYPE.Direction),                   //Direction
    End(CODETYPE.Flag, CODETYPE.Functional), Nop(CODETYPE.Flag), Stop(CODETYPE.Flag),                 //Flag
    Life(CODETYPE.Bio),               //Bio


}

class Ribosome {

    companion object {

        val nop = Protein{cell, _ ->
                cell.proteinPoint++
            1}
        val functionProteins = mapOf(
            Code.Absorb to Protein{cell, block -> Cell.absorb(cell, block) },
            Code.Detect to Protein{cell, block -> Cell.detectAnd(cell, block) },
            Code.Move to Protein{cell, block -> Cell.move(cell, block) },
            Code.Split to Protein{cell, block -> Cell.split(cell, block) },
            Code.Attack to Protein{cell, block -> Cell.attack(cell, block) },
            Code.Skip to Protein{cell, block -> Cell.skip(cell, block) },
            Code.Switch to Protein{cell, block -> Cell.switch(cell, block) },
            Code.End to Protein{cell, block -> Cell.end(cell, block) },
        )

        val mutations: List<(Array<Code>)->Array<Code>> = listOf(
            {codes->
                val mutationIndex = codes.indices.random()
                codes[mutationIndex] = Code.values().random()
                codes
            },
            {codes->
                val mutationIndex = codes.indices.random()
                codes.drop(mutationIndex)
                codes
            },
            {codes->
                val mutationIndex = codes.indices.random()
                codes.plus(Code.values().random())
                codes
            },
            {codes->
                val startIndex = codes.indices.random()
                val endIndex = codes.indices.random()
                if (startIndex<endIndex){
                    codes.reverse(startIndex, endIndex)
                }
                codes
            },
            {codes->
                val startIndex = codes.indices.random()
                val endIndex = codes.indices.random()
                if (startIndex<endIndex){
                    codes.plus(codes.copyOfRange(startIndex, endIndex))
                }
                codes
            },
        )

        fun mutation(codes: List<Code>): List<Code>{
            var times: Int = (codes.size*0.01).toInt()+1
            var newCodes: Array<Code> = codes.toTypedArray()
            while (times>0){
                newCodes = mutations.random()(newCodes)
                times--
            }
            return newCodes.toList()

        }

        fun randomCode(codeType: CODETYPE): Code{
            return Code.values().filter { codeType in it.codeType }.random()
        }

        fun randomDNA(): List<Code>{
            return List(Random.nextInt(1000)) { Code.values().random() }
        }

        fun toCode(codeString: String): Code{
            for (code in Code.values()){
                if (codeString == code.toString()){
                    return code
                }
            }
            return Code.Nop
        }

        fun createProteinCode(dna: Array<String>): List<Code> {
            return dna.map { toCode(it) }
        }

        fun decodeForProtein(code: Code): Protein {
            if (CODETYPE.Functional in code.codeType) {
                functionProteins[code]?.let { return it }
                return nop
            }else {
                return nop
            }
        }

//        fun decodeForMaterial(code: Code): Code {
//
//            return code
//        }
    }

}
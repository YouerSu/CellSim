import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.delay
import module.*
import java.util.regex.Pattern
import kotlin.random.Random


@Composable
@Preview
fun App() {


    val width = remember { mutableStateOf(50) }
    val height = remember { mutableStateOf(50) }
    Environment.environment = Environment(width.value, height.value)
//    Environment.environment?.addInit()
    Environment.environment?.initMaterial(Code.Glucose, 5)
//    for (i in 0..1){
    val aCell = Cell(Cell.powerCell.proteins)
    aCell.materials.addPack(Pack(Code.Glucose,100))
    aCell.materials.addPack(Pack(Code.Life,2))
    Environment.environment?.addCellToRandomBlock(aCell)
//    }
//    Environment.environment?.initRandomCells()
    MaterialTheme {
        Column {
            EnvironmentCanvas(width, height)
        }

    }
}

@Composable
fun OutPutCellInfo(){
    var text by remember { mutableStateOf("Hello, World!") }
    Button({
        val cell = Environment.environment?.randomCell()
        if (cell!=null){
            val index = cell.materials.getPackIndex(Code.Glucose)
            if (index!=null){
                text = cell.materials[index].amount.toString()
            }else{
                text = "Have Cell No Glu"
            }
            val ind = cell.materials.getPackIndex(Code.Life)
            if (ind!=null){
                text += " Life:${cell.materials[ind].amount}"
            }else{
                text += " No Life"
            }
        }else{
            text = "No Cell"
        }
    }){
        Text(text)
    }

}

@Composable
@Preview
fun EnvironmentCanvas(width: MutableState<Int>, height: MutableState<Int>) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var times by remember { mutableStateOf(0) }
    var isRun by remember { mutableStateOf(true) }
    var maxGeneration by remember { mutableStateOf(0) }
    var scale by remember { mutableStateOf(5f) }
    var speed by remember { mutableStateOf(100) }
    var restart by remember { mutableStateOf(20) }
    var recoverRate by remember { mutableStateOf(15) }

    var cells by remember { mutableStateOf(Environment.environment?.getRemainsCell(restart)) }
    LaunchedEffect(isRun){
        while (isRun) {
            delay(speed.toLong())
            Environment.environment?.refreshAll()
//            Environment.environment?.addMaterial()
            times++
            Environment.environment?.addRandomMaterial(recoverRate)


            cells = Environment.environment?.getRemainsCell(restart)
            cells?.let {
//                Environment.environment?.initMaterial(Code.Glucose, 10)
//                Environment.environment?.initRandomCells()
                for (cell in it) {
                    cell.generation++
                    if (cell.generation > maxGeneration) {
                        maxGeneration = cell.generation
                        println(cell.proteins)
                        val newCell = Cell.mutate(cell)
                        newCell.materials.addPack(Pack(Code.Glucose,100))
                        newCell.materials.addPack(Pack(Code.Life,1))
                        newCell.color = Color(Random.nextInt(255),Random.nextInt(255),Random.nextInt(255))
                        Environment.environment?.addCellToRandomBlock(newCell)
                    }
                }
            }
//                    Environment.environment?.addCellToRandomBlock(cell)

//
//
//                }
//                val aCell = Cell(Cell.powerCell.proteins)
//                aCell.materials.addPack(Pack(Code.Glucose,100))
//                aCell.materials.addPack(Pack(Code.Life,2))
//
//            }

//            Environment.environment?.env?.randomOrNull()?.randomOrNull()?.addRandomMaterial()
//            if (number%5==0){
//                val cell = Cell.myCell
//                cell.materials.add(Pack(Ribosome.randomCode(CODETYPE.Material),50))
//                cell.materials.add(Pack(Code.Life, 2))
//                Environment.environment?.addCellTo(10,0, cell)
//            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),

//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Column {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Text("Cell Number:${Environment.environment?.cellNumber}")
                Spacer(
                    modifier = Modifier.size(16.dp)
                )
                Text("Time:$times")
                Spacer(
                    modifier = Modifier.size(16.dp)
                )
                Button({
                    isRun = isRun.not()
                }){
                    val text = if (isRun) "Stop" else "Run"
                    Text(text)
                }

                Spacer(
                    modifier = Modifier.size(16.dp)
                )
                Button({
                    Environment.environment?.killCell()
                }){
                    val text = "Clear Cells"
                    Text(text)
                }
                Spacer(
                    modifier = Modifier.size(16.dp)
                )
                Button({
                    val aCell = Cell(Cell.powerCell.proteins)
                    aCell.materials.addPack(Pack(Code.Glucose,100))
                    aCell.materials.addPack(Pack(Code.Life,2))
                    Environment.environment?.addCellToRandomBlock(aCell)
                }){
                    val text = "Add Cell"
                    Text(text)
                }

            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("restart cells:")
                BasicTextField(
                    modifier = Modifier.background(Color.Cyan),
                    value = restart.toString(),
                    onValueChange = {
                        if (it.isNumeric()){
                            restart = it.toInt()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(
                    modifier = Modifier.size(16.dp)
                )
                Text("recover rate:")
                BasicTextField(
                    modifier = Modifier.background(Color.Cyan),
                    value = recoverRate.toString(),
                    onValueChange = {
                        if (it.isNumeric()){
                            recoverRate = it.toInt()
                        }
                    },
                )
            }
        }



        Canvas(modifier = Modifier
            .size(500.dp)
            .shadow(3.dp)
            .padding(start = 6.dp, top = 6.dp, end = 2.dp, bottom = 2.dp)
            .clipToBounds()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .offset(offsetX.dp, offsetY.dp)
        ) {
            scale(scale, pivot = Offset(0f, 0f)) {
                Environment.environment?.env?.indices?.forEach { x-> Environment.environment!!.env[x].indices.forEach { y ->
                    drawCircle(Environment.environment!!.env[x][y].blockColor(), center = Offset(x.toFloat(), y.toFloat()), radius = 0.5f)
                } }

                if (Environment.environment != null){
                    val canvasWidth = Environment.environment!!.width
                    val canvasHeight = Environment.environment!!.height
                    val lineColor = Color.Gray

                    // Draw vertical lines
                    for (x in -1 until canvasWidth+1) {
                        drawLine(
                            color = lineColor,
                            start = Offset(x.toFloat(), -1f),
                            end = Offset(x.toFloat(), canvasHeight.toFloat())
                        )
                    }

                    // Draw horizontal lines
                    for (y in -1 until canvasHeight+1) {
                        drawLine(
                            color = lineColor,
                            start = Offset(-1f, y.toFloat()),
                            end = Offset(canvasWidth.toFloat(), y.toFloat())
                        )
                    }
                }
            }
            // Change this to control the grid size



            drawCircle(Color(times),0f)
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Slider(
                value = scale,
                onValueChange = { scale = it },
                colors = SliderDefaults.colors(),
                valueRange = 0.5f..10f
            )
            Text("Scale:$scale")

            Spacer(Modifier.size(24.dp))

            Slider(
                value = speed.toFloat(),
                onValueChange = { speed = it.toInt() },
                colors = SliderDefaults.colors(),
                valueRange = 10f..2000f
            )
            Text("Speed:${speed}ms")
        }
    }




}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}

fun String.isNumeric(): Boolean {
    val pattern = Pattern.compile("[0-9]+")
    return pattern.matcher(this).matches()
}
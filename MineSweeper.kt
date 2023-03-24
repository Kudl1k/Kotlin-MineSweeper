package minesweeper
import java.util.Scanner
import kotlin.random.Random

var mineSweeper = mutableListOf(mutableListOf<Char>(),mutableListOf<Char>(),mutableListOf<Char>(),mutableListOf<Char>(),mutableListOf<Char>(),mutableListOf<Char>(),mutableListOf<Char>(),mutableListOf<Char>(),mutableListOf<Char>())
var numberOfFoundBombs = 0
var numberOfPlacedBombs = 0
var cords = mutableListOf<Pair<Int,Int>>()
var canPrint = mutableListOf(mutableListOf<Int>(),mutableListOf<Int>(),mutableListOf<Int>(),mutableListOf<Int>(),mutableListOf<Int>(),mutableListOf<Int>(),mutableListOf<Int>(),mutableListOf<Int>(),mutableListOf<Int>())
var isBombThere = mutableListOf(mutableListOf<Boolean>(),mutableListOf<Boolean>(),mutableListOf<Boolean>(),mutableListOf<Boolean>(),mutableListOf<Boolean>(),mutableListOf<Boolean>(),mutableListOf<Boolean>(),mutableListOf<Boolean>(),mutableListOf<Boolean>())

fun loadMap(){
    for (i in 0 until 9){
        for (j in 0 until 9){
            mineSweeper[i].add('.')
            canPrint[i].add(0)
            isBombThere[i].add(false)
        }
    }
}

fun createListOfPairs(){
    for (i in 0 until 9){
        for (j in 0 until 9){
            cords.add(Pair(i,j))
        }
    }
}



fun setBombs(bombs: Int){
    createListOfPairs()
    var counter = bombs
    while (counter > 0){
        val randomInt = Random.nextInt(0, cords.size-1)
        val (yBombCord: Int,xBombCord: Int) = cords[randomInt]
        if (mineSweeper[yBombCord][xBombCord] != 'X'){
            mineSweeper[yBombCord][xBombCord] = 'X'
            isBombThere[yBombCord][xBombCord] = true
            counter--
            checkBombs(yBombCord,xBombCord)
        }
    }
}



fun printMap(){
    checkPrinting()
    println(" │123456789│\n" +
            "—│—————————│")
    for (i in 0 until 9){
        print("${i+1}│")
        for (j in 0 until 9){
            if (canPrint[i][j] == 1){
                print(mineSweeper[i][j])
            } else if (canPrint[i][j] == 2){
                print("*")
            } else {
                print(".")
            }
        }
        println("|")
    }
    println("—│—————————│")
}



fun checkBombs(y: Int, x: Int){
    nebotady@for (i in y-1..y+1){
        tady@for (j in x-1..x+1){
            if (i<0) continue@nebotady
            if (j<0) continue@tady
            if (i>8) continue@nebotady
            if (j>8) continue@tady
            if (mineSweeper[i][j] == '.'){
                mineSweeper[i][j] = '1'
            } else if(mineSweeper[i][j] == 'X'){
                mineSweeper[i][j] = 'X'
            } else {
                val num1 = mineSweeper[i][j].toString().toInt() + 1
                mineSweeper[i][j] = num1.digitToChar()
            }
        }
    }
}

fun placeBombFlag(y: Int, x: Int){
    if (isBombThere[y][x] && canPrint[y][x] == 2){
        canPrint[y][x] = 0
    } else if (!isBombThere[y][x] && canPrint[y][x] == 2) {
        canPrint[y][x] = 0
    } else if (isBombThere[y][x]){
        canPrint[y][x] = 2
    } else if (!isBombThere[y][x] && canPrint[y][x] == 0){
        canPrint[y][x] = 2
    }
}

fun floodFill(y: Int, x: Int){
    if (x < 0 || x > 8 || y < 0 || y > 8 ) return

    if (mineSweeper[y][x] == '.' || mineSweeper[y][x] == '*' ){
        mineSweeper[y][x] = '/'

        floodFill(y+1,x)
        floodFill(y-1,x)
        floodFill(y,x+1)
        floodFill(y,x-1)
        floodFill(y+1,x+1)
        floodFill(y-1,x-1)
        floodFill(y+1,x-1)
        floodFill(y-1,x+1)
    }
    return
}

fun canPrintChar(y: Int, x: Int){
    nebotady@for (i in y-1..y+1){
        tady@for (j in x-1..x+1){
            if (i<0) continue@nebotady
            if (j<0) continue@tady
            if (i>8) continue@nebotady
            if (j>8) continue@tady
            canPrint[i][j] = 1
        }
    }
}

fun checkPrinting(){
    for (i in 0 until 9){
        for (j in 0 until 9){
            if (mineSweeper[i][j] == '/'){
                canPrintChar(i,j)
            }
        }
    }
}

fun checkBombsNumber(){
    numberOfFoundBombs = 0
    numberOfPlacedBombs = 0
    for (i in 0 until 9){
        for (j in 0 until 9){
            if (canPrint[i][j] == 2){
                numberOfPlacedBombs++
            }
            if (canPrint[i][j] == 2 && mineSweeper[i][j] == 'X'){
                numberOfFoundBombs++
            }
        }
    }
}


fun main() {
    loadMap()
    var win: Boolean = true
    val scanner = Scanner(System.`in`)
    print("How many mines do you want on the field?")
    val numberOfBombs = scanner.nextInt()
    numberOfFoundBombs = 0
    numberOfPlacedBombs = 0
    setBombs(numberOfBombs)
    loop@while (numberOfFoundBombs != numberOfBombs || numberOfFoundBombs != numberOfPlacedBombs){
        printMap()
        print("Set/unset mines marks or claim a cell as free: ")
        val cord2 = scanner.nextInt()
        val cord1 = scanner.nextInt()
        val typeOfInput = scanner.next()
        if (typeOfInput == "free"){
            if (isBombThere[cord1-1][cord2-1]){
                win = false
                for (i in 0 until 9){
                    canPrint[i].replaceAll { 1 }
                }
                printMap()
                println("You stepped on a mine and failed!")
                break@loop
            }
            if (mineSweeper[cord1-1][cord2-1] == '.' || mineSweeper[cord1-1][cord2-1] == '*'){
                floodFill(cord1-1,cord2-1)
            } else {
                canPrint[cord1-1][cord2-1] = 1
            }
        } else if (typeOfInput == "mine"){
            placeBombFlag(cord1-1,cord2-1)
        }
        checkBombsNumber()
    }

    if (win) println("Congratulations! You found all the mines!")
}
